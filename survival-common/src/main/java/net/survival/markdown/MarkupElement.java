package net.survival.markdown;

import net.survival.markdown.style.Style;

public abstract class MarkupElement {
    private final Style style;

    public MarkupElement(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }
}