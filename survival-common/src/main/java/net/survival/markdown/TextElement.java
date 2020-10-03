package net.survival.markdown;

import net.survival.markdown.style.Style;

public final class TextElement extends MarkupElement {
    private final String text;

    public TextElement(Style style, String text) {
        super(style);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}