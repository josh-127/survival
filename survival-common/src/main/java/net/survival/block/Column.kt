package net.survival.block

import java.util.*

class Column {
    companion object {
        const val XLENGTH = Chunk.XLENGTH
        const val ZLENGTH = Chunk.ZLENGTH
        const val BASE_AREA = XLENGTH * ZLENGTH
    }

    private val chunks: Stack<Chunk>
    var isNew = true; private set
    var isModified = false; private set

    constructor() {
        chunks = Stack()
    }

    private constructor(chunks: Stack<Chunk>) {
        this.chunks = chunks
    }

    fun getChunk(index: Int): Chunk = chunks[index]
    fun getChunkOrNull(index: Int): Chunk? = if (index < chunks.size) chunks[index] else null
    val topChunk: Chunk get() = chunks.peek()

    fun pushChunk(chunk: Chunk) {
        chunks.push(chunk)
    }

    val height: Int get() = chunks.size

    fun getBlock(x: Int, y: Int, z: Int): Block {
        val index = y / Chunk.YLENGTH
        return when {
            index >= chunks.size -> StandardBlocks.AIR
            else -> chunks[index].getBlock(x, y % Chunk.YLENGTH, z)
        }
    }

    fun setBlock(x: Int, y: Int, z: Int, to: Block?) {
        val index = y / Chunk.YLENGTH
        while (index >= chunks.size) {
            chunks.push(Chunk())
        }
        chunks[index].setBlock(x, y % Chunk.YLENGTH, z, to)
        isModified = true
    }

    fun isInBounds(lx: Int, ly: Int, lz: Int): Boolean =
        lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && lz < ZLENGTH

    fun clearNewFlag() {
        isNew = false
    }

    fun clearModifiedFlag() {
        isModified = false
    }
}