package net.survival.world.chunk;

import java.nio.ByteBuffer;

class ChunkCodec
{
    private static final int CHUNK_HEADER_SIZE = 1;

    public static ByteBuffer compressChunk(Chunk chunk) {
        int compressedDataLength = (int) AllocationUnitEncoding.padLength(
                Chunk.VOLUME * 2 + CHUNK_HEADER_SIZE);
        ByteBuffer compressedData = ByteBuffer.allocate(compressedDataLength);

        byte flags = (byte) (chunk.isDecorated() ? 1 : 0);
        compressedData.put(flags);

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

        while (compressedData.position() < compressedData.capacity())
            compressedData.put((byte) 0);

        compressedData.flip();
        return compressedData;
    }

    public static Chunk decompressChunk(ByteBuffer serializedChunkData) {
        Chunk chunk = new Chunk();

        byte flags = serializedChunkData.get();
        if ((flags & 1) != 0)
            chunk.markDecorated();

        int index = 0;
        while (serializedChunkData.hasRemaining()) {
            short rleStrip = serializedChunkData.getShort();
            int length = (rleStrip & 0xF000) >>> 12;
            short blockID = (short) (rleStrip & 0x0FFF);

            for (int i = 0; i <= length; ++i)
                chunk.blockIDs[index++] = blockID;
        }

        return chunk;
    }
}