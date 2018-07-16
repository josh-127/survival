package net.survival.world.gen.terrain;

import net.survival.world.chunk.Chunk;

public class ChunkPrimer
{
    private final short[] blockIDs;
    public final int chunkX;
    public final int chunkZ;
    
    public ChunkPrimer(int cx, int cz) {
        blockIDs = new short[Chunk.VOLUME];
        chunkX = cx;
        chunkZ = cz;
    }
    
    public short getBlockID(int localX, int localY, int localZ) {
        return blockIDs[localXyzToIndex(localX, localY, localZ)];
    }
    
    void setBlockID(int localX, int localY, int localZ, short to) {
        blockIDs[localXyzToIndex(localX, localY, localZ)] = to;
    }

    private int localXyzToIndex(int x, int y, int z) {
        return x + (z * Chunk.XLENGTH) + (y * Chunk.BASE_AREA);
    }
    
    public boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && ly >= 0 && lz >= 0 && lx < Chunk.XLENGTH && ly < Chunk.YLENGTH && lz < Chunk.ZLENGTH;
    }
    
    public Chunk toChunk() {
        Chunk chunk = new Chunk();
        
        for (int y = 0; y < Chunk.YLENGTH; ++y) {
            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                for (int x = 0; x < Chunk.XLENGTH; ++x)
                    chunk.setBlockID(x, y, z, getBlockID(x, y, z));
            }
        }
        
        return chunk;
    }
}