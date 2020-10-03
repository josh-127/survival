package net.survival.markdown;

public abstract class Page {
    private final MarkupElement root;

    public Page(MarkupElement root) {
        this.root = root;
    }

    public MarkupElement getRoot() {
        return root;
    }
}