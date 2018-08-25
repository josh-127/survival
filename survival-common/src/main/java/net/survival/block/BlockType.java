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
    GRANITE(bt -> {
        bt.name = "Granite";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone_granite.png");
    }),
    POLISHED_GRANITE(bt -> {
        bt.name = "Polished Granite";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone_granite_smooth.png");
    }),
    DIORITE(bt -> {
        bt.name = "Diorite";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone_diorite.png");
    }),
    POLISHED_DIORITE(bt -> {
        bt.name = "Polished Diorite";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone_diorite_smooth.png");
    }),
    ANDESITE(bt -> {
        bt.name = "Andesite";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone_andesite.png");
    }),
    POLISHED_ANDESITE(bt -> {
        bt.name = "Polished Andesite";
        bt.setTextureOnAllFaces("ProgrammerARt-v3.0/textures/blocks/stone_andesite_smooth.png");
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
    COARSE_DIRT(bt -> {
        bt.name = "Coarse Dirt";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/coarse_dirt.png");
    }),
    PODZOL(bt -> {
        bt.name = "Podzol";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/dirt_podzol_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/dirt_podzol_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/dirt.png");
    }),
    COBBLESTONE(bt -> {
        bt.name = "Cobblestone";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
    }),
    OAK_PLANKS(bt -> {
        bt.name = "Oak Planks";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
    }),
    SPRUCE_PLANKS(bt -> {
        bt.name = "Spruce Planks";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_spruce.png");
    }),
    BIRCH_PLANKS(bt -> {
        bt.name = "Birch Planks";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_birch.png");
    }),
    JUNGLE_PLANKS(bt -> {
        bt.name = "Jungle Planks";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_jungle.png");
    }),
    ACACIA_PLANKS(bt -> {
        bt.name = "Acacia Planks";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_acacia.png");
    }),
    DARK_OAK_PLANKS(bt -> {
        bt.name = "Dark Oak Planks";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_big_oak.png");
    }),
    OAK_SAPLING(bt -> {
        bt.name = "Oak Sapling";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sapling_oak.png");
        bt.model = BlockModel.SAPLING;
    }),
    SPRUCE_SAPLING(bt -> {
        bt.name = "Spruce Sapling";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sapling_spruce.png");
        bt.model = BlockModel.SAPLING;
    }),
    BIRCH_SAPLING(bt -> {
        bt.name = "Birch Sapling";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sapling_birch.png");
        bt.model = BlockModel.SAPLING;
    }),
    JUNGLE_SAPLING(bt -> {
        bt.name = "Jungle Sapling";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sapling_jungle.png");
        bt.model = BlockModel.SAPLING;
    }),
    ACACIA_SAPLING(bt -> {
        bt.name = "Acacia Sapling";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sapling_acacia.png");
        bt.model = BlockModel.SAPLING;
    }),
    DARK_OAK_SAPLING(bt -> {
        bt.name = "Dark Oak Sapling";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sapling_roofed_oak.png");
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
    RED_SAND(bt -> {
        bt.name = "Red Sand";
        // TODO: Desaturate ProgrammerArt-v3.0/textures/blocks/red_sand.png
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
    SPRUCE_LOG(bt -> {
        bt.name = "Spruce Log";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/log_spruce.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/log_spruce_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/log_spruce_top.png");
    }),
    BIRCH_LOG(bt -> {
        bt.name = "Birch Log";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/log_birch.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/log_birch_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/log_birch_top.png");
    }),
    JUNGLE_LOG(bt -> {
        bt.name = "Jungle Log";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/log_jungle.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/log_jungle_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/log_jungle_top.png");
    }),
    OAK_LEAVES(bt -> {
        bt.name = "Oak Leaves";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/leaves_oak.png");
    }),
    SPRUCE_LEAVES(bt -> {
        bt.name = "Spruce Leaves";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/leaves_spruce.png");
    }),
    BIRCH_LEAVES(bt -> {
        bt.name = "Birch Leaves";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/leaves_birch.png");
    }),
    JUNGLE_LEAVES(bt -> {
        bt.name = "Jungle Leaves";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/leaves_jungle.png");
    }),
    SPONGE(bt -> {
        bt.name = "Sponge";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sponge.png");
    }),
    WET_SPONGE(bt -> {
        bt.name = "Wet Sponge";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/sponge_wet.png");
    }),
    GLASS(bt -> {
        bt.name = "Glass";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/glass.png");
        bt.model = BlockModel.GLASS;
    }),
    LAPIS_LAZULI_ORE(bt -> {
        bt.name = "Lapis Ore";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lapis_ore.png");
    }),
    LAPIS_LAZULI_BLOCK(bt -> {
        bt.name = "Lapis Block";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lapis_block.png");
    }),
    SANDSTONE(bt -> {
        bt.name = "Sandstone";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png");
    }),
    CHISELED_SANDSTONE(bt -> {
        bt.name = "Chiseled Sandstone";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/sandstone_carved.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png");
    }),
    SMOOTH_SANDSTONE(bt -> {
        bt.name = "Smooth Sandstone";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/sandstone_smooth.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png");
    }),
    COBWEB(bt -> {
        bt.name = "Cobweb";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/web.png");
        bt.model = BlockModel.SAPLING;
    }),
    DEAD_SHRUB(bt -> {
        bt.name = "Dead Shrub";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/deadbush.png");
        bt.model = BlockModel.SAPLING;
    }),
    TALL_GRASS(bt -> {
        bt.name = "Tall Grass";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/tallgrass.png");
        bt.model = BlockModel.SAPLING;
    }),
    FERN(bt -> {
        bt.name = "Fern";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/fern.png");
        bt.model = BlockModel.SAPLING;
    }),
    WHITE_WOOL(bt -> {
        bt.name = "White Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_white.png");
    }),
    ORANGE_WOOL(bt -> {
        bt.name = "Orange Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_orange.png");
    }),
    MAGENTA_WOOL(bt -> {
        bt.name = "Magenta Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_magenta.png");
    }),
    LIGHT_BLUE_WOOL(bt -> {
        bt.name = "Light Blue Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_light_blue.png");
    }),
    YELLOW_WOOL(bt -> {
        bt.name = "Yellow Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_yellow.png");
    }),
    LIME_WOOL(bt -> {
        bt.name = "Lime Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_lime.png");
    }),
    PINK_WOOL(bt -> {
        bt.name = "Pink Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_pink.png");
    }),
    GRAY_WOOL(bt -> {
        bt.name = "Gray Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_gray.png");
    }),
    LIGHT_GRAY_WOOL(bt -> {
        bt.name = "Light Gray Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_silver.png");
    }),
    CYAN_WOOL(bt -> {
        bt.name = "Cyan Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_cyan.png");
    }),
    PURPLE_WOOL(bt -> {
        bt.name = "Purple Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_purple.png");
    }),
    BLUE_WOOL(bt -> {
        bt.name = "Blue Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_blue.png");
    }),
    BROWN_WOOL(bt -> {
        bt.name = "Brown Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_brown.png");
    }),
    GREEN_WOOL(bt -> {
        bt.name = "Green Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_green.png");
    }),
    RED_WOOL(bt -> {
        bt.name = "Red Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_red.png");
    }),
    BLACK_WOOL(bt -> {
        bt.name = "Black Wool";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wool_colored_black.png");
    }),
    DANDELION(bt -> {
        bt.name = "Dandelion";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_dandelion.png");
        bt.model = BlockModel.SAPLING;
    }),
    POPPY(bt -> {
        bt.name = "Poppy";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_rose.png");
        bt.model = BlockModel.SAPLING;
    }),
    BLUE_ORCHID(bt -> {
        bt.name = "Blue Orchid";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_blue_orchid.png");
        bt.model = BlockModel.SAPLING;
    }),
    ALLIUM(bt -> {
        bt.name = "Allium";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_allium.png");
        bt.model = BlockModel.SAPLING;
    }),
    AZURE_BLUET(bt -> {
        bt.name = "Azure Bluet";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_houstonia.png");
        bt.model = BlockModel.SAPLING;
    }),
    RED_TULIP(bt -> {
        bt.name = "Red Tulip";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_tulip_red.png");
        bt.model = BlockModel.SAPLING;
    }),
    ORANGE_TULIP(bt -> {
        bt.name = "Orange Tulip";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_tulip_orange.png");
        bt.model = BlockModel.SAPLING;
    }),
    WHITE_TULIP(bt -> {
        bt.name = "White Tulip";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_tulip_white.png");
        bt.model = BlockModel.SAPLING;
    }),
    PINK_TULIP(bt -> {
        bt.name = "Pink Tulip";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_tulip_pink.png");
        bt.model = BlockModel.SAPLING;
    }),
    OXEYE_DAISY(bt -> {
        bt.name = "Oxeye Daisy";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/flower_oxeye_daisy.png");
        bt.model = BlockModel.SAPLING;
    }),
    BROWN_MUSHROOM(bt -> {
        bt.name = "Brown Mushroom";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/mushroom_brown.png");
        bt.model = BlockModel.SAPLING;
    }),
    RED_MUSHROOM(bt -> {
        bt.name = "Red Mushroom";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/mushroom_red.png");
        bt.model = BlockModel.SAPLING;
    }),
    GOLD_BLOCK(bt -> {
        bt.name = "Gold Block";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/gold_block.png");
    }),
    IRON_BLOCK(bt -> {
        bt.name = "Iron Block";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/iron_block.png");
    }),
    DOUBLE_STONE_SLAB(bt -> {
        bt.name = "Double Stone Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/stone_slab_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/stone_slab_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/stone_slab_top.png");
    }),
    DOUBLE_SANDSTONE_SLAB(bt -> {
        bt.name = "Double Sandstone Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png");
    }),
    DOUBLE_WOODEN_SLAB(bt -> {
        bt.name = "Double Wooden Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
    }),
    DOUBLE_COBBLESTONE_SLAB(bt -> {
        bt.name = "Double Cobblestone Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
    }),
    DOUBLE_BRICK_SLAB(bt -> {
        bt.name = "Double Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/brick.png");
    }),
    DOUBLE_STONE_BRICK_SLAB(bt -> {
        bt.name = "Double Stone Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stonebrick.png");
    }),
    DOUBLE_NETHER_BRICK_SLAB(bt -> {
        bt.name = "Double Nether Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/nether_brick.png");
    }),
    DOUBLE_QUARTZ_SLAB(bt -> {
        bt.name = "Double Quartz Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/quartz_block_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/quartz_block_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/quartz_block_bottom.png");
    }),
    STONE_SLAB_BOTTOM(bt -> {
        bt.name = "Stone Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/stone_slab_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/stone_slab_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/stone_slab_top.png");
        bt.model = BlockModel.BOTTOM_SLAB;
    }),
    STONE_SLAB_TOP(bt -> {
        bt.name = "Stone Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/stone_slab_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/stone_slab_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/stone_slab_top.png");
        bt.model = BlockModel.TOP_SLAB;
    }),
    SANDSTONE_SLAB_BOTTOM(bt -> {
        bt.name = "Sandstone Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png");
        bt.model = BlockModel.BOTTOM_SLAB;
    }),
    SANDSTONE_SLAB_TOP(bt -> {
        bt.name = "Sandstone Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png");
        bt.model = BlockModel.TOP_SLAB;
    }),
    WOODEN_SLAB_BOTTOM(bt -> {
        bt.name = "Wooden Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.BOTTOM_SLAB;
    }),
    WOODEN_SLAB_TOP(bt -> {
        bt.name = "Wooden Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.TOP_SLAB;
    }),
    BRICK_SLAB_BOTTOM(bt -> {
        bt.name = "Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/brick.png");
        bt.model = BlockModel.BOTTOM_SLAB;
    }),
    BRICK_SLAB_TOP(bt -> {
        bt.name = "Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/brick.png");
        bt.model = BlockModel.TOP_SLAB;
    }),
    STONE_BRICK_SLAB_BOTTOM(bt -> {
        bt.name = "Stone Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stonebrick.png");
        bt.model = BlockModel.BOTTOM_SLAB;
    }),
    STONE_BRICK_SLAB_TOP(bt -> {
        bt.name = "Stone Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stonebrick.png");
        bt.model = BlockModel.TOP_SLAB;
    }),
    NETHER_BRICK_SLAB_BOTTOM(bt -> {
        bt.name = "Nether Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/nether_brick.png");
        bt.model = BlockModel.BOTTOM_SLAB;
    }),
    NETHER_BRICK_SLAB_TOP(bt -> {
        bt.name = "Nether Brick Slab";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/nether_brick.png");
        bt.model = BlockModel.TOP_SLAB;
    }),
    QUARTZ_SLAB_BOTTOM(bt -> {
        bt.name = "Quartz Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/quartz_block_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/quartz_block_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/quartz_block_bottom.png");
        bt.model = BlockModel.BOTTOM_SLAB;
    }),
    QUARTZ_SLAB_TOP(bt -> {
        bt.name = "Quartz Slab";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/quartz_block_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/quartz_block_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/quartz_block_bottom.png");
        bt.model = BlockModel.TOP_SLAB;
    }),
    BRICK(bt -> {
        bt.name = "Bricks";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/brick.png");
    }),
    TNT(bt -> {
        bt.name = "TNT";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/tnt_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/tnt_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/tnt_bottom.png");
    }),
    BOOKSHELF(bt -> {
        bt.name = "Bookshelf";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/bookshelf.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
    }),
    MOSS_STONE(bt -> {
        bt.name = "Moss Stone";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone_mossy.png");
    }),
    OBSIDIAN(bt -> {
        bt.name = "Obsidian";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/obsidian.png");
    }),
    TORCH(bt -> {
        bt.name = "Torch";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/torch_on.png");
        bt.model = BlockModel.TORCH;
    }),
    FIRE(bt -> {
        bt.name = "Fire";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/fire_layer_0.png");
        bt.model = BlockModel.FIRE;
    }),
    OAK_WOOD_STAIRS_NORTH(bt -> {
        bt.name = "Oak Wood Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.NORTH_STAIRS;
    }),
    OAK_WOOD_STAIRS_SOUTH(bt -> {
        bt.name = "Oak Wood Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.SOUTH_STAIRS;
    }),
    OAK_WOOD_STAIRS_EAST(bt -> {
        bt.name = "Oak Wood Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.EAST_STAIRS;
    }),
    OAK_WOOD_STAIRS_WEST(bt -> {
        bt.name = "Oak Wood Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.WEST_STAIRS;
    }),
    OAK_WOOD_CEILING_STAIRS_NORTH(bt -> {
        bt.name = "Oak Wood Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.NORTH_CEILING_STAIRS;
    }),
    OAK_WOOD_CEILING_STAIRS_SOUTH(bt -> {
        bt.name = "Oak Wood Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.SOUTH_CEILING_STAIRS;
    }),
    OAK_WOOD_CEILING_STAIRS_EAST(bt -> {
        bt.name = "Oak Wood Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.EAST_CEILING_STAIRS;
    }),
    OAK_WOOD_CEILING_STAIRS_WEST(bt -> {
        bt.name = "Oak Wood Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.WEST_CEILING_STAIRS;
    }),
    DIAMOND_ORE(bt -> {
        bt.name = "Diamond Ore";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/diamond_ore.png");
    }),
    DIAMOND_BLOCK(bt -> {
        bt.name = "Diamond Block";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/diamond_block.png");
    }),
    CRAFTING_TABLE(bt -> {
        bt.name = "Crafting Table";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/crafting_table_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/crafting_table_bottom.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/crafting_table_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/crafting_table_side2.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/crafting_table_front.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/crafting_table_front.png");
    }),
    WHEAT_CROPS_0(bt -> {
        bt.name = "Wheat Crops";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wheat_stage_0.png");
        bt.model = BlockModel.CROPS;
    }),
    WHEAT_CROPS_1(bt -> {
        bt.name = "Wheat Crops";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wheat_stage_1.png");
        bt.model = BlockModel.CROPS;
    }),
    WHEAT_CROPS_2(bt -> {
        bt.name = "Wheat Crops";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wheat_stage_2.png");
        bt.model = BlockModel.CROPS;
    }),
    WHEAT_CROPS_3(bt -> {
        bt.name = "Wheat Crops";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wheat_stage_3.png");
        bt.model = BlockModel.CROPS;
    }),
    WHEAT_CROPS_4(bt -> {
        bt.name = "Wheat Crops";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wheat_stage_4.png");
        bt.model = BlockModel.CROPS;
    }),
    WHEAT_CROPS_5(bt -> {
        bt.name = "Wheat Crops";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wheat_stage_5.png");
        bt.model = BlockModel.CROPS;
    }),
    WHEAT_CROPS_6(bt -> {
        bt.name = "Wheat Crops";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/wheat_stage_6.png");
        bt.model = BlockModel.CROPS;
    }),
    DRY_FARMLAND(bt -> {
        bt.name = "Farmland";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/dirt.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/farmland_dry.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/dirt.png");
        bt.model = BlockModel.FARMLAND;
    }),
    WET_FARMLAND(bt -> {
        bt.name = "Farmland";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/dirt.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/farmland_wet.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/dirt.png");
        bt.model = BlockModel.FARMLAND;
    }),
    FURNACE_NORTH(bt -> {
        bt.name = "Furnace";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/furnace_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/furnace_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/furnace_front_off.png");
    }),
    FURNACE_SOUTH(bt -> {
        bt.name = "Furnace";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/furnace_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/furnace_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/furnace_front_off.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
    }),
    FURNACE_EAST(bt -> {
        bt.name = "Furnace";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/furnace_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/furnace_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/furnace_front_off.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
    }),
    FURNACE_WEST(bt -> {
        bt.name = "Furnace";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/furnace_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/furnace_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/furnace_front_off.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/furnace_side.png");
    }),
    COBBLESTONE_STAIRS_NORTH(bt -> {
        bt.name = "Cobblestone Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        bt.model = BlockModel.NORTH_STAIRS;
    }),
    COBBLESTONE_STAIRS_SOUTH(bt -> {
        bt.name = "Cobblestone Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        bt.model = BlockModel.SOUTH_STAIRS;
    }),
    COBBLESTONE_STAIRS_EAST(bt -> {
        bt.name = "Cobblestone Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        bt.model = BlockModel.EAST_STAIRS;
    }),
    COBBLESTONE_STAIRS_WEST(bt -> {
        bt.name = "Cobblestone Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        bt.model = BlockModel.WEST_STAIRS;
    }),
    COBBLESTONE_CEILING_STAIRS_NORTH(bt -> {
        bt.name = "Cobblestone Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        bt.model = BlockModel.NORTH_CEILING_STAIRS;
    }),
    COBBLESTONE_CEILING_STAIRS_SOUTH(bt -> {
        bt.name = "Cobblestone Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        bt.model = BlockModel.SOUTH_CEILING_STAIRS;
    }),
    COBBLESTONE_CEILING_STAIRS_EAST(bt -> {
        bt.name = "Cobblestone Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        bt.model = BlockModel.EAST_CEILING_STAIRS;
    }),
    COBBLESTONE_CEILING_STAIRS_WEST(bt -> {
        bt.name = "Cobblestone Stairs";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        bt.model = BlockModel.WEST_CEILING_STAIRS;
    }),
    LEVER_FLOOR_OFF(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_FLOOR_OFF;
    }),
    LEVER_FLOOR_ON(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_FLOOR_ON;
    }),
    LEVER_CEILING_OFF(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_CEILING_OFF;
    }),
    LEVER_CEILING_ON(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_CEILING_ON;
    }),
    LEVER_NORTH_OFF(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_NORTH_OFF;
    }),
    LEVER_NORTH_ON(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_NORTH_ON;
    }),
    LEVER_SOUTH_OFF(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_SOUTH_OFF;
    }),
    LEVER_SOUTH_ON(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_SOUTH_ON;
    }),
    LEVER_EAST_OFF(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_EAST_OFF;
    }),
    LEVER_EAST_ON(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_EAST_ON;
    }),
    LEVER_WEST_OFF(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_WEST_OFF;
    }),
    LEVER_WEST_ON(bt -> {
        bt.name = "Lever";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/lever.png");
        bt.model = BlockModel.LEVER_WEST_ON;
    }),
    STONE_PRESSURE_PLATE_OFF(bt -> {
        bt.name = "Stone Pressure Plate";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.PRESSURE_PLATE_OFF;
    }),
    STONE_PRESSURE_PLATE_ON(bt -> {
        bt.name = "Stone Pressure Plate";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.PRESSURE_PLATE_ON;
    }),
    WOODEN_PRESSURE_PLATE_OFF(bt -> {
        bt.name = "Wooden Pressure Plate";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.PRESSURE_PLATE_OFF;
    }),
    WOODEN_PRESSURE_PLATE_ON(bt -> {
        bt.name = "Wooden Pressure Plate";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.PRESSURE_PLATE_ON;
    }),
    STONE_BUTTON_FLOOR_OFF(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_FLOOR_OFF;
    }),
    STONE_BUTTON_FLOOR_ON(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_FLOOR_ON;
    }),
    STONE_BUTTON_CEILING_OFF(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_CEILING_OFF;
    }),
    STONE_BUTTON_CEILING_ON(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_CEILING_ON;
    }),
    STONE_BUTTON_NORTH_OFF(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_NORTH_OFF;
    }),
    STONE_BUTTON_NORTH_ON(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_NORTH_ON;
    }),
    STONE_BUTTON_SOUTH_OFF(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_SOUTH_OFF;
    }),
    STONE_BUTTON_SOUTH_ON(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_SOUTH_ON;
    }),
    STONE_BUTTON_EAST_OFF(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_EAST_OFF;
    }),
    STONE_BUTTON_WEST_ON(bt -> {
        bt.name = "Stone Button";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/stone.png");
        bt.model = BlockModel.BUTTON_WEST_ON;
    }),
    REDSTONE_ORE(bt -> {
        bt.name = "Redstone Ore";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/redstone_ore.png");
    }),
    GLOWING_REDSTONE_ORE(bt -> {
        bt.name = "Glowing Redstone Ore";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/redstone_ore.png");
    }),
    SNOW(bt -> {
        bt.name = "Snow";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/snow.png");
        bt.model = BlockModel.SNOW_LAYER_0;
    }),
    ICE(bt -> {
        bt.name = "Ice";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/ice.png");
        bt.model = BlockModel.ICE;
    }),
    SNOW_BLOCK(bt -> {
        bt.name = "Snow Block";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/snow.png");
    }),
    CACTUS(bt -> {
        bt.name = "Cactus";
        bt.setTextureOnSides("ProgrammerArt-v3.0/textures/blocks/cactus_side.png");
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/cactus_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/cactus_bottom.png");
        bt.model = BlockModel.CACTUS;
    }),
    CLAY(bt -> {
        bt.name = "Clay";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/clay.png");
    }),
    SUGAR_CANES(bt -> {
        bt.name = "Sugar Canes";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/reeds.png");
        bt.model = BlockModel.SAPLING;
    }),
    PUMPKIN_NORTH(bt -> {
        bt.name = "Pumpkin";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_off.png");
    }),
    PUMPKIN_SOUTH(bt -> {
        bt.name = "Pumpkin";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_off.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
    }),
    PUMPKIN_EAST(bt -> {
        bt.name = "Pumpkin";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_off.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
    }),
    PUMPKIN_WEST(bt -> {
        bt.name = "Pumpkin";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_off.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
    }),
    OAK_FENCE(bt -> {
        bt.name = "Oak Fence";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        bt.model = BlockModel.FENCE;
    }),
    NETHERRACK(bt -> {
        bt.name = "Netherrack";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/netherrack.png");
    }),
    SOUL_SAND(bt -> {
        bt.name = "Soul Sand";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/soul_sand.png");
    }),
    GLOWSTONE(bt -> {
        bt.name = "Glowstone";
        bt.setTextureOnAllFaces("ProgrammerArt-v3.0/textures/blocks/glowstone.png");
    }),
    LIT_PUMPKIN_NORTH(bt -> {
        bt.name = "Jack o'Lantern";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_on.png");
    }),
    LIT_PUMPKIN_SOUTH(bt -> {
        bt.name = "Jack o'Lantern";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_on.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
    }),
    LIT_PUMPKIN_EAST(bt -> {
        bt.name = "Jack o'Lantern";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_on.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
    }),
    LIT_PUMPKIN_WEST(bt -> {
        bt.name = "Jack o'Lantern";
        bt.setTexture(BlockFace.TOP, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.BOTTOM, "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png");
        bt.setTexture(BlockFace.LEFT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_on.png");
        bt.setTexture(BlockFace.RIGHT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.FRONT, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
        bt.setTexture(BlockFace.BACK, "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png");
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