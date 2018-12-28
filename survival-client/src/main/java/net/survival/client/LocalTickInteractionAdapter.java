package net.survival.client;

import net.survival.world.actor.interaction.TickInteractionAdapter;

public class LocalTickInteractionAdapter implements TickInteractionAdapter
{
    private double elapsedTime;

    @Override
    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double to) {
        elapsedTime = to;
    }
}