package net.survival.actor.message;

import net.survival.interaction.InteractionContext;

public class DrawMessage extends ActorMessage
{
    public DrawMessage(int destActorID) {
        super(destActorID);
    }

    @Override
    public void accept(ActorMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
}