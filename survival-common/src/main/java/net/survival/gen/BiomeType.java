package net.survival.gen;

import net.survival.block.state.BlockState;
import net.survival.block.state.DirtBlock;
import net.survival.block.state.GrassBlock;
import net.survival.block.state.SandBlock;
import net.survival.block.state.StoneBlock;

public enum BiomeType
{
    OCEAN((bt) -> {
        bt.topBlock = DirtBlock.INSTANCE;
        bt.minElevation = 48.0;
        bt.maxElevation = 60.0;
        bt.biomeViewerColor = 0x000000FF;
    }),
    GRASSLAND((bt) -> {
        bt.topBlock = GrassBlock.INSTANCE;
        bt.minElevation = 64.0;
        bt.maxElevation = 96.0;
        bt.biomeViewerColor = 0x0000FF00;
    }),
    FOREST((bt) -> {
        bt.topBlock = GrassBlock.INSTANCE;
        bt.minElevation = 64.0;
        bt.maxElevation = 96.0;
        bt.biomeViewerColor = 0x00009F00;
    }),
    EXTREME_HILLS((bt) -> {
        bt.topBlock = GrassBlock.INSTANCE;
        bt.minElevation = 72.0;
        bt.maxElevation = 192.0;
        bt.biomeViewerColor = 0x00FF7F00;
    }),
    DESERT((bt) -> {
        bt.topBlock = SandBlock.INSTANCE;
        bt.minElevation = 64.0;
        bt.maxElevation = 72.0;
        bt.biomeViewerColor = 0x00FFFF00;
    }),
    TUNDRA((bt) -> {
        bt.topBlock = GrassBlock.INSTANCE;
        bt.minElevation = 72.0;
        bt.maxElevation = 96.0;
        bt.biomeViewerColor = 0x0000FFFF;
    });

    private static final BiomeType[] cachedValues = values();

    private BlockState topBlock;

    private double minElevation;
    private double maxElevation;

    private int biomeViewerColor;

    private BiomeType(Builder builder) {
        topBlock = StoneBlock.INSTANCE;
        minElevation = 0.0;
        maxElevation = 1.0;
        builder.build(this);
    }

    public static BiomeType[] getCachedValues() {
        return cachedValues;
    }

    public static BiomeType byId(int id) {
        return cachedValues[id];
    }

    public BlockState getTopBlock() {
        return topBlock;
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