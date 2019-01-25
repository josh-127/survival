package net.survival.ui;

public class UiBodyElement extends UiBranchElement
{
    @Override
    public <R> R accept(UiElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(UiElementVoidVisitor visitor) {
        visitor.visit(this);
    }
}