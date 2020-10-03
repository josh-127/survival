package net.survival.markdown;

import java.util.Collections;
import java.util.List;

import net.survival.markdown.style.Style;

public final class DivisionElement extends MarkupElement {
    private List<MarkupElement> children;

    public DivisionElement(Style style, List<MarkupElement> children) {
        super(style);
        this.children = Collections.unmodifiableList(children);
    }

    public List<MarkupElement> getChildren() {
        return children;
    }
}