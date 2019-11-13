package net.survival.markup.style;

public final class ColoredBackground extends Background
{
    private final double red;
    private final double green;
    private final double blue;

    public ColoredBackground(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }
}