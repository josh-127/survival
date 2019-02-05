package net.survival.client;

import java.io.File;

import org.lwjgl.glfw.GLFW;

import net.survival.actor.Actor;
import net.survival.actor.ActorSpace;
import net.survival.actor.NpcActor;
import net.survival.actor.PlayerActor;
import net.survival.actor.message.ActorMessage;
import net.survival.actor.message.JumpMessage;
import net.survival.actor.message.MoveMessage;
import net.survival.actor.message.StepMessage;
import net.survival.block.BlockSpace;
import net.survival.block.column.CircularColumnStageMask;
import net.survival.block.column.ColumnDbPipe;
import net.survival.block.column.ColumnPos;
import net.survival.block.column.ColumnRequest;
import net.survival.block.column.ColumnServer;
import net.survival.block.message.BlockMessage;
import net.survival.block.message.BreakBlockMessage;
import net.survival.block.message.CheckInColumnsMessage;
import net.survival.block.message.CheckOutColumnsMessage;
import net.survival.block.message.ColumnResponseMessage;
import net.survival.block.message.MaskColumnsMessage;
import net.survival.client.graphics.CompositeDisplay;
import net.survival.client.graphics.GraphicsSettings;
import net.survival.client.graphics.VisibilityFlags;
import net.survival.client.graphics.opengl.GLDisplay;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.input.GlfwKeyboardAdapter;
import net.survival.client.input.GlfwMouseAdapter;
import net.survival.client.input.Keyboard;
import net.survival.client.input.Mouse;
import net.survival.client.particle.ClientParticleSpace;
import net.survival.gen.InfiniteColumnGenerator;
import net.survival.input.Key;
import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessageQueue;
import net.survival.interaction.MessageVisitor;
import net.survival.particle.message.BurstParticlesMessage;
import net.survival.particle.message.ParticleMessage;

public class Client implements AutoCloseable
{
    private static final String WINDOW_TITLE = "Survival";

    private static final double TICKS_PER_SECOND = 60.0;
    private static final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;

    private final ColumnDbPipe.ClientSide columnPipe;

    private final BlockSpace blockSpace;
    private final ActorSpace actorSpace = new ActorSpace();
    private final ClientParticleSpace particleSpace = new ClientParticleSpace();

    private final CircularColumnStageMask columnMask = new CircularColumnStageMask(10);

    private final CompositeDisplay compositeDisplay;
    private final FpvCamera fpvCamera = new FpvCamera(0.0f, -1.0f);

    private final MessageQueue messageQueue = new MessageQueue();

    private final LocalInteractionContext interactionContext;

    private final int playerID;
    private final Actor player;

    private static final double SAVE_INTERVAL = 10.0;
    private double saveTimer = SAVE_INTERVAL;

    private Client(ColumnDbPipe.ClientSide columnPipe) {
        this.columnPipe = columnPipe;

        blockSpace = new BlockSpace(columnPipe);

        interactionContext = new LocalInteractionContext(
                actorSpace, blockSpace, particleSpace, messageQueue);

        playerID = actorSpace.addActor(new PlayerActor(60.0, 96.0, 20.0));
        player = actorSpace.getActor(playerID);

        compositeDisplay = new CompositeDisplay(
                blockSpace, actorSpace, particleSpace, GraphicsSettings.WINDOW_WIDTH, GraphicsSettings.WINDOW_HEIGHT);
    }

    @Override
    public void close() throws RuntimeException {
        compositeDisplay.close();
        new CheckInColumnsMessage().accept(blockSpace, interactionContext);
    }

    public void tick(double elapsedTime) {
        // Camera
        final var PI = Math.PI;
        var cursorDX = Mouse.getDeltaX(); var cursorDY = Mouse.getDeltaY();
        var jsX = 0.0; var jsZ = 0.0; var camYaw = fpvCamera.yaw;
        if (Keyboard.isKeyDown(Key.W)) { jsX += Math.sin(camYaw); jsZ -= Math.cos(camYaw); }
        if (Keyboard.isKeyDown(Key.S)) { jsX += Math.sin(camYaw + PI); jsZ -= Math.cos(camYaw + PI); }
        if (Keyboard.isKeyDown(Key.A)) { jsX += Math.sin(camYaw - PI / 2.0); jsZ -= Math.cos(camYaw - PI / 2.0); }
        if (Keyboard.isKeyDown(Key.D)) { jsX += Math.sin(camYaw + PI / 2.0); jsZ -= Math.cos(camYaw + PI / 2.0); }

        fpvCamera.rotate(-cursorDX / 128.0, -cursorDY / 128.0);
        messageQueue.enqueueMessage(new MoveMessage(playerID, jsX, jsZ));
        if (Keyboard.isKeyPressed(Key.SPACE)) messageQueue.enqueueMessage(new JumpMessage(playerID));

        // Loading/Saving Columns
        var cx = ColumnPos.toColumnX((int) Math.floor(player.getX()));
        var cz = ColumnPos.toColumnZ((int) Math.floor(player.getZ()));
        columnMask.setCenter(cx, cz);

        saveTimer -= elapsedTime;
        if (saveTimer <= 0.0) {
            saveTimer = SAVE_INTERVAL;
            messageQueue.enqueueMessage(new CheckInColumnsMessage());
            messageQueue.enqueueMessage(new MaskColumnsMessage(columnMask));
        }

        messageQueue.enqueueMessage(new CheckOutColumnsMessage(columnMask.getColumnPositions()));

        for (var r = columnPipe.pollResponse(); r != null; r = columnPipe.pollResponse()) {
            messageQueue.enqueueMessage(new ColumnResponseMessage(r));
        }

        // Misc. Setup
        interactionContext.setElapsedTime(elapsedTime);
        for (var entry : actorSpace.iterateActorMap()) {
            var actorID = entry.getKey();
            var actor = entry.getValue();
            messageQueue.enqueueMessage(new StepMessage(actorID));
        }

        // Application-Wide Message Dispatcher
        while (!messageQueue.isEmpty()) {
            var m = messageQueue.dequeueMessage();

            m.accept(new MessageVisitor() {
                @Override
                public void visit(InteractionContext ic, ActorMessage message) {
                    var actorID = message.getDestActorID();
                    var actor = actorSpace.getActor(actorID);
                    message.accept(actor, interactionContext);
                }
                
                @Override
                public void visit(InteractionContext ic, BlockMessage message) {
                    message.accept(blockSpace, interactionContext);
                }
                
                @Override
                public void visit(InteractionContext ic, ParticleMessage message) {
                    message.accept(particleSpace);
                }
            }, interactionContext);
        }

        messageQueue.nextFrame();

        // Misc. Code
        particleSpace.step(elapsedTime);

        // Temporary Test Code
        temporaryTestCode();

        // Client Display
        for (var entry : blockSpace.iterateColumnMap()) {
            var hashedPos = entry.getKey();
            var column = entry.getValue();

            if (column.isBlocksModified()) {
                column.clearModificationFlags();
                compositeDisplay.redrawColumn(hashedPos);
            }
        }

        compositeDisplay.moveCamera((float) player.getX(), (float) (player.getY() + 1.0), (float) player.getZ());
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
        var columnGenerator = new InfiniteColumnGenerator(22L);

        var columnDbPipe = new ColumnDbPipe();
        var columnServer = new ColumnServer(
                new File(System.getProperty("user.dir") + "/../.world/columns"),
                columnDbPipe.getServerSide(),
                columnGenerator);

        var columnServerThread = new Thread(columnServer);
        columnServerThread.start();

        var display = new GLDisplay(GraphicsSettings.WINDOW_WIDTH, GraphicsSettings.WINDOW_HEIGHT, WINDOW_TITLE);
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
            var px = player.getX(); var py = player.getY() + 1.0; var pz = player.getZ();
            final var DELTA = 0.0078125;
            for (var zz = 0.0; zz < 7.0; zz += DELTA) {
                px += DELTA * Math.sin(fpvCamera.yaw) * Math.cos(fpvCamera.pitch);
                py += DELTA * Math.sin(fpvCamera.pitch);
                pz -= DELTA * Math.cos(fpvCamera.yaw) * Math.cos(fpvCamera.pitch);
                var pxi = (int) Math.floor(px); var pyi = (int) Math.floor(py); var pzi = (int) Math.floor(pz);
                if (blockSpace.getBlockFullID(pxi, pyi, pzi) != 0) {
                    if (Mouse.isLmbPressed()) messageQueue.enqueueMessage(new BreakBlockMessage(pxi, pyi, pzi));
                    break;
                }
            }
        }

        if (Keyboard.isKeyPressed(Key.T))
            actorSpace.addActor(new NpcActor(player.getX(), player.getY(), player.getZ()));
        else if (Keyboard.isKeyPressed(Key.Y))
            messageQueue.enqueueMessage(new BurstParticlesMessage(player.getX(), player.getY(), player.getZ(), 1.0, 64));

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