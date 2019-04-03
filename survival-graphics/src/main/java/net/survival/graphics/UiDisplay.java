package net.survival.graphics;

import java.util.ArrayList;

import net.survival.graphics.opengl.GLImmediateDrawCall;
import net.survival.interaction.InteractionContext;
import net.survival.render.message.DrawButtonMessage;
import net.survival.render.message.DrawLabelMessage;
import net.survival.render.message.UiRenderMessage;
import net.survival.render.message.UiRenderMessageVisitor;

public class UiDisplay implements GraphicsResource
{
    private final FontRenderer fontRenderer = new FontRenderer();
    private final ArrayList<UiRenderMessage> controlsToDisplay = new ArrayList<>();

    @Override
    public void close() throws RuntimeException {
        fontRenderer.close();
    }

    public void drawControl(UiRenderMessage message) {
        controlsToDisplay.add(message);
    }

    public void display() {
        for (var message : controlsToDisplay) {
            message.accept(new UiRenderMessageVisitor() {
                @Override
                public void visit(InteractionContext ic, DrawButtonMessage message) {
                    displayText(new TextElement(
                            message.getText(),
                            message.getFontSize(),
                            message.getLeft(),
                            message.getTop()));
                }

                @Override
                public void visit(InteractionContext ic, DrawLabelMessage message) {
                    displayText(new TextElement(
                            message.getText(),
                            message.getFontSize(),
                            message.getLeft(),
                            message.getTop()));
                }
            }, null);
        }

        controlsToDisplay.clear();
    }

    private void displayRectangle(GLImmediateDrawCall drawCall, RectangleElement e) {
        if (e.held)
            drawCall.color(0.2f, 0.2f, 0.2f);
        else if (e.hovered)
            drawCall.color(0.8f, 0.8f, 0.8f);
        else
            drawCall.color(0.5f, 0.5f, 0.5f);

        drawCall.vertex((float) e.left, (float) e.bottom, 0.0f);
        drawCall.vertex((float) e.right, (float) e.bottom, 0.0f);
        drawCall.vertex((float) e.right, (float) e.top, 0.0f);
        drawCall.vertex((float) e.right, (float) e.top, 0.0f);
        drawCall.vertex((float) e.left, (float) e.top, 0.0f);
        drawCall.vertex((float) e.left, (float) e.bottom, 0.0f);
    }

    private void displayText(TextElement e) {
        fontRenderer.drawText(
                e.text,
                (float) e.left,
                (float) e.top,
                0.0f,
                (float) e.fontSize,
                (float) e.fontSize);
    }

    private static class RectangleElement
    {
        public boolean hovered;
        public boolean held;
        public final double left;
        public final double top;
        public final double right;
        public final double bottom;

        public RectangleElement(
                boolean hovered,
                boolean held,
                double left,
                double top,
                double right,
                double bottom)
        {
            this.hovered = hovered;
            this.held = held;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    private static class TextElement
    {
        public final String text;
        public final double fontSize;
        public final double left;
        public final double top;

        public TextElement(String text, double fontSize, double left, double top) {
            this.text = text;
            this.fontSize = fontSize;
            this.left = left;
            this.top = top;
        }
    }
}