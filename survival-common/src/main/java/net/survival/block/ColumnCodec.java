package net.survival.block;

import java.nio.ByteBuffer;

import net.survival.util.XIntegerArray;

class ColumnCodec
{
    private static final int COLUMN_HEADER_SIZE = 2;

    public static ByteBuffer compressColumn(Column column) {
        var compressedDataLength = Column.VOLUME * 8 + COLUMN_HEADER_SIZE;
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
        var rawData = chunk.getRawData();
        var underlyingArray = rawData.underlyingArray;
        var blockPalette = chunk.getBlockPalette();

        compressedData.putInt(underlyingArray.length);
        compressedData.put((byte) rawData.bitsPerElement);
        for (var i = 0; i < underlyingArray.length; ++i)
            compressedData.putLong(underlyingArray[i]);

        compressedData.putInt(blockPalette.length);
        for (var i = 0; i < blockPalette.length; ++i)
            compressedData.putInt(blockPalette[i]);
    }

    public static Column decompressColumn(ByteBuffer compressedData) {
        var column = new Column();

        var enabledChunks = compressedData.get();

        for (var i = 0; i < Column.HEIGHT; ++i) {
            if ((enabledChunks & (1 << i)) != 0) {
                var chunk = decompressChunk(compressedData);
                column.setChunk(i, chunk);
            }
        }

        return column;
    }

    private static Chunk decompressChunk(ByteBuffer compressedData) {
        var underlyingArrayLength = compressedData.getInt();
        var bitsPerElement = (int) compressedData.get();

        var underlyingArray = new long[underlyingArrayLength];

        for (var i = 0; i < underlyingArray.length; ++i)
            underlyingArray[i] = compressedData.getLong();

        var rawData = XIntegerArray.moveUnderlyingArray(underlyingArray, Chunk.VOLUME, bitsPerElement);

        var blockPaletteLength = compressedData.getInt();
        var blockPalette = new int[blockPaletteLength];

        for (var i = 0; i < blockPalette.length; ++i)
            blockPalette[i] = compressedData.getInt();

        return new Chunk(rawData, blockPalette);
    }
}