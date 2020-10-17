package net.survival.client

import net.survival.graphics.*
import net.survival.graphics.opengl.GLDisplay
import net.survival.graphics.opengl.GLRenderContext
import net.survival.render.ModelType
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

private const val TICKS_PER_SECOND = 60.0
private const val SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND
private const val NANOS_PER_TICK = SECONDS_PER_TICK * 1000000000.0

private const val WINDOW_WIDTH = 1600
private const val WINDOW_HEIGHT = 900
private const val WINDOW_TITLE = "Survival"

private const val ASSET_ROOT_PATH = "./assets/"
private const val TEXTURE_PATH = ASSET_ROOT_PATH
private const val FONT_PATH = "textures/fonts/default"
private const val PIXELS_PER_EM = 24

object Main {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val shouldQuit = AtomicBoolean()
        val renderClient = RenderClientImpl()
        val keyboard = MutableKeyboard()
        val mouse = MutableMouse()

        val tick = Tick(shouldQuit, renderClient, keyboard, mouse)
        val render = Render(shouldQuit, renderClient, keyboard, mouse)
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
    private val renderClient: RenderClientImpl,
    private val keyboard: Keyboard,
    private val mouse: Mouse
): Runnable {
    private var now: Long = 0L
    private var prevTime: Long = 0L
    private var unprocessedTicks: Double = 0.0

    override fun run() {
        var cameraX = 0.0f

        resetAndTickWhile({ !keyboard.isKeyDown(Key.ESCAPE) }) {
            if (keyboard.isKeyDown(Key.LEFT)) {
                cameraX -= 0.1f
            }
            else if (keyboard.isKeyDown(Key.RIGHT)) {
                cameraX += 0.1f
            }

            renderClient.send(RenderCommand.SetProjectionMatrix(
                Matrix4f().apply {
                    perspective(
                        Math.toRadians(60.0).toFloat(),
                        WINDOW_WIDTH.toFloat() / WINDOW_HEIGHT.toFloat(),
                        1.0f / 16.0f,
                        1536.0f
                    )
                }
            ))
            renderClient.send(RenderCommand.DrawSkybox(
                0.8f, 1.0f, 1.0f,
                0.8f, 1.0f, 1.0f,
                0.25f, 0.5f, 1.0f
            ))
            renderClient.send(RenderCommand.PushMatrix(
                Matrix4f().apply {
                    lookAt(
                        cameraX, 2.0f, -5.0f,
                        0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f,
                    )
                }
            ))
            renderClient.send(RenderCommand.DrawModel(
                ModelType.HUMAN
            ))
            renderClient.send(RenderCommand.PopMatrix)
            renderClient.send(RenderCommand.DrawClouds)
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

    private fun resetAndTickWhile(condition: () -> Boolean, tickFunc: (Double) -> Unit) {
        resetTimer()
        while (condition()) {
            tick(tickFunc)
        }
    }
}

private class Render(
    private val shouldQuit: AtomicBoolean,
    private val renderClient: RenderClientImpl,
    private val keyboard: MutableKeyboard,
    private val mouse: MutableMouse
): Runnable {
    private lateinit var display: GLDisplay
    private lateinit var keyboardAdapter: GlfwKeyboardAdapter
    private lateinit var mouseAdapter: GlfwMouseAdapter

    private var frameCounter: Int = 0
    private var frameRateTimer: Long = 0L
    private var frameRate: Int = 0

    override fun run() {
        display = GLDisplay(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE)
        keyboardAdapter = GlfwKeyboardAdapter(keyboard)
        mouseAdapter = GlfwMouseAdapter(display.underlyingGlfwWindow, mouse)
        GLFW.glfwSetKeyCallback(display.underlyingGlfwWindow, keyboardAdapter)
        GLFW.glfwSetCursorPosCallback(display.underlyingGlfwWindow, mouseAdapter)

        GLRenderContext.init()
        Assets.setup(
            TextureAtlas(TEXTURE_PATH, true),
            TextureAtlas(TEXTURE_PATH, false)
        )
        val renderer = RenderServer()

        resetTimer()
        while (!shouldQuit.get()) {
            render {
                Assets.getMipmappedTextureAtlas().updateTextures()
                Assets.getTextureAtlas().updateTextures()

                GLRenderContext.clearColorBuffer(0.0f, 0.0f, 0.0f, 0.0f)
                GLRenderContext.clearDepthBuffer(1.0f)

                var cmd = renderClient.poll()
                while (cmd != null) {
                    when (cmd) {
                        is RenderCommand.SetProjectionMatrix -> renderer.setProjectionMatrix(cmd.matrix)
                        is RenderCommand.PushMatrix -> renderer.pushMatrix(cmd.matrix)
                        RenderCommand.PopMatrix -> renderer.popMatrix()
                        is RenderCommand.DrawModel -> renderer.drawModel(cmd.modelType)
                        is RenderCommand.SetColumn -> {}
                        RenderCommand.DrawClouds -> renderer.drawClouds()
                        is RenderCommand.DrawSkybox -> renderer.drawSkybox(
                            cmd.br, cmd.bg, cmd.bb,
                            cmd.mr, cmd.mg, cmd.mb,
                            cmd.tr, cmd.tg, cmd.tb,
                        )
                        is RenderCommand.DrawText -> renderer.drawText(
                            cmd.text, cmd.x, cmd.z
                        )
                    }
                    cmd = renderClient.poll()
                }
            }
        }
    }

    private fun resetTimer() {
        frameCounter = 0
        frameRateTimer = System.currentTimeMillis()
        frameRate = 0
    }

    private fun render(renderFunc: (Int) -> Unit) {
        GLDisplay.pollEvents()
        keyboardAdapter.swapBuffers()
        mouseAdapter.swapBuffers()

        if (renderClient.shouldPresent()) {
            renderFunc(frameRate)
            display.swapBuffers()
        }

        if (System.currentTimeMillis() - frameRateTimer > 1000) {
            frameRateTimer += 1000
            frameRate = frameCounter
            frameCounter = 0
        }
    }
}

private class WorldSave(private val shouldQuit: AtomicBoolean): Runnable {
    override fun run() {
        while (!shouldQuit.get()) {}
    }
}
