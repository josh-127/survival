package net.survival.world.actor.interaction;

import net.survival.block.BlockState;

public interface BlockInteractionAdapter
{
    BlockState getBlock(int x, int y, int z);
    void setBlock(int x, int y, int z, BlockState to);
}