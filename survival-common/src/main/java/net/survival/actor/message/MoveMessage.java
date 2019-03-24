package net.survival.actor.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.MessagePriority;

public class MoveMessage extends ActorMessage
{
    private final double dx;
    private final double dz;

    public MoveMessage(int destActorId, double dx, double dz) {
        super(destActorId);
        this.dx = dx;
        this.dz = dz;
    }

    @Override
    public void accept(ActorMessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    @Override
    public int getPriority() {
        return MessagePriority.GAMEPLAY_PRE_STEP;
    }

    public double getDX() {
        return dx;
    }

    public double getDZ() {
        return dz;
    }
}