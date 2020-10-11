package net.survival.gen

import net.survival.block.Block
import net.survival.block.StandardBlocks
import java.util.*

class DefaultColumnDecorator(
    private val includeWater: Boolean
) {
    private val grasslandSurface = listOf(
        SurfaceLayer(StandardBlocks.GRASS, 1, 1),
        SurfaceLayer(StandardBlocks.GRASS, 3, 5),
    )
    private val grasslandSurfaceDecorator = SurfaceDecorator(grasslandSurface)

    fun decorate(columnPos: Long, primer: ColumnPrimer) {
        BedrockDecorator.decorate(primer)
        grasslandSurfaceDecorator.decorate(columnPos, primer)
        if (includeWater) {
            WaterDecorator.decorate(primer)
        }
    }
}

object BedrockDecorator {
    fun decorate(primer: ColumnPrimer) {
        for (z in 0 until ColumnPrimer.ZLENGTH) {
            for (x in 0 until ColumnPrimer.XLENGTH) {
                primer.setBlock(x, 0, z, StandardBlocks.BEDROCK)
            }
        }
    }
}

object WaterDecorator {
    private const val SEA_LEVEL = 63

    fun decorate(primer: ColumnPrimer) {
        for (x in 0 until ColumnPrimer.XLENGTH) {
            for (z in 0 until ColumnPrimer.ZLENGTH) {
                val surfaceY = primer.getTopLevel(x, z)
                if (surfaceY < 0) {
                    continue
                }
                for (y in surfaceY + 1..SEA_LEVEL) {
                    primer.setBlock(x, y, z, StandardBlocks.WATER)
                }
            }
        }
    }
}

private class SurfaceDecorator(
    private val layers: List<SurfaceLayer>
) {
    private val random = Random()

    fun decorate(columnPos: Long, primer: ColumnPrimer) {
        random.setSeed(columnPos)
        for (z in 0 until ColumnPrimer.ZLENGTH) {
            for (x in 0 until ColumnPrimer.XLENGTH) {
                decorateStrip(x, z, primer)
            }
        }
    }

    private fun decorateStrip(x: Int, z: Int, primer: ColumnPrimer) {
        var y = primer.getTopLevel(x, z)
        if (y == -1) return
        val layerCount = layers.size
        for (i in 0 until layerCount) {
            val layer = layers[i]
            val minThickness = layer.minThickness
            val thicknessRange = layer.thicknessRange
            var thickness = minThickness + random.nextInt(thicknessRange)
            if (y - thickness < 0) {
                thickness = y
            }

            val block = layer.block
            while (thickness > 0) {
                primer.setBlock(x, y, z, block)
                --y
                --thickness
            }
        }
    }
}

private data class SurfaceLayer(
    val block: Block,
    val minThickness: Int,
    val maxThickness: Int
) {
    val thicknessRange: Int get() = maxThickness - minThickness + 1
}
