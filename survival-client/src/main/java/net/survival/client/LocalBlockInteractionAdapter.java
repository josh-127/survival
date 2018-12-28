package net.survival.client;

import net.survival.world.World;
import net.survival.world.actor.interaction.BlockInteractionAdapter;

public class LocalBlockInteractionAdapter implements BlockInteractionAdapter
{
    private final World world;

    public LocalBlockInteractionAdapter(World world) {
        this.world = world;
    }

    @Override
    public short getBlock(int x, int y, int z) {
        return world.getBlock(x, y, z);
    }

    @Override
    public void setBlock(int x, int y, int z, short to) {
    }
}