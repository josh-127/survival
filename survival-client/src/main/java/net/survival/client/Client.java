package net.survival.client;

import java.io.File;
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
import net.survival.client.input.Key;
import net.survival.client.input.Keyboard;
import net.survival.client.input.Mouse;
import net.survival.client.ui.BasicUI;
import net.survival.entity.CharacterModel;
import net.survival.entity.Npc;
import net.survival.entity.NpcMovementStyle;
import net.survival.entity.Player;
import net.survival.util.HitBox;
import net.survival.world.EntitySystem;
import net.survival.world.World;
import net.survival.world.actor.ActorServiceCollection;
import net.survival.world.actor.AlarmService;
import net.survival.world.actor.EventQueue;
import net.survival.world.actor.LocomotiveService;
import net.survival.world.actor.v0_1_0_snapshot.NpcActor;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkDbPipe;
import net.survival.world.chunk.ChunkPos;
import net.survival.world.chunk.ChunkRequest;
import net.survival.world.chunk.ChunkServer;
import net.survival.world.chunk.ChunkSystem;
import net.survival.world.gen.InfiniteChunkGenerator;
import net.survival.world.gen.decoration.WorldDecorator;
import net.survival.world.chunk.CircularChunkStageMask;

public class Client implements AutoCloseable
{
    private static final String WINDOW_TITLE = "Survival";

    private static final double TICKS_PER_SECOND = 60.0;
    private static final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;

    private final World world;

    private final CircularChunkStageMask chunkLoader;
    private final InfiniteChunkGenerator chunkGenerator;
    private final WorldDecorator worldDecorator;
    private final ChunkSystem chunkSystem;

    private final EntitySystem entitySystem;

    private final BasicUI basicUI = new BasicUI();
    private final BasicUI.Server uiServer;

    private final CompositeDisplay compositeDisplay;
    private final FpvCamera fpvCamera;

    private Player player;

    private final EventQueue eventQueue;
    private final AlarmService[] alarmServices;
    private final LocomotiveService locomotiveService;
    private final ActorServiceCollection actorServiceCollection;

    private Client(ChunkDbPipe.ClientSide chunkDbPipe) {
        world = new World();

        chunkLoader = new CircularChunkStageMask(10);
        chunkGenerator = new InfiniteChunkGenerator(22L);
        worldDecorator = WorldDecorator.createDefault();
        chunkSystem = new ChunkSystem(world, chunkLoader, chunkDbPipe, chunkGenerator, worldDecorator);

        entitySystem = new EntitySystem();

        uiServer = basicUI.getServer();

        compositeDisplay = new CompositeDisplay(
                world,
                GraphicsSettings.WINDOW_WIDTH,
                GraphicsSettings.WINDOW_HEIGHT,
                basicUI.getClient());

        fpvCamera = new FpvCamera(new Vector3d(60.0, 72.0, 20.0), 0.0f, -1.0f);

        eventQueue = new EventQueue();
        alarmServices = new AlarmService[16];
        for (int i = 0; i < alarmServices.length; ++i)
            alarmServices[i] = new AlarmService(eventQueue.getProducer(), i);
        locomotiveService = new LocomotiveService(eventQueue.getProducer(), world);
        actorServiceCollection = new ActorServiceCollection(alarmServices, locomotiveService);
    }

    @Override
    public void close() throws RuntimeException {
        compositeDisplay.close();
    }

    public void tick(double elapsedTime) {
        double cursorDX = Mouse.getDeltaX();
        double cursorDY = Mouse.getDeltaY();
        fpvCamera.rotate(-cursorDX / 128.0, -cursorDY / 128.0);

        if (Keyboard.isKeyPressed(Key.R)) {
            Player newPlayer = new Player();
            newPlayer.x = player != null ? player.x : fpvCamera.position.x;
            newPlayer.y = player != null ? player.y + 3.0 : fpvCamera.position.y;
            newPlayer.z = player != null ? player.z : fpvCamera.position.z;
            newPlayer.hitBox = HitBox.PLAYER;
            newPlayer.visible = false;
            player = newPlayer;
            world.addCharacter(newPlayer);
        }

        double joystickX = 0.0;
        double joystickZ = 0.0;
        double joystickY = 0.0;

        if (Keyboard.isKeyDown(Key.W)) {
            joystickX += Math.sin(fpvCamera.yaw);
            joystickZ -= Math.cos(fpvCamera.yaw);
        }
        if (Keyboard.isKeyDown(Key.S)) {
            joystickX += Math.sin(fpvCamera.yaw + Math.PI);
            joystickZ -= Math.cos(fpvCamera.yaw + Math.PI);
        }
        if (Keyboard.isKeyDown(Key.A)) {
            joystickX += Math.sin(fpvCamera.yaw - Math.PI / 2.0);
            joystickZ -= Math.cos(fpvCamera.yaw - Math.PI / 2.0);
        }
        if (Keyboard.isKeyDown(Key.D)) {
            joystickX += Math.sin(fpvCamera.yaw + Math.PI / 2.0);
            joystickZ -= Math.cos(fpvCamera.yaw + Math.PI / 2.0);
        }

        if (Keyboard.isKeyDown(Key.SPACE)) {
            joystickY = 1.0;
        }
        if (Keyboard.isKeyDown(Key.LEFT_SHIFT)) {
            joystickY = -1.0;
        }

        if (player != null) {
            player.setMoveDirectionControlValues(joystickX, joystickZ);

            if (Keyboard.isKeyPressed(Key.SPACE))
                player.setJumpControlValue();
        }
        else {
            final double CAMERA_SPEED = 40.0;
            fpvCamera.position.x += joystickX * CAMERA_SPEED * elapsedTime;
            fpvCamera.position.z += joystickZ * CAMERA_SPEED * elapsedTime;
            fpvCamera.position.y += joystickY * 20.0 * elapsedTime;
        }

        int cx = ChunkPos.toChunkX((int) Math.floor(fpvCamera.position.x));
        int cz = ChunkPos.toChunkZ((int) Math.floor(fpvCamera.position.z));
        chunkLoader.setCenter(cx, cz);

        chunkSystem.update(elapsedTime);

        entitySystem.update(world, elapsedTime);

        world.collectActors();
        for (AlarmService alarmService : alarmServices)
            alarmService.tick(elapsedTime);
        locomotiveService.tick(elapsedTime);

        EventQueue.Consumer eventConsumer = eventQueue.getConsumer();
        for (EventQueue.EventPacket eventPacket : eventConsumer) {
            eventPacket.target.onEventNotification(actorServiceCollection, eventPacket.eventArgs);
        }

        if (player != null) {
            fpvCamera.position.x = player.x;
            fpvCamera.position.y = player.y + 0.8;
            fpvCamera.position.z = player.z;
        }

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

            npcActor.setup(actorServiceCollection);
        }

        if (Keyboard.isKeyPressed(Key.Y)) {
            Npc npc = new Npc();
            npc.x = fpvCamera.position.x;
            npc.y = fpvCamera.position.y;
            npc.z = fpvCamera.position.z;
            npc.hitBox = HitBox.NPC;
            npc.moveSpeed = 4.0;
            npc.model = CharacterModel.GOAT;
            world.addCharacter(npc);
        }

        if (Keyboard.isKeyPressed(Key.U)) {
            Npc npc = new Npc();
            npc.x = fpvCamera.position.x;
            npc.y = fpvCamera.position.y;
            npc.z = fpvCamera.position.z;
            npc.hitBox = HitBox.NPC;
            npc.moveSpeed = 4.0;
            npc.model = CharacterModel.SLIME;
            npc.movementStyle = NpcMovementStyle.SLIME;
            world.addCharacter(npc);
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

        uiServer.button("1234567890", 0, 48, 80, 32 + 48);

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
                (float) fpvCamera.position.x,
                (float) fpvCamera.position.y,
                (float) fpvCamera.position.z);
        compositeDisplay.orientCamera(
                (float) fpvCamera.yaw,
                (float) fpvCamera.pitch);
        compositeDisplay.setCameraFov((float) Math.toRadians(60.0));
        compositeDisplay.resizeCamera(
                GraphicsSettings.WINDOW_WIDTH,
                GraphicsSettings.WINDOW_HEIGHT);
        compositeDisplay.setCameraClipPlanes(0.0625f, 768.0f);

        compositeDisplay.tick(elapsedTime);
    }

    private void render(double frameRate) {
        compositeDisplay.display(frameRate);
    }

    public static void main(String[] args) throws InterruptedException {
        GLDisplay display = new GLDisplay(GraphicsSettings.WINDOW_WIDTH,
                GraphicsSettings.WINDOW_HEIGHT, WINDOW_TITLE);
        GlfwKeyboardAdapter keyboardAdapter = new GlfwKeyboardAdapter();
        GlfwMouseAdapter mouseAdapter = new GlfwMouseAdapter(display.getUnderlyingGlfwWindow());
        GLFW.glfwSetKeyCallback(display.getUnderlyingGlfwWindow(), keyboardAdapter);
        GLFW.glfwSetCursorPosCallback(display.getUnderlyingGlfwWindow(), mouseAdapter);
        GLRenderContext.init();

        ChunkDbPipe chunkDbPipe = new ChunkDbPipe();

        Client program = new Client(chunkDbPipe.getClientSide());
        ChunkServer chunkServer = new ChunkServer(
                new File(System.getProperty("user.dir") + "/../.world/chunks"),
                chunkDbPipe.getServerSide());

        Thread chunkServerThread = new Thread(chunkServer);
        chunkServerThread.start();

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

        chunkDbPipe.getClientSide().request(ChunkRequest.createCloseRequest());
        chunkServerThread.join();

        program.close();
        display.close();
    }
}