package net.survival.actor.message;

import net.survival.interaction.InteractionContext;

public class JumpMessage extends ActorMessage
{
    public JumpMessage(int destActorID) {
        super(destActorID);
    }

    @Override
    public void accept(ActorMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
}