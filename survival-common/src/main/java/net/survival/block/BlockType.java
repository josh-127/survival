package net.survival.block;

import java.util.Iterator;

public enum BlockType
{
    EMPTY(bt -> {
        bt.name = "<EMPTY>";
        bt.solid = false;
        bt.model = BlockModel.INVISIBLE;
    }),

    STONE(bt -> {
        bt.name = "Stone";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
    }),
    GRASS(bt -> {
        bt.name = "Grass";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/grass_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/grass_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/dirt.png");
    }),
    DIRT(bt -> {
        bt.name = "Dirt";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/dirt.png");
    }),
    COBBLESTONE(bt -> {
        bt.name = "Cobblestone";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
    }),
    OAK_PLANKS(bt -> {
        bt.name = "Oak Planks";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
    }),
    OAK_SAPLING(bt -> {
        bt.name = "Oak Sapling";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sapling_oak.png");
        bt.model = BlockModel.SAPLING;
    }),
    BEDROCK(bt -> {
        bt.name = "Bedrock";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/bedrock.png");
    }),
    WATER(bt -> {
        bt.name = "Water";
        bt.setTextureOnAllFaces("textures/blocks/water.png");
    }),
    SAND(bt -> {
        bt.name = "Sand";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sand.png");
    }),
    GRAVEL(bt -> {
        bt.name = "Gravel";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/gravel.png");
    }),
    GOLD_ORE(bt -> {
        bt.name = "Gold Ore";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/gold_ore.png");
    }),
    IRON_ORE(bt -> {
        bt.name = "Iron Ore";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/iron_ore.png");
    }),
    COAL_ORE(bt -> {
        bt.name = "Coal Ore";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/coal_ore.png");
    }),
    OAK_LOG(bt -> {
        bt.name = "Oak Log";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/log_oak.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/log_oak_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/log_oak_top.png");
    }),
    OAK_LEAVES(bt -> {
        bt.name = "Oak Leaves";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/leaves_oak.png");
    }),
    SPONGE(bt -> {
        bt.name = "Sponge";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sponge.png");
    }),
    GLASS(bt -> {
        bt.name = "Glass";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/glass.png");
    }),
    LAPIS_ORE(bt -> {
        bt.name = "Lapis Ore";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lapis_ore.png");
    }),
    LAPIS_BLOCK(bt -> {
        bt.name = "Lapis Block";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lapis_block.png");
    }),
    OAK_FENCE(bt -> {
        bt.name = "Oak Fence";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.FENCE;
    }),

    //
    // Special
    //
    TEMP_SOLID(bt -> {
        bt.name = "<TEMP_SOLID>";
    }),
    UNDEFINED(bt -> {
        bt.name = "<UNDEFINED>";
        bt.solid = false;
    });

    private static final BlockType[] cachedValues = values();

    public final short id;

    private String name;
    private final String[] textures;
    private boolean solid;
    private BlockModel model;

    private BlockType(Builder builder) {
        id = (short) ordinal();
        name = "<UNDEFINED>";
        textures = new String[BlockFace.values().length];
        solid = true;
        model = BlockModel.DEFAULT;

        builder.build(this);
    }

    public static BlockType[] getCachedValues() {
        return cachedValues;
    }

    public static BlockType byID(short id) {
        return cachedValues[id];
    }

    public static Iterable<BlockType> iterateAll() {
        return () -> new Iterator<BlockType>() {
            private int index = 0;

            @Override
            public BlockType next() {
                return cachedValues[index++];
            }

            @Override
            public boolean hasNext() {
                return index < cachedValues.length;
            }
        };
    }

    public String getName() {
        return name;
    }

    public String getTexture(BlockFace blockFace) {
        return textures[blockFace.ordinal()];
    }

    private void setTexture(BlockFace blockFace, String to) {
        textures[blockFace.ordinal()] = to;
    }

    private void setTextureOnAllFaces(String to) {
        for (int i = 0; i < textures.length; ++i)
            textures[i] = to;
    }

    private void setTextureOnSides(String to) {
        textures[BlockFace.LEFT.ordinal()] = to;
        textures[BlockFace.RIGHT.ordinal()] = to;
        textures[BlockFace.FRONT.ordinal()] = to;
        textures[BlockFace.BACK.ordinal()] = to;
    }

    public boolean isSolid() {
        return solid;
    }

    public BlockModel getModel() {
        return model;
    }

    private interface Builder
    {
        void build(BlockType bt);
    }
}