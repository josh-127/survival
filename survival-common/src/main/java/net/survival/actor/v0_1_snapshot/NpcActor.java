package net.survival.actor.v0_1_snapshot;

import net.survival.actor.Actor;
import net.survival.actor.Locomotion;
import net.survival.actor.Message;
import net.survival.actor.TickMessage;
import net.survival.actor.interaction.InteractionContext;
import net.survival.util.HitBox;

public class NpcActor implements Actor
{
    private final Locomotion locomotion;

    public NpcActor(double x, double y, double z) {
        locomotion = new Locomotion(x, y, z, HitBox.DEFAULT);
    }

    @Override
    public void update(InteractionContext ic, Message message) {
        if (message instanceof TickMessage) {
            locomotion.tick(this, ic);
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
}