package net.survival.actor.message;

import net.survival.interaction.InteractionContext;

public class HurtMessage extends ActorMessage
{
    private final double amount;

    public HurtMessage(int destActorID, double amount) {
        super(destActorID);
        this.amount = amount;
    }

    @Override
    public void accept(ActorMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    public double getAmount() {
        return amount;
    }
}