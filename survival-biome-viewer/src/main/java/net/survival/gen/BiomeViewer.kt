package net.survival.gen

import java.awt.event.ActionEvent
import javax.swing.*

private const val TITLE = "Biome Viewer"

class BiomeViewer private constructor(seed: Long): JFrame() {
    private val viewport: ViewportComponent = ViewportComponent()

    init {
        addKeyListener(viewport)

        jMenuBar = JMenuBar()
        createGeneratorMenu()
        setSize(800, 600)

        title = TITLE
        isVisible = true
        extendedState = extendedState or MAXIMIZED_BOTH
        defaultCloseOperation = EXIT_ON_CLOSE
        requestFocusInWindow()

        viewport.size = size
        add(viewport)
    }

    private fun createGeneratorMenu() {
        val parent = this
        val menu = JMenu("Generator")
        jMenuBar.add(menu)

        menu.add(JMenuItem(object: AbstractAction("Set Seed") {
            override fun actionPerformed(e: ActionEvent) {
                JOptionPane.showMessageDialog(parent, "Not implemented yet.", "Set Seed", JOptionPane.WARNING_MESSAGE)
            }
        }))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            BiomeViewer(0L)
        }
    }
}