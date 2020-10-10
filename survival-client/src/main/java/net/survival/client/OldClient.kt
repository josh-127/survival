package net.survival.client

import net.survival.actor.Actor
import net.survival.actor.Physics
import net.survival.block.ColumnPos
import net.survival.block.StandardBlocks
import net.survival.block.io.ColumnDbPipe
import net.survival.block.io.ColumnDbPipe.ClientSide
import net.survival.block.io.ColumnServer
import net.survival.block.message.CloseColumnRequest
import net.survival.client.input.*
import net.survival.gen.DefaultColumnGenerator
import net.survival.graphics.ColumnInvalidationPriority
import net.survival.graphics.CompositeDisplay
import net.survival.graphics.VisibilityFlags
import net.survival.graphics.opengl.GLDisplay
import net.survival.graphics.opengl.GLRenderContext
import net.survival.graphics.particle.ClientParticleSpace
import net.survival.util.HitBox
import net.survival.world.ColumnIO
import net.survival.world.World
import org.lwjgl.glfw.GLFW
import java.io.File
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

private const val WINDOW_WIDTH = 1600
private const val WINDOW_HEIGHT = 900
private const val WINDOW_TITLE = "Survival"
private const val TICKS_PER_SECOND = 60.0
private const val SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND
private const val MILLIS_PER_TICK = SECONDS_PER_TICK * 1000.0
private const val SAVE_INTERVAL = 10.0

private class OldClient(columnPipe: ClientSide): AutoCloseable {
    private val particleSpace = ClientParticleSpace()
    private val compositeDisplay: CompositeDisplay
    private val world = World()
    private val player = Actor().apply {
        x = 60.0; y = 128.0; z = 20.0
        hitBox = HitBox.PLAYER
        movementSpeed = 5.0
    }
    private val fpvCamera = FpvCamera(0.0, -1.0)
    private val columnIO: ColumnIO
    private var saveTimer = 0.0

    init {
        compositeDisplay = CompositeDisplay(particleSpace, WINDOW_WIDTH, WINDOW_HEIGHT)
        columnIO = ColumnIO(columnPipe)
    }

    override fun close() {
        compositeDisplay.close()
    }

    fun tick(elapsedTime: Double, frameRate: Int) {
        // Loading/Saving Columns
        saveTimer -= elapsedTime
        if (saveTimer <= 0.0) {
            saveTimer = SAVE_INTERVAL
            columnIO.maskColumns(ColumnMaskFactory.createCircle(12, player.x, player.z))
        }
        columnIO.update()
        world.columns.clear()
        world.columns.putAll(columnIO.columns)

        // Misc. Code.
        val pi = Math.PI
        val cursorDX = Mouse.deltaX; val cursorDY = Mouse.deltaY
        var jsX = 0.0; var jsZ = 0.0; val camYaw = fpvCamera.yaw
        if (Keyboard.isKeyDown(Key.W)) { jsX += sin(camYaw); jsZ -= cos(camYaw) }
        if (Keyboard.isKeyDown(Key.S)) { jsX += sin(camYaw + pi); jsZ -= cos(camYaw + pi) }
        if (Keyboard.isKeyDown(Key.A)) { jsX += sin(camYaw - pi / 2.0); jsZ -= cos(camYaw - pi / 2.0) }
        if (Keyboard.isKeyDown(Key.D)) { jsX += sin(camYaw + pi / 2.0); jsZ -= cos(camYaw + pi / 2.0) }
        if (Mouse.mode == Mouse.MODE_CENTERED) fpvCamera.rotate(-cursorDX / 128.0, -cursorDY / 128.0)
        player.move(jsX, jsZ)
        if (Keyboard.isKeyPressed(Key.SPACE)) player.jump(1.1)
        temporaryTestCode()
        Physics.tick(player, world, elapsedTime)

        for ((columnPos, column) in world.columns) {
            if (column.isNew) {
                compositeDisplay.invalidateColumn(columnPos, column, ColumnInvalidationPriority.LOW)
                column.clearNewFlag()
            }
            else if (column.isModified) {
                compositeDisplay.invalidateColumn(columnPos, column, ColumnInvalidationPriority.LOW)
                column.clearModifiedFlag()
            }
        }

        compositeDisplay.moveCamera(player.x.toFloat(), (player.y + 1.0).toFloat(), player.z.toFloat())
        compositeDisplay.orientCamera(fpvCamera.yaw.toFloat(), fpvCamera.pitch.toFloat())
        compositeDisplay.setCameraParams(Math.toRadians(60.0).toFloat(), WINDOW_WIDTH.toFloat(), WINDOW_HEIGHT.toFloat(), 0.0625f, 1536.0f)
        compositeDisplay.drawLabel(String.format("Memory Usage: %.2f MiB", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0 / 1024.0), 3.0, 0.0, 0.0)
        compositeDisplay.drawLabel(String.format("Frame Rate: %d FPS", frameRate), 3.0, 0.0, 1.1)
        compositeDisplay.drawLabel(String.format("X: %d", floor(player.x).toInt()), 1.0, 0.0, 2.2)
        compositeDisplay.drawLabel(String.format("Y: %d", floor(player.y).toInt()), 1.0, 0.0, 3.3)
        compositeDisplay.drawLabel(String.format("Z: %d", floor(player.z).toInt()), 1.0, 0.0, 4.4)
    }

