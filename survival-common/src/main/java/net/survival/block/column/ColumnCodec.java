package net.survival.block.column;

import java.nio.ByteBuffer;

class ColumnCodec
{
    private static final int COLUMN_HEADER_SIZE = 2;

    public static ByteBuffer compressColumn(Column column) {
        int compressedDataLength = Column.VOLUME * 4 + COLUMN_HEADER_SIZE;
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
        for (int i = 0; i < Chunk.VOLUME; ++i) {
            compressedData.putInt(chunk.blockIDs[i]);
        }
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
        for (int i = 0; i < Chunk.VOLUME; ++i) {
            chunk.blockIDs[i] = compressedData.getInt();
        }
    }
}