package net.survival.world.gen.infinite;

import net.survival.block.BlockType;

public enum BiomeType
{
    OCEAN((bt) -> {
        bt.topBlockID = BlockType.LIMESTONE_DIRT.getID();
        bt.minElevation = -64.0;
        bt.maxElevation = -16.0;
        bt.biomeViewerColor = 0x000000FF;
    }),
    GRASSLAND((bt) -> {
        bt.topBlockID = BlockType.GRASS.getID();
        bt.minElevation = 2.0;
        bt.maxElevation = 8.0;
        bt.biomeViewerColor = 0x0000FF00;
    }),
    FOREST((bt) -> {
        bt.topBlockID = BlockType.GRASS.getID();
        bt.minElevation = 8.0;
        bt.maxElevation = 32.0;
        bt.biomeViewerColor = 0x00009F00;
    }),
    EXTREME_HILLS((bt) -> {
        bt.topBlockID = BlockType.GRASS.getID();
        bt.minElevation = 36.0;
        bt.maxElevation = 128.0;
        bt.biomeViewerColor = 0x00FF7F00;
    }),
    DESERT((bt) -> {
        bt.topBlockID = BlockType.LIMESTONE_SAND.getID();
        bt.minElevation = 2.0;
        bt.maxElevation = 8.0;
        bt.biomeViewerColor = 0x00FFFF00;
    }),
    TUNDRA((bt) -> {
        bt.topBlockID = BlockType.GRASS.getID();
        bt.minElevation = 2.0;
        bt.maxElevation = 4.0;
        bt.biomeViewerColor = 0x0000FFFF;
    });
    
    private static final BiomeType[] cachedValues = values();
    
    private short topBlockID;
    
    private double minElevation;
    private double maxElevation;
    
    private int biomeViewerColor;
    
    private BiomeType(Builder builder) {
        topBlockID = BlockType.GRANITE.getID();
        minElevation = 0.0;
        maxElevation = 1.0;
        builder.build(this);
    }
    
    public static BiomeType[] getCachedValues() {
        return cachedValues;
    }
    
    public static BiomeType byID(int id) {
        return cachedValues[id];
    }
    
    public short getTopBlockID() {
        return topBlockID;
    }
    
    public double getMinElevation() {
        return minElevation;
    }
    
    public double getMaxElevation() {
        return maxElevation;
    }
    
    public double getElevationRange() {
        return maxElevation - minElevation;
    }
    
    public int getBiomeViewerColor() {
        return biomeViewerColor;
    }
    
    private interface Builder
    {
        void build(BiomeType bt);
    }
}