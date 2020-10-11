package net.survival.graphics

import org.lwjgl.stb.STBImage

class Bitmap {
    var width: Int; private set
    var height: Int; private set
    var pixelArray: IntArray; private set

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
        pixelArray = IntArray(width * height)
    }

    private constructor(width: Int, height: Int, pixels: IntArray) {
        this.width = width
        this.height = height
        pixelArray = pixels
    }

    fun getPixel(x: Int, y: Int): Int =
        pixelArray[x + (height - y - 1) * width]

    fun setPixel(x: Int, y: Int, to: Int) {
        pixelArray[x + (height - y - 1) * width] = to
    }

    fun isInBound(x: Int, y: Int): Boolean =
        x >= 0 && y >= 0 && x < width && y < height

    companion object {
        private val stbi_load_w = IntArray(1)
        private val stbi_load_h = IntArray(1)
        private val stbi_load_comp = IntArray(1)

        fun fromFile(filePath: String): Bitmap {
            val data = STBImage.stbi_load(filePath, stbi_load_w, stbi_load_h, stbi_load_comp, 4)
                ?: throw RuntimeException(filePath)

            // TODO: Change this terrible ugly hack.
            val width = stbi_load_w[0]
            val height = stbi_load_h[0]
            val pixels = IntArray(width * height)
            data.asIntBuffer()[pixels]
            STBImage.stbi_image_free(data)

            return Bitmap(width, height, pixels)
        }

        fun blit(
            src: Bitmap,
            srcX: Int, srcY: Int, srcW: Int, srcH: Int,
            dst: Bitmap,
            dstX: Int, dstY: Int
        ) {
            for (y in srcY until srcY + srcH) {
                val blitY = dstY + y
                if (blitY < 0 || blitY >= dst.height) {
                    continue
                }
                for (x in srcX until srcX + srcW) {
                    val blitX = dstX + x
                    if (blitX < 0 || blitX >= dst.width) {
                        continue
                    }
                    val srcPixel = src.getPixel(x, y)
                    dst.setPixel(blitX, blitY, srcPixel)
                }
            }
        }
    }
}