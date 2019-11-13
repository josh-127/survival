package net.survival.graphics;

final class DrawTextCommand extends DrawCommand
{
    private final String text;
    private final double x;
    private final double y;
    private final double z;

    public DrawTextCommand(String text, double x, double y, double z) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getText() {
        return text;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}