package net.survival.client.graphics.opengl;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import net.survival.client.graphics.GraphicsResource;

public class GLDisplay implements GraphicsResource
{
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
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwMakeContextCurrent(window);

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);
        int monitorWidth = vidMode.width();
        int monitorHeight = vidMode.height();

        int windowX = (monitorWidth - width) / 2;
        int windowY = (monitorHeight - height) / 2;
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