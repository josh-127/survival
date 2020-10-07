package net.survival.block.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import net.survival.block.BlockModel;
import net.survival.block.Chunk;
import net.survival.block.Column;
import net.survival.block.Block;
import net.survival.util.XIntegerArray;

class ColumnCodec {
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
            if (chunk != null) {
                compressChunk(chunk, columnBuffer);
            }
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

        for (var i = 0; i < underlyingArray.length; ++i) {
            buffer.putLong(underlyingArray[i]);
        }

        for (var block : blockPalette) {
            serializeBlock(block, buffer);
        }
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
        for (var i = 0; i < underlyingArray.length; ++i) {
            underlyingArray[i] = buffer.getLong();
        }

        var rawData = XIntegerArray.moveUnderlyingArray(underlyingArray, Chunk.VOLUME, bitsPerElement);

        var blockPalette = new Block[blockPaletteLength];
        for (var i = 0; i < blockPalette.length; ++i) {
            blockPalette[i] = deserializeBlock(buffer);
        }

        return new Chunk(rawData, blockPalette);
    }

    private void serializeBlock(Block block, ByteBuffer buffer) {
        buffer.putDouble(block.hardness);
        buffer.putDouble(block.resistance);
        buffer.put(block.solid ? (byte) 1 : 0);
        serializeBlockModel(block.model, buffer);
    }

    private Block deserializeBlock(ByteBuffer buffer) {
        var hardness = buffer.getDouble();
        var resistance = buffer.getDouble();
        var solid = buffer.get() == 1;
        var model = deserializeBlockModel(buffer);
        return new Block(hardness, resistance, solid, model);
    }

    private void serializeBlockModel(BlockModel model, ByteBuffer buffer) {
        buffer.putInt(model.vertexData.length);
        for (var i = 0; i < model.vertexData.length; ++i) {
            if (model.vertexData[i] != null) {
                buffer.putInt(model.vertexData[i].length);
                for (var j = 0; j < model.vertexData[i].length; ++j) {
                    buffer.putFloat(model.vertexData[i][j]);
                }
            }
            else {
                buffer.putInt(0);
            }
        }

        buffer.putInt(model.textures.length);
        for (var i = 0; i < model.textures.length; ++i) {
            if (model.textures[i] != null) {
                buffer.putInt(model.textures[i].length());
                buffer.put(model.textures[i].getBytes());
            }
            else {
                buffer.putInt(0);
            }
        }

        buffer.put(model.blocking);
    }

    private BlockModel deserializeBlockModel(ByteBuffer buffer) {
        var numFaces = buffer.getInt();
        var vertexData = new float[numFaces][];
        for (var i = 0; i < numFaces; ++i) {
            var length = buffer.getInt();
            if (length != 0) {
                vertexData[i] = new float[length];
                for (var j = 0; j < length; ++j) {
                    vertexData[i][j] = buffer.getFloat();
                }
            }
        }

        var numTextures = buffer.getInt();
        var textures = new String[numTextures];
        for (var i = 0; i < numTextures; ++i) {
            var length = buffer.getInt();
            if (length != 0) {
                var bytes = new byte[length];
                buffer.get(bytes);
                textures[i] = new String(bytes);
            }
        }

        var blocking = buffer.get();

        return new BlockModel(vertexData, textures, blocking);
    }
}