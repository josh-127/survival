package net.survival.world.chunk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChunkServer implements Runnable
{
    private static int FOOTER_LENGTH = 2 * VirtualAllocationUnit.STRUCTURE_SIZE;

    private final File file;
    private final VirtualMemoryAllocator allocator = new VirtualMemoryAllocator();
    private final ChunkDirectory directory = new ChunkDirectory();

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ChunkDbPipe.ServerSide chunkPipe;
    private FileChannel fileChannel;

    public ChunkServer(File file, ChunkDbPipe.ServerSide chunkPipe) {
        this.file = file;
        this.chunkPipe = chunkPipe;
    }

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        try {
            if (!loadMetadata())
                return;

            while (running.get()) {
                for (ChunkRequest request : chunkPipe.getRequests()) {
                    long chunkPos = request.chunkPos;

                    if (request.type == ChunkRequest.TYPE_GET) {
                        chunkPipe.respond(new ChunkResponse(
                                chunkPos, loadChunk(chunkPos)));
                    }
                    else if (request.type == ChunkRequest.TYPE_POST) {
                        saveChunk(chunkPos, request.chunk);
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

    private Chunk loadChunk(long chunkPos) {
        VirtualAllocationUnit existingVau = directory.get(chunkPos);
        if (existingVau == null)
            return null;

        ByteBuffer compressedData = ByteBuffer.allocateDirect((int) existingVau.length);
        Chunk chunk = ChunkCodec.decompressChunk(compressedData);

        return chunk;
    }

    private void saveChunk(long chunkPos, Chunk chunk) throws IOException {
        VirtualAllocationUnit existingVau = directory.get(chunkPos);
        if (existingVau != null)
            allocator.freeMemory(existingVau.address);

        ByteBuffer compressedData = ChunkCodec.compressChunk(chunk);
        VirtualAllocationUnit chunkVau = allocator.allocateMemoryAndReturnVau(compressedData.limit());
        if (chunkVau == null)
            throw new RuntimeException("Cannot allocate anymore chunks.");

        directory.put(chunkPos, chunkVau);

        fileChannel.position(chunkVau.address);
        while (compressedData.hasRemaining())
            fileChannel.write(compressedData);
    }

    @SuppressWarnings("resource")
    private boolean loadMetadata() throws IOException {
        boolean fileExists = file.exists();

        if (fileExists) {
            fileChannel = new RandomAccessFile(file, "rw").getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(FOOTER_LENGTH);
            fileChannel.position(file.length() - FOOTER_LENGTH);
            while (buffer.hasRemaining())
                fileChannel.read(buffer);
            buffer.flip();

            VirtualAllocationUnit allocatorVau = new VirtualAllocationUnit();
            VirtualAllocationUnit directoryVau = new VirtualAllocationUnit();
            allocatorVau.readFrom(buffer);
            directoryVau.readFrom(buffer);

            int metadataSectionSize = (int) (allocatorVau.length + directoryVau.length);
            ByteBuffer metadataBuffer = ByteBuffer.allocateDirect(metadataSectionSize);

            fileChannel.position(allocatorVau.address);
            while (metadataBuffer.hasRemaining())
                fileChannel.read(metadataBuffer);
            metadataBuffer.flip();

            allocator.readFrom(metadataBuffer);
            directory.readFrom(metadataBuffer);

            return true;
        }

        return false;
    }

    private void saveMetadata() throws IOException {
        fileChannel.position(allocator.size());

        ByteBuffer metadataBuffer = ByteBuffer.allocateDirect(
                allocator.getSerializedSize() +
                directory.getSerializedSize() +
                FOOTER_LENGTH);

        VirtualAllocationUnit allocatorVau = new VirtualAllocationUnit(
                fileChannel.position(),
                allocator.getSerializedSize(),
                true);

        allocator.writeTo(metadataBuffer);

        VirtualAllocationUnit directoryVau = new VirtualAllocationUnit(
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