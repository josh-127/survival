package net.survival.client;

import net.survival.block.Block;
import net.survival.world.World;
import net.survival.world.actor.interaction.BlockInteractionAdapter;

public class LocalBlockInteractionAdapter implements BlockInteractionAdapter
{
    private final World world;

    public LocalBlockInteractionAdapter(World world) {
        this.world = world;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return world.getBlockState(x, y, z);
    }

    @Override
    public void setBlock(int x, int y, int z, Block to) {
        world.setBlockState(x, y, z, to);
    }
}