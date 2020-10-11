package net.survival.graphics

data class BitmapRegion(
    var left: Int,
    var top: Int,
    var right: Int,
    var bottom: Int,
    var bitmap: Bitmap?
) {
    val width: Int get() = right - left
    val height: Int get() = bottom - top
    val area: Int get() = width * height
}