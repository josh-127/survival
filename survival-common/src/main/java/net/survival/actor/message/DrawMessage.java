package net.survival.actor.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class DrawMessage extends ActorMessage
{
    public DrawMessage(int destActorId) {
        super(destActorId);
    }

    @Override
    public void accept(ActorMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.GAMEPLAY_DRAW;
    }
}