package net.survival.world;

public interface EntityBehavior
{
    default void preTick(World world, double elapsedTime) {}

    default void tick(World world, double elapsedTime) {}

    default void postTick(World world, double elapsedTime) {}
}