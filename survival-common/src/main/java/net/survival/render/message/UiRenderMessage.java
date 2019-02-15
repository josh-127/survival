package net.survival.render.message;

import net.survival.interaction.InteractionContext;

public abstract class UiRenderMessage extends RenderMessage
{
    @Override
    public void accept(RenderMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    public abstract void accept(UiRenderMessageVisitor visitor, InteractionContext ic);
}