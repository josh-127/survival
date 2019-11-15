package net.survival.block;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicBoolean;

import net.survival.block.message.CloseColumnRequest;
import net.survival.block.message.ColumnResponse;
import net.survival.block.message.GetColumnRequest;
import net.survival.block.message.PostColumnRequest;

public class ColumnServer implements Runnable
{
    private static int FOOTER_LENGTH = 2 * VirtualAllocationUnit.STRUCTURE_SIZE;

    private final File file;
    private final VirtualMemoryAllocator allocator = new VirtualMemoryAllocator();
    private final ColumnDirectory directory = new ColumnDirectory();

    private final ColumnProvider columnGenerator;
    private final ColumnCodec columnCodec;

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ColumnDbPipe.ServerSide columnPipe;
    private FileChannel fileChannel;

    public ColumnServer(File file, ColumnDbPipe.ServerSide columnPipe, ColumnProvider columnGenerator) {
        this.file = file;
        this.columnPipe = columnPipe;
        this.columnGenerator = columnGenerator;
        columnCodec = new ColumnCodec();
    }

    @Override
    @SuppressWarnings("resource")
    public void run() {
        try {
            var fileExists = file.exists();
            fileChannel = new RandomAccessFile(file, "rw").getChannel();

            if (fileExists)
                loadMetadata();

            while (running.get()) {
                var request = columnPipe.waitForRequest();
                
                if (request instanceof CloseColumnRequest) {
                    running.set(false);
                }
                else if (request instanceof GetColumnRequest) {
                    var gcr = (GetColumnRequest) request;
                    var columnPos = gcr.getColumnPos();
                    
                    try {
                        var column = loadColumn(columnPos);
                        columnPipe.respond(new ColumnResponse(columnPos, column));
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (request instanceof PostColumnRequest) {
                    var pcr = (PostColumnRequest) request;
                    try {
                        saveColumn(pcr.getColumnPos(), pcr.getColumn());
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            saveMetadata();
            fileChannel.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Column loadColumn(long columnPos) throws IOException {
        var existingVau = directory.get(columnPos);
        if (existingVau == null) {
            return columnGenerator.provideColumn(columnPos);
        }

        fileChannel.position(existingVau.address);
        return columnCodec.decompressColumn(fileChannel, (int) existingVau.length);
    }

    private void saveColumn(long columnPos, Column column) throws IOException {
        var existingVau = directory.get(columnPos);
        if (existingVau != null) {
            allocator.freeMemory(existingVau.address);
        }

        var compressedData = columnCodec.compressColumn(column);
        var columnVau = allocator.allocateMemoryAndReturnVau(compressedData.limit());
        if (columnVau == null)
            throw new RuntimeException("Cannot allocate anymore columns.");

        directory.put(columnPos, columnVau);

        fileChannel.position(columnVau.address);
        while (compressedData.hasRemaining())
            fileChannel.write(compressedData);
        compressedData.flip();
    }

    private void loadMetadata() throws IOException {
        var buffer = ByteBuffer.allocate(FOOTER_LENGTH);
        fileChannel.position(file.length() - FOOTER_LENGTH);
        while (buffer.hasRemaining())
            fileChannel.read(buffer);
        buffer.flip();

        var allocatorVau = new VirtualAllocationUnit();
        var directoryVau = new VirtualAllocationUnit();
        allocatorVau.readFrom(buffer);
        directoryVau.readFrom(buffer);

        var metadataSectionSize = (int) (allocatorVau.length + directoryVau.length);
        var metadataBuffer = ByteBuffer.allocateDirect(metadataSectionSize);

        fileChannel.position(allocatorVau.address);
        while (metadataBuffer.hasRemaining())
            fileChannel.read(metadataBuffer);
        metadataBuffer.flip();

        allocator.readFrom(metadataBuffer);
        directory.readFrom(metadataBuffer);
    }

    private void saveMetadata() throws IOException {
        fileChannel.position(allocator.size());

        var metadataBuffer = ByteBuffer.allocateDirect(
                allocator.getSerializedSize() +
                directory.getSerializedSize() +
                FOOTER_LENGTH);

        var allocatorVau = new VirtualAllocationUnit(
                fileChannel.position(),
                allocator.getSerializedSize(),
                true);

        allocator.writeTo(metadataBuffer);

        var directoryVau = new VirtualAllocationUnit(
                fileChannel.position(),
                directory.getSerializedSize(),
                true);

        directory.writeTo(metadataBuffer);

        allocatorVau.writeTo(metadataBuffer);
        directoryVau.writeTo(metadataBuffer);

        metadataBuffer.flip();
        while (metadataBuffer.hasRemaining())
            fileChannel.write(metadataBuffer);
    }
}