    fun render() {
        compositeDisplay.display()
    }

    private fun temporaryTestCode() {
        if (Mouse.isLmbPressed || Mouse.isRmbPressed) {
            var px = player.x
            var py = player.y + 1.0
            var pz = player.z
            val delta = 0.0078125
            var zz = 0.0
            while (zz < 7.0) {
                px += delta * sin(fpvCamera.yaw) * cos(fpvCamera.pitch)
                py += delta * sin(fpvCamera.pitch)
                pz -= delta * cos(fpvCamera.yaw) * cos(fpvCamera.pitch)
                val pxi = floor(px).toInt()
                val pyi = floor(py).toInt()
                val pzi = floor(pz).toInt()
                val cx = ColumnPos.toColumnX(pxi)
                val cz = ColumnPos.toColumnZ(pzi)
                if (world.columns.containsKey(ColumnPos.hashPos(cx, cz)) && pyi >= 0.0 && world.getBlock(pxi, pyi, pzi) != StandardBlocks.AIR) {
                    if (Mouse.isLmbPressed) world.setBlock(pxi, pyi, pzi, StandardBlocks.AIR)
                    break
                }
                zz += delta
            }
        }

        // Visibility Flags
        if (Keyboard.isKeyPressed(Key._1)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.BLOCKS)
        if (Keyboard.isKeyPressed(Key._2)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.ENTITIES)
        if (Keyboard.isKeyPressed(Key._3)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.PARTICLES)
        if (Keyboard.isKeyPressed(Key._4)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.SKYBOX)
        if (Keyboard.isKeyPressed(Key._5)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.CLOUDS)
        if (Keyboard.isKeyPressed(Key._6)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.HUD)
        if (Keyboard.isKeyPressed(Key._7)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.DEBUG_GEOMETRY)

        // Mouse Control
        if (Keyboard.isKeyPressed(Key.TAB)) {
            if (Mouse.mode == Mouse.MODE_NORMAL) Mouse.changeMode(Mouse.MODE_CENTERED)
            else Mouse.changeMode(Mouse.MODE_NORMAL)
        }
    }

}

object OldMain {
    @JvmStatic
    fun main(args: Array<String>) {
        val columnGenerator = DefaultColumnGenerator(4000L)
        val columnDbPipe = ColumnDbPipe()

        val columnServer = ColumnServer(
            File(System.getProperty("user.dir") + "/.world/columns"),
            columnDbPipe.serverSide,
            columnGenerator
        )
        val columnServerThread = Thread(columnServer)
        columnServerThread.start()

        val display = GLDisplay(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE)
        val keyboardAdapter = GlfwKeyboardAdapter()
        val mouseAdapter = GlfwMouseAdapter(display.underlyingGlfwWindow)
        GLFW.glfwSetKeyCallback(display.underlyingGlfwWindow, keyboardAdapter)
        GLFW.glfwSetCursorPosCallback(display.underlyingGlfwWindow, mouseAdapter)

        GLRenderContext.init()
        val program = OldClient(columnDbPipe.clientSide)

        var now = System.currentTimeMillis()
        var prevTime = now
        var unprocessedTicks = 0.0
        var frameCounter = 0
        var frameRateTimer = System.currentTimeMillis()
        var frameRate = 0
        var running = true

        while (running) {
            now = System.currentTimeMillis()
            unprocessedTicks += (now - prevTime) / MILLIS_PER_TICK
            prevTime = now

            if (unprocessedTicks >= 1.0) {
                while (unprocessedTicks >= 1.0) {
                    mouseAdapter.tick()
                    program.tick(SECONDS_PER_TICK, frameRate)
                    keyboardAdapter.nextInputFrame()
                    unprocessedTicks -= 1.0
                }
                ++frameCounter
                program.render()
                display.swapBuffers()
            }

            if (System.currentTimeMillis() - frameRateTimer > 1000) {
                frameRateTimer += 1000
                frameRate = frameCounter
                frameCounter = 0
            }

            GLDisplay.pollEvents()
            running = Keyboard.isKeyUp(Key.ESCAPE) && !display.shouldClose()
        }

        program.close()
        display.close()
        columnDbPipe.clientSide.request(CloseColumnRequest())
        columnServerThread.join()
    }
}
