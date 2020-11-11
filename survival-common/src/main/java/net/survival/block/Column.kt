package net.survival.block

import java.util.*

class Column {
    companion object {
        const val XLENGTH = Chunk.XLENGTH
        const val ZLENGTH = Chunk.ZLENGTH
        const val BASE_AREA = XLENGTH * ZLENGTH
    }

    private val chunks: ArrayList<Chunk>

    constructor() {
        chunks = ArrayList(8)
    }

    constructor(column: Column) {
        chunks = ArrayList(column.chunks.size)
        for (chunk in column.chunks) {
            chunks.add(Chunk(chunk))
        }
    }

    fun getChunk(index: Int): Chunk = chunks[index]
    fun getChunkOrNull(index: Int): Chunk? = chunks.getOrNull(index)
    fun addChunk(chunk: Chunk) {
        chunks.add(chunk)
    }

    val height: Int get() = chunks.size

    fun getBlock(x: Int, y: Int, z: Int): Block {
        val index = y / Chunk.YLENGTH
        return when {
            index >= chunks.size -> StandardBlocks.AIR
            else -> chunks[index].getBlock(x, y % Chunk.YLENGTH, z)
        }
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        val index = y / Chunk.YLENGTH
        while (index >= chunks.size) {
            chunks.add(Chunk())
        }
        chunks[index].setBlock(x, y % Chunk.YLENGTH, z, block)
    }

    fun isInBounds(lx: Int, ly: Int, lz: Int): Boolean =
        lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && lz < ZLENGTH
}