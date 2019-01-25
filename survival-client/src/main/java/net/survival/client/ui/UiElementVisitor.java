package net.survival.client.ui;

public interface UiElementVisitor<R>
{
    R visit(UiBodyElement element);
    R visit(UiImageElement element);
    R visit(UiTextElement element);
}