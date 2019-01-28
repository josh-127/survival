package net.survival.client.graphics;

import net.survival.ui.UiBodyElement;
import net.survival.ui.UiDom;
import net.survival.ui.UiElementVoidVisitor;
import net.survival.ui.UiImageElement;
import net.survival.ui.UiTextElement;

class NewUiDisplay implements UiElementVoidVisitor
{
    private final FontRenderer fontRenderer = new FontRenderer();
    private UiDom currentDom = new UiDom();

    public void setUiDom(UiDom to) {
        currentDom = to;
    }

    public void display() {
        currentDom.getBody().accept(this);
    }

    @Override
    public void visit(UiBodyElement element) {
        for (var child : element.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(UiImageElement element) {
    }

    @Override
    public void visit(UiTextElement element) {
        fontRenderer.drawText(element.getText(), 0.0f, 0.0f, 0.0f, 3.0f, 3.0f);
    }
}