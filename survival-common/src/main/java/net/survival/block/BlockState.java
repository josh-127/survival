package net.survival.block;

public class BlockState implements Comparable<BlockState>
{
    private final int id;
    private final String internalName;
    private final String displayName;
    private final String keywords;
    private final double hardness;
    private final double resistance;
    private final boolean solid;
    private final BlockModel model;
    private final String[] textures;

    private BlockState(
            int id,
            String internalName,
            String displayName,
            String keywords,
            double hardness,
            double resistance,
            boolean solid,
            BlockModel model,
            String[] textures)
    {
        this.id = id;
        this.internalName = internalName;
        this.displayName = displayName;
        this.keywords = keywords;
        this.hardness = hardness;
        this.resistance = resistance;
        this.solid = solid;
        this.model = model;
        this.textures = textures;
    }

    @Override
    public int compareTo(BlockState o) {
        if (id < o.id) return -1;
        if (id > o.id) return 1; 
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(BlockState.class))
            return false;

        BlockState other = (BlockState) obj;
        return id == other.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public int getID() {
        return id;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getKeywords() {
        return keywords;
    }

    public double getHardness() {
        return hardness;
    }

    public double getResistance() {
        return resistance;
    }
    
    public boolean isSolid() {
        return solid;
    }

    public BlockModel getModel() {
        return model;
    }
    
    public String getTexture(BlockFace blockFace) {
        return textures[blockFace.ordinal()];
    }

    public static class Builder
    {
        private final BlockRegistry registry;
        private String internalName = "<UNDEFINED>";
        private String displayName = "<UNDEFINED>";
        private String keywords = "";
        private double hardness = 1.0;
        private double resistance = 1.0;
        private boolean solid = true;
        private BlockModel model = BlockModel.DEFAULT;
        private final String[] textures = new String[BlockFace.getCachedValues().length];

        public Builder(BlockRegistry registry) {
            this.registry = registry;
        }

        public BlockState build() {
            return new BlockState(
                    registry.getNextID(),
                    internalName,
                    displayName,
                    keywords,
                    hardness,
                    resistance,
                    solid,
                    model,
                    textures);
        }

        public Builder withInternalName(String as) {
            internalName = as;
            return this;
        }

        public Builder withDisplayName(String as) {
            displayName = as;
            return this;
        }

        public Builder withKeywords(String as) {
            keywords = as;
            return this;
        }

        public Builder withHardness(double as) {
            hardness = as;
            return this;
        }

        public Builder withResistance(double as) {
            resistance = as;
            return this;
        }

        public Builder withSolidEnabled(boolean enabled) {
            solid = enabled;
            return this;
        }

        public Builder withModel(BlockModel as) {
            model = as;
            return this;
        }
        
        public Builder withTexture(BlockFace blockFace, String as) {
            textures[blockFace.ordinal()] = as;;
            return this;
        }
        
        public Builder withTextureOnSides(String as) {
            textures[BlockFace.LEFT.ordinal()] = as;
            textures[BlockFace.RIGHT.ordinal()] = as;
            textures[BlockFace.FRONT.ordinal()] = as;
            textures[BlockFace.BACK.ordinal()] = as;
            return this;
        }
        
        public Builder withTextureOnAllFaces(String as) {
            for (int i = 0; i < BlockFace.getCachedValues().length; ++i)
                textures[i] = as;
            return this;
        }
    }
}