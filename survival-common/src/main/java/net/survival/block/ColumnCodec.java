package net.survival.block;

import java.nio.ByteBuffer;

import net.survival.util.XIntegerArray;

class ColumnCodec
{
    private static final int COLUMN_HEADER_SIZE = 2;
    private static final int CHUNK_HEADER_SIZE = 6;

    private final ByteBuffer columnBuffer = ByteBuffer.allocateDirect(
            COLUMN_HEADER_SIZE + 8 * CHUNK_HEADER_SIZE + 8 * Column.VOLUME);

    public ByteBuffer compressColumn(Column column) {
        columnBuffer.clear();

        var enabledChunks = (byte) 0;
        for (var i = 0; i < Column.HEIGHT; ++i) {
            if (column.getChunk(i) != null)
                enabledChunks |= 1 << i;
        }
        columnBuffer.put(enabledChunks);

        for (var i = 0; i < Column.HEIGHT; ++i) {
            var chunk = column.getChunk(i);
            if (chunk != null)
                compressChunk(chunk, columnBuffer);
        }

        columnBuffer.flip();
        return columnBuffer;
    }

    private void compressChunk(Chunk chunk, ByteBuffer buffer) {
        var rawData = chunk.getRawData();
        var underlyingArray = rawData.underlyingArray;
        var blockPalette = chunk.getBlockPalette();

        buffer.putShort((short) underlyingArray.length);
        buffer.putShort((short) rawData.bitsPerElement);
        buffer.putShort((short) blockPalette.length);
        for (var i = 0; i < underlyingArray.length; ++i)
            buffer.putLong(underlyingArray[i]);

        for (var i = 0; i < blockPalette.length; ++i)
            buffer.putInt(blockPalette[i]);
    }

    public Column decompressColumn(ByteBuffer compressedData) {
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

    private Chunk decompressChunk(ByteBuffer compressedData) {
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