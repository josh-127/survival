package net.survival.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class UiBranchElement extends UiElement
{
    private final ArrayList<UiElement> children;

    public UiBranchElement() {
        children = new ArrayList<>();
    }

    public UiBranchElement(List<UiElement> children) {
        this.children = new ArrayList<>(children);
    }

    public List<UiElement> getChildren() {
        return Collections.unmodifiableList(children);
    }
}