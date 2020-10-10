package net.survival.gen

import net.survival.gen.layer.GenLayer
import net.survival.gen.layer.GenLayerFactory
import java.awt.Canvas
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.image.BufferStrategy
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import javax.swing.Timer

class ViewportComponent: Canvas(), ActionListener, KeyListener {
    private val map: GenLayer = GenLayerFactory.createBiomeLayer(768, 768, 0L)
    private var offsetX = 0
    private var offsetZ = 0
    private var prevOffsetX = 0
    private var prevOffsetZ = 0
    private var upPressed = false
    private var downPressed = false
    private var leftPressed = false
    private var rightPressed = false
    private var bs: BufferStrategy? = null

    fun redraw() {
        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val destPixels = (dest.raster.dataBuffer as DataBufferInt).data
        bs = getBufferStrategy()
        if (bs == null) {
            createBufferStrategy(4)
            return
        }
        val VIEWPORT_WIDTH = map.lengthX
        val VIEWPORT_HEIGHT = map.lengthZ
        val VIEWPORT_X = (width - VIEWPORT_WIDTH) / 2
        val VIEWPORT_Z = (height - VIEWPORT_HEIGHT) / 2
        val destWidth = dest.width
        val destHeight = dest.height

        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, getWidth(), getHeight());
        for (z in 0 until VIEWPORT_HEIGHT) {
            for (x in 0 until VIEWPORT_WIDTH) {
                val biomeID = map.sampleNearest(x, z)
                val biomeColor = BiomeType.byId(biomeID.toInt()).biomeViewerColor
                destPixels[VIEWPORT_X + x + (VIEWPORT_Z + z) * destWidth] = biomeColor
            }
        }
        val graphics = bs!!.drawGraphics
        graphics.drawImage(dest, 0, 0, destWidth, destHeight, null)
        graphics.dispose()
        bs!!.show()
    }

    override fun actionPerformed(e: ActionEvent) {
        val INCREMENT = 7
        prevOffsetX = offsetX
        prevOffsetZ = offsetZ
        if (upPressed) offsetZ -= INCREMENT
        if (downPressed) offsetZ += INCREMENT
        if (leftPressed) offsetX -= INCREMENT
        if (rightPressed) offsetX += INCREMENT
        if (offsetX != prevOffsetX || offsetZ != prevOffsetZ) {
            map.generate(offsetX, offsetZ)
            redraw()
        }
    }

    override fun keyTyped(e: KeyEvent) {}
    override fun keyPressed(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_UP -> upPressed = true
            KeyEvent.VK_DOWN -> downPressed = true
            KeyEvent.VK_LEFT -> leftPressed = true
            KeyEvent.VK_RIGHT -> rightPressed = true
        }
    }

    override fun keyReleased(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_UP -> upPressed = false
            KeyEvent.VK_DOWN -> downPressed = false
            KeyEvent.VK_LEFT -> leftPressed = false
            KeyEvent.VK_RIGHT -> rightPressed = false
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }

    init {
        map.generate(offsetX, offsetZ)
        background = Color.BLACK
        val timer = Timer(67, this)
        timer.isRepeats = true
        timer.start()
    }
}