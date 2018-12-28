package net.survival.world.column;

import java.nio.ByteBuffer;

class ColumnCodec
{
    private static final int COLUMN_HEADER_SIZE = 2;

    public static ByteBuffer compressColumn(Column column) {
        int compressedDataLength = Column.VOLUME * 2 + COLUMN_HEADER_SIZE;
        ByteBuffer compressedData = ByteBuffer.allocate(compressedDataLength);

        byte flags = (byte) (column.isDecorated() ? 1 : 0);
        compressedData.put(flags);

        byte enabledChunks = 0;
        for (int i = 0; i < Column.HEIGHT; ++i) {
            if (column.getChunk(i) != null)
                enabledChunks |= 1 << i;
        }
        compressedData.put(enabledChunks);

        for (int i = 0; i < Column.HEIGHT; ++i) {
            Chunk chunk = column.getChunk(i);
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

    public static Column decompressColumn(ByteBuffer compressedData) {
        Column column = new Column();

        byte flags = compressedData.get();
        if ((flags & 1) != 0)
            column.markDecorated();

        byte enabledChunks = compressedData.get();

        for (int i = 0; i < Column.HEIGHT; ++i) {
            if ((enabledChunks & (1 << i)) != 0) {
                Chunk chunk = column.getChunkLazy(i);
                decompressChunk(compressedData, chunk);
            }
        }

        return column;
    }

    private static void decompressChunk(ByteBuffer compressedData, Chunk chunk) {
        int index = 0;
        short rleStrip = compressedData.getShort();

        while (rleStrip != -1) {
            int length = ((rleStrip & 0xF000) >>> 12) + 1;
            short blockID = (short) (rleStrip & 0x0FFF);

            for (int i = 0; i < length; ++i)
                chunk.blockIDs[index++] = blockID;

            rleStrip = compressedData.getShort();
        }
    }
}