package net.survival.world.actor.v0_1_0_snapshot;

import net.survival.world.actor.Actor;

public class PlayerActor extends Actor
{
    private double x;
    private double y;
    private double z;

    public PlayerActor(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }
}