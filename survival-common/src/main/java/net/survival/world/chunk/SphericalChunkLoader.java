package net.survival.world.chunk;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class SphericalChunkLoader implements ChunkLoader
{
    private final TreeSet<Long> chunkPositions;
    private final int radius;
    
    private int prevOffsetCX;
    private int prevOffsetCY;
    private int prevOffsetCZ;
    
    public SphericalChunkLoader(int radius) {
        chunkPositions = new TreeSet<>();
        this.radius = radius;
    }
    
    public void setCenter(int offsetCX, int offsetCY, int offsetCZ) {
        if (offsetCX == prevOffsetCX && offsetCY == prevOffsetCY && offsetCZ == prevOffsetCZ)
            return;
        
        chunkPositions.clear();
        
        int radiusSquared = radius * radius;
        
        for (int z = -radius; z < radius; ++z) {
            for (int x = -radius; x < radius; ++x) {
                for (int y = -radius; y < radius; ++y) {
                    if (squareDistance(x, y, z) <= radiusSquared) {
                        int cx = offsetCX + x;
                        int cy = offsetCY + y;
                        int cz = offsetCZ + z;
                        chunkPositions.add(ChunkPos.hashPos(cx, cy, cz));
                    }
                }
            }
        }
    }
    
    @Override
    public Set<Long> getChunkPositions() {
        return Collections.unmodifiableSet(chunkPositions);
    }
    
    private int squareDistance(int x, int y, int z) {
        return (x * x) + (y * y) + (z * z);
    }
}