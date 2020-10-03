package net.survival.graphics.markdown;

import java.util.Collection;

public class RenderedPage {
    private final RenderedElementCollection elements;
    private final double width;
    private final double height;

    public RenderedPage(Collection<RenderedElement> elements, double width, double height) {
        this.elements = new RenderedElementCollection(elements);
        this.width = width;
        this.height = height;
    }

    public RenderedElementCollection getElements() {
        return elements;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}