package net.survival.world.actor.v0_1_0_snapshot;

import net.survival.util.HitBox;
import net.survival.world.actor.Actor;
import net.survival.world.actor.Locomotion;
import net.survival.world.actor.Message;
import net.survival.world.actor.TickMessage;
import net.survival.world.actor.interaction.InteractionContext;

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