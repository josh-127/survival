package net.survival.actor;

import net.survival.actor.interaction.InteractionContext;

public class DrawMessage extends Message
{
    public DrawMessage(int destActorID) {
        super(destActorID);
    }

    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
}