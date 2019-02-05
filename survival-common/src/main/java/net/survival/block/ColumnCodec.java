package net.survival.block;

import java.nio.ByteBuffer;

class ColumnCodec
{
    private static final int COLUMN_HEADER_SIZE = 2;

    public static ByteBuffer compressColumn(Column column) {
        var compressedDataLength = Column.VOLUME * 4 + COLUMN_HEADER_SIZE;
        var compressedData = ByteBuffer.allocate(compressedDataLength);

        var enabledChunks = (byte) 0;
        for (var i = 0; i < Column.HEIGHT; ++i) {
            if (column.getChunk(i) != null)
                enabledChunks |= 1 << i;
        }
        compressedData.put(enabledChunks);

        for (var i = 0; i < Column.HEIGHT; ++i) {
            var chunk = column.getChunk(i);
            if (chunk != null)
                compressChunk(chunk, compressedData);
        }

        compressedData.flip();
        return compressedData;
    }

    private static void compressChunk(Chunk chunk, ByteBuffer compressedData) {
        for (var i = 0; i < Chunk.VOLUME; ++i) {
            compressedData.putInt(chunk.blockIDs[i]);
        }
    }

    public static Column decompressColumn(ByteBuffer compressedData) {
        var column = new Column();

        var enabledChunks = compressedData.get();

        for (var i = 0; i < Column.HEIGHT; ++i) {
            if ((enabledChunks & (1 << i)) != 0) {
                var chunk = column.getChunkLazy(i);
                decompressChunk(compressedData, chunk);
            }
        }

        return column;
    }

    private static void decompressChunk(ByteBuffer compressedData, Chunk chunk) {
        for (var i = 0; i < Chunk.VOLUME; ++i) {
            chunk.blockIDs[i] = compressedData.getInt();
        }
    }
}