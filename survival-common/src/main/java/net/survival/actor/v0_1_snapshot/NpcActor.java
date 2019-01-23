package net.survival.actor.v0_1_snapshot;

import net.survival.actor.Actor;
import net.survival.actor.Locomotion;
import net.survival.actor.message.HurtMessage;
import net.survival.actor.message.StepMessage;
import net.survival.interaction.InteractionContext;
import net.survival.util.HitBox;

public class NpcActor implements Actor
{
    private final Locomotion locomotion;
    private double pitch = 0.0;
    private double direction = 0.0;
    private double timer = 0.0;
    private boolean isWalking;
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
            if (timer > 0.0) {
                timer -= ic.getElapsedTime();
            }
            else {
                timer = 1.0 + Math.random() * 5.0;
                isWalking = !isWalking;
                if (isWalking)
                    direction = 2.0 * Math.PI * Math.random();
            }

            if (isWalking)
                locomotion.setMovementDirection(Math.sin(direction), Math.cos(direction));
            else
                locomotion.setMovementDirection(0.0, 0.0);

            locomotion.tick(this, ic);
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
        return direction;
    }

    @Override
    public double getPitch() {
        return pitch;
    }
}