package net.survival.ui;

import java.util.List;

public class UiBodyElement extends UiBranchElement
{
    public UiBodyElement() {}

    public UiBodyElement(List<UiElement> children) {
        super(children);
    }

    @Override
    public <R> R accept(UiElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(UiElementVoidVisitor visitor) {
        visitor.visit(this);
    }
}