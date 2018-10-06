package net.survival.world.chunk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.concurrent.DeferredResult;
import net.survival.concurrent.Promise;

public class ChunkDatabase implements PersistentChunkStorage, AutoCloseable
{
    // TODO: Make factory method:
    //       public static DeferredResult<ChunkDatabase> loadDatabase(File file) { ... }

    private static int FOOTER_LENGTH = 16;

    private final File file;
    private final FileOperationMultiplexer fileOperationMultiplexer;
    private final VirtualMemoryAllocator virtualMemoryAllocator;

    // The ordering of on-disk chunkDirectory entries depends on Long2LongMap's implementation.
    // However, any change in the implementation will still be backwards compatible with
    // ChunkDatabase's file format.
    private final Long2LongOpenHashMap chunkDirectory;

    @SuppressWarnings("resource")
    public ChunkDatabase(File file) {
        try {
            this.file = file;
            boolean fileExists = file.exists();

            FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
            fileOperationMultiplexer = new FileOperationMultiplexer(fileChannel);
            
            if (fileExists) {
                ByteBuffer buffer = ByteBuffer.allocate(FOOTER_LENGTH);
                fileChannel.position(file.length() - FOOTER_LENGTH);
                while (buffer.hasRemaining())
                    fileChannel.read(buffer);
                buffer.flip();

                long allocatorEAU = buffer.getLong();
                long chunkDirectoryEAU = buffer.getLong();
                long allocatorPosition = AllocationUnitEncoding.decodeAddress(allocatorEAU);
                int allocatorDataLength = (int) AllocationUnitEncoding.decodeLength(allocatorEAU);
                long chunkDirectoryPosition = AllocationUnitEncoding.decodeAddress(chunkDirectoryEAU);
                int chunkDirectoryDataLength = (int) AllocationUnitEncoding.decodeLength(chunkDirectoryEAU);

                ByteBuffer deserializedAllocator = ByteBuffer.allocate(allocatorDataLength);
                fileChannel.position(allocatorPosition);
                while (deserializedAllocator.hasRemaining())
                    fileChannel.read(deserializedAllocator);
                deserializedAllocator.flip();
                virtualMemoryAllocator = VirtualMemoryAllocator.deserialize(deserializedAllocator);

                ByteBuffer deserializedChunkDirectory = ByteBuffer.allocate(chunkDirectoryDataLength);
                fileChannel.position(chunkDirectoryPosition);
                while (deserializedChunkDirectory.hasRemaining())
                    fileChannel.read(deserializedChunkDirectory);
                deserializedChunkDirectory.flip();
                chunkDirectory = deserializeChunkDirectory(deserializedChunkDirectory);
            }
            else {
                virtualMemoryAllocator = new VirtualMemoryAllocator();
                chunkDirectory = new Long2LongOpenHashMap();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws RuntimeException {
        // File isn't truncated, which is why it crashes when closing the application
        // for the second time.
        fileOperationMultiplexer.close();

        try {
            @SuppressWarnings("resource")
            FileChannel metadataSection = new RandomAccessFile(file, "rw").getChannel();

            long sectionStartPosition = virtualMemoryAllocator.size();
            metadataSection.position(sectionStartPosition);

            ByteBuffer allocatorData = virtualMemoryAllocator.serialize();
            int allocatorDataLength = (int) AllocationUnitEncoding.padLength(allocatorData.limit());
            long allocatorPosition = metadataSection.position();
            while (allocatorData.hasRemaining())
                metadataSection.write(allocatorData);

            ByteBuffer chunkDirectoryData = serializeChunkDirectory();
            int chunkDirectoryDataLength = (int) AllocationUnitEncoding.padLength(chunkDirectoryData.limit());
            long chunkDirectoryPosition = metadataSection.position();
            while (chunkDirectoryData.hasRemaining())
                metadataSection.write(chunkDirectoryData);

            ByteBuffer footer = ByteBuffer.allocate(FOOTER_LENGTH);
            footer.putLong(AllocationUnitEncoding.encode(allocatorPosition, allocatorDataLength, true));
            footer.putLong(AllocationUnitEncoding.encode(chunkDirectoryPosition, chunkDirectoryDataLength, true));
            footer.flip();
            while (footer.hasRemaining())
                metadataSection.write(footer);

            metadataSection.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        fileOperationMultiplexer.update();
    }

    @Override
    public DeferredResult<Chunk> provideChunkAsync(long hashedPos) {
        long address = chunkDirectory.getOrDefault(hashedPos, -1L);
        if (address == -1L)
            return null;

        LoadingChunk loadingChunk = new LoadingChunk();
        //loadingChunk.deserializedData = fileOperationMultiplexer.read(address, length)
        return null;
    }

    @Override
    public void moveAndSaveChunkAsync(long hashedPos, Chunk chunk) {
        long existingEAU = chunkDirectory.getOrDefault(hashedPos, AllocationUnitEncoding.INVALID_EAU);
        if (existingEAU != AllocationUnitEncoding.INVALID_EAU) {
            long existingAddress = AllocationUnitEncoding.decodeAddress(existingEAU);
            virtualMemoryAllocator.freeMemory(existingAddress);
        }

        ByteBuffer compressedData = ChunkCodec.compressChunk(chunk);
        long dataLength = compressedData.limit();
        long dataEAU = virtualMemoryAllocator.allocateMemoryAndReturnEAU(dataLength);
        chunkDirectory.put(hashedPos, dataEAU);

        long dataAddress = AllocationUnitEncoding.decodeAddress(dataEAU);
        fileOperationMultiplexer.write(compressedData, dataAddress);
    }

    private ByteBuffer serializeChunkDirectory() {
        int bufferLength = (int) AllocationUnitEncoding.padLength(4 + chunkDirectory.size() * 16);
        ByteBuffer buffer = ByteBuffer.allocate(bufferLength);

        buffer.putInt(chunkDirectory.size());

        ObjectIterator<Long2LongMap.Entry> iterator =
                chunkDirectory.long2LongEntrySet().fastIterator();

        while (iterator.hasNext()) {
            Long2LongMap.Entry entry = iterator.next();
            long hashedPos = entry.getLongKey();
            long dataEAU = entry.getLongValue();
            buffer.putLong(hashedPos);
            buffer.putLong(dataEAU);
        }

        while (buffer.position() < buffer.capacity())
            buffer.put((byte) 0);

        buffer.flip();
        return buffer;
    }

    public Long2LongOpenHashMap deserializeChunkDirectory(ByteBuffer source) {
        int size = source.getInt();
        Long2LongOpenHashMap destination = new Long2LongOpenHashMap(size);

        for (int i = 0; i < size; ++i) {
            long hashedPos = source.getLong();
            long dataEAU = source.getLong();
            destination.put(hashedPos, dataEAU);
        }

        return destination;
    }

    private static class LoadingChunk extends Promise<Chunk>
    {
        public DeferredResult<ByteBuffer> deserializedData;
    }
}