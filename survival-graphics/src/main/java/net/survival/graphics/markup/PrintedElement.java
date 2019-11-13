package net.survival.graphics.markup;

import net.survival.util.Rectangle;

public class PrintedElement
{
    private final Rectangle boundingBox;
    private final double z;

    public PrintedElement(Rectangle boundingBox, double z) {
        this.boundingBox = boundingBox;
        this.z = z;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public double getZ() {
        return z;
    }
}