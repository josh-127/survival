package net.survival.world.chunk;

import it.unimi.dsi.fastutil.longs.LongSet;

public interface ChunkStageMask
{
    LongSet getChunkPositions();
}