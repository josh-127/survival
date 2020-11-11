package net.survival.block

import it.unimi.dsi.fastutil.objects.Object2ShortArrayMap
import net.survival.util.XIntegerArray
import java.util.*

class Chunk {
    companion object {
        const val XLENGTH = 16
        const val YLENGTH = 16
        const val ZLENGTH = 16
        const val BASE_AREA = XLENGTH * ZLENGTH
        const val VOLUME = BASE_AREA * YLENGTH
    }

    var rawData: XIntegerArray; private set
    private val rawIdToBlockMap: ArrayList<Block>
    private val blockToRawIdMap: Object2ShortArrayMap<Block>

    constructor() {
        rawData = XIntegerArray(VOLUME, 1)
        rawIdToBlockMap = ArrayList(4)
        blockToRawIdMap = Object2ShortArrayMap(4)
        rawIdToBlockMap.add(StandardBlocks.AIR)
        blockToRawIdMap[StandardBlocks.AIR] = 0.toShort()
    }

    constructor(chunk: Chunk) {
        rawData = XIntegerArray(chunk.rawData)
        rawIdToBlockMap = ArrayList(chunk.rawIdToBlockMap)
        blockToRawIdMap = Object2ShortArrayMap(chunk.blockToRawIdMap)
    }

    constructor(rawData: XIntegerArray, blockPalette: Array<Block>) {
        this.rawData = rawData
        rawIdToBlockMap = ArrayList(blockPalette.size)
        blockToRawIdMap = Object2ShortArrayMap(blockPalette.size)
        for (i in blockPalette.indices) {
            rawIdToBlockMap.add(blockPalette[i])
        }
        for (i in blockPalette.indices) {
            blockToRawIdMap[blockPalette[i]] = i.toShort()
        }
    }

    val blockPalette: Array<Block> get() = rawIdToBlockMap.toTypedArray()

    fun getBlock(index: Int): Block {
        return rawIdToBlockMap[rawData[index].toInt()]
    }

    fun getBlock(x: Int, y: Int, z: Int): Block {
        return getBlock(localPositionToIndex(x, y, z))
    }

    fun setBlock(index: Int, block: Block) {
        if (blockToRawIdMap.containsKey(block)) {
            rawData[index] = blockToRawIdMap.getShort(block).toLong()
        }
        else {
            val newRawId = blockToRawIdMap.size
            blockToRawIdMap[block] = newRawId.toShort()
            rawIdToBlockMap.add(block)
            if (!rawData.isValidValue(newRawId.toLong())) {
                rawData = rawData.getResized(rawData.bitsPerElement + 1)

                // TODO: Perform garbage collection on the block palette.
            }
            rawData[index] = newRawId.toLong()
        }
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        val index = localPositionToIndex(x, y, z)
        setBlock(index, block)
    }

    fun localPositionToIndex(x: Int, y: Int, z: Int): Int =
        x + z * XLENGTH + y * BASE_AREA

    fun isInBounds(x: Int, y: Int, z: Int): Boolean =
        x >= 0 && y >= 0 && z >= 0 && x < XLENGTH && y < YLENGTH && z < ZLENGTH
}