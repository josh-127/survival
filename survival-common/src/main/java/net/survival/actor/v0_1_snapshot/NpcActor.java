package net.survival.actor.v0_1_snapshot;

import net.survival.actor.Actor;
import net.survival.actor.Locomotion;
import net.survival.actor.StepMessage;
import net.survival.actor.interaction.InteractionContext;
import net.survival.util.HitBox;

public class NpcActor implements Actor
{
    private final Locomotion locomotion;

    public NpcActor(double x, double y, double z) {
        locomotion = new Locomotion(x, y, z, HitBox.DEFAULT);
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