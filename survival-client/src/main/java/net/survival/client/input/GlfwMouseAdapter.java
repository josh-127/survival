package net.survival.client.input;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class GlfwMouseAdapter extends MouseAdapter implements GLFWCursorPosCallbackI
{
    private double mouseX;
    private double mouseY;

    public void tick() {
        setMousePosition(mouseX, mouseY);
    }

    @Override
    public void invoke(long window, double x, double y) {
        mouseX = x;
        mouseY = y;
    }
}