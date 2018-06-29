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
            ROCK_SALT,SCHIST,
            SHALE,
            SLATE
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