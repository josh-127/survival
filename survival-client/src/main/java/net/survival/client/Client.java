package net.survival.client;

import org.lwjgl.glfw.GLFW;

import net.survival.client.graphics.ClientDisplay;
import net.survival.client.graphics.GraphicsSettings;
import net.survival.client.graphics.gui.GuiDisplay;
import net.survival.client.graphics.opengl.GLDisplay;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.gui.Control;
import net.survival.client.input.GlfwKeyboardProvider;
import net.survival.client.input.GlfwMouseProvider;
import net.survival.client.input.Key;
import net.survival.client.input.Keyboard;
import net.survival.entity.Cow;
import net.survival.world.EntityPhysics;
import net.survival.world.World;
import net.survival.world.chunk.ChunkPos;
import net.survival.world.chunk.DefaultChunkProvider;
import net.survival.world.chunk.EntityRelocator;
import net.survival.world.chunk.SphericalChunkLoader;
import net.survival.world.gen.infinite.InfiniteChunkGenerator;

public class Client implements AutoCloseable
{
    private static final String WINDOW_TITLE = "Survival";

    private final World world;
    
    private final DefaultChunkProvider chunkProvider;
    private final SphericalChunkLoader chunkLoader;
    private final EntityPhysics entityPhysics;
    private final EntityRelocator entityRelocator;
    
    private final Control control;
    
    private final ClientDisplay clientDisplay;
    private final GuiDisplay guiDisplay;
    
    private final UserController userController;
    
    private Client() {
        world = new World();
        chunkProvider = new DefaultChunkProvider(world, new InfiniteChunkGenerator(0L));
        chunkLoader = new SphericalChunkLoader(8);
        entityPhysics = new EntityPhysics(world);
        entityRelocator = new EntityRelocator(world);
        
        chunkProvider.addChunkLoader(chunkLoader);
        
        control = new Control();
        control.getClientRectangle().setRight(0.1);
        control.getClientRectangle().setBottom(0.025);
        control.setText("");
        
        clientDisplay = new ClientDisplay(world, GraphicsSettings.WINDOW_WIDTH, GraphicsSettings.WINDOW_HEIGHT);
        guiDisplay = new GuiDisplay(control);
        
        userController = new UserController();
    }

    @Override
    public void close() throws RuntimeException {
        clientDisplay.close();
    }
    
    public void tick(double elapsedTime) {
        userController.tick(elapsedTime);
        
        float x = (float) userController.camera.position.x;
        float y = (float) userController.camera.position.y;
        float z = (float) userController.camera.position.z;
        float yaw = (float) userController.camera.yaw;
        float pitch = (float) userController.camera.pitch;

        int cx = ChunkPos.toChunkX((int) Math.floor(x));
        int cy = ChunkPos.toChunkY((int) Math.floor(y));
        int cz = ChunkPos.toChunkZ((int) Math.floor(z));
        chunkLoader.setCenter(cx, cy, cz);
        
        chunkProvider.tick(elapsedTime);
        entityPhysics.tick(elapsedTime);
        entityRelocator.relocateEntities();
        
        if (Keyboard.isKeyPressed(Key.T)) {
            Cow beyonce = new Cow();
            beyonce.moveTo(Math.random() * 32.0, 64.0f, Math.random() * 32.0);
            world.addEntity(beyonce);
        }
        
        clientDisplay.getCamera().moveTo(x, y, z);
        clientDisplay.getCamera().orient(yaw, pitch);
        clientDisplay.getCamera().setFov((float) Math.toRadians(60.0));
        clientDisplay.getCamera().resize(GraphicsSettings.WINDOW_WIDTH, GraphicsSettings.WINDOW_HEIGHT);
        clientDisplay.getCamera().setClipPlanes(0.0625f, 768.0f);
    }

    private void render(double frameRate) {
        clientDisplay.display(frameRate);
        guiDisplay.display();
    }

    public static void main(String[] args) {
        GLDisplay display = new GLDisplay(GraphicsSettings.WINDOW_WIDTH, GraphicsSettings.WINDOW_HEIGHT, WINDOW_TITLE);
        GlfwKeyboardProvider keyboardProvider = new GlfwKeyboardProvider();
        GlfwMouseProvider mouseProvider = new GlfwMouseProvider();
        GLFW.glfwSetKeyCallback(display.getUnderlyingGlfwWindow(), keyboardProvider);
        GLFW.glfwSetCursorPosCallback(display.getUnderlyingGlfwWindow(), mouseProvider);
        GLRenderContext.init();
        
        Client program = new Client();

        final double MILLIS_PER_TICK = World.SECONDS_PER_TICK * 1000.0;
        long now = System.currentTimeMillis();
        long prevTime = now;
        double unprocessedTicks = 0.0;
        
        int frameCounter = 0;
        long frameRateTimer = System.currentTimeMillis();
        int frameRate = 0;

        for (boolean running = true; running; running = Keyboard.isKeyUp(Key.ESCAPE) && !display.shouldClose()) {
            now = System.currentTimeMillis();
            unprocessedTicks += (now - prevTime) / MILLIS_PER_TICK;
            prevTime = now;
            
            if (unprocessedTicks >= 1.0) {
                while (unprocessedTicks >= 1.0) {
                    mouseProvider.tick();
                    program.tick(World.SECONDS_PER_TICK);
                    keyboardProvider.nextInputFrame();
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
    }
}