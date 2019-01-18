package net.survival.actor;

import net.survival.actor.interaction.InteractionContext;

public class DrawMessage extends Message
{
    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
}