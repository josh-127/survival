package net.survival.block;

import java.util.Iterator;

public enum BlockType
{
    EMPTY((bt) -> {
        bt.name = "<EMPTY>";
        bt.solid = false;
        bt.visible = false;
    }),
    
    //
    // Raw Stone
    //
    ANDESITE((bt) -> {
        bt.name = "Andesite";
        bt.setTextureOnAllFaces("textures/blocks/stone/andesite.png");
    }),
    BASALT((bt) -> {
        bt.name = "Basalt";
        bt.setTextureOnAllFaces("textures/blocks/stone/basalt.png");
    }),
    CHALK((bt) -> {
        bt.name = "Chalk";
        bt.setTextureOnAllFaces("textures/blocks/stone/chalk.png");
    }),
    CHERT((bt) -> {
        bt.name = "Chert";
        bt.setTextureOnAllFaces("textures/blocks/stone/chert.png");
    }),
    CLAYSTONE((bt) -> {
        bt.name = "Claystone";
        bt.setTextureOnAllFaces("textures/blocks/stone/claystone.png");
    }),
    CONGLOMERATE((bt) -> {
        bt.name = "Conglomerate";
        bt.setTextureOnAllFaces("textures/blocks/stone/conglomerate.png");
    }),
    DACITE((bt) -> {
        bt.name = "Dacite";
        bt.setTextureOnAllFaces("textures/blocks/stone/dacite.png");
    }),
    DIORITE((bt) -> {
        bt.name = "Diorite";
        bt.setTextureOnAllFaces("textures/blocks/stone/diorite.png");
    }),
    DOLOMITE((bt) -> {
        bt.name = "Dolomite";
        bt.setTextureOnAllFaces("textures/blocks/stone/dolomite.png");
    }),
    GABBRO((bt) -> {
        bt.name = "Gabbro";
        bt.setTextureOnAllFaces("textures/blocks/stone/gabbro.png");
    }),
    GNEISS((bt) -> {
        bt.name = "Gneiss";
        bt.setTextureOnAllFaces("textures/blocks/stone/gneiss.png");
    }),
    GRANITE((bt) -> {
        bt.name = "Granite";
        bt.setTextureOnAllFaces("textures/blocks/stone/granite.png");
    }),
    LIMESTONE((bt) -> {
        bt.name = "Limestone";
        bt.setTextureOnAllFaces("textures/blocks/stone/limestone.png");
    }),
    MARBLE((bt) -> {
        bt.name = "Marble";
        bt.setTextureOnAllFaces("textures/blocks/stone/marble.png");
    }),
    PHYLLITE((bt) -> {
        bt.name = "Phyllite";
        bt.setTextureOnAllFaces("textures/blocks/stone/phyllite.png");
    }),
    QUARTZITE((bt) -> {
        bt.name = "Quartzite";
        bt.setTextureOnAllFaces("textures/blocks/stone/quartzite.png");
    }),
    RHYOLITE((bt) -> {
        bt.name = "Rhyolite";
        bt.setTextureOnAllFaces("textures/blocks/stone/rhyolite.png");
    }),
    ROCK_SALT((bt) -> {
        bt.name = "Rock Salt";
        bt.setTextureOnAllFaces("textures/blocks/stone/rock_salt.png");
    }),
    SCHIST((bt) -> {
        bt.name = "Schist";
        bt.setTextureOnAllFaces("textures/blocks/stone/schist.png");
    }),
    SHALE((bt) -> {
        bt.name = "Shale";
        bt.setTextureOnAllFaces("textures/blocks/stone/shale.png");
    }),
    SLATE((bt) -> {
        bt.name = "Slate";
        bt.setTextureOnAllFaces("textures/blocks/stone/slate.png");
    }),
    
