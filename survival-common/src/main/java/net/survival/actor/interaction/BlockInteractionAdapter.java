package net.survival.actor.interaction;

import net.survival.blocktype.Block;

public interface BlockInteractionAdapter
{
    Block getBlock(int x, int y, int z);
    void setBlock(int x, int y, int z, Block to);
}