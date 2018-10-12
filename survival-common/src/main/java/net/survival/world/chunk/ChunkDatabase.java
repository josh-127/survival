package net.survival.world.chunk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.Queue;

import net.survival.concurrent.DeferredResult;
import net.survival.concurrent.Promise;
import net.survival.io.FileOperationMultiplexer;

public class ChunkDatabase implements PersistentChunkStorage, AutoCloseable
{
    // TODO: Make factory method:
    //       public static DeferredResult<ChunkDatabase> loadDatabase(File file) { ... }

    private static int FOOTER_LENGTH = 2 * VirtualAllocationUnit.STRUCTURE_SIZE;

    private final File file;
    private final FileOperationMultiplexer fileIO;
    private final VirtualMemoryAllocator allocator;
    private final ChunkDirectory directory;

    private final Queue<DeferredChunk> deferredChunks;

    @SuppressWarnings("resource")
    public ChunkDatabase(File file) {
        this.file = file;
        allocator = new VirtualMemoryAllocator();
        directory = new ChunkDirectory();
        deferredChunks = new LinkedList<>();

        try {
            boolean fileExists = file.exists();

            if (fileExists) {
                FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();

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
            }

            AsynchronousFileChannel channel = AsynchronousFileChannel.open(
                    FileSystems.getDefault().getPath(file.getAbsolutePath()),
                    StandardOpenOption.READ,StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE);
            fileIO = new FileOperationMultiplexer(channel);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws RuntimeException {
        try {
            finish();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void finish() throws IOException {
        fileIO.close();

        @SuppressWarnings("resource")
        FileChannel channel = new RandomAccessFile(file, "rw").getChannel();

        channel.position(allocator.size());

        ByteBuffer metadataBuffer = ByteBuffer.allocateDirect(
                allocator.getSerializedSize() +
                directory.getSerializedSize() +
                FOOTER_LENGTH);

        VirtualAllocationUnit allocatorVau = new VirtualAllocationUnit(
                channel.position(),
                allocator.getSerializedSize(),
                true);

        allocator.writeTo(metadataBuffer);

        VirtualAllocationUnit directoryVau = new VirtualAllocationUnit(
                channel.position(),
                directory.getSerializedSize(),
                true);

        directory.writeTo(metadataBuffer);

        allocatorVau.writeTo(metadataBuffer);
        directoryVau.writeTo(metadataBuffer);

        metadataBuffer.flip();
        while (metadataBuffer.hasRemaining())
            channel.write(metadataBuffer);

        channel.close();
    }

    public void update() {
        fileIO.update();

        if (!deferredChunks.isEmpty()) {
            DeferredChunk deferredChunk = deferredChunks.peek();
            ByteBuffer compressedData = deferredChunk.deferredCompressedData.pollResult();

            if (compressedData != null) {
                Chunk chunk = ChunkCodec.decompressChunk(compressedData);
                deferredChunk.setResult(chunk);
                deferredChunks.remove();
            }
        }
    }

    @Override
    public DeferredResult<Chunk> provideChunkAsync(long hashedPos) {
        VirtualAllocationUnit existingVau = directory.get(hashedPos);
        if (existingVau == null)
            return null;

        DeferredResult<ByteBuffer> buffer = fileIO.read(existingVau.address, (int) existingVau.length);
        DeferredChunk deferredChunk = new DeferredChunk(buffer);

        deferredChunks.add(deferredChunk);
        return deferredChunk;
    }

    @Override
    public void moveAndSaveChunkAsync(long hashedPos, Chunk chunk) {
        VirtualAllocationUnit existingVau = directory.get(hashedPos);
        if (existingVau != null)
            allocator.freeMemory(existingVau.address);

        ByteBuffer compressedData = ChunkCodec.compressChunk(chunk);
        VirtualAllocationUnit chunkVau = allocator.allocateMemoryAndReturnVau(compressedData.limit());
        if (chunkVau == null)
            throw new RuntimeException("Cannot allocate anymore chunks.");

        directory.put(hashedPos, chunkVau);
        fileIO.borrowAndWrite(compressedData, chunkVau.address);
    }

    private static class DeferredChunk extends Promise<Chunk>
    {
        DeferredResult<ByteBuffer> deferredCompressedData;

        public DeferredChunk(DeferredResult<ByteBuffer> deferredCompressedData) {
            this.deferredCompressedData = deferredCompressedData;
        }
    }
}