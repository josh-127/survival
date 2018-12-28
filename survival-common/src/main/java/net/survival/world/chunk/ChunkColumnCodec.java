package net.survival.world.chunk;

import java.nio.ByteBuffer;

class ChunkColumnCodec
{
    private static final int CHUNK_HEADER_SIZE = 2;

    public static ByteBuffer compressChunkColumn(ChunkColumn chunkColumn) {
        int compressedDataLength = ChunkColumn.VOLUME * 2 + CHUNK_HEADER_SIZE;
        ByteBuffer compressedData = ByteBuffer.allocate(compressedDataLength);

        byte flags = (byte) (chunkColumn.isDecorated() ? 1 : 0);
        compressedData.put(flags);

        byte enabledChunks = 0;
        for (int i = 0; i < ChunkColumn.HEIGHT; ++i) {
            if (chunkColumn.getChunk(i) != null)
                enabledChunks |= 1 << i;
        }
        compressedData.put(enabledChunks);

        for (int i = 0; i < ChunkColumn.HEIGHT; ++i) {
            Chunk chunk = chunkColumn.getChunk(i);
            if (chunk != null)
                compressChunk(chunk, compressedData);
        }

        compressedData.flip();
        return compressedData;
    }

    private static void compressChunk(Chunk chunk, ByteBuffer compressedData) {
        int counter = 0;

        while (counter < Chunk.VOLUME) {
            int start = counter;
            int startBlockID = chunk.blockIDs[start];

            while (counter < Chunk.VOLUME
                    && counter - start < 16
                    && chunk.blockIDs[counter] == startBlockID)
            {
                ++counter;
            }

            short rleStrip = (short) (startBlockID & 0x0FFF);
            rleStrip |= (short) (((counter - start - 1) & 0xF) << 12);

            compressedData.putShort(rleStrip);
        }

        compressedData.putShort((short) -1);
    }

    public static ChunkColumn decompressChunkColumn(ByteBuffer compressedChunkData) {
        ChunkColumn chunkColumn = new ChunkColumn();

        byte flags = compressedChunkData.get();
        if ((flags & 1) != 0)
            chunkColumn.markDecorated();

        byte enabledChunks = compressedChunkData.get();

        for (int i = 0; i < ChunkColumn.HEIGHT; ++i) {
            if ((enabledChunks & (1 << i)) != 0) {
                Chunk chunk = chunkColumn.getChunkLazy(i);
                decompressChunk(compressedChunkData, chunk);
            }
        }

        return chunkColumn;
    }

    private static void decompressChunk(ByteBuffer compressedChunkData, Chunk chunk) {
        int index = 0;
        short rleStrip = compressedChunkData.getShort();

        while (rleStrip != -1) {
            int length = ((rleStrip & 0xF000) >>> 12) + 1;
            short blockID = (short) (rleStrip & 0x0FFF);

            for (int i = 0; i < length; ++i)
                chunk.blockIDs[index++] = blockID;

            rleStrip = compressedChunkData.getShort();
        }
    }
}