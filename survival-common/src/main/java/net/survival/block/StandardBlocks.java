package net.survival.block;

public final class StandardBlocks {
    public static final Block AIR         = createBuilder().setModel(StandardBlockModels.EMPTY).setSolid(false).setHardness(0.0).setResistance(0.0).build();
    public static final Block BEDROCK     = createBuilder().setModel(StandardBlockModels.BEDROCK).build();
    public static final Block CACTUS      = createBuilder().setModel(StandardBlockModels.CACTUS).build();
    public static final Block COBBLESTONE = createBuilder().setModel(StandardBlockModels.COBBLESTONE).build();
    public static final Block DIRT        = createBuilder().setModel(StandardBlockModels.DIRT).build();
    public static final Block GRASS       = createBuilder().setModel(StandardBlockModels.GRASS).build();
    public static final Block GRAVEL      = createBuilder().setModel(StandardBlockModels.GRAVEL).build();
    public static final Block OAK_LEAVES  = createBuilder().setModel(StandardBlockModels.OAK_LEAVES).build();
    public static final Block OAK_LOG     = createBuilder().setModel(StandardBlockModels.OAK_LOG).build();
    public static final Block OAK_SAPLING = createBuilder().setModel(StandardBlockModels.OAK_SAPLING).build();
    public static final Block SAND        = createBuilder().setModel(StandardBlockModels.SAND).build();
    public static final Block SANDSTONE   = createBuilder().setModel(StandardBlockModels.SANDSTONE).build();
    public static final Block STONE       = createBuilder().setModel(StandardBlockModels.STONE).build();
    public static final Block WATER       = createBuilder().setModel(StandardBlockModels.WATER).build();

    private StandardBlocks() {}

    private static Block.Builder createBuilder() {
        return new Block.Builder();
    }
}
