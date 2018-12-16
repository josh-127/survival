package net.survival.world.actor.v0_1_0_snapshot;

import net.survival.world.actor.Actor;
import net.survival.world.actor.ActorModel;

public class NpcActor extends Actor
{
    public NpcActor(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        model = ActorModel.HUMAN;
    }
}