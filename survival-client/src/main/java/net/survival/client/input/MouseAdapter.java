package net.survival.client.input;

public abstract class MouseAdapter
{
    protected void setMousePosition(double x, double y) {
        Mouse.prevX = Mouse.x;
        Mouse.prevY = Mouse.y;
        Mouse.x = x;
        Mouse.y = y;
    }
}