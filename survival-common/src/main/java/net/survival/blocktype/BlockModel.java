package net.survival.blocktype;

public enum BlockModel
{
    DEFAULT(bm -> {
        bm.blockingTop = true;
        bm.blockingBottom = true;
        bm.blockingFront = true;
        bm.blockingBack = true;
        bm.blockingLeft = true;
        bm.blockingRight = true;
    }),
    INVISIBLE,
    GLASS,
    FENCE,
    SAPLING,
    BOTTOM_SLAB(bm -> {
        bm.blockingBottom = true;
    }),
    TOP_SLAB(bm -> {
        bm.blockingTop = true;
    }),
    NORTH_STAIRS(bm -> {
        bm.blockingBottom = true;
        bm.blockingBack = true;
    }),
    SOUTH_STAIRS(bm -> {
        bm.blockingBottom = true;
        bm.blockingFront = true;
    }),
    EAST_STAIRS(bm -> {
        bm.blockingBottom = true;
        bm.blockingRight = true;
    }),
    WEST_STAIRS(bm -> {
        bm.blockingBottom = true;
        bm.blockingLeft = true;
    }),
    NORTH_CEILING_STAIRS(bm -> {
        bm.blockingTop = true;
        bm.blockingBack = true;
    }),
    SOUTH_CEILING_STAIRS(bm -> {
        bm.blockingTop = true;
        bm.blockingFront = true;
    }),
    EAST_CEILING_STAIRS(bm -> {
        bm.blockingTop = true;
        bm.blockingRight = true;
    }),
    WEST_CEILING_STAIRS(bm -> {
        bm.blockingTop = true;
        bm.blockingLeft = true;
    }),
    TORCH,
    FIRE,
    CROPS,
    FARMLAND(bm -> {
        bm.blockingBottom = true;
    }),
    LEVER_FLOOR_OFF,
    LEVER_FLOOR_ON,
    LEVER_CEILING_OFF,
    LEVER_CEILING_ON,
    LEVER_NORTH_OFF,
    LEVER_NORTH_ON,
    LEVER_SOUTH_OFF,
    LEVER_SOUTH_ON,
    LEVER_EAST_OFF,
    LEVER_EAST_ON,
    LEVER_WEST_OFF,
    LEVER_WEST_ON,
    PRESSURE_PLATE_OFF,
    PRESSURE_PLATE_ON,
    BUTTON_FLOOR_OFF,
    BUTTON_FLOOR_ON,
    BUTTON_CEILING_OFF,
    BUTTON_CEILING_ON,
    BUTTON_NORTH_OFF,
    BUTTON_NORTH_ON,
    BUTTON_SOUTH_OFF,
    BUTTON_SOUTH_ON,
    BUTTON_EAST_OFF,
    BUTTON_EAST_ON,
    BUTTON_WEST_OFF,
    BUTTON_WEST_ON,
    SNOW_LAYER_0,
    ICE,
    CACTUS;

    private static final BlockModel[] cachedValues = values();

    public final int id;

    private boolean blockingTop;
    private boolean blockingBottom;
    private boolean blockingFront;
    private boolean blockingBack;
    private boolean blockingLeft;
    private boolean blockingRight;

    private BlockModel() {
        id = ordinal();
    }

    private BlockModel(Builder builder) {
        this();
        builder.build(this);
    }

    public static BlockModel[] getCachedValues() {
        return cachedValues;
    }

    public static BlockModel byId(int id) {
        return cachedValues[id];
    }

    public boolean isBlockingTop() {
        return blockingTop;
    }

    public boolean isBlockingBottom() {
        return blockingBottom;
    }

    public boolean isBlockingFront() {
        return blockingFront;
    }

    public boolean isBlockingBack() {
        return blockingBack;
    }

    public boolean isBlockingLeft() {
        return blockingLeft;
    }

    public boolean isBlockingRight() {
        return blockingRight;
    }

    private interface Builder
    {
        void build(BlockModel bm);
    }
}