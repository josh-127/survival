package net.survival.world.gen.v1;

import net.survival.world.chunk.Chunk;

class ChunkPrimer
{
    private final short[] blockIDs;
    
    public ChunkPrimer() {
        blockIDs = new short[Chunk.VOLUME];
    }
    
    public short getBlockID(int localX, int localY, int localZ) {
        return blockIDs[localXyzToIndex(localX, localY, localZ)];
    }
    
    public void setBlockID(int localX, int localY, int localZ, short to) {
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