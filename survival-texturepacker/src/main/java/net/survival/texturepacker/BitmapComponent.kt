package net.survival.texturepacker

import net.survival.graphics.Bitmap
import java.awt.Color
import java.awt.Graphics
import javax.swing.JComponent
import kotlin.math.min

internal class BitmapComponent: JComponent() {
    var bmp: Bitmap? = null

    fun getBitmap(): Bitmap? {
        return bmp
    }

    fun setBitmap(bitmap: Bitmap?) {
        this.bmp = bitmap
        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.color = Color.BLACK
        g.fillRect(0, 0, width, height)

        if (bmp != null) {
            val width = min(width, bmp!!.width)
            val height = min(height, bmp!!.height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val srcColor = bmp!!.getPixel(x, y)
                    val red = srcColor and 0xFF
                    val green = srcColor and 0xFF00 ushr 8
                    val blue = srcColor and 0xFF0000 ushr 16
                    val destColor = red shl 16 or (green shl 8) or blue
                    g.color = Color(destColor)
                    g.fillRect(x, y, 1, 1)
                }
            }
        }
    }
}