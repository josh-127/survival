package net.survival.gen

import net.survival.block.StandardBlocks
import java.util.*

abstract class ColumnDecorator {
    abstract fun decorate(columnPos: Long, primer: ColumnPrimer)
}

class DefaultColumnDecorator(
    private val includeWater: Boolean
): ColumnDecorator() {
    private val bedrockDecorator = BedrockDecorator()
    private val grasslandSurface = SurfaceDescription(listOf(
        SurfaceLayer(StandardBlocks.GRASS, 1, 1),
        SurfaceLayer(StandardBlocks.GRASS, 3, 5),
    ))
    private val grasslandSurfaceDecorator = SurfaceDecorator(grasslandSurface)
    private val waterDecorator = WaterDecorator()

    override fun decorate(columnPos: Long, primer: ColumnPrimer) {
        bedrockDecorator.decorate(columnPos, primer)
        grasslandSurfaceDecorator.decorate(columnPos, primer)
        if (includeWater) {
            waterDecorator.decorate(columnPos, primer)
        }
    }
}

private class BedrockDecorator: ColumnDecorator() {
    override fun decorate(columnPos: Long, primer: ColumnPrimer) {
        for (z in 0 until ColumnPrimer.ZLENGTH) {
            for (x in 0 until ColumnPrimer.XLENGTH) {
                primer.setBlock(x, 0, z, StandardBlocks.BEDROCK)
            }
        }
    }
}

private class WaterDecorator: ColumnDecorator() {
    companion object {
        private const val SEA_LEVEL = 63
    }

    override fun decorate(columnPos: Long, primer: ColumnPrimer) {
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
    private val description: SurfaceDescription
): ColumnDecorator() {
    private val random = Random()

    override fun decorate(columnPos: Long, primer: ColumnPrimer) {
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
        val layerCount = description.layers.size
        for (i in 0 until layerCount) {
            val layer = description.layers[i]
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
