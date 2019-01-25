package net.survival.client.ui;

public interface UiElementVoidVisitor
{
    void visit(UiBodyElement element);
    void visit(UiImageElement element);
    void visit(UiTextElement element);
}