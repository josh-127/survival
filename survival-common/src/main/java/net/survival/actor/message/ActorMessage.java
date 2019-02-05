package net.survival.actor.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.Message;
import net.survival.interaction.MessageVisitor;

public abstract class ActorMessage extends Message
{
    private final int destActorID;

    public ActorMessage(int destActorID) {
        this.destActorID = destActorID;
    }

    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    public abstract void accept(ActorMessageVisitor visitor, InteractionContext ic);

    public int getDestActorID() {
        return destActorID;
    }
}