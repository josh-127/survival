package net.survival.world.chunk;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class CircularChunkLoader implements ChunkLoader
{
    private final TreeSet<Long> chunkPositions;
    private final int radius;
    
    private int prevOffsetCX;
    private int prevOffsetCZ;
    
    public CircularChunkLoader(int radius) {
        chunkPositions = new TreeSet<>();
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
        }    }
    
    @Override
    public Set<Long> getChunkPositions() {
        return Collections.unmodifiableSet(chunkPositions);
    }
    
    private int squareDistance(int x, int z) {
        return (x * x) + (z * z);
    }
}