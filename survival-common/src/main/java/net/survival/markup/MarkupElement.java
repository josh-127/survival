package net.survival.markup;

import net.survival.markup.style.Style;

public abstract class MarkupElement
{
    private final Style style;

    public MarkupElement(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }
}