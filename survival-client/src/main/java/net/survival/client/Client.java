package net.survival.client;

import java.io.File;

import net.survival.actor.Actor;
import net.survival.block.io.ColumnDbPipe;
import net.survival.block.io.ColumnServer;
import net.survival.block.StandardBlocks;
import org.lwjgl.glfw.GLFW;

import net.survival.actor.Physics;
import net.survival.block.ColumnPos;
import net.survival.block.message.CloseColumnRequest;
import net.survival.client.input.GlfwKeyboardAdapter;
import net.survival.client.input.GlfwMouseAdapter;
import net.survival.client.input.Key;
import net.survival.client.input.Keyboard;
import net.survival.client.input.Mouse;
import net.survival.gen.DefaultColumnGenerator;
import net.survival.graphics.ColumnInvalidationPriority;
import net.survival.graphics.CompositeDisplay;
import net.survival.graphics.VisibilityFlags;
import net.survival.graphics.opengl.GLDisplay;
import net.survival.graphics.opengl.GLRenderContext;
import net.survival.graphics.particle.ClientParticleSpace;
import net.survival.util.HitBox;
import net.survival.world.ColumnIO;
import net.survival.world.World;

public class Client implements AutoCloseable {
    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 900;
    private static final String WINDOW_TITLE = "Survival";

    private static final double TICKS_PER_SECOND = 60.0;
    private static final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;

    private final ClientParticleSpace particleSpace = new ClientParticleSpace();
    private final CompositeDisplay compositeDisplay;
    private final World world = new World();
    private final Actor player = new Actor();
    private final FpvCamera fpvCamera = new FpvCamera(0.0f, -1.0f);

    private final ColumnIO columnIO;

    private static final double SAVE_INTERVAL = 30.0;
    private double saveTimer;

    private Client(ColumnDbPipe.ClientSide columnPipe) {
        compositeDisplay = new CompositeDisplay(particleSpace, WINDOW_WIDTH, WINDOW_HEIGHT);
        player.x = 60.0;
        player.y = 128.0;
        player.z = 20.0;
        player.hitBox = HitBox.PLAYER;
        player.movementSpeed = 5.0;

        columnIO = new ColumnIO(columnPipe);
    }

    @Override
    public void close() throws RuntimeException {
        compositeDisplay.close();
    }

    public void tick(double elapsedTime, int frameRate) {
        // Loading/Saving Columns
        saveTimer -= elapsedTime;
        if (saveTimer <= 0.0) {
            saveTimer = SAVE_INTERVAL;
            columnIO.maskColumns(ColumnMaskFactory.createCircle(12, player.x, player.z));
        }

        columnIO.update();
        world.getColumns().clear();
        world.getColumns().putAll(columnIO.getColumns());

        // Misc. Code.
        final var PI = Math.PI;
        var cursorDX = Mouse.getDeltaX(); var cursorDY = Mouse.getDeltaY();
        var jsX = 0.0; var jsZ = 0.0; var camYaw = fpvCamera.yaw;
        if (Keyboard.isKeyDown(Key.W)) { jsX += Math.sin(camYaw); jsZ -= Math.cos(camYaw); }
        if (Keyboard.isKeyDown(Key.S)) { jsX += Math.sin(camYaw + PI); jsZ -= Math.cos(camYaw + PI); }
        if (Keyboard.isKeyDown(Key.A)) { jsX += Math.sin(camYaw - PI / 2.0); jsZ -= Math.cos(camYaw - PI / 2.0); }
        if (Keyboard.isKeyDown(Key.D)) { jsX += Math.sin(camYaw + PI / 2.0); jsZ -= Math.cos(camYaw + PI / 2.0); }

        if (Mouse.getMode() == Mouse.MODE_CENTERED) fpvCamera.rotate(-cursorDX / 128.0, -cursorDY / 128.0);
        player.move(jsX, jsZ);
        if (Keyboard.isKeyPressed(Key.SPACE)) player.jump(1.1);

        temporaryTestCode(elapsedTime);
        
        Physics.tick(player, world, elapsedTime);
        
        for (var entry : world.getColumns().entrySet()) {
            var columnPos = entry.getKey();
            var column = entry.getValue();
            if (column.isNew()) {
                compositeDisplay.invalidateColumn(columnPos, column, ColumnInvalidationPriority.LOW);
                column.clearNewFlag();
            }
            else if (column.isModified()) {
                compositeDisplay.invalidateColumn(columnPos, column, ColumnInvalidationPriority.LOW);
                column.clearModifiedFlag();
            }
        }
        
        compositeDisplay.moveCamera((float) player.x, (float) (player.y + 1.0), (float) player.z);
        compositeDisplay.orientCamera((float) fpvCamera.yaw, (float) fpvCamera.pitch);
        compositeDisplay.setCameraParams((float) Math.toRadians(60.0), WINDOW_WIDTH, WINDOW_HEIGHT, 0.0625f, 1536.0f);
        
        compositeDisplay.drawLabel(String.format("Memory Usage: %.2f MiB", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0 / 1024.0), 3.0, 0.0, 0.0);
        compositeDisplay.drawLabel(String.format("Frame Rate: %d FPS", frameRate), 3.0, 0.0, 1.1);
        compositeDisplay.drawLabel(String.format("X: %d", (int) Math.floor(player.x)), 1.0, 0.0, 2.2);
        compositeDisplay.drawLabel(String.format("Y: %d", (int) Math.floor(player.y)), 1.0, 0.0, 3.3);
        compositeDisplay.drawLabel(String.format("Z: %d", (int) Math.floor(player.z)), 1.0, 0.0, 4.4);
    }

