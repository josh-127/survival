package net.survival.gen.biome;

import net.survival.blocktype.BlockType;

public enum BiomeType
{
    OCEAN((bt) -> {
        bt.topBlockID = BlockType.DIRT.getFullID();
        bt.minElevation = 16.0;
        bt.maxElevation = 56.0;
        bt.biomeViewerColor = 0x000000FF;
    }),
    GRASSLAND((bt) -> {
        bt.topBlockID = BlockType.GRASS_BLOCK.getFullID();
        bt.minElevation = 70.0;
        bt.maxElevation = 72.0;
        bt.biomeViewerColor = 0x0000FF00;
    }),
    FOREST((bt) -> {
        bt.topBlockID = BlockType.GRASS_BLOCK.getFullID();
        bt.minElevation = 72.0;
        bt.maxElevation = 86.0;
        bt.biomeViewerColor = 0x00009F00;
    }),
    EXTREME_HILLS((bt) -> {
        bt.topBlockID = BlockType.GRASS_BLOCK.getFullID();
        bt.minElevation = 72.0;
        bt.maxElevation = 128.0;
        bt.biomeViewerColor = 0x00FF7F00;
    }),
    DESERT((bt) -> {
        bt.topBlockID = BlockType.SAND.getFullID();
        bt.minElevation = 67.0;
        bt.maxElevation = 68.0;
        bt.biomeViewerColor = 0x00FFFF00;
    }),
    TUNDRA((bt) -> {
        bt.topBlockID = BlockType.GRASS_BLOCK.getFullID();
        bt.minElevation = 65.0;
        bt.maxElevation = 72.0;
        bt.biomeViewerColor = 0x0000FFFF;
    });

    private static final BiomeType[] cachedValues = values();

    private int topBlockID;

    private double minElevation;
    private double maxElevation;

    private int biomeViewerColor;

    private BiomeType(Builder builder) {
        topBlockID = BlockType.STONE.getFullID();
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

    public int getTopBlockID() {
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