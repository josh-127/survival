package net.survival.ui;

public interface UiElementVoidVisitor
{
    void visit(UiBodyElement element);
    void visit(UiImageElement element);
    void visit(UiTextElement element);
}