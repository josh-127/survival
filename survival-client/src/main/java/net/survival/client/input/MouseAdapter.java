package net.survival.client.input;

public abstract class MouseAdapter {
    protected void setMousePosition(double x, double y) {
        Mouse.prevX = Mouse.x;
        Mouse.prevY = Mouse.y;
        Mouse.x = x;
        Mouse.y = y;
    }

    protected void setLmbDown(boolean down) {
        Mouse.prevLmbDown = Mouse.lmbDown;
        Mouse.lmbDown = down;
    }

    protected void setRmbDown(boolean down) {
        Mouse.prevRmbDown = Mouse.rmbDown;
        Mouse.rmbDown = down;
    }

    protected void clearModeChangedFlag() {
        Mouse.modeChanged = false;
    }
}