package net.survival.actor.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class JumpMessage extends ActorMessage
{
    public JumpMessage(int destActorId) {
        super(destActorId);
    }

    @Override
    public void accept(ActorMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.GAMEPLAY_PRE_STEP;
    }
}