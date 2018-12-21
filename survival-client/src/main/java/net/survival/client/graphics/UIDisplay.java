package net.survival.client.graphics;

import java.util.ArrayList;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.ui.BasicUI;
import net.survival.client.ui.ControlDisplayDesc;

public class UIDisplay implements GraphicsResource
{
    private final FontRenderer fontRenderer = new FontRenderer();
    private final BasicUI.Client uiClientPipe;

    private final ArrayList<RectangleElement> rectanglesToDisplay = new ArrayList<>();
    private final ArrayList<TextElement> textToDisplay = new ArrayList<>();

    public UIDisplay(BasicUI.Client uiClientPipe) {
        this.uiClientPipe = uiClientPipe;
    }

    @Override
    public void close() throws RuntimeException {
        fontRenderer.close();
    }

    public void display() {
        for (ControlDisplayDesc desc : uiClientPipe) {
            if (desc.type == ControlDisplayDesc.TYPE_BUTTON) {
                rectanglesToDisplay.add(new RectangleElement(
                        desc.hovered,
                        desc.held,
                        desc.left,
                        desc.top,
                        desc.right,
                        desc.bottom));
                textToDisplay.add(new TextElement(desc.text, desc.left, desc.top));
            }
            else if (desc.type == ControlDisplayDesc.TYPE_LABEL) {
                textToDisplay.add(new TextElement(desc.text, desc.left, desc.top));
            }
        }

        for (TextElement textElement : textToDisplay)
            displayText(textElement);
        textToDisplay.clear();

        GLImmediateDrawCall drawCall = GLImmediateDrawCall.beginTriangles(null);

        for (RectangleElement rectangleElement : rectanglesToDisplay)
            displayRectangle(drawCall, rectangleElement);
        rectanglesToDisplay.clear();

        drawCall.end();
    }

    private void displayRectangle(GLImmediateDrawCall drawCall, RectangleElement e) {
        drawCall.color(1.0f, 1.0f, 1.0f);
        drawCall.vertex(e.left, e.bottom, 0.0f);
        drawCall.vertex(e.right, e.bottom, 0.0f);
        drawCall.vertex(e.right, e.top, 0.0f);
        drawCall.vertex(e.right, e.top, 0.0f);
        drawCall.vertex(e.left, e.top, 0.0f);
        drawCall.vertex(e.left, e.bottom, 0.0f);
    }

    private void displayText(TextElement e) {
        fontRenderer.drawText(e.text, e.left, e.top, 0.0f, 3.0f, 3.0f);
    }

    private static class RectangleElement
    {
        public boolean hovered;
        public boolean held;
        public final int left;
        public final int top;
        public final int right;
        public final int bottom;

        public RectangleElement(
                boolean hovered,
                boolean held,
                int left,
                int top,
                int right,
                int bottom)
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
        public final int left;
        public final int top;

        public TextElement(String text, int left, int top) {
            this.text = text;
            this.left = left;
            this.top = top;
        }
    }
}