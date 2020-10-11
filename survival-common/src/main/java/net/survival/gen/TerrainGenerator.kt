package net.survival.gen

import net.survival.block.ColumnPos
import net.survival.block.StandardBlocks
import net.survival.util.DoubleMap3D
import net.survival.util.ImprovedNoiseGenerator

private const val NBLOCK_YLENGTH = ColumnPrimer.YLENGTH / 32
private const val NBLOCK_ZLENGTH = ColumnPrimer.ZLENGTH / 4
private const val NBLOCK_XLENGTH = ColumnPrimer.XLENGTH / 4
private const val NMAP_YLENGTH = ColumnPrimer.YLENGTH / NBLOCK_YLENGTH + 1
private const val NMAP_ZLENGTH = ColumnPrimer.ZLENGTH / NBLOCK_ZLENGTH + 1
private const val NMAP_XLENGTH = ColumnPrimer.XLENGTH / NBLOCK_XLENGTH + 1

private const val MAIN_NOISE_XSCALE = 1.0 / 128.0
private const val MAIN_NOISE_YSCALE = 1.0 / 64.0
private const val MAIN_NOISE_ZSCALE = 1.0 / 128.0
private const val MAIN_NOISE_OCTAVE_COUNT = 6

private const val ELEVATION_BASE = 64.0
private const val ELEVATION_RANGE = 96.0

class DefaultTerrainGenerator(
    private val seed: Long
) {
    private val densityMap: DoubleMap3D = DoubleMap3D(NMAP_XLENGTH, NMAP_YLENGTH, NMAP_ZLENGTH)

    fun generate(columnPos: Long, primer: ColumnPrimer) {
        val cx = ColumnPos.columnXFromHashedPos(columnPos)
        val cz = ColumnPos.columnZFromHashedPos(columnPos)
        val offsetX = cx * (NMAP_XLENGTH - 1)
        val offsetZ = cz * (NMAP_ZLENGTH - 1)

        ImprovedNoiseGenerator.generate3D(
            offsetX.toDouble(),
            0.0,
            offsetZ.toDouble(),
            MAIN_NOISE_XSCALE,
            MAIN_NOISE_YSCALE,
            MAIN_NOISE_ZSCALE,
            MAIN_NOISE_OCTAVE_COUNT,
            seed,
            densityMap
        )

        generateBase(primer)
    }

    private fun generateBase(primer: ColumnPrimer) {
        for (y in 0 until ColumnPrimer.YLENGTH) {
            val noiseMapY = y.toDouble() / NBLOCK_YLENGTH
            for (z in 0 until ColumnPrimer.ZLENGTH) {
                val noiseMapZ = z.toDouble() / NBLOCK_ZLENGTH
                for (x in 0 until ColumnPrimer.XLENGTH) {
                    val noiseMapX = x.toDouble() / NBLOCK_XLENGTH
                    val density = densityMap.sampleLinear(noiseMapX, noiseMapY, noiseMapZ)
                    val threshold = (y - ELEVATION_BASE) / ELEVATION_RANGE
                    if (density >= threshold) {
                        primer.setBlock(x, y, z, StandardBlocks.STONE)
                    }
                }
            }
        }
    }
}
