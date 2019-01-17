package net.survival.blocktype;

public enum StairDirection
{
    NORTH(BlockModel.NORTH_STAIRS),
    EAST(BlockModel.EAST_STAIRS),
    SOUTH(BlockModel.SOUTH_STAIRS),
    WEST(BlockModel.WEST_STAIRS),
    CEILING_NORTH(BlockModel.NORTH_CEILING_STAIRS),
    CEILING_EAST(BlockModel.EAST_CEILING_STAIRS),
    CEILING_SOUTH(BlockModel.SOUTH_CEILING_STAIRS),
    CEILING_WEST(BlockModel.WEST_CEILING_STAIRS);

    private static final StairDirection[] cachedValues = values();

    private BlockModel model;

    private StairDirection(BlockModel model) {
        this.model = model;
    }

    public static StairDirection[] getCachedValues() {
        return cachedValues;
    }

    public BlockModel getModel() {
        return model;
    }
}