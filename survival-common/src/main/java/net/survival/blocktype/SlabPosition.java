package net.survival.blocktype;

public enum SlabPosition
{
    BOTTOM(BlockModel.BOTTOM_SLAB),
    TOP(BlockModel.TOP_SLAB);

    private static final SlabPosition[] cachedValues = values();

    private final BlockModel model;

    private SlabPosition(BlockModel model) {
        this.model = model;
    }

    public static SlabPosition[] getCachedValues() {
        return cachedValues;
    }

    public BlockModel getModel() {
        return model;
    }
}