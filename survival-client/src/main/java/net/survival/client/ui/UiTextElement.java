package net.survival.client.ui;

public class UiTextElement extends UiLeafElement
{
    private final String text;

    public UiTextElement(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
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