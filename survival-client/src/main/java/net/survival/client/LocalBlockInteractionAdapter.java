package net.survival.client;

import net.survival.block.BlockRegistry;
import net.survival.block.BlockState;
import net.survival.world.World;
import net.survival.world.actor.interaction.BlockInteractionAdapter;

public class LocalBlockInteractionAdapter implements BlockInteractionAdapter
{
    private final World world;

    public LocalBlockInteractionAdapter(World world) {
        this.world = world;
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return BlockRegistry.INSTANCE.getBlock(world.getBlock(x, y, z));
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState to) {
    }
}