    private void render() {
        compositeDisplay.display();
    }

    public static void main(String[] args) throws InterruptedException {
        var columnGenerator = new DefaultColumnGenerator(4000L);

        var columnDbPipe = new ColumnDbPipe();
        var columnServer = new ColumnServer(
                new File(System.getProperty("user.dir") + "/.world/columns"),
                columnDbPipe.getServerSide(),
                columnGenerator);

        var columnServerThread = new Thread(columnServer);
        columnServerThread.start();

        var display = new GLDisplay(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE);
        var keyboardAdapter = new GlfwKeyboardAdapter();
        var mouseAdapter = new GlfwMouseAdapter(display.getUnderlyingGlfwWindow());
        GLFW.glfwSetKeyCallback(display.getUnderlyingGlfwWindow(), keyboardAdapter);
        GLFW.glfwSetCursorPosCallback(display.getUnderlyingGlfwWindow(), mouseAdapter);
        GLRenderContext.init();

        var program = new Client(columnDbPipe.getClientSide());

        final var MILLIS_PER_TICK = SECONDS_PER_TICK * 1000.0;
        var now = System.currentTimeMillis();
        var prevTime = now;
        var unprocessedTicks = 0.0;

        var frameCounter = 0;
        var frameRateTimer = System.currentTimeMillis();
        var frameRate = 0;

        for (var running = true; running; running = Keyboard.isKeyUp(Key.ESCAPE)
                && !display.shouldClose())
        {
            now = System.currentTimeMillis();
            unprocessedTicks += (now - prevTime) / MILLIS_PER_TICK;
            prevTime = now;

            if (unprocessedTicks >= 1.0) {
                while (unprocessedTicks >= 1.0) {
                    mouseAdapter.tick();
                    program.tick(SECONDS_PER_TICK, frameRate);
                    keyboardAdapter.nextInputFrame();
                    unprocessedTicks -= 1.0;
                }

                ++frameCounter;
                program.render();
                display.swapBuffers();
            }

            if (System.currentTimeMillis() - frameRateTimer > 1000) {
                frameRateTimer += 1000;
                frameRate = frameCounter;
                frameCounter = 0;
            }

            GLDisplay.pollEvents();
        }

        program.close();
        display.close();

        columnDbPipe.getClientSide().request(new CloseColumnRequest());
        columnServerThread.join();
    }

    private void temporaryTestCode(double elapsedTime) {
        if (Mouse.isLmbPressed() || Mouse.isRmbPressed()) {
            var px = player.x; var py = player.y + 1.0; var pz = player.z;
            final var DELTA = 0.0078125;
            for (var zz = 0.0; zz < 7.0; zz += DELTA) {
                px += DELTA * Math.sin(fpvCamera.yaw) * Math.cos(fpvCamera.pitch);
                py += DELTA * Math.sin(fpvCamera.pitch);
                pz -= DELTA * Math.cos(fpvCamera.yaw) * Math.cos(fpvCamera.pitch);
                var pxi = (int) Math.floor(px); var pyi = (int) Math.floor(py); var pzi = (int) Math.floor(pz);
                var cx = ColumnPos.toColumnX(pxi); var cz = ColumnPos.toColumnZ(pzi);
                if (world.getColumns().containsKey(ColumnPos.hashPos(cx, cz)) && pyi >= 0.0 && !(world.getBlock(pxi, pyi, pzi).equals(StandardBlocks.AIR))) {
                    if (Mouse.isLmbPressed()) world.setBlock(pxi, pyi, pzi, StandardBlocks.AIR);
                    break;
                }
            }
        }

        // Visibility Flags
        if (Keyboard.isKeyPressed(Key._1)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.BLOCKS);
        if (Keyboard.isKeyPressed(Key._2)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.ENTITIES);
        if (Keyboard.isKeyPressed(Key._3)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.PARTICLES);
        if (Keyboard.isKeyPressed(Key._4)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.SKYBOX);
        if (Keyboard.isKeyPressed(Key._5)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.CLOUDS);
        if (Keyboard.isKeyPressed(Key._6)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.HUD);
        if (Keyboard.isKeyPressed(Key._7)) compositeDisplay.toggleVisibilityFlags(VisibilityFlags.DEBUG_GEOMETRY);

        // Mouse Control
        if (Keyboard.isKeyPressed(Key.TAB)) {
            if (Mouse.getMode() == Mouse.MODE_NORMAL) Mouse.setMode(Mouse.MODE_CENTERED);
            else                                      Mouse.setMode(Mouse.MODE_NORMAL);
        }
    }
}