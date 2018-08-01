package net.survival.world.chunk;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

public class CircularChunkLoader implements ChunkLoader
{
    private final LongSet chunkPositions;
    private final int radius;

    private int prevOffsetCX;
    private int prevOffsetCZ;

    public CircularChunkLoader(int radius) {
        chunkPositions = new LongOpenHashSet(radius * radius);
        this.radius = radius;

        forceSetCenter(0, 0);
    }

    public void setCenter(int offsetCX, int offsetCZ) {
        if (offsetCX == prevOffsetCX && offsetCZ == prevOffsetCZ)
            return;

        forceSetCenter(offsetCX, offsetCZ);
    }

    private void forceSetCenter(int offsetCX, int offsetCZ) {
        chunkPositions.clear();

        int radiusSquared = radius * radius;

        for (int z = -radius; z < radius; ++z) {
            for (int x = -radius; x < radius; ++x) {
                if (squareDistance(x, z) <= radiusSquared) {
                    int cx = offsetCX + x;
                    int cz = offsetCZ + z;
                    chunkPositions.add(ChunkPos.hashPos(cx, cz));
                }
            }
        }
    }

    @Override
    public LongSet getChunkPositions() {
        return new LongArraySet(chunkPositions);
    }

    private int squareDistance(int x, int z) {
        return (x * x) + (z * z);
    }
}