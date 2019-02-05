package net.survival.actor.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class StepMessage extends ActorMessage
{
    public StepMessage(int destActorID) {
        super(destActorID);
    }

    @Override
    public void accept(ActorMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.GAMEPLAY_STEP;
    }
}