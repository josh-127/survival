package net.survival.block;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import net.survival.util.XIntegerArray;

class ColumnCodec
{
    private static final int COLUMN_HEADER_SIZE = 2;
    private static final int CHUNK_HEADER_SIZE = 6;
    private static final int MAX_COLUMN_VOLUME = Column.BASE_AREA * 4096;

    private final ByteBuffer columnBuffer = ByteBuffer.allocateDirect(
            COLUMN_HEADER_SIZE + 8 * CHUNK_HEADER_SIZE + 8 * MAX_COLUMN_VOLUME);

    public ByteBuffer compressColumn(Column column) {
        columnBuffer.clear();

        columnBuffer.putInt(column.getHeight());

        for (var i = 0; i < column.getHeight(); ++i) {
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

    public Column decompressColumn(FileChannel fileChannel, int bufferSize) {
        columnBuffer.clear();
        columnBuffer.limit(bufferSize);

        while (columnBuffer.hasRemaining()) {
            try {
                fileChannel.read(columnBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        columnBuffer.flip();

        var column = new Column();
        var columnHeight = columnBuffer.getInt();

        for (var i = 0; i < columnHeight; ++i) {
                var chunk = decompressChunk(columnBuffer);
                column.pushChunk(chunk);
        }

        return column;
    }

    private Chunk decompressChunk(ByteBuffer buffer) {
        var underlyingArrayLength = (int) buffer.getShort();
        var bitsPerElement = (int) buffer.getShort();
        var blockPaletteLength = (int) buffer.getShort();

        var underlyingArray = new long[underlyingArrayLength];
        for (var i = 0; i < underlyingArray.length; ++i)
            underlyingArray[i] = buffer.getLong();

        var rawData = XIntegerArray.moveUnderlyingArray(underlyingArray, Chunk.VOLUME, bitsPerElement);

        var blockPalette = new int[blockPaletteLength];
        for (var i = 0; i < blockPalette.length; ++i)
            blockPalette[i] = buffer.getInt();

        return new Chunk(rawData, blockPalette);
    }
}