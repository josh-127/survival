package net.survival.actor.v0_1_snapshot;

import net.survival.actor.Actor;
import net.survival.actor.Locomotion;
import net.survival.actor.interaction.InteractionContext;
import net.survival.actor.message.HurtMessage;
import net.survival.actor.message.StepMessage;
import net.survival.util.HitBox;

public class NpcActor implements Actor
{
    private final Locomotion locomotion;
    private double yaw;
    private double pitch = 0.0;
    private double health = 50.0;

    public NpcActor(double x, double y, double z) {
        locomotion = new Locomotion(x, y, z, HitBox.DEFAULT);
    }

    @Override
    public void visit(InteractionContext ic, HurtMessage message) {
        health -= message.getAmount();
    }

    @Override
    public void visit(InteractionContext ic, StepMessage message) {
        if (health > 0.0) {
            locomotion.setMovementDirection(Math.sin(yaw), Math.cos(yaw));
            locomotion.tick(this, ic);
            yaw += 0.5 * ic.getElapsedTime();
        }
        else {
            if (pitch < Math.PI / 2.0)
                pitch += 4.0 * ic.getElapsedTime();
            if (pitch > Math.PI / 2.0)
                pitch = Math.PI / 2.0;
        }
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

    @Override
    public double getYaw() {
        return yaw;
    }

    @Override
    public double getPitch() {
        return pitch;
    }
}