package net.survival.block.io

import net.survival.block.Block
import net.survival.block.BlockModel
import net.survival.block.Chunk
import net.survival.block.Column
import net.survival.util.XIntegerArray
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

private const val COLUMN_HEADER_SIZE = 2
private const val CHUNK_HEADER_SIZE = 6
private const val MAX_COLUMN_VOLUME = Column.BASE_AREA * 4096

internal class ColumnCodec {
    private val columnBuffer = ByteBuffer.allocateDirect(
        COLUMN_HEADER_SIZE + 8 * CHUNK_HEADER_SIZE + 8 * MAX_COLUMN_VOLUME)

    fun compressColumn(column: Column): ByteBuffer {
        columnBuffer.clear()
        columnBuffer.putInt(column.height)
        for (i in 0 until column.height) {
            val chunk = column.getChunk(i)
            compressChunk(chunk, columnBuffer)
        }
        columnBuffer.flip()
        return columnBuffer
    }

    private fun compressChunk(chunk: Chunk, buffer: ByteBuffer) {
        val rawData = chunk.rawData
        val underlyingArray = rawData.underlyingArray
        val blockPalette = chunk.blockPalette
        buffer.putShort(underlyingArray.size.toShort())
        buffer.putShort(rawData.bitsPerElement.toShort())
        buffer.putShort(blockPalette.size.toShort())

        for (i in underlyingArray.indices) {
            buffer.putLong(underlyingArray[i])
        }
        for (block in blockPalette) {
            serializeBlock(block, buffer)
        }
    }

    fun decompressColumn(fileChannel: FileChannel, bufferSize: Int): Column {
        columnBuffer.clear()
        columnBuffer.limit(bufferSize)
        while (columnBuffer.hasRemaining()) {
            fileChannel.read(columnBuffer)
        }
        columnBuffer.flip()

        val column = Column()
        val columnHeight = columnBuffer.int

        for (i in 0 until columnHeight) {
            val chunk = decompressChunk(columnBuffer)
            column.pushChunk(chunk)
        }

        return column
    }

    private fun decompressChunk(buffer: ByteBuffer): Chunk {
        val underlyingArrayLength = buffer.short.toInt()
        val bitsPerElement = buffer.short.toInt()
        val blockPaletteLength = buffer.short.toInt()
        val underlyingArray = LongArray(underlyingArrayLength)

        for (i in underlyingArray.indices) {
            underlyingArray[i] = buffer.long
        }

        val rawData = XIntegerArray.moveUnderlyingArray(underlyingArray, Chunk.VOLUME, bitsPerElement)
        val blockPalette = ArrayList<Block>(blockPaletteLength)
        for (i in 0 until blockPaletteLength) {
            blockPalette.add(deserializeBlock(buffer))
        }

        return Chunk(rawData, blockPalette.toTypedArray())
    }

    private fun serializeBlock(block: Block, buffer: ByteBuffer) {
        buffer.putDouble(block.hardness)
        buffer.putDouble(block.resistance)
        buffer.put(if (block.solid) 1.toByte() else 0)
        serializeBlockModel(block.model, buffer)
    }

    private fun deserializeBlock(buffer: ByteBuffer): Block {
        val hardness = buffer.double
        val resistance = buffer.double
        val solid = buffer.get().toInt() == 1
        val model = deserializeBlockModel(buffer)
        return Block(hardness, resistance, solid, model)
    }

    private fun serializeBlockModel(model: BlockModel, buffer: ByteBuffer) {
        buffer.putInt(model.vertexData.size)
        for (i in model.vertexData.indices) {
            val vertexDataAtFace = model.vertexData[i]
            if (vertexDataAtFace != null) {
                buffer.putInt(vertexDataAtFace.size)
                for (vertex in vertexDataAtFace) {
                    buffer.putFloat(vertex)
                }
            }
            else {
                buffer.putInt(0)
            }
        }

        buffer.putInt(model.textures.size)
        for (i in model.textures.indices) {
            val textureAtFace = model.textures[i]
            if (textureAtFace != null) {
                buffer.putInt(textureAtFace.length)
                buffer.put(textureAtFace.toByteArray())
            }
            else {
                buffer.putInt(0)
            }
        }

        buffer.put(model.blockingFlags)
    }

    private fun deserializeBlockModel(buffer: ByteBuffer): BlockModel {
        val numFaces = buffer.int
        val vertexData = arrayOfNulls<FloatArray>(numFaces)
        for (i in 0 until numFaces) {
            val length = buffer.int
            if (length != 0) {
                vertexData[i] = FloatArray(length)
                for (j in 0 until length) {
                    vertexData[i]!![j] = buffer.float
                }
            }
        }

        val numTextures = buffer.int
        val textures = arrayOfNulls<String>(numTextures)
        for (i in 0 until numTextures) {
            val length = buffer.int
            if (length != 0) {
                val bytes = ByteArray(length)
                buffer[bytes]
                textures[i] = String(bytes)
            }
        }

        val blocking = buffer.get()
        return BlockModel(vertexData, textures, blocking)
    }
}