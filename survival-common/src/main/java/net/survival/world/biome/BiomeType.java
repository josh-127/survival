package net.survival.world.biome;

import net.survival.block.BlockRegistry;

public enum BiomeType
{
    OCEAN((bt) -> {
        bt.topBlockID = BlockRegistry.INSTANCE.guessFirstBlock("andesite dirt").getID();
        bt.minElevation = 16.0;
        bt.maxElevation = 61.0;
        bt.biomeViewerColor = 0x000000FF;
    }),
    GRASSLAND((bt) -> {
        bt.topBlockID = BlockRegistry.INSTANCE.guessFirstBlock("grass").getID();
        bt.minElevation = 65.0;
        bt.maxElevation = 72.0;
        bt.biomeViewerColor = 0x0000FF00;
    }),
    FOREST((bt) -> {
        bt.topBlockID = BlockRegistry.INSTANCE.guessFirstBlock("grass").getID();
        bt.minElevation = 72.0;
        bt.maxElevation = 86.0;
        bt.biomeViewerColor = 0x00009F00;
    }),
    EXTREME_HILLS((bt) -> {
        bt.topBlockID = BlockRegistry.INSTANCE.guessFirstBlock("grass").getID();
        bt.minElevation = 72.0;
        bt.maxElevation = 128.0;
        bt.biomeViewerColor = 0x00FF7F00;
    }),
    DESERT((bt) -> {
        bt.topBlockID = BlockRegistry.INSTANCE.guessFirstBlock("andesite sand").getID();
        bt.minElevation = 65.0;
        bt.maxElevation = 72.0;
        bt.biomeViewerColor = 0x00FFFF00;
    }),
    TUNDRA((bt) -> {
        bt.topBlockID = BlockRegistry.INSTANCE.guessFirstBlock("grass").getID();
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
        topBlockID = BlockRegistry.INSTANCE.guessFirstBlock("andesite stone").getID();
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