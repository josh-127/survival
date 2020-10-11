package net.survival.client

import kotlin.Throws
import java.lang.InterruptedException
import kotlin.jvm.JvmStatic
import java.util.concurrent.atomic.AtomicBoolean
import java.lang.Runnable
import net.survival.graphics.opengl.GLDisplay
import net.survival.client.input.GlfwKeyboardAdapter
import net.survival.client.input.GlfwMouseAdapter
import net.survival.graphics.CompositeDisplay
import net.survival.graphics.RenderClient
import net.survival.graphics.RenderCommand
import org.lwjgl.glfw.GLFW
import net.survival.graphics.opengl.GLRenderContext
import java.util.concurrent.ConcurrentLinkedQueue

private const val TICKS_PER_SECOND = 60.0
private const val SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND
private const val NANOS_PER_TICK = SECONDS_PER_TICK * 1000000000.0

private const val WINDOW_WIDTH = 1600
private const val WINDOW_HEIGHT = 900
private const val WINDOW_TITLE = "Survival"

object Main {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val shouldQuit = AtomicBoolean()
        val renderClient = RenderClientImpl()

        val tick = Tick(shouldQuit, renderClient)
        val render = Render(shouldQuit, renderClient)
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

private class RenderClientImpl: RenderClient {
    private val renderQueue = ConcurrentLinkedQueue<RenderCommand>()
    private val shouldRender = AtomicBoolean()

    override fun send(command: RenderCommand) { renderQueue.add(command) }
    override fun present() = shouldRender.set(true)

    fun poll(): RenderCommand? = renderQueue.poll()
    fun shouldPresent(): Boolean = shouldRender.getAndSet(false)
}

private class Tick(
    private val shouldQuit: AtomicBoolean,
    private val renderClient: RenderClientImpl
): Runnable {
    private var now: Long = 0L
    private var prevTime: Long = 0L
    private var unprocessedTicks: Double = 0.0

    override fun run() {
        resetTimer()

        while (true) {
            tick {
                renderClient.send(RenderCommand.MoveCamera(0.0f, 0.0f, 0.0f))
                renderClient.send(RenderCommand.OrientCamera(0.0f, 0.0f, 0.0f))
                renderClient.send(RenderCommand.SetCameraParams(
                    Math.toRadians(60.0).toFloat(),
                    WINDOW_WIDTH.toFloat(),
                    WINDOW_HEIGHT.toFloat(),
                    1.0f / 16.0f,
                    1536.0f
                ))
            }
        }

        shouldQuit.set(true)
    }

    private fun resetTimer() {
        now = System.nanoTime()
        prevTime = now
        unprocessedTicks = 0.0
    }

    private fun tick(tickFunc: (Double) -> Unit) {
        now = System.nanoTime()
        unprocessedTicks += (now - prevTime) / NANOS_PER_TICK
        prevTime = now

        if (unprocessedTicks >= 1.0) {
            while (unprocessedTicks >= 1.0) {
                unprocessedTicks -= 1.0
                tickFunc(SECONDS_PER_TICK)
            }
            renderClient.present()
        }
    }
}

private class Render(
    private val shouldQuit: AtomicBoolean,
    private val renderClient: RenderClientImpl
): Runnable {
    private lateinit var display: GLDisplay
    private lateinit var keyboardAdapter: GlfwKeyboardAdapter
    private lateinit var mouseAdapter: GlfwMouseAdapter

    private var frameCounter: Int = 0
    private var frameRateTimer: Long = 0L
    private var frameRate: Int = 0

    override fun run() {
        display = GLDisplay(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE)
        keyboardAdapter = GlfwKeyboardAdapter()
        mouseAdapter = GlfwMouseAdapter(display.underlyingGlfwWindow)
        GLFW.glfwSetKeyCallback(display.underlyingGlfwWindow, keyboardAdapter)
        GLFW.glfwSetCursorPosCallback(display.underlyingGlfwWindow, mouseAdapter)
        GLRenderContext.init()

        val compositeDisplay = CompositeDisplay(WINDOW_WIDTH, WINDOW_HEIGHT)

        resetTimer()
        while (!shouldQuit.get()) {
            render {
                var cmd = renderClient.poll()
                while (cmd != null) {
                    when (cmd) {
                        is RenderCommand.DrawModel -> compositeDisplay.drawModel(
                            cmd.x, cmd.y, cmd.z,
                            cmd.yaw, cmd.pitch, cmd.roll,
                            cmd.scaleX, cmd.scaleY, cmd.scaleZ,
                            cmd.modelType
                        )
                        is RenderCommand.SetColumn -> compositeDisplay.setColumn(
                            cmd.columnPos, cmd.column, cmd.invalidationPriority
                        )
                        is RenderCommand.MoveCamera -> compositeDisplay.moveCamera(
                            cmd.x, cmd.y, cmd.z
                        )
                        is RenderCommand.OrientCamera -> compositeDisplay.orientCamera(
                            cmd.yaw, cmd.pitch
                        )
                        is RenderCommand.SetCameraParams -> compositeDisplay.setCameraParams(
                            cmd.fov, cmd.width, cmd.height, cmd.nearClipPlane, cmd.farClipPlane
                        )
                        is RenderCommand.SetCloudParams -> compositeDisplay.setCloudParams(
                            cmd.seed, cmd.density, cmd.elevation, cmd.speedX, cmd.speedZ, cmd.alpha
                        )
                        is RenderCommand.SetSkyColor -> compositeDisplay.setSkyColor(
                            cmd.br, cmd.bg, cmd.bb,
                            cmd.mr, cmd.mg, cmd.mb,
                            cmd.tr, cmd.tg, cmd.tb
                        )
                        is RenderCommand.DrawText -> compositeDisplay.drawText(
                            cmd.text, cmd.fontSize, cmd.x, cmd.z
                        )
                    }
                    cmd = renderClient.poll()
                }

                compositeDisplay.display()
            }
        }
    }

    private fun resetTimer() {
        frameCounter = 0
        frameRateTimer = System.currentTimeMillis()
        frameRate = 0
    }

    private fun render(renderFunc: (Int) -> Unit) {
        if (renderClient.shouldPresent()) {
            renderFunc(frameRate)
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

private class WorldSave(private val shouldQuit: AtomicBoolean): Runnable {
    override fun run() {
        while (!shouldQuit.get()) {}
    }
}
