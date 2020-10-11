package net.survival.gen

import net.survival.block.Block

data class SurfaceDescription(
    val layers: List<SurfaceLayer>
)

data class SurfaceLayer(
    val block: Block,
    val minThickness: Int,
    val maxThickness: Int
) {
    val thicknessRange: Int get() = maxThickness - minThickness + 1
}
