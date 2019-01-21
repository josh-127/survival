package net.survival.actor.message;

import net.survival.actor.interaction.InteractionContext;

public class JumpMessage extends Message
{
    public JumpMessage(int destActorID) {
        super(destActorID);
    }

    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }
}