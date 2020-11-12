package net.survival.client

import net.survival.actor.Actor
import net.survival.block.ColumnPos
import net.survival.gen.ColumnGenerator
import net.survival.graphics.*
import net.survival.graphics.opengl.GLDisplay
import net.survival.graphics.opengl.GLRenderContext
import net.survival.player.Player
import net.survival.render.ModelType
import net.survival.world.TransientWorld
import net.survival.world.WorldProcess
import net.survival.world.WorldProcessCommand
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

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
        val world = TransientWorld().apply {
            val columnGenerator = ColumnGenerator(0L)
            for (z in -20..19) {
                for (x in -20..19) {
                    val columnPos = ColumnPos.hashPos(x, z)
                    val column = columnGenerator.generate(columnPos)
                    setColumn(columnPos, column)
                }
            }
        }

        val actorId = 1L
        val playerUuid = UUID.randomUUID()
        world.insertActor(Actor(actorId).apply { y = 80.0; movementSpeed = 3.0 })
        world.insertPlayer(Player(playerUuid, "Player", "Player", actorId))

        val worldProcess = WorldProcess(world)
        val cam = FpvCamera(
            0.0, 0.0, 0.0,
            yaw = 0.0,
            pitch = -1.0,
            fov = Math.toRadians(60.0),
            width = WINDOW_WIDTH.toDouble(),
            height = WINDOW_HEIGHT.toDouble(),
            nearClipPlane = 1.0 / 16.0,
            farClipPlane = 1536.0,
        )

        resetAndTickWhile({ !keyboard.isKeyDown(Key.ESCAPE) }) {
            // Initialize command list
            val commandList = ArrayList<WorldProcessCommand>(8)

            var jsx = 0.0; var jsz = 0.0
            if (keyboard.isKeyDown(Key.W)) { jsx += sin(cam.yaw); jsz -= cos(cam.yaw) }
            if (keyboard.isKeyDown(Key.S)) { jsx += sin(cam.yaw + PI); jsz -= cos(cam.yaw + PI) }
            if (keyboard.isKeyDown(Key.A)) { jsx += sin(cam.yaw - PI / 2.0); jsz -= cos(cam.yaw - PI / 2.0) }
            if (keyboard.isKeyDown(Key.D)) { jsx += sin(cam.yaw + PI / 2.0); jsz -= cos(cam.yaw + PI / 2.0) }
            commandList.add(WorldProcessCommand.ControlPlayer(
                playerUuid, jsx, jsz, keyboard.isKeyPressed(Key.SPACE)
            ))
            val actor = world.getActor(actorId)
            cam.moveTo(actor.x, actor.y, actor.z)
            cam.rotate(-mouse.deltaX / 128.0, -mouse.deltaY / 128.0)

            // Tick World
            world.getAndResetChanges()
            worldProcess.tick(commandList, SECONDS_PER_TICK)

            // Render
            renderClient.send(RenderCommand.SetProjectionMatrix(cam.getProjectionMatrix()))
            renderClient.send(RenderCommand.PushMatrix(cam.getViewMatrix()))
            renderClient.send(RenderCommand.DrawSkybox(0.8f, 1.0f, 1.0f, 0.8f, 1.0f, 1.0f, 0.25f, 0.5f, 1.0f))
            renderClient.send(RenderCommand.DrawClouds)
            renderClient.send(RenderCommand.PushMatrix(Matrix4f().apply { translate(0.0f, 72.0f, 0.0f) }))
            renderClient.send(RenderCommand.DrawModel(ModelType.HUMAN))
            renderClient.send(RenderCommand.PopMatrix)
            renderClient.send(RenderCommand.PopMatrix)
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

        if (renderClient.shouldPresent()) {
            keyboardAdapter.swapBuffers()
            mouseAdapter.swapBuffers()
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
