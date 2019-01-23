package net.survival.actor.message;

import net.survival.interaction.InteractionContext;

public class MoveMessage extends ActorMessage
{
    private final double dx;
    private final double dz;

    public MoveMessage(int destActorID, double dx, double dz) {
        super(destActorID);
        this.dx = dx;
        this.dz = dz;
    }

    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    public double getDX() {
        return dx;
    }

    public double getDZ() {
        return dz;
    }
}