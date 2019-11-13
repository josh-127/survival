package net.survival.graphics.markup;

import java.util.Collection;

public class PrintedPage
{
    private final PrintedElementCollection elements;
    private final double width;
    private final double height;

    public PrintedPage(Collection<PrintedElement> elements, double width, double height) {
        this.elements = new PrintedElementCollection(elements);
        this.width = width;
        this.height = height;
    }

    public PrintedElementCollection getElements() {
        return elements;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}