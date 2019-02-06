package net.survival.render.message;

import net.survival.interaction.InteractionContext;

public interface RenderMessageVisitor
{
    void visit(InteractionContext ic, DrawModelMessage message);
}