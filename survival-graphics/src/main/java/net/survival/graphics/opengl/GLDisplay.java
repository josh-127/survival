package net.survival.graphics.opengl;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.GL;

import net.survival.graphics.GraphicsResource;

public class GLDisplay implements GraphicsResource {
    private static int totalWindows = 0;

    private final long window;

    public GLDisplay(int width, int height, String title) {
        if (totalWindows == 0) {
            if (!glfwInit()) {
                throw new GLException("Unable to initialize GLFW.");
            }
            else {
                glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
                glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 4);
                glfwSwapInterval(0);
            }
        }

        window = glfwCreateWindow(width, height, title, 0, 0);
        glfwMakeContextCurrent(window);

        var monitor = glfwGetPrimaryMonitor();
        var vidMode = glfwGetVideoMode(monitor);
        var monitorWidth = vidMode.width();
        var monitorHeight = vidMode.height();

        var windowX = (monitorWidth - width) / 2;
        var windowY = (monitorHeight - height) / 2;
        glfwSetWindowPos(window, windowX, windowY);

        glfwShowWindow(window);

        GL.createCapabilities();
    }

    @Override
    public void close() {
        glfwDestroyWindow(window);

        if (totalWindows > 0)
            --totalWindows;

        if (totalWindows == 0)
            glfwTerminate();
    }

    public long getUnderlyingGlfwWindow() {
        return window;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public static void pollEvents() {
        glfwPollEvents();
    }
}