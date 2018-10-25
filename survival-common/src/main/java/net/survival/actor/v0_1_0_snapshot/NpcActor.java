package net.survival.actor.v0_1_0_snapshot;

import net.survival.actor.Actor;
import net.survival.actor.ActorModel;
import net.survival.util.HitBox;
import net.survival.util.MathEx;

public class NpcActor extends Actor
{
    public NpcActor(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        hitBox = HitBox.NPC;
        model = ActorModel.HUMAN;
    }
}