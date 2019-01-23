package net.survival.client;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.survival.actor.Actor;
import net.survival.actor.ActorSpace;
import net.survival.actor.message.HurtMessage;
import net.survival.actor.message.JumpMessage;
import net.survival.actor.message.MoveMessage;
import net.survival.actor.message.StepMessage;
import net.survival.actor.v0_1_snapshot.NpcActor;
import net.survival.actor.v0_1_snapshot.PlayerActor;
import net.survival.block.BlockSpace;
import net.survival.block.column.CircularColumnStageMask;
import net.survival.block.column.Column;
import net.survival.block.column.ColumnDbPipe;
import net.survival.block.column.ColumnPos;
import net.survival.block.column.ColumnRequest;
import net.survival.block.column.ColumnServer;
import net.survival.block.column.ColumnSystem;
import net.survival.block.message.BreakBlockMessage;
import net.survival.block.message.PlaceBlockMessage;
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
import net.survival.gen.InfiniteColumnGenerator;
import net.survival.gen.decoration.WorldDecorator;
import net.survival.input.Key;
import net.survival.interaction.InteractionContext;

public class Client implements AutoCloseable
{
    private static final String WINDOW_TITLE = "Survival";

    private static final double TICKS_PER_SECOND = 60.0;
    private static final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;

    private final BlockSpace blockSpace = new BlockSpace();
    private final ActorSpace actorSpace = new ActorSpace();

    private final CircularColumnStageMask columnMask = new CircularColumnStageMask(10);
    private final InfiniteColumnGenerator columnGenerator = new InfiniteColumnGenerator(22L);
    private final WorldDecorator worldDecorator = WorldDecorator.createDefault();
    private final ColumnSystem columnSystem;

    private final BasicUI basicUI = new BasicUI();
    private final BasicUI.Server uiServer;

    private final CompositeDisplay compositeDisplay;
    private final FpvCamera fpvCamera = new FpvCamera(new Vector3d(60.0, 72.0, 20.0), 0.0f, -1.0f);

    private final Queue<MoveMessage> moveMessages = new LinkedList<>();
    private final Queue<JumpMessage> jumpMessages = new LinkedList<>();
    private final Queue<HurtMessage> hurtMessages = new LinkedList<>();
    private final Queue<BreakBlockMessage> breakBlockMessages = new LinkedList<>();
    private final Queue<PlaceBlockMessage> placeBlockMessages = new LinkedList<>();

    private final LocalBlockInteractionAdapter blockInteraction = new LocalBlockInteractionAdapter(blockSpace);
    private final LocalKeyboardInteractionAdapter keyboardInteraction = new LocalKeyboardInteractionAdapter();
    private final LocalTickInteractionAdapter tickInteraction = new LocalTickInteractionAdapter();
    private final InteractionContext interactionContext = new InteractionContext(
            blockInteraction, keyboardInteraction, tickInteraction);

    private int npcID = -1;
    private int playerID = -1;
    private Actor player;

    private Client(ColumnDbPipe.ClientSide columnDbPipe) {
        columnSystem = new ColumnSystem(blockSpace, columnMask, columnDbPipe, columnGenerator, worldDecorator);

        uiServer = basicUI.getServer();

        compositeDisplay = new CompositeDisplay(
                blockSpace,
                actorSpace,
                GraphicsSettings.WINDOW_WIDTH,
                GraphicsSettings.WINDOW_HEIGHT,
                basicUI.getClient());
    }

    @Override
    public void close() throws RuntimeException {
        compositeDisplay.close();
        columnSystem.saveAllColumns();
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
        if (player == null) {
            fpvCamera.position.x += jsX * CAMERA_SPEED * elapsedTime;
            fpvCamera.position.z += jsZ * CAMERA_SPEED * elapsedTime;
            fpvCamera.position.y += jsY * 20.0 * elapsedTime;
        }
        else {
            moveMessages.add(new MoveMessage(playerID, jsX, jsZ));
        }

        if (playerID != -1 && Keyboard.isKeyPressed(Key.SPACE)) {
            jumpMessages.add(new JumpMessage(playerID));
        }

        //
        // Column System
        //
        int cx = ColumnPos.toColumnX((int) Math.floor(fpvCamera.position.x));
        int cz = ColumnPos.toColumnZ((int) Math.floor(fpvCamera.position.z));
        columnMask.setCenter(cx, cz);
        columnSystem.update(elapsedTime);

        //
        // Actor System
        //
        tickInteraction.setElapsedTime(elapsedTime);

        while (!moveMessages.isEmpty()) {
            MoveMessage moveMessage = moveMessages.remove();
            moveMessage.accept(actorSpace.getActor(moveMessage.getDestActorID()), interactionContext);
        }

        while (!jumpMessages.isEmpty()) {
            JumpMessage jumpMessage = jumpMessages.remove();
            jumpMessage.accept(actorSpace.getActor(jumpMessage.getDestActorID()), interactionContext);
        }

        while (!hurtMessages.isEmpty()) {
            HurtMessage hurtMessage = hurtMessages.remove();
            hurtMessage.accept(actorSpace.getActor(hurtMessage.getDestActorID()), interactionContext);
        }

        for (Map.Entry<Integer, Actor> entry : actorSpace.iterateActorMap()) {
            int actorID = entry.getKey();
            Actor actor = entry.getValue();
            new StepMessage(actorID).accept(actor, interactionContext);
        }

        //
        // Block System
        //
        while (!breakBlockMessages.isEmpty()) {
            BreakBlockMessage breakBlockMessage = breakBlockMessages.remove();
            breakBlockMessage.accept(blockSpace);
        }

        while (!placeBlockMessages.isEmpty()) {
            PlaceBlockMessage placeBlockMessage = placeBlockMessages.remove();
            placeBlockMessage.accept(blockSpace);
        }

        //
        // Temporary Test Code
        //
        temporaryTestCode();

        //
        // Client Display
        //
        Iterator<Long2ObjectMap.Entry<Column>> columnMapIt = blockSpace.getColumnMapFastIterator();
        while (columnMapIt.hasNext()) {
            Long2ObjectMap.Entry<Column> entry = columnMapIt.next();
            long hashedPos = entry.getLongKey();
            Column column = entry.getValue();

            if (column.isBlocksModified()) {
                column.clearModificationFlags();
                compositeDisplay.redrawColumn(hashedPos);
            }
        }

        if (player == null ) {
            compositeDisplay.moveCamera(
                    (float) fpvCamera.position.x, (float) fpvCamera.position.y, (float) fpvCamera.position.z);
        }
        else {
            compositeDisplay.moveCamera(
                    (float) player.getX(), (float) (player.getY() + 1.0), (float) player.getZ());
        }

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
        ColumnDbPipe columnDbPipe = new ColumnDbPipe();
        ColumnServer columnServer = new ColumnServer(
                new File(System.getProperty("user.dir") + "/../.world/columns"),
                columnDbPipe.getServerSide());

        Thread columnServerThread = new Thread(columnServer);
        columnServerThread.start();

        GLDisplay display = new GLDisplay(GraphicsSettings.WINDOW_WIDTH,
                GraphicsSettings.WINDOW_HEIGHT, WINDOW_TITLE);
        GlfwKeyboardAdapter keyboardAdapter = new GlfwKeyboardAdapter();
        GlfwMouseAdapter mouseAdapter = new GlfwMouseAdapter(display.getUnderlyingGlfwWindow());
        GLFW.glfwSetKeyCallback(display.getUnderlyingGlfwWindow(), keyboardAdapter);
        GLFW.glfwSetCursorPosCallback(display.getUnderlyingGlfwWindow(), mouseAdapter);
        GLRenderContext.init();

        Client program = new Client(columnDbPipe.getClientSide());

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

        columnDbPipe.getClientSide().request(ColumnRequest.createCloseRequest());
        columnServerThread.join();
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
                if (blockSpace.getBlockFullID(pxi, pyi, pzi) != 0) {
                    if (Mouse.isLmbPressed()) {
                        breakBlockMessages.add(new BreakBlockMessage(pxi, pyi, pzi));
                    }
                    break;
                }
            }
        }

        if (player == null && Keyboard.isKeyPressed(Key.R)) {
            player = new PlayerActor(
                    fpvCamera.position.x,
                    fpvCamera.position.y,
                    fpvCamera.position.z);
            playerID = actorSpace.addActor(player);
        }
        else if (Keyboard.isKeyPressed(Key.T)) {
            NpcActor npcActor = new NpcActor(
                    fpvCamera.position.x,
                    fpvCamera.position.y,
                    fpvCamera.position.z);
            npcID = actorSpace.addActor(npcActor);
        }

        if (npcID != -1 && Keyboard.isKeyPressed(Key.K)) {
            hurtMessages.add(new HurtMessage(npcID, 10.0));
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

        if (uiServer.button("Change Clouds", 3.0, 0, 48, 320, 32 + 48)) {
            compositeDisplay.setCloudSeed(compositeDisplay.getCloudSeed() + 1L);
        }
    }
}