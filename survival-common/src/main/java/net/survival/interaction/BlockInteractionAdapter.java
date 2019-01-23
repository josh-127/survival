package net.survival.interaction;

import net.survival.blocktype.Block;

public interface BlockInteractionAdapter
{
    Block getBlock(int x, int y, int z);

    void breakBlock(int x, int y, int z);
    void placeBlock(int x, int y, int z, int fullID);
}