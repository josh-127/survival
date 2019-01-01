package net.survival.block;

import static net.survival.block.BlockProperty.booleanValue;
import static net.survival.block.BlockProperty.stringValue;

public class BlockFeatureSet_V0_1
{
    public static final String NAME = "NAME";
    public static final String TOP_TEXTURE = "TOP_TEXTURE";
    public static final String BOTTOM_TEXTURE = "BOTTOM_TEXTURE";
    public static final String LEFT_TEXTURE = "LEFT_TEXTURE";
    public static final String RIGHT_TEXTURE = "RIGHT_TEXTURE";
    public static final String FRONT_TEXTURE = "FRONT_TEXTURE";
    public static final String BACK_TEXTURE = "BACK_TEXTURE";
    public static final String SOLID = "SOLID";
    public static final String MODEL = "MODEL";

    private final BlockRegistry registry;

    private BlockFeatureSet_V0_1(BlockRegistry registry) {
        this.registry = registry;
    }

    public static void registerBlocks(BlockRegistry registry) {
        new BlockFeatureSet_V0_1(registry).registerBlocksInternal();
    }

    private void registerBlocksInternal() {
        registry.insertProperty(KnownBlockProperty.VERSION_INTRODUCED, () -> "0.1.0");
        registry.insertProperty(NAME,           () -> "<UNDEFINED>");
        registry.insertProperty(TOP_TEXTURE,    () -> "");
        registry.insertProperty(BOTTOM_TEXTURE, () -> "");
        registry.insertProperty(LEFT_TEXTURE,   () -> "");
        registry.insertProperty(RIGHT_TEXTURE,  () -> "");
        registry.insertProperty(FRONT_TEXTURE,  () -> "");
        registry.insertProperty(BACK_TEXTURE,   () -> "");
        registry.insertProperty(SOLID,          () -> true);
        registry.insertProperty(MODEL,          () -> BlockModel.DEFAULT.name());

        registry.insert(
                stringValue(NAME, "<EMPTY>"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.INVISIBLE.name()));

        registerDefault("Stone",             "ProgrammerArt-v3.0/textures/blocks/stone.png");
        registerDefault("Granite",           "ProgrammerArt-v3.0/textures/blocks/stone_granite.png");
        registerDefault("Polished Granite",  "ProgrammerArt-v3.0/textures/blocks/stone_granite_smooth.png");
        registerDefault("Diorite",           "ProgrammerArt-v3.0/textures/blocks/stone_diorite.png");
        registerDefault("Polished Diorite",  "ProgrammerArt-v3.0/textures/blocks/stone_diorite_smooth.png");
        registerDefault("Andesite",          "ProgrammerArt-v3.0/textures/blocks/stone_andesite.png");
        registerDefault("Polished Andesite", "ProgrammerArt-v3.0/textures/blocks/stone_andesite_smooth.png");

        registry.insert(
                stringValue(NAME, "Grass"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/grass_top.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/grass_side.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/grass_side.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/grass_side.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/grass_side.png"));

        registerDefault("Dirt",        "ProgrammerArt-v3.0/textures/blocks/dirt.png");
        registerDefault("Coarse Dirt", "ProgrammerArt-v3.0/textures/blocks/coarse_dirt.png");

        registry.insert(
                stringValue(NAME, "Podzol"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt_podzol_top.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt_podzol_side.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt_podzol_side.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt_podzol_side.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt_podzol_side.png"));

        registerDefault("Cobblestone",     "ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        registerDefault("Oak Planks",      "ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        registerDefault("Spruce Planks",   "ProgrammerArt-v3.0/textures/blocks/planks_spruce.png");
        registerDefault("Birch Planks",    "ProgrammerArt-v3.0/textures/blocks/planks_birch.png");
        registerDefault("Jungle Planks",   "ProgrammerArt-v3.0/textures/blocks/planks_jungle.png");
        registerDefault("Acacia Planks",   "ProgrammerArt-v3.0/textures/blocks/planks_acacia.png");
        registerDefault("Dark Oak Planks", "ProgrammerArt-v3.0/textures/blocks/planks_big_oak.png");

        registerSapling("Oak Sapling",      "ProgrammerArt-v3.0/textures/blocks/sapling_oak.png");
        registerSapling("Spruce Sapling",   "ProgrammerArt-v3.0/textures/blocks/sapling_spruce.png");
        registerSapling("Birch Sapling",    "ProgrammerArt-v3.0/textures/blocks/sapling_birch.png");
        registerSapling("Jungle Sapling",   "ProgrammerArt-v3.0/textures/blocks/sapling_jungle.png");
        registerSapling("Acacia Sapling",   "ProgrammerArt-v3.0/textures/blocks/sapling_acacia.png");
        registerSapling("Dark Oak Sapling", "ProgrammerArt-v3.0/textures/blocks/sapling_roofed_oak.png");

        registerDefault("Bedrock", "ProgrammerArt-v3.0/textures/blocks/bedrock.png");
        registerDefault("Water", "ProgrammerArt-v3.0/textures/blocks/water.png");
        registerDefault("Sand", "ProgrammerArt-v3.0/textures/blocks/sand.png");
        registerDefault("Red Sand", "ProgrammerArt-v3.0/textures/blocks/sand.png");
        registerDefault("Gravel", "ProgrammerArt-v3.0/textures/blocks/gravel.png");
        registerDefault("Gold Ore", "ProgrammerArt-v3.0/textures/blocks/gold_ore.png");
        registerDefault("Iron Ore", "ProgrammerArt-v3.0/textures/blocks/iron_ore.png");
        registerDefault("Coal Ore", "ProgrammerArt-v3.0/textures/blocks/coal_ore.png");

        registerLog(
                "Oak Log",
                "ProgrammerArt-v3.0/textures/blocks/log_oak_top.png",
                "ProgrammerArt-v3.0/textures/blocks/log_oak.png");
        registerLog(
                "Spruce Log",
                "ProgrammerArt-v3.0/textures/blocks/log_spruce_top.png",
                "ProgrammerArt-v3.0/textures/blocks/log_spruce.png");
        registerLog(
                "Birch Log",
                "ProgrammerArt-v3.0/textures/blocks/log_birch_top.png",
                "ProgrammerArt-v3.0/textures/blocks/log_birch.png");
        registerLog(
                "Jungle Log",
                "ProgrammerArt-v3.0/textures/blocks/log_jungle_top.png",
                "ProgrammerArt-v3.0/textures/blocks/log_jungle.png");

        registerDefault("Oak Leaves",    "ProgrammerArt-v3.0/textures/blocks/leaves_oak.png");
        registerDefault("Spruce Leaves", "ProgrammerArt-v3.0/textures/blocks/leaves_spruce.png");
        registerDefault("Birch Leaves",  "ProgrammerArt-v3.0/textures/blocks/leaves_birch.png");
        registerDefault("Jungle Leaves", "ProgrammerArt-v3.0/textures/blocks/leaves_jungle.png");
        registerDefault("Sponge",        "ProgrammerArt-v3.0/textures/blocks/sponge.png");
        registerDefault("Wet Sponge",    "ProgrammerArt-v3.0/textures/blocks/sponge_wet.png");

        registry.insert(
                stringValue(NAME, "Glass"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/glass.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/glass.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/glass.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/glass.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/glass.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/glass.png"),
                stringValue(MODEL, BlockModel.GLASS.name()));

        registerDefault("Lapis Ore", "ProgrammerArt-v3.0/textures/blocks/lapis_ore.png");
        registerDefault("Lapis Block", "ProgrammerArt-v3.0/textures/blocks/lapis_block.png");

        registry.insert(
                stringValue(NAME, "Sandstone"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png"));

        registry.insert(
                stringValue(NAME, "Chiseled Sandstone"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_carved.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_carved.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_carved.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_carved.png"));

        registry.insert(
                stringValue(NAME, "Smooth Sandstone"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_smooth.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_smooth.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_smooth.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/sandstone_smooth.png"));

        registerSapling("Cobweb",     "ProgrammerArt-v3.0/textures/blocks/web.png");
        registerSapling("Dead Shrub", "ProgrammerArt-v3.0/textures/blocks/deadbush.png");
        registerSapling("Tall Grass", "ProgrammerArt-v3.0/textures/blocks/tallgrass.png");
        registerSapling("Fern",       "ProgrammerArt-v3.0/textures/blocks/fern.png");

        registerDefault("White Wool",      "ProgrammerArt-v3.0/textures/blocks/wool_colored_white.png");
        registerDefault("Orange Wool",     "ProgrammerArt-v3.0/textures/blocks/wool_colored_orange.png");
        registerDefault("Magenta Wool",    "ProgrammerArt-v3.0/textures/blocks/wool_colored_magenta.png");
        registerDefault("Light Blue Wool", "ProgrammerArt-v3.0/textures/blocks/wool_colored_light_blue.png");
        registerDefault("Yellow Wool",     "ProgrammerArt-v3.0/textures/blocks/wool_colored_yellow.png");
        registerDefault("Lime Wool",       "ProgrammerArt-v3.0/textures/blocks/wool_colored_lime.png");
        registerDefault("Pink Wool",       "ProgrammerArt-v3.0/textures/blocks/wool_colored_pink.png");
        registerDefault("Gray Wool",       "ProgrammerArt-v3.0/textures/blocks/wool_colored_gray.png");
        registerDefault("Light Gray Wool", "ProgrammerArt-v3.0/textures/blocks/wool_colored_silver.png");
        registerDefault("Cyan Wool",       "ProgrammerArt-v3.0/textures/blocks/wool_colored_cyan.png");
        registerDefault("Purple Wool",     "ProgrammerArt-v3.0/textures/blocks/wool_colored_purple.png");
        registerDefault("Blue Wool",       "ProgrammerArt-v3.0/textures/blocks/wool_colored_blue.png");
        registerDefault("Brown Wool",      "ProgrammerArt-v3.0/textures/blocks/wool_colored_brown.png");
        registerDefault("Green Wool",      "ProgrammerArt-v3.0/textures/blocks/wool_colored_green.png");
        registerDefault("Red Wool",        "ProgrammerArt-v3.0/textures/blocks/wool_colored_red.png");
        registerDefault("Black Wool",      "ProgrammerArt-v3.0/textures/blocks/wool_colored_black.png");

        registerSapling("Dandelion",      "ProgrammerArt-v3.0/textures/blocks/flower_dandelion.png");
        registerSapling("Poppy",          "ProgrammerArt-v3.0/textures/blocks/flower_rose.png");
        registerSapling("Blue Orchid",    "ProgrammerArt-v3.0/textures/blocks/flower_blue_orchid.png");
        registerSapling("Allium",         "ProgrammerArt-v3.0/textures/blocks/flower_allium.png");
        registerSapling("Azure Bluet",    "ProgrammerArt-v3.0/textures/blocks/flower_houstonia.png");
        registerSapling("Red Tulip",      "ProgrammerArt-v3.0/textures/blocks/flower_tulip_red.png");
        registerSapling("Orange Tulip",   "ProgrammerArt-v3.0/textures/blocks/flower_tulip_orange.png");
        registerSapling("White Tulip",    "ProgrammerArt-v3.0/textures/blocks/flower_tulip_white.png");
        registerSapling("Pink Tulip",     "ProgrammerArt-v3.0/textures/blocks/flower_tulip_pink.png");
        registerSapling("Oxeye Daisy",    "ProgrammerArt-v3.0/textures/blocks/flower_oxeye_daisy.png");
        registerSapling("Brown Mushroom", "ProgrammerArt-v3.0/textures/blocks/mushroom_brown.png");
        registerSapling("Red Mushroom",   "ProgrammerArt-v3.0/textures/blocks/mushroom_red.png");

        registerDefault("Gold Block", "ProgrammerArt-v3.0/textures/blocks/gold_block.png");
        registerDefault("Iron Block", "ProgrammerArt-v3.0/textures/blocks/iron_block.png");

        registerSlabs(
                "Stone Slab",
                "ProgrammerArt-v3.0/textures/blocks/stone_slab_top.png",
                "ProgrammerArt-v3.0/textures/blocks/stone_slab_side.png");
        registerSlabs(
                "Sandstone Slab",
                "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png",
                "ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png");
        registerSlabs(
                "Wooden Slab",
                "ProgrammerArt-v3.0/textures/blocks/planks_oak.png");
        registerSlabs(
                "Cobblestone Slab",
                "ProgrammerArt-v3.0/textures/blocks/cobblestone.png");
        registerSlabs(
                "Brick Slab",
                "ProgrammerArt-v3.0/textures/blocks/brick.png");
        registerSlabs(
                "Stone Brick Slab",
                "ProgrammerArt-v3.0/textures/blocks/stonebrick.png");
        registerSlabs(
                "Nether Brick Slab",
                "ProgrammerArt-v3.0/textures/blocks/nether_brick.png");
        registerSlabs(
                "Quartz Slab",
                "ProgrammerArt-v3.0/textures/blocks/quartz_block_top.png",
                "ProgrammerArt-v3.0/textures/blocks/quartz_block_bottom.png",
                "ProgrammerArt-v3.0/textures/blocks/quartz_block_side.png");

        registerDefault("Bricks", "ProgrammerArt-v3.0/textures/blocks/brick.png");

        registerVertical(
                "TNT",
                "ProgrammerArt-v3.0/textures/blocks/tnt_top.png",
                "ProgrammerArt-v3.0/textures/blocks/tnt_bottom.png",
                "ProgrammerArt-v3.0/textures/blocks/tnt_side.png");
        registerVertical(
                "Bookshelf",
                "ProgrammerArt-v3.0/textures/blocks/planks_oak.png",
                "ProgrammerArt-v3.0/textures/blocks/bookshelf.png");

        registerDefault("Moss Stone", "ProgrammerArt-v3.0/textures/blocks/cobblestone_mossy.png");
        registerDefault("Obsidian", "ProgrammerArt-v3.0/textures/blocks/obsidian.png");

        registry.insert(
                stringValue(NAME, "Torch"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/torch_on.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.TORCH.name()));

        registry.insert(
                stringValue(NAME, "Fire"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/fire_layer_0.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.FIRE.name()));

        registerStairs("Oak Wood Stairs", "ProgrammerArt-v3.0/textures/blocks/planks_oak.png");

        registerDefault("Diamond Ore", "ProgrammerArt-v3.0/textures/blocks/diamond_ore.png");
        registerDefault("Diamond Block", "ProgrammerArt-v3.0/textures/blocks/diamond_block.png");

        registry.insert(
                stringValue(NAME, "Crafting Table"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/crafting_table_top.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/crafting_table_bottom.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/crafting_table_side.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/crafting_table_side2.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/crafting_table_front.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/crafting_table_front.png"));

        for (int i = 0; i < 7; ++i) {
            registry.insert(
                    stringValue(NAME, "Wheat Crops"),
                    stringValue(TOP_TEXTURE, String.format("ProgrammerArt-v3.0/textures/blocks/wheat_stage_%d.png", i)),
                    booleanValue(SOLID, false),
                    stringValue(MODEL, BlockModel.CROPS.name()));
        }

        registry.insert(
                stringValue(NAME, "Farmland"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/farmland_dry.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.FARMLAND.name()));

        registry.insert(
                stringValue(NAME, "Farmland"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/farmland_wet.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/dirt.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.FARMLAND.name()));

        registerDirectional(
                "Furnace",
                "ProgrammerArt-v3.0/textures/blocks/furnace_top.png",
                "ProgrammerArt-v3.0/textures/blocks/furnace_top.png",
                "ProgrammerArt-v3.0/textures/blocks/furnace_side.png",
                "ProgrammerArt-v3.0/textures/blocks/furnace_front_off.png");

        registerStairs("Cobblestone Stairs", "ProgrammerArt-v3.0/textures/blocks/cobblestone.png");

        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_FLOOR_OFF.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_FLOOR_ON.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_CEILING_OFF.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_CEILING_ON.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_NORTH_OFF.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_NORTH_ON.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_SOUTH_OFF.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_SOUTH_ON.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_EAST_OFF.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_EAST_ON.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_WEST_OFF.name()));
        registry.insert(
                stringValue(NAME, "Lever"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/lever.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.LEVER_WEST_ON.name()));

        registry.insert(
                stringValue(NAME, "Stone Pressure Plate"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                stringValue(MODEL, BlockModel.PRESSURE_PLATE_OFF.name()));
        registry.insert(
                stringValue(NAME, "Stone Pressure Plate"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                stringValue(MODEL, BlockModel.PRESSURE_PLATE_ON.name()));

        registry.insert(
                stringValue(NAME, "Wooden Pressure Plate"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/planks_oak.png"),
                stringValue(MODEL, BlockModel.PRESSURE_PLATE_OFF.name()));
        registry.insert(
                stringValue(NAME, "Wooden Pressure Plate"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/planks_oak.png"),
                stringValue(MODEL, BlockModel.PRESSURE_PLATE_ON.name()));

        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_FLOOR_OFF.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_FLOOR_ON.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_CEILING_OFF.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_CEILING_ON.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_NORTH_OFF.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_NORTH_ON.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_SOUTH_OFF.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_SOUTH_ON.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_EAST_OFF.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_EAST_ON.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_WEST_OFF.name()));
        registry.insert(
                stringValue(NAME, "Stone Button"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/stone.png"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.BUTTON_WEST_ON.name()));

        registerDefault("Redstone Ore",         "ProgrammerArt-v3.0/textures/blocks/redstone_ore.png");
        registerDefault("Glowing Redstone Ore", "ProgrammerArt-v3.0/textures/blocks/redstone_ore.png");

        registry.insert(
                stringValue(NAME, "Snow"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/snow.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/snow.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/snow.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/snow.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/snow.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/snow.png"),
                stringValue(MODEL, BlockModel.SNOW_LAYER_0.name()));

        registerDefault("Ice",        "ProgrammerArt-v3.0/textures/blocks/ice.png");
        registerDefault("Snow Block", "ProgrammerArt-v3.0/textures/blocks/snow.png");

        registry.insert(
                stringValue(NAME, "Cactus"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/cactus_top.png"),
                stringValue(BOTTOM_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/cactus_bottom.png"),
                stringValue(LEFT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/cactus_side.png"),
                stringValue(RIGHT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/cactus_side.png"),
                stringValue(FRONT_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/cactus_side.png"),
                stringValue(BACK_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/cactus_side.png"),
                stringValue(MODEL, BlockModel.CACTUS.name()));

        registerDefault("Clay", "ProgrammerArt-v3.0/textures/blocks/clay.png");

        registerSapling("Sugar Canes", "ProgrammerArt-v3.0/textures/blocks/reeds.png");

        registerDirectional(
                "Pumpkin",
                "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png",
                "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png",
                "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png",
                "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_off.png");

        registry.insert(
                stringValue(NAME, "Oak Fence"),
                stringValue(TOP_TEXTURE, "ProgrammerArt-v3.0/textures/blocks/planks_oak.png"),
                stringValue(MODEL, BlockModel.FENCE.name()));

        registerDefault("Netherrack", "ProgrammerArt-v3.0/textures/blocks/netherrack.png");
        registerDefault("Soul Sand", "ProgrammerArt-v3.0/textures/blocks/soul_sand.png");
        registerDefault("Glowstone", "ProgrammerArt-v3.0/textures/blocks/glowstone.png");

        registerDirectional(
                "Jack o'Lantern",
                "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png",
                "ProgrammerArt-v3.0/textures/blocks/pumpkin_top.png",
                "ProgrammerArt-v3.0/textures/blocks/pumpkin_side.png",
                "ProgrammerArt-v3.0/textures/blocks/pumpkin_face_on.png");

        registry.insert(
                stringValue(NAME, "<TEMP_SOLID>"),
                stringValue(MODEL, BlockModel.INVISIBLE.name()));
        registry.insert(
                stringValue(NAME, "<UNDEFINED>"),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.INVISIBLE.name()));
    }

    private void registerDefault(String name, String texture) {
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture));
    }

    private void registerSapling(String name, String texture) {
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                booleanValue(SOLID, false),
                stringValue(MODEL, BlockModel.SAPLING.name()));
    }

    private void registerLog(String name, String topTexture, String sideTexture) {
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, topTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture));
    }

    private void registerSlabs(String name, String texture) {
        registry.insert(
                stringValue(NAME, String.format("Double %s", name)),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.BOTTOM_SLAB.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.TOP_SLAB.name()));
    }

    private void registerSlabs(String name, String topTexture, String sideTexture) {
        registry.insert(
                stringValue(NAME, String.format("Double %s", name)),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, topTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, topTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture),
                stringValue(MODEL, BlockModel.BOTTOM_SLAB.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, topTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture),
                stringValue(MODEL, BlockModel.TOP_SLAB.name()));
    }

    private void registerSlabs(String name, String topTexture, String bottomTexture, String sideTexture) {
        registry.insert(
                stringValue(NAME, String.format("Double %s", name)),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, bottomTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, bottomTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture),
                stringValue(MODEL, BlockModel.BOTTOM_SLAB.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, bottomTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture),
                stringValue(MODEL, BlockModel.TOP_SLAB.name()));
    }

    private void registerVertical(String name, String topTexture, String sideTexture) {
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, topTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture));
    }

    private void registerVertical(String name, String topTexture, String bottomTexture, String sideTexture) {
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, bottomTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture));
    }

    private void registerStairs(String name, String texture) {
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.NORTH_STAIRS.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.SOUTH_STAIRS.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.EAST_STAIRS.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.WEST_STAIRS.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.NORTH_CEILING_STAIRS.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.SOUTH_CEILING_STAIRS.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.EAST_CEILING_STAIRS.name()));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, texture),
                stringValue(BOTTOM_TEXTURE, texture),
                stringValue(LEFT_TEXTURE, texture),
                stringValue(RIGHT_TEXTURE, texture),
                stringValue(FRONT_TEXTURE, texture),
                stringValue(BACK_TEXTURE, texture),
                stringValue(MODEL, BlockModel.WEST_CEILING_STAIRS.name()));
    }

    private void registerDirectional(
            String name,
            String topTexture,
            String bottomTexture,
            String sideTexture,
            String frontTexture)
    {
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, bottomTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, frontTexture));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, bottomTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, frontTexture),
                stringValue(BACK_TEXTURE, sideTexture));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, bottomTexture),
                stringValue(LEFT_TEXTURE, sideTexture),
                stringValue(RIGHT_TEXTURE, frontTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture));
        registry.insert(
                stringValue(NAME, name),
                stringValue(TOP_TEXTURE, topTexture),
                stringValue(BOTTOM_TEXTURE, bottomTexture),
                stringValue(LEFT_TEXTURE, frontTexture),
                stringValue(RIGHT_TEXTURE, sideTexture),
                stringValue(FRONT_TEXTURE, sideTexture),
                stringValue(BACK_TEXTURE, sideTexture));
    }
}