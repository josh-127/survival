package net.survival.texturepacker

import net.survival.graphics.Bitmap
import net.survival.graphics.BitmapAtlasBuilder
import java.awt.event.ActionEvent
import java.nio.file.Paths
import javax.swing.*

private const val WINDOW_WIDTH = 800
private const val WINDOW_HEIGHT = 600
private const val TITLE = "Texture Packer"

class TexturePacker private constructor(): JFrame() {
    private val atlasBuilder: BitmapAtlasBuilder = BitmapAtlasBuilder(WINDOW_WIDTH, WINDOW_HEIGHT)
    private val viewport: BitmapComponent = BitmapComponent()

    init {
        contentPane = viewport
        jMenuBar = JMenuBar()
        createMainMenu()
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        title = TITLE
        isVisible = true
        //setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        defaultCloseOperation = EXIT_ON_CLOSE
        requestFocusInWindow()
    }

    private fun createMainMenu() {
        val parent = this
        val menu = JMenu("Atlas")
        jMenuBar.add(menu)

        menu.add(JMenuItem(object: AbstractAction("Add Texture") {
            override fun actionPerformed(e: ActionEvent) {
                val currentDir = System.getProperty("user.dir")
                val assetDir = Paths.get(currentDir, "../assets/ProgrammerArt-v3.0/textures/blocks")
                    .toAbsolutePath()
                    .toString()

                val chooser = JFileChooser(assetDir)
                chooser.isMultiSelectionEnabled = true

                val result = chooser.showOpenDialog(parent)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val files = chooser.selectedFiles
                    var bitmaps: Array<Bitmap?>? = arrayOfNulls(files.size)

                    try {
                        for (i in bitmaps!!.indices) {
                            bitmaps[i] = Bitmap.fromFile(files[i].canonicalPath)
                        }
                    }
                    catch (e1: Exception) {
                        JOptionPane.showMessageDialog(
                            parent,
                            "Unable to open file.",
                            TITLE,
                            JOptionPane.ERROR_MESSAGE
                        )
                        bitmaps = null
                    }

                    if (bitmaps != null) {
                        for (bitmap in bitmaps) {
                            atlasBuilder.addBitmap(bitmap)
                        }
                        val newAtlas = atlasBuilder.build()
                        viewport.bitmap = newAtlas
                    }
                }
            }
        }))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TexturePacker()
        }
    }
}