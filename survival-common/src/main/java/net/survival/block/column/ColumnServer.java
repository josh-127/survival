package net.survival.block.column;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class ColumnServer implements Runnable
{
    private static int FOOTER_LENGTH = 2 * VirtualAllocationUnit.STRUCTURE_SIZE;

    private final File file;
    private final VirtualMemoryAllocator allocator = new VirtualMemoryAllocator();
    private final ColumnDirectory directory = new ColumnDirectory();

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ColumnDbPipe.ServerSide columnPipe;
    private FileChannel fileChannel;

    public ColumnServer(File file, ColumnDbPipe.ServerSide columnPipe) {
        this.file = file;
        this.columnPipe = columnPipe;
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
                var request = (ColumnRequest) null;

                do {
                    request = columnPipe.waitForRequest();
                    var columnPos = request.columnPos;

                    if (request.type == ColumnRequest.TYPE_GET) {
                        columnPipe.respond(new ColumnResponse(
                                columnPos, loadColumn(columnPos)));
                    }
                    else if (request.type == ColumnRequest.TYPE_POST) {
                        saveColumn(columnPos, request.column);
                    }
                    else if (request.type == ColumnRequest.TYPE_CLOSE) {
                        running.set(false);
                        break;
                    }
                }
                while (request != null);
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
        if (existingVau == null)
            return null;

        var compressedData = ByteBuffer.allocateDirect((int) existingVau.length);

        fileChannel.position(existingVau.address);
        while (compressedData.hasRemaining())
            fileChannel.read(compressedData);
        compressedData.flip();

        var column = ColumnCodec.decompressColumn(compressedData);

        return column;
    }

    private void saveColumn(long columnPos, Column column) throws IOException {
        var existingVau = directory.get(columnPos);
        if (existingVau != null)
            allocator.freeMemory(existingVau.address);

        var compressedData = ColumnCodec.compressColumn(column);
        var columnVau = allocator.allocateMemoryAndReturnVau(compressedData.limit());
        if (columnVau == null)
            throw new RuntimeException("Cannot allocate anymore columns.");

        directory.put(columnPos, columnVau);

        fileChannel.position(columnVau.address);
        while (compressedData.hasRemaining())
            fileChannel.write(compressedData);
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