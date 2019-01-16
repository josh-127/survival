package net.survival.world.actor.interaction;

import net.survival.block.Block;

public interface BlockInteractionAdapter
{
    Block getBlock(int x, int y, int z);
    void setBlock(int x, int y, int z, Block to);
}