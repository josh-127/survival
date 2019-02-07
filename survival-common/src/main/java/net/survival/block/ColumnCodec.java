package net.survival.block;

import java.nio.ByteBuffer;

import net.survival.util.XIntegerArray;

class ColumnCodec
{
    private static final int COLUMN_HEADER_SIZE = 2;
    private static final int CHUNK_HEADER_SIZE = 6;

    public static ByteBuffer compressColumn(Column column) {
        var compressedDataLength = COLUMN_HEADER_SIZE;

        for (var i = 0; i < Column.HEIGHT; ++i) {
            var chunk = column.getChunk(i);
            if (chunk != null) {
                var rawData = chunk.getRawData();
                var blockPalette = chunk.getBlockPalette();

                compressedDataLength += CHUNK_HEADER_SIZE;
                compressedDataLength += rawData.underlyingArray.length * 8;
                compressedDataLength += blockPalette.length * 4;
            }
        }

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

        compressedData.putShort((short) underlyingArray.length);
        compressedData.putShort((short) rawData.bitsPerElement);
        compressedData.putShort((short) blockPalette.length);
        for (var i = 0; i < underlyingArray.length; ++i)
            compressedData.putLong(underlyingArray[i]);

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
        var underlyingArrayLength = (int) compressedData.getShort();
        var bitsPerElement = (int) compressedData.getShort();
        var blockPaletteLength = (int) compressedData.getShort();

        var underlyingArray = new long[underlyingArrayLength];
        for (var i = 0; i < underlyingArray.length; ++i)
            underlyingArray[i] = compressedData.getLong();

        var rawData = XIntegerArray.moveUnderlyingArray(underlyingArray, Chunk.VOLUME, bitsPerElement);

        var blockPalette = new int[blockPaletteLength];
        for (var i = 0; i < blockPalette.length; ++i)
            blockPalette[i] = compressedData.getInt();

        return new Chunk(rawData, blockPalette);
    }
}