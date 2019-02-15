package net.survival.render.message;

import net.survival.interaction.InteractionContext;

public interface UiRenderMessageVisitor
{
    void visit(InteractionContext ic, DrawButtonMessage message);
    void visit(InteractionContext ic, DrawLabelMessage message);
}