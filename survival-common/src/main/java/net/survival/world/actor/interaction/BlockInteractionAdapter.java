package net.survival.world.actor.interaction;

public interface BlockInteractionAdapter
{
    short getBlock(int x, int y, int z);
    void setBlock(int x, int y, int z, short to);
}