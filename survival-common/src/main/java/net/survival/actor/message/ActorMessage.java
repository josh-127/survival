package net.survival.actor.message;

import net.survival.interaction.InteractionContext;

public abstract class ActorMessage
{
    private final int destActorID;

    public ActorMessage(int destActorID) {
        this.destActorID = destActorID;
    }

    public abstract void accept(ActorMessageVisitor visitor, InteractionContext ic);

    public int getDestActorID() {
        return destActorID;
    }
}