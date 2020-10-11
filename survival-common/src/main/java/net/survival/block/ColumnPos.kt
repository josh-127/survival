package net.survival.block

import kotlin.math.floor

object ColumnPos {
    fun positionEquals(cx1: Int, cz1: Int, cx2: Int, cz2: Int): Boolean =
        cx1 == cx2 && cz1 == cz2

    fun toColumnX(x: Int): Int = floor(x.toDouble() / Column.XLENGTH).toInt()
    fun toColumnZ(z: Int): Int = floor(z.toDouble() / Column.ZLENGTH).toInt()

    fun toLocalX(cx: Int, gx: Int): Int = gx - cx * Column.XLENGTH
    fun toLocalZ(cz: Int, gz: Int): Int = gz - cz * Column.ZLENGTH

    fun toGlobalX(cx: Int, lx: Int): Int = lx + cx * Column.XLENGTH
    fun toGlobalZ(cz: Int, lz: Int): Int = lz + cz * Column.ZLENGTH

    fun getGlobalWestBound(cx: Int): Int = toGlobalX(cx, 0)
    fun getGlobalEastBound(cx: Int): Int = toGlobalX(cx, Column.XLENGTH)
    fun getGlobalNorthBound(cz: Int): Int = toGlobalZ(cz, 0)
    fun getGlobalSouthBound(cz: Int): Int = toGlobalZ(cz, Column.ZLENGTH)

    fun isInBounds(lx: Int, ly: Int, lz: Int): Boolean =
        lx >= 0 && lx < Column.XLENGTH && ly >= 0 && lz >= 0 && lz < Column.ZLENGTH

    /**
     * Hashes a column position into a 64-bit integer. Bits 0-31: column x-position
     * Bits 32-63: column z-position
     *
     * @param cx
     * column x-position
     * @param cz
     * column z-position
     * @return the hashed position
     */
    @JvmStatic
    fun hashPos(cx: Int, cz: Int): Long =
        ((cz.toLong() and 0xFFFFFFFFL) shl 32) or (cx.toLong() and 0xFFFFFFFFL)

    /**
     * Retrieves the column x-position from a hashed position.
     *
     * @param hashedPos
     * hashed position
     * @return column x-position
     */
    @JvmStatic
    fun columnXFromHashedPos(hashedPos: Long): Int =
        (hashedPos and 0x00000000_FFFFFFFFL).toInt()

    /**
     * Retrieves the column z-position from a hashed position.
     *
     * @param hashedPos
     * hashed position
     * @return column z-position
     */
    @JvmStatic
    fun columnZFromHashedPos(hashedPos: Long): Int =
        ((hashedPos and -0x1_00000000L) ushr 32).toInt()
}