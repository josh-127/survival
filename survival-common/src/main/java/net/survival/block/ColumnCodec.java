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
        for (var y = 0; y < Chunk.YLENGTH; ++y) {
            for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                for (var x = 0; x < Chunk.XLENGTH; ++x)
                    compressedData.putInt(chunk.getBlockFullID(x, y, z));
            }
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
        for (var y = 0; y < Chunk.YLENGTH; ++y) {
            for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                for (var x = 0; x < Chunk.XLENGTH; ++x)
                    chunk.setBlockFullID(x, y, z, compressedData.getInt());
            }
        }
    }
}