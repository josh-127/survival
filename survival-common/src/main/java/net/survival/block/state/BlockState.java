package net.survival.block.state;

public abstract class BlockState
{
    public abstract void accept(BlockStateVisitor visitor);

    public String getDisplayName() {
        return "<undefined>";
    }

    public double getHardness() {
        return 1.0;
    }

    public double getResistance() {
        return 1.0;
    }

    public boolean isSolid() {
        return true;
    }

    public BlockModel getModel() {
        return BlockModel.DEFAULT;
    }

    public String getTexture(BlockFace blockFace) {
        return null;
    }
}