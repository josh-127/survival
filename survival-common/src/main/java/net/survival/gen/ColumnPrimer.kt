package net.survival.gen

import net.survival.block.Block
import net.survival.block.Column
import net.survival.block.StandardBlocks
import kotlin.math.floor

class ColumnPrimer {
    companion object {
        const val XLENGTH = Column.XLENGTH
        const val YLENGTH = 256
        const val ZLENGTH = Column.ZLENGTH
        const val BASE_AREA = XLENGTH * ZLENGTH
        const val VOLUME = BASE_AREA * YLENGTH
    }

    val blocks: Array<Block> = Array(VOLUME) { StandardBlocks.AIR }

    fun toColumn(): Column {
        val column = Column()
        var index = 0
        for (y in 0 until YLENGTH) {
            for (z in 0 until ZLENGTH) {
                for (x in 0 until XLENGTH) {
                    if (blocks[index] != StandardBlocks.AIR) {
                        column.setBlock(x, y, z, blocks[index])
                    }
                    ++index
                }
            }
        }
        return column
    }

    fun getBlock(x: Int, y: Int, z: Int): Block {
        return blocks[localPositionToIndex(x, y, z)]
    }

    fun sampleNearestBlock(x: Double, y: Double, z: Double): Block {
        val xi = floor(x).toInt()
        val yi = floor(y).toInt()
        val zi = floor(z).toInt()
        return getBlock(xi, yi, zi)
    }

    fun getTopLevel(x: Int, z: Int): Int {
        var topLevel = YLENGTH - 1
        while (topLevel >= 0 && getBlock(x, topLevel, z) == StandardBlocks.AIR) {
            --topLevel
        }
        return topLevel
    }

    fun setBlock(x: Int, y: Int, z: Int, to: Block) {
        requireNotNull(to) { "to" }
        blocks[localPositionToIndex(x, y, z)] = to
    }

    fun placeBlockIfEmpty(x: Int, y: Int, z: Int, to: Block): Boolean {
        if (getBlock(x, y, z) == StandardBlocks.AIR) {
            setBlock(x, y, z, to)
            return true
        }
        return false
    }

    fun replaceBlockIfExists(x: Int, y: Int, z: Int, replacement: Block): Boolean {
        if (getBlock(x, y, z) != StandardBlocks.AIR) {
            setBlock(x, y, z, replacement)
            return true
        }
        return false
    }

    fun clear() {
        blocks.fill(StandardBlocks.AIR)
    }

    private fun localPositionToIndex(x: Int, y: Int, z: Int): Int {
        return x + z * XLENGTH + y * BASE_AREA
    }
}