    //
    // Dirt
    //
    ANDESITE_DIRT((bt) -> {
       bt.name = "Andesite Dirt";
       bt.setTextureOnAllFaces("textures/blocks/dirt/andesite.png");
    }),
    BASALT_DIRT((bt) -> {
        bt.name = "Basalt Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/basalt.png");
    }),
    CHALK_DIRT((bt) -> {
        bt.name = "Chalk Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/chalk.png");
    }),
    CHERT_DIRT((bt) -> {
        bt.name = "Chert Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/chert.png");
    }),
    CLAYSTONE_DIRT((bt) -> {
        bt.name = "Claystone Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/claystone.png");
    }),
    CONGLOMERATE_DIRT((bt) -> {
        bt.name = "Conglomerate Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/conglomerate.png");
    }),
    DIORITE_DIRT((bt) -> {
        bt.name = "Diorite Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/diorite.png");
    }),
    DOLOMITE_DIRT((bt) -> {
        bt.name = "Dolomite Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/dolomite.png");
    }),
    GABBRO_DIRT((bt) -> {
        bt.name = "Gabbro Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/gabbro.png");
    }),
    DACITE_DIRT((bt) -> {
        bt.name = "Dacite Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/dacite.png");
    }),
    GNEISS_DIRT((bt) -> {
        bt.name = "Gneiss Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/gneiss.png");
    }),
    GRANITE_DIRT((bt) -> {
        bt.name = "Granite Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/granite.png");
    }),
    LIMESTONE_DIRT((bt) -> {
        bt.name = "Limestone Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/limestone.png");
    }),
    MARBLE_DIRT((bt) -> {
        bt.name = "Marble Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/marble.png");
    }),
    PEAT_DIRT((bt) -> {
        bt.name = "Peat";
        bt.setTextureOnAllFaces("textures/blocks/dirt/peat.png");
    }),
    PHYLLITE_DIRT((bt) -> {
        bt.name = "Phyllite Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/phyllite.png");
    }),
    QUARTZITE_DIRT((bt) -> {
        bt.name = "Quartzite Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/quartzite.png");
    }),
    RHYOLITE_DIRT((bt) -> {
        bt.name = "Rhyolite Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/rhyolite.png");
    }),
    ROCK_SALT_DIRT((bt) -> {
        bt.name = "Rock Salt Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/rock_salt.png");
    }),
    SCHIST_DIRT((bt) -> {
        bt.name = "Schist Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/schist.png");
    }),
    SHALE_DIRT((bt) -> {
        bt.name = "Shale Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/shale.png");
    }),
    SLATE_DIRT((bt) -> {
        bt.name = "Slate Dirt";
        bt.setTextureOnAllFaces("textures/blocks/dirt/slate.png");
    }),
    
    //
    // Sand
    //
    ANDESITE_SAND((bt) -> {
        bt.name = "Andesite Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/andesite.png");
    }),
    BASALT_SAND((bt) -> {
        bt.name = "Basalt Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/basalt.png");
    }),
    CHALK_SAND((bt) -> {
        bt.name = "Chalk Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/chalk.png");
    }),
    CHERT_SAND((bt) -> {
        bt.name = "Chert Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/chert.png");
    }),
    CLAYSTONE_SAND((bt) -> {
        bt.name = "Claystone Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/claystone.png");
    }),
    CONGLOMERATE_SAND((bt) -> {
        bt.name = "Conglomerate Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/conglomerate.png");
    }),
    DACITE_SAND((bt) -> {
        bt.name = "Dacite Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/dacite.png");
    }),
    DIORITE_SAND((bt) -> {
        bt.name = "Diorite Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/diorite.png");
    }),
    DOLOMITE_SAND((bt) -> {
        bt.name = "Dolomite Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/dolomite.png");
    }),
    GABBRO_SAND((bt) -> {
        bt.name = "Gabbro Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/gabbro.png");
    }),
    GNEISS_SAND((bt) -> {
        bt.name = "Gneiss Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/gneiss.png");
    }),
    GRANITE_SAND((bt) -> {
        bt.name = "Granite Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/granite.png");
    }),
    LIMESTONE_SAND((bt) -> {
        bt.name = "Limestone Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/limestone.png");
    }),
    MARBLE_SAND((bt) -> {
        bt.name = "Marble Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/marble.png");
    }),
    PHYLLITE_SAND((bt) -> {
        bt.name = "Phyllite Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/phyllite.png");
    }),
    QUARTZITE_SAND((bt) -> {
        bt.name = "Quartzite Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/quartzite.png");
    }),
    RHYOLITE_SAND((bt) -> {
        bt.name = "Rhyolite Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/rhyolite.png");
    }),
    ROCK_SALT_SAND((bt) -> {
        bt.name = "Rock Salt Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/rock_salt.png");
    }),
    SCHIST_SAND((bt) -> {
        bt.name = "Schist Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/schist.png");
    }),
    SHALE_SAND((bt) -> {
        bt.name = "Shale Sand";
        bt.setTextureOnAllFaces("textures/blocks/sand/shale.png");
    }),
    
    //
    // Leaves
    //
    ACACIA_LEAVES((bt) -> {
        bt.name = "Acacia Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/acacia.png");
    }),
    ASH_LEAVES((bt) -> {
        bt.name = "Ash Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/ash.png");
    }),
    ASPEN_LEAVES((bt) -> {
        bt.name = "Aspen Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/aspen.png");
    }),
    BIRCH_LEAVES((bt) -> {
        bt.name = "Birch Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/birch.png");
    }),
    CHESTNUT_LEAVES((bt) -> {
        bt.name = "Chestnut Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/chestnut.png");
    }),
    DOUGLAS_FIR_LEAVES((bt) -> {
        bt.name = "Douglas Fir Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/douglas_fir.png");
    }),
    HICKORY_LEAVES((bt) -> {
        bt.name = "Hickory Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/hickory.png");
    }),
    KAPOK_LEAVES((bt) -> {
        bt.name = "Kapok Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/kapok.png");
    }),
    MAPLE_LEAVES((bt) -> {
        bt.name = "Maple Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/maple.png");
    }),
    OAK_LEAVES((bt) -> {
        bt.name = "Oak Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/oak.png");
    }),
    PINE_LEAVES((bt) -> {
        bt.name = "Pine Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/pine.png");
    }),
    SEQUOIA_LEAVES((bt) -> {
        bt.name = "Sequoia Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/sequoia.png");
    }),
    SPRUCE_LEAVES((bt) -> {
        bt.name = "Spruce Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/spruce.png");
    }),
    SYCAMORE_LEAVES((bt) -> {
        bt.name = "Sycamore Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/sycamore.png");
    }),
    WHITE_CEDAR_LEAVES((bt) -> {
        bt.name = "White Cedar Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/white_cedar.png");
    }),
    WHITE_ELM_LEAVES((bt) -> {
        bt.name = "White Elm Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/white_elm.png");
    }),
    WILLOW_LEAVES((bt) -> {
        bt.name = "Willow Leaves";
        bt.setTextureOnAllFaces("textures/blocks/wood/leaves/willow.png");
    }),
    
    //
    // Wooden Logs
    //
    ACACIA_LOG((bt) -> {
        bt.name = "Acacia Log";
        bt.setTextureOnSides("textures/blocks/wood/log/acacia_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/acacia_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/acacia_top.png");
    }),
    ASH_LOG((bt) -> {
        bt.name = "Ash Log";
        bt.setTextureOnSides("textures/blocks/wood/log/ash_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/ash_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/ash_top.png");
    }),
    ASPEN_LOG((bt) -> {
        bt.name = "Aspen Log";
        bt.setTextureOnSides("textures/blocks/wood/log/aspen_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/aspen_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/aspen_top.png");
    }),
    BIRCH_LOG((bt) -> {
        bt.name = "Birch Log";
        bt.setTextureOnSides("textures/blocks/wood/log/birch_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/birch_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/birch_top.png");
    }),
    CHESTNUT_LOG((bt) -> {
        bt.name = "Chestnut Log";
        bt.setTextureOnSides("textures/blocks/wood/log/chestnut_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/chestnut_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/chestnut_top.png");
    }),
    DOUGLAS_FIR_LOG((bt) -> {
        bt.name = "Douglas Fir Log";
        bt.setTextureOnSides("textures/blocks/wood/log/douglas_fir_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/douglas_fir_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/douglas_fir_top.png");
    }),
    HICKORY_LOG((bt) -> {
        bt.name = "Hickory Log";
        bt.setTextureOnSides("textures/blocks/wood/log/hickory_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/hickory_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/hickory_top.png");
    }),
    KAPOK_LOG((bt) -> {
        bt.name = "Kapok Log";
        bt.setTextureOnSides("textures/blocks/wood/log/kapok_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/kapok_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/kapok_top.png");
    }),
    MAPLE_LOG((bt) -> {
        bt.name = "Maple Log";
        bt.setTextureOnSides("textures/blocks/wood/log/maple_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/maple_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/maple_top.png");
    }),
    OAK_LOG((bt) -> {
        bt.name = "Oak Log";
        bt.setTextureOnSides("textures/blocks/wood/log/oak_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/oak_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/oak_top.png");
    }),
    PINE_LOG((bt) -> {
        bt.name = "Pine Log";
        bt.setTextureOnSides("textures/blocks/wood/log/pine_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/pine_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/pine_top.png");
    }),
    SEQUOIA_LOG((bt) -> {
        bt.name = "Sequoia Log";
        bt.setTextureOnSides("textures/blocks/wood/log/sequoia_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/sequoia_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/sequoia_top.png");
    }),
    SPRUCE_LOG((bt) -> {
        bt.name = "Spruce Log";
        bt.setTextureOnSides("textures/blocks/wood/log/spruce_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/spruce_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/spruce_top.png");
    }),
    SYCAMORE_LOG((bt) -> {
        bt.name = "Sycamore Log";
        bt.setTextureOnSides("textures/blocks/wood/log/sycamore_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/sycamore_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/sycamore_top.png");
    }),
    WHITE_CEDAR_LOG((bt) -> {
        bt.name = "White Cedar Log";
        bt.setTextureOnSides("textures/blocks/wood/log/white_cedar_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/white_cedar_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/white_cedar_top.png");
    }),
    WHITE_ELM_LOG((bt) -> {
        bt.name = "White Elm Log";
        bt.setTextureOnSides("textures/blocks/wood/log/white_elm_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/white_elm_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/white_elm_top.png");
    }),
    WILLOW_LOG((bt) -> {
        bt.name = "Willow Log";
        bt.setTextureOnSides("textures/blocks/wood/log/willow_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/wood/log/willow_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/wood/log/willow_top.png");
    }),
    
    //
    // Wooden Planks
    //
    ACACIA_PLANKS((bt) -> {
        bt.name = "Acacia Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/acacia.png");
    }),
    ASH_PLANKS((bt) -> {
        bt.name = "Ash Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/ash.png");
    }),
    ASPEN_PLANKS((bt) -> {
        bt.name = "Aspen Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/aspen.png");
    }),
    BIRCH_PLANKS((bt) -> {
        bt.name = "Birch Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/birch.png");
    }),
    CHESTNUT_PLANKS((bt) -> {
        bt.name = "Chestnut Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/chestnut.png");
    }),
    DOUGLAS_FIR_PLANKS((bt) -> {
        bt.name = "Douglas Fir Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/douglas_fir.png");
    }),
    HICKORY_PLANKS((bt) -> {
        bt.name = "Hickory Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/hickory.png");
    }),
    KAPOK_PLANKS((bt) -> {
        bt.name = "Kapok Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/kapok.png");
    }),
    MAPLE_PLANKS((bt) -> {
        bt.name = "Maple Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/maple.png");
    }),
    OAK_PLANKS((bt) -> {
        bt.name = "Oak Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/oak.png");
    }),
    PINE_PLANKS((bt) -> {
        bt.name = "Pine Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/pine.png");
    }),
    SEQUOIA_PLANKS((bt) -> {
        bt.name = "Sequoia Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/sequoia.png");
    }),
    SPRUCE_PLANKS((bt) -> {
        bt.name = "Spruce Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/spruce.png");
    }),
    SYCAMORE_PLANKS((bt) -> {
        bt.name = "Sycamore Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/sycamore.png");
    }),
    WHITE_CEDAR_PLANKS((bt) -> {
        bt.name = "White Cedar Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/white_cedar.png");
    }),
    WHITE_ELM_PLANKS((bt) -> {
        bt.name = "White Elm Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/white_elm.png");
    }),
    WILLOW_PLANKS((bt) -> {
        bt.name = "Willow Planks";
        bt.setTextureOnAllFaces("textures/blocks/wood/plank/willow.png");
    }),
    
    //
    // Placeholders
    //
    GRASS((bt) -> {
        bt.name = "Grass";
        bt.setTextureOnSides("textures/blocks/grass_side.png");
        bt.setTexture(BlockFace.TOP, "textures/blocks/grass_top.png");
        bt.setTexture(BlockFace.BOTTOM, "textures/blocks/dirt/granite.png");
    }),
    WATER((bt) -> {
        bt.name = "Water";
        bt.setTextureOnAllFaces("textures/blocks/water.png");
    }),
    
    //
    // Special
    //
    TEMP_SOLID((bt) -> {
        bt.name = "<TEMP_SOLID>";
    }),
    UNDEFINED((bt) -> {
        bt.name = "<UNDEFINED>";
    });
    
    private static final BlockType[] cachedValues = values();
    
    private static final BlockType[] stoneTypes = {
            ANDESITE,
            BASALT,
            CHALK,
            CHERT,
            CLAYSTONE,
            CONGLOMERATE,
            DACITE,
            DIORITE,
            DOLOMITE,
            GABBRO,
            GNEISS,
            GRANITE,
            LIMESTONE,
            MARBLE,
            PHYLLITE,
            QUARTZITE,
            RHYOLITE,
            ROCK_SALT,
            SCHIST,
            SHALE,
            SLATE
    };
    
    private static final BlockType[] leavesTypes = {
            ACACIA_LEAVES,
            ASH_LEAVES,
            ASPEN_LEAVES,
            BIRCH_LEAVES,
            CHESTNUT_LEAVES,
            DOUGLAS_FIR_LEAVES,
            HICKORY_LEAVES,
            KAPOK_LEAVES,
            MAPLE_LEAVES,
            OAK_LEAVES,
            PINE_LEAVES,
            SEQUOIA_LEAVES,
            SPRUCE_LEAVES,
            SYCAMORE_LEAVES,
            WHITE_CEDAR_LEAVES,
            WHITE_ELM_LEAVES,
            WILLOW_LEAVES
    };
    
    private static final BlockType[] woodenLogTypes = {
            ACACIA_LOG,
            ASH_LOG,
            ASPEN_LOG,
            BIRCH_LOG,
            CHESTNUT_LOG,
            DOUGLAS_FIR_LOG,
            HICKORY_LOG,
            KAPOK_LOG,
            MAPLE_LOG,
            OAK_LOG,
            PINE_LOG,
            SEQUOIA_LOG,
            SPRUCE_LOG,
            SYCAMORE_LOG,
            WHITE_CEDAR_LOG,
            WHITE_ELM_LOG,
            WILLOW_LOG
    };
    
    private static final BlockType[] woodenPlanksTypes = {
            ACACIA_PLANKS,
            ASH_PLANKS,
            ASPEN_PLANKS,
            BIRCH_PLANKS,
            CHESTNUT_PLANKS,
            DOUGLAS_FIR_PLANKS,
            HICKORY_PLANKS,
            KAPOK_PLANKS,
            MAPLE_PLANKS,
            OAK_PLANKS,
            PINE_PLANKS,
            SEQUOIA_PLANKS,
            SPRUCE_PLANKS,
            SYCAMORE_PLANKS,
            WHITE_CEDAR_PLANKS,
            WHITE_ELM_PLANKS,
            WILLOW_PLANKS
    };
    
    private final short id;
    private String name;
    private final String[] textures;
    private boolean solid;
    private boolean visible;
    
    private BlockType(Builder builder) {
        id = (short) ordinal();
        name = "<UNDEFINED>";
        textures = new String[BlockFace.values().length];
        solid = true;
        visible = true;
        
        builder.build(this);
    }
    
    public static BlockType[] getCachedValues() {
        return cachedValues;
    }
    
    public static BlockType[] getStoneTypes() {
        return stoneTypes;
    }
    
    public boolean isStone() {
        return isType(stoneTypes);
    }
    
    public static BlockType[] getLeavesTypes() {
        return leavesTypes;
    }
    
    public boolean isLeaves() {
        return isType(leavesTypes);
    }
    
    public static BlockType[] getWoodenLogTypes() {
        return woodenLogTypes;
    }
    
    public boolean isWoodenLog() {
        return isType(woodenLogTypes);
    }
    
    public static BlockType[] getWoodenPlanksTypes() {
        return woodenPlanksTypes;
    }
    
    public boolean isWoodenPlanks() {
        return isType(woodenPlanksTypes);
    }
    
    public boolean isGrass() {
        return this == GRASS;
    }
    
    private boolean isType(BlockType[] types) {
        for (int i = 0; i < types.length; ++i) {
            if (this == types[i])
                return true;
        }
        
        return false;
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
    
    public short getID() {
        return id;
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
    
    public boolean isVisible() {
        return visible;
    }
    
    private interface Builder
    {
        abstract void build(BlockType bt);
    }
}