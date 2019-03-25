package net.survival.render.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class DrawButtonMessage extends UiRenderMessage
{
    private final String text;
    private final double fontSize;
    private final double left;
    private final double top;

    public DrawButtonMessage(String text, double fontSize, double left, double top) {
        this.text = text;
        this.fontSize = fontSize;
        this.left = left;
        this.top = top;
    }

    public String getText() {
        return text;
    }

    public double getFontSize() {
        return fontSize;
    }

    public double getLeft() {
        return left;
    }

    public double getTop() {
        return top;
    }

    @Override
    public void accept(UiRenderMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.RESERVED_DRAW.ordinal();
    }
}