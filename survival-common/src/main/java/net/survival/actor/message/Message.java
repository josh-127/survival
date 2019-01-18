package net.survival.actor.message;

import net.survival.actor.interaction.InteractionContext;

public abstract class Message
{
    private final int destActorID;

    public Message(int destActorID) {
        this.destActorID = destActorID;
    }

    public abstract void accept(MessageVisitor visitor, InteractionContext ic);

    public int getDestActorID() {
        return destActorID;
    }
}