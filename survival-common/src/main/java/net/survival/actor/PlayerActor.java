package net.survival.actor;

import net.survival.actor.message.JumpMessage;
import net.survival.actor.message.MoveMessage;
import net.survival.actor.message.StepMessage;
import net.survival.interaction.InteractionContext;
import net.survival.util.HitBox;

public class PlayerActor implements Actor
{
    private final Locomotion locomotion;

    public PlayerActor(double x, double y, double z) {
        locomotion = new Locomotion(x, y, z, HitBox.PLAYER);
        locomotion.setMovementSpeed(5.0);
    }

    @Override
    public void visit(InteractionContext ic, MoveMessage message) {
        locomotion.setMovementDirection(message.getDX(), message.getDZ());
    }

    @Override
    public void visit(InteractionContext ic, JumpMessage message) {
        locomotion.jump(1.0);
    }

    @Override
    public void visit(InteractionContext ic, StepMessage message) {
        locomotion.tick(this, ic);
    }

    @Override
    public double getX() {
        return locomotion.getX();
    }

    @Override
    public double getY() {
        return locomotion.getY();
    }

    @Override
    public double getZ() {
        return locomotion.getZ();
    }
}