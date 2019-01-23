package net.survival.actor.message;

import net.survival.actor.interaction.InteractionContext;

public class StepMessage extends ActorMessage
{
    public StepMessage(int destActorID) {
        super(destActorID);
    }

    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
}