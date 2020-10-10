package net.survival.client

import kotlin.Throws
import java.lang.InterruptedException
import kotlin.jvm.JvmStatic
import java.util.concurrent.atomic.AtomicBoolean
import java.lang.Runnable
import net.survival.world.WorldScript
import net.survival.graphics.opengl.GLDisplay
import net.survival.client.input.GlfwKeyboardAdapter
import net.survival.client.input.GlfwMouseAdapter
import org.lwjgl.glfw.GLFW
import net.survival.graphics.opengl.GLRenderContext

private const val TICKS_PER_SECOND = 60.0
private const val SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND
private const val NANOS_PER_TICK = SECONDS_PER_TICK * 1000000000.0

private const val WINDOW_WIDTH = 1600;
private const val WINDOW_HEIGHT = 900;
private const val WINDOW_TITLE = "Survival"

object Main {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val shouldQuit = AtomicBoolean()
        val shouldRender = AtomicBoolean()

        val tick = Tick(shouldQuit, shouldRender)
        val render = Render(shouldQuit, shouldRender)
        val worldSave = WorldSave(shouldQuit)

        val tickThread = Thread(tick, "Tick")
        val renderThread = Thread(render, "Render")
        val worldSaveThread = Thread(worldSave, "WorldSave")

        tickThread.start()
        renderThread.start()
        worldSaveThread.start()

        tickThread.join()
        renderThread.join()
        worldSaveThread.join()
    }
}

private class Tick(
    private val shouldQuit: AtomicBoolean,
    private val shouldRender: AtomicBoolean
): Runnable {
    override fun run() {
        var now = System.nanoTime()
        var prevTime = now
        var unprocessedTicks = 0.0
        var script: WorldScript? = object: WorldScript {
            override fun runCycle(): WorldScript {
                return this
            }
        }
        while (script != null) {
            now = System.nanoTime()
            unprocessedTicks += (now - prevTime) / NANOS_PER_TICK
            prevTime = now
            if (unprocessedTicks >= 1.0) {
                while (unprocessedTicks >= 1.0) {
                    unprocessedTicks -= 1.0
                    script = script!!.runCycle()
                }
                shouldRender.set(true)
            }
        }
        shouldQuit.set(true)
    }
}

private class Render(
    private val shouldQuit: AtomicBoolean,
    private val shouldRender: AtomicBoolean
): Runnable {
    override fun run() {
        val display = GLDisplay(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE)
        val keyboardAdapter = GlfwKeyboardAdapter()
        val mouseAdapter = GlfwMouseAdapter(display.underlyingGlfwWindow)
        GLFW.glfwSetKeyCallback(display.underlyingGlfwWindow, keyboardAdapter)
        GLFW.glfwSetCursorPosCallback(display.underlyingGlfwWindow, mouseAdapter)

        GLRenderContext.init()

        var frameCounter = 0
        var frameRateTimer = System.currentTimeMillis()
        var frameRate = 0

        while (!shouldQuit.get()) {
            if (shouldRender.getAndSet(false)) {
                display.swapBuffers()
            }
            if (System.currentTimeMillis() - frameRateTimer > 1000) {
                frameRateTimer += 1000
                frameRate = frameCounter
                frameCounter = 0
            }
            GLDisplay.pollEvents()
        }
    }
}

private class WorldSave(private val shouldQuit: AtomicBoolean): Runnable {
    override fun run() {
        while (!shouldQuit.get()) {
        }
    }
}
