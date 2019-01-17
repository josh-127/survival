package net.survival.client;

import net.survival.block.Block;
import net.survival.world.BlockSpace;
import net.survival.world.actor.interaction.BlockInteractionAdapter;

public class LocalBlockInteractionAdapter implements BlockInteractionAdapter
{
    private final BlockSpace blockSpace;

    public LocalBlockInteractionAdapter(BlockSpace blockSpace) {
        this.blockSpace = blockSpace;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return blockSpace.getBlockState(x, y, z);
    }

    @Override
    public void setBlock(int x, int y, int z, Block to) {
        blockSpace.setBlockState(x, y, z, to);
    }
}