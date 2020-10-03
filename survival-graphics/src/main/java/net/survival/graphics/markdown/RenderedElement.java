package net.survival.graphics.markdown;

import net.survival.util.Rectangle;

public class RenderedElement {
    private final Rectangle boundingBox;
    private final double z;

    public RenderedElement(Rectangle boundingBox, double z) {
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