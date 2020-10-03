package net.survival.client.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class GlfwMouseAdapter extends MouseAdapter implements GLFWCursorPosCallbackI {
    private final long window;

    private double mouseX;
    private double mouseY;

    public GlfwMouseAdapter(long window) {
        this.window = window;
        GLFW.glfwSetInputMode(window, GLFW.GLFW_STICKY_MOUSE_BUTTONS, GLFW.GLFW_TRUE);
    }

    public void tick() {
        setMousePosition(mouseX, mouseY);
        setLmbDown(GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS);
        setRmbDown(GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS);

        if (Mouse.modeChanged) {
            if (Mouse.mode == Mouse.MODE_NORMAL)
                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            else if (Mouse.mode == Mouse.MODE_CENTERED)
                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

            clearModeChangedFlag();
        }
    }

    @Override
    public void invoke(long window, double x, double y) {
        mouseX = x;
        mouseY = y;
    }
}