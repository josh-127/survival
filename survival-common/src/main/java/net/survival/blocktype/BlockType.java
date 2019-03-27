package net.survival.blocktype;

public final class BlockType
{
    public static final BasicBlock AIR =
            register(basicBlock()
                    .withDisplayName("<air>")
                    .withHardness(0.0)
                    .withResistance(0.0)
                    .withSolidity(false)
                    .withModel(BlockModel.INVISIBLE)
                    .build());
    public static final BasicBlock BEDROCK =
            register(basicBlock()
                    .withDisplayName("Bedrock")
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/bedrock.png")
                    .build());
    public static final BasicBlock TEMP_SOLID =
            register(basicBlock()
                    .withDisplayName("<temp_solid>")
                    .build());
    public static final BasicBlock STONE =
            register(basicBlock()
                    .withDisplayName("Stone")
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png")
                    .build());
    public static final BasicBlock COBBLESTONE =
            register(basicBlock()
                    .withDisplayName("Cobblestone")
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png")
                    .build());
    public static final BasicBlock DIRT =
            register(basicBlock()
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/dirt.png")
                    .build());
    public static final BasicBlock GRAVEL =
            register(basicBlock()
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/gravel.png")
                    .build());
    public static final BasicBlock SAND =
            register(basicBlock()
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sand.png")
                    .build());
    public static final BasicBlock SANDSTONE =
            register(basicBlock()
                    .withTextureOnSides("ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png")
                    .withTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png")
                    .withTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png")
                    .build());
    public static final BasicBlock GRASS_BLOCK =
            register(basicBlock()
                    .withDisplayName("Grass Block")
                    .withTextureOnSides("ProgrammerArt-v3.0/textures/blocks/grass_side.png")
                    .withTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/grass_top.png")
                    .withTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/dirt.png")
                    .build());
    public static final StairBlock STONE_STAIRS =
            register(stairBlock(
                    basicBlock()
                    .withDisplayName("Stone Stairs")
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png")
                    .build()));
    public static final SlabBlock STONE_SLABS =
            register(slabBlock(
                    basicBlock()
                    .withDisplayName("Stone Slabs")
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png")
                    .build()));
    public static final BasicBlock WATER =
            register(basicBlock()
                    .withDisplayName("Water")
                    .withTextureOnAllFaces("textures/blocks/water.png")
                    .build());
    public static final BasicBlock CACTUS =
            register(basicBlock()
                    .withDisplayName("Cactus")
                    .withTextureOnSides("ProgrammerArt-v3.0/textures/blocks/cactus_side.png")
                    .withTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/cactus_top.png")
                    .withTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/cactus_bottom.png")
                    .build());
    public static final BasicBlock OAK_SAPLING =
            register(basicBlock()
                    .withDisplayName("Oak Sapling")
                    .withTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sapling_oak.png")
                    .withModel(BlockModel.SAPLING)
                    .withSolidity(false)
                    .build());

    private static Block[] blocks;
    private static int blockCount = 0;

    private BlockType() {}

    public static Block byTypeId(int typeId) {
        return blocks[typeId];
    }

    public static Block byFullId(int fullId) {
        var typeId = (short) ((fullId & 0xFFFF0000) >>> 16);
        var encodedState = (short) (fullId & 0xFFFF);
        return blocks[typeId].withState(encodedState);
    }

    public static Block[] getAllBlocks() {
        return blocks;
    }

    private static <T extends Block> T register(T block) {
        if (blocks == null)
            blocks = new Block[4096];
        blocks[blockCount++] = block;
        return block;
    }

    private static BasicBlock.Builder basicBlock() {
        return new BasicBlock.Builder((short) blockCount);
    }

    private static StairBlock stairBlock(BasicBlock baseBlock) {
        return new StairBlock((short) blockCount, (short) 0, baseBlock);
    }

    private static SlabBlock slabBlock(BasicBlock baseBlock) {
        return new SlabBlock((short) blockCount, (short) 0, baseBlock);
    }
}