package net.survival.block;

public class BasicBlock extends Block
{
    protected final String displayName;
    protected final double hardness;
    protected final double resistance;
    protected final boolean solid;
    protected final BlockModel model;
    protected final String[] textures;

    private BasicBlock(
            short typeID,
            String displayName,
            double hardness,
            double resistance,
            boolean solid,
            BlockModel model,
            String[] textures)
    {
        super(typeID);
        this.displayName = displayName;
        this.hardness = hardness;
        this.resistance = resistance;
        this.solid = solid;
        this.model = model;
        this.textures = textures;
    }

    @Override
    protected short getEncodedState() {
        return 0;
    }

    @Override
    public Block withState(short encodedState) {
        return this;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public double getHardness() {
        return hardness;
    }

    @Override
    public double getResistance() {
        return resistance;
    }

    @Override
    public boolean isSolid() {
        return solid;
    }

    @Override
    public BlockModel getModel() {
        return model;
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return textures[blockFace.ordinal()];
    }

    public static class Builder
    {
        private final short typeID;
        private String displayName = "<undefined>";
        private double hardness = 1.0;
        private double resistance = 1.0;
        private boolean solid = true;
        private BlockModel model = BlockModel.DEFAULT;
        private String[] textures = new String[BlockFace.getCachedValues().length];

        public Builder(short typeID) {
            this.typeID = typeID;
        }

        public BasicBlock build() {
            return new BasicBlock(
                    typeID,
                    displayName,
                    hardness,
                    resistance,
                    solid,
                    model,
                    textures);
        }

        public Builder withDisplayName(String as) {
            displayName = as;
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

        public Builder withSolidity(boolean as) {
            solid = as;
            return this;
        }

        public Builder withModel(BlockModel as) {
            model = as;
            return this;
        }

        public Builder withTexture(BlockFace blockFace, String as) {
            textures[blockFace.ordinal()] = as;
            return this;
        }

        public Builder withTextureOnSides(String as) {
            textures[BlockFace.LEFT.ordinal()] = as;
            textures[BlockFace.RIGHT.ordinal()] = as;
            textures[BlockFace.FRONT.ordinal()] = as;
            textures[BlockFace.BACK.ordinal()] = as;
            return this;
        }

        public Builder withTextureOnTopAndBottom(String as) {
            textures[BlockFace.TOP.ordinal()] = as;
            textures[BlockFace.BOTTOM.ordinal()] = as;
            return this;
        }

        public Builder withTextureOnAllFaces(String as) {
            for (int i = 0; i < textures.length; ++i)
                textures[i] = as;
            return this;
        }
    }
}