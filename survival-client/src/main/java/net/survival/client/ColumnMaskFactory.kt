package net.survival.client

import net.survival.block.ColumnPos
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

object ColumnMaskFactory {
    fun createCircle(radius: Int, offsetX: Double, offsetZ: Double): Set<Long> {
        val radiusSquared = radius * radius
        val offsetCX = ColumnPos.toColumnX(floor(offsetX).toInt())
        val offsetCZ = ColumnPos.toColumnZ(floor(offsetZ).toInt())
        val mask = HashSet<Long>(ceil(Math.PI * radiusSquared).toInt())
        for (z in -radius until radius) {
            for (x in -radius until radius) {
                if (squareDistance(x, z) <= radiusSquared) {
                    val cx = offsetCX + x
                    val cz = offsetCZ + z
                    mask.add(ColumnPos.hashPos(cx, cz))
                }
            }
        }
        return mask
    }

    private fun squareDistance(x: Int, z: Int): Int {
        return x * x + z * z
    }
}