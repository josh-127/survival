package net.survival.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.survival.block.BlockType;
import net.survival.client.graphics.CompositeDisplay;
import net.survival.client.graphics.GraphicsSettings;
import net.survival.client.graphics.VisibilityFlags;
import net.survival.client.graphics.opengl.GLDisplay;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.input.GlfwKeyboardAdapter;
import net.survival.client.input.GlfwMouseAdapter;
import net.survival.client.input.Keyboard;
import net.survival.client.input.Mouse;
import net.survival.client.ui.BasicUI;
import net.survival.util.MathEx;
import net.survival.world.World;
import net.survival.world.actor.Actor;
import net.survival.world.actor.Message;
import net.survival.world.actor.TickMessage;
import net.survival.world.actor.interaction.InteractionContext;
import net.survival.world.actor.v0_1_0_snapshot.NpcActor;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkDbPipe;
import net.survival.world.chunk.ChunkPos;
import net.survival.world.chunk.ChunkRequest;
import net.survival.world.chunk.ChunkServer;
import net.survival.world.chunk.ChunkSystem;
import net.survival.world.gen.InfiniteChunkGenerator;
import net.survival.world.gen.decoration.WorldDecorator;
import survival.input.Key;
import net.survival.world.chunk.CircularChunkStageMask;

public class Client implements AutoCloseable
{
    private static final String WINDOW_TITLE = "Survival";

    private static final double TICKS_PER_SECOND = 60.0;
    private static final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;

    private final World world = new World();

    private final CircularChunkStageMask chunkMask = new CircularChunkStageMask(10);
    private final InfiniteChunkGenerator chunkGenerator = new InfiniteChunkGenerator(22L);
    private final WorldDecorator worldDecorator = WorldDecorator.createDefault();
    private final ChunkSystem chunkSystem;

    private final BasicUI basicUI = new BasicUI();
    private final BasicUI.Server uiServer;

    private final CompositeDisplay compositeDisplay;
    private final FpvCamera fpvCamera = new FpvCamera(new Vector3d(60.0, 72.0, 20.0), 0.0f, -1.0f);

    private final ArrayList<Message> actorMessages = new ArrayList<>();
    private final LocalBlockInteractionAdapter blockInteraction = new LocalBlockInteractionAdapter(world);
    private final LocalKeyboardInteractionAdapter keyboardInteraction = new LocalKeyboardInteractionAdapter();
    private final LocalTickInteractionAdapter tickInteraction = new LocalTickInteractionAdapter();
    private final InteractionContext interactionContext = new InteractionContext(
            blockInteraction, keyboardInteraction, tickInteraction);

    private Client(ChunkDbPipe.ClientSide chunkDbPipe) {
        chunkSystem = new ChunkSystem(world, chunkMask, chunkDbPipe, chunkGenerator, worldDecorator);

        uiServer = basicUI.getServer();

        compositeDisplay = new CompositeDisplay(
                world,
                GraphicsSettings.WINDOW_WIDTH,
                GraphicsSettings.WINDOW_HEIGHT,
                basicUI.getClient());
    }

    @Override
    public void close() throws RuntimeException {
        compositeDisplay.close();
        chunkSystem.saveAllChunks();
    }

    public void tick(double elapsedTime) {
        //
        // Camera
        //
        final double CAMERA_SPEED = 20.0;
        final double PI = Math.PI;
        double cursorDX = Mouse.getDeltaX();
        double cursorDY = Mouse.getDeltaY();
        double jsX = 0.0;
        double jsZ = 0.0;
        double jsY = 0.0;
        double camYaw = fpvCamera.yaw;
        if (Keyboard.isKeyDown(Key.W)) { jsX += Math.sin(camYaw); jsZ -= Math.cos(camYaw); }
        if (Keyboard.isKeyDown(Key.S)) { jsX += Math.sin(camYaw + PI); jsZ -= Math.cos(camYaw + PI); }
        if (Keyboard.isKeyDown(Key.A)) { jsX += Math.sin(camYaw - PI / 2.0); jsZ -= Math.cos(camYaw - PI / 2.0); }
        if (Keyboard.isKeyDown(Key.D)) { jsX += Math.sin(camYaw + PI / 2.0); jsZ -= Math.cos(camYaw + PI / 2.0); }
        if (Keyboard.isKeyDown(Key.SPACE)) jsY = 0.5;
        if (Keyboard.isKeyDown(Key.LEFT_SHIFT)) jsY = -0.5;

        fpvCamera.rotate(-cursorDX / 128.0, -cursorDY / 128.0);
        fpvCamera.position.x += jsX * CAMERA_SPEED * elapsedTime;
        fpvCamera.position.z += jsZ * CAMERA_SPEED * elapsedTime;
        fpvCamera.position.y += jsY * 20.0 * elapsedTime;

        //
        // Chunk System
        //
        int cx = ChunkPos.toChunkX((int) Math.floor(fpvCamera.position.x));
        int cz = ChunkPos.toChunkZ((int) Math.floor(fpvCamera.position.z));
        chunkMask.setCenter(cx, cz);
        chunkSystem.update(elapsedTime);

        //
        // Actor System
        //
        tickInteraction.setElapsedTime(elapsedTime);
        actorMessages.add(TickMessage.DEFAULT);

        while (!actorMessages.isEmpty()) {
            ArrayList<Message> remainingMessages = new ArrayList<>(actorMessages);
            actorMessages.clear();

            for (Message message : remainingMessages) {
                if (message.recipient == Message.ALL_ACTORS) {
                    for (Actor actor : world.getActors()) {
                        actor.update(interactionContext, message);
                    }
                }
            }
        }

        //
        // Temporary Test Code
        //
        temporaryTestCode();

        //
        // Client Display
        //
        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            if (chunk.isBlocksModified()) {
                chunk.clearModificationFlags();
                compositeDisplay.redrawChunk(hashedPos);
            }
        }

        compositeDisplay.moveCamera(
                (float) fpvCamera.position.x, (float) fpvCamera.position.y, (float) fpvCamera.position.z);
        compositeDisplay.orientCamera((float) fpvCamera.yaw, (float) fpvCamera.pitch);
        compositeDisplay.setCameraFov((float) Math.toRadians(60.0));
        compositeDisplay.resizeCamera(GraphicsSettings.WINDOW_WIDTH, GraphicsSettings.WINDOW_HEIGHT);
        compositeDisplay.setCameraClipPlanes(0.0625f, 768.0f);
        compositeDisplay.tick(elapsedTime);
    }

    private void render(double frameRate) {
        compositeDisplay.display(frameRate);
    }

    public static void main(String[] args) throws InterruptedException {
        ChunkDbPipe chunkDbPipe = new ChunkDbPipe();
        ChunkServer chunkServer = new ChunkServer(
                new File(System.getProperty("user.dir") + "/../.world/chunks"),
                chunkDbPipe.getServerSide());

        Thread chunkServerThread = new Thread(chunkServer);
        chunkServerThread.start();

        GLDisplay display = new GLDisplay(GraphicsSettings.WINDOW_WIDTH,
                GraphicsSettings.WINDOW_HEIGHT, WINDOW_TITLE);
        GlfwKeyboardAdapter keyboardAdapter = new GlfwKeyboardAdapter();
        GlfwMouseAdapter mouseAdapter = new GlfwMouseAdapter(display.getUnderlyingGlfwWindow());
        GLFW.glfwSetKeyCallback(display.getUnderlyingGlfwWindow(), keyboardAdapter);
        GLFW.glfwSetCursorPosCallback(display.getUnderlyingGlfwWindow(), mouseAdapter);
        GLRenderContext.init();

        Client program = new Client(chunkDbPipe.getClientSide());

        final double MILLIS_PER_TICK = SECONDS_PER_TICK * 1000.0;
        long now = System.currentTimeMillis();
        long prevTime = now;
        double unprocessedTicks = 0.0;

        int frameCounter = 0;
        long frameRateTimer = System.currentTimeMillis();
        int frameRate = 0;

        for (boolean running = true; running; running = Keyboard.isKeyUp(Key.ESCAPE)
                && !display.shouldClose())
        {
            now = System.currentTimeMillis();
            unprocessedTicks += (now - prevTime) / MILLIS_PER_TICK;
            prevTime = now;

            if (unprocessedTicks >= 1.0) {
                while (unprocessedTicks >= 1.0) {
                    mouseAdapter.tick();
                    program.tick(SECONDS_PER_TICK);
                    keyboardAdapter.nextInputFrame();
                    unprocessedTicks -= 1.0;
                }

                ++frameCounter;
                program.render(frameRate);
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

        chunkDbPipe.getClientSide().request(ChunkRequest.createCloseRequest());
        chunkServerThread.join();
    }

    private void temporaryTestCode() {
        if (Mouse.isLmbPressed() || Mouse.isRmbPressed()) {
            double px = fpvCamera.position.x;
            double py = fpvCamera.position.y;
            double pz = fpvCamera.position.z;
            final double DELTA = 0.0078125;
            for (double zz = 0.0; zz < 7.0; zz += DELTA) {
                px += DELTA * Math.sin(fpvCamera.yaw) * Math.cos(fpvCamera.pitch);
                py += DELTA * Math.sin(fpvCamera.pitch);
                pz -= DELTA * Math.cos(fpvCamera.yaw) * Math.cos(fpvCamera.pitch);
                int pxi = (int) Math.floor(px);
                int pyi = (int) Math.floor(py);
                int pzi = (int) Math.floor(pz);
                if (world.getBlock(pxi, pyi, pzi) != BlockType.EMPTY.id) {
                    if (Mouse.isLmbPressed())
                        world.setBlock(pxi, pyi, pzi, BlockType.EMPTY.id);
                    else if (Mouse.isRmbPressed())
                        world.setBlock(pxi, pyi, pzi, BlockType.OAK_FENCE.id);
                    break;
                }
            }
        }

        if (Keyboard.isKeyPressed(Key.T)) {
            NpcActor npcActor = new NpcActor(
                    fpvCamera.position.x,
                    fpvCamera.position.y,
                    fpvCamera.position.z);
            world.addActor(npcActor);
        }

        if (Keyboard.isKeyPressed(Key._1))
            compositeDisplay.toggleVisibilityFlags(VisibilityFlags.BLOCKS);
        if (Keyboard.isKeyPressed(Key._2))
            compositeDisplay.toggleVisibilityFlags(VisibilityFlags.ENTITIES);
        if (Keyboard.isKeyPressed(Key._3))
            compositeDisplay.toggleVisibilityFlags(VisibilityFlags.SKYBOX);
        if (Keyboard.isKeyPressed(Key._4))
            compositeDisplay.toggleVisibilityFlags(VisibilityFlags.CLOUDS);
        if (Keyboard.isKeyPressed(Key._5))
            compositeDisplay.toggleVisibilityFlags(VisibilityFlags.HUD);
        if (Keyboard.isKeyPressed(Key._6))
            compositeDisplay.toggleVisibilityFlags(VisibilityFlags.DEBUG_GEOMETRY);

        float spx = compositeDisplay.getCloudSpeedX();
        float spz = compositeDisplay.getCloudSpeedZ();
        if (Keyboard.isKeyDown(Key.UP)) {
            spx += 5.0f;
            compositeDisplay.setCloudSpeed(spx, spz);
        }
        else if (Keyboard.isKeyDown(Key.DOWN)) {
            spx -= 5.0f;
            compositeDisplay.setCloudSpeed(spx, spz);
        }

        if (Keyboard.isKeyPressed(Key.TAB)) {
            if (Mouse.getMode() == Mouse.MODE_NORMAL)
                Mouse.setMode(Mouse.MODE_CENTERED);
            else
                Mouse.setMode(Mouse.MODE_NORMAL);
        }

        if (uiServer.button("...", 3.0, 0, 48, 80, 32 + 48)) {
            compositeDisplay.setCloudSeed(compositeDisplay.getCloudSeed() + 1L);
        }
    }
}