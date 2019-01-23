package net.survival.actor.message;

import net.survival.actor.interaction.InteractionContext;

public class HurtMessage extends ActorMessage
{
    private final double amount;

    public HurtMessage(int destActorID, double amount) {
        super(destActorID);
        this.amount = amount;
    }

    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    public double getAmount() {
        return amount;
    }
}