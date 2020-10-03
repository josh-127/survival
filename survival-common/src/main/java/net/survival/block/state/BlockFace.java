package net.survival.block.state;

public enum BlockFace {
    TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK;

    private static final BlockFace[] cachedValues = values();

    public static BlockFace[] getCachedValues() {
        return cachedValues;
    }
}