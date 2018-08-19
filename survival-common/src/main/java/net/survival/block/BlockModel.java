package net.survival.block;

public enum BlockModel
{
    DEFAULT,
    INVISIBLE,
    FENCE,
    SAPLING;

    private static final BlockModel[] cachedValues = values();

    public final int id;

    private BlockModel() {
        id = ordinal();
    }

    public static BlockModel[] getCachedValues() {
        return cachedValues;
    }

    public static BlockModel byID(int id) {
        return cachedValues[id];
    }
}