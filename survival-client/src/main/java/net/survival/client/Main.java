package net.survival.client;

import net.survival.client.input.GlfwKeyboardAdapter;
import net.survival.client.input.GlfwMouseAdapter;
import net.survival.graphics.opengl.GLDisplay;
import net.survival.graphics.opengl.GLRenderContext;
import net.survival.world.WorldScript;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;

public final class Main {
    public static void main(String[] args) throws InterruptedException {
        var shouldQuit = new AtomicBoolean();
        var shouldRender = new AtomicBoolean();

        var tick = new Tick(shouldQuit, shouldRender);
        var render = new Render(shouldQuit, shouldRender);
        var worldSave = new WorldSave(shouldQuit);
        var tickThread = new Thread(tick, "Tick");
        var renderThread = new Thread(render, "Render");
        var worldSaveThread = new Thread(worldSave, "WorldSave");

        tickThread.start();
        renderThread.start();
        worldSaveThread.start();

        tickThread.join();
        renderThread.join();
        worldSaveThread.join();
    }

    private static final class Tick implements Runnable {
        private static final double TICKS_PER_SECOND = 60.0;
        private static final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;
        private static final double NANOS_PER_TICK = SECONDS_PER_TICK * 1000000000.0;

        private final AtomicBoolean shouldQuit;
        private final AtomicBoolean shouldRender;

        public Tick(AtomicBoolean shouldQuit, AtomicBoolean shouldRender) {
            this.shouldQuit = shouldQuit;
            this.shouldRender = shouldRender;
        }

        @Override
        public void run() {
            var now = System.nanoTime();
            var prevTime = now;
            var unprocessedTicks = 0.0;

            WorldScript script = new WorldScript() {
                @Override
                public WorldScript runCycle() {
                    return this;
                }
            };

            while (script != null) {
                now = System.nanoTime();
                unprocessedTicks += (now - prevTime) / NANOS_PER_TICK;
                prevTime = now;

                if (unprocessedTicks >= 1.0) {
                    while (unprocessedTicks >= 1.0) {
                        unprocessedTicks -= 1.0;
                        script = script.runCycle();
                    }
                    shouldRender.set(true);
                }
            }

            shouldQuit.set(true);
        }
    }

    private static final class Render implements Runnable {
        private static final int WINDOW_WIDTH = 1600;
        private static final int WINDOW_HEIGHT = 900;
        private static final String WINDOW_TITLE = "Survival";

        private final AtomicBoolean shouldQuit;
        private final AtomicBoolean shouldRender;

        public Render(AtomicBoolean shouldQuit, AtomicBoolean shouldRender) {
            this.shouldQuit = shouldQuit;
            this.shouldRender = shouldRender;
        }

        @Override
        public void run() {
            var display = new GLDisplay(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE);
            var keyboardAdapter = new GlfwKeyboardAdapter();
            var mouseAdapter = new GlfwMouseAdapter(display.getUnderlyingGlfwWindow());
            GLFW.glfwSetKeyCallback(display.getUnderlyingGlfwWindow(), keyboardAdapter);
            GLFW.glfwSetCursorPosCallback(display.getUnderlyingGlfwWindow(), mouseAdapter);
            GLRenderContext.init();

            var frameCounter = 0;
            var frameRateTimer = System.currentTimeMillis();
            var frameRate = 0;

            while (!shouldQuit.get()) {
                if (shouldRender.getAndSet(false)) {
                    display.swapBuffers();
                }

                if (System.currentTimeMillis() - frameRateTimer > 1000) {
                    frameRateTimer += 1000;
                    frameRate = frameCounter;
                    frameCounter = 0;
                }

                GLDisplay.pollEvents();
            }
        }
    }

    private static final class WorldSave implements Runnable {
        private final AtomicBoolean shouldQuit;

        public WorldSave(AtomicBoolean shouldQuit) {
            this.shouldQuit = shouldQuit;
        }

        @Override
        public void run() {
            while (!shouldQuit.get()) {
            }
        }
    }
}
