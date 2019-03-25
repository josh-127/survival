package net.survival.actor.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class HurtMessage extends ActorMessage
{
    private final double amount;

    public HurtMessage(int destActorId, double amount) {
        super(destActorId);
        this.amount = amount;
    }

    @Override
    public void accept(ActorMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.GAMEPLAY_PRE_STEP.ordinal();
    }

    public double getAmount() {
        return amount;
    }
}