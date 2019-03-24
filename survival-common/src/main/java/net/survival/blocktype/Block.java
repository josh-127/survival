package net.survival.blocktype;

public abstract class Block
{
    protected final short typeId;

    public Block(short typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getFullId() {
        return (typeId << 16) | getEncodedState();
    }
    
    public boolean is(Block block) {
        return getFullId() == block.getFullId();
    }

    public boolean isType(Block block) {
        return typeId == block.typeId;
    }

    protected abstract short getEncodedState();
    protected abstract Block withState(short encodedState);

    public abstract String getDisplayName();
    public abstract double getHardness();
    public abstract double getResistance();
    public abstract boolean isSolid();
    public abstract BlockModel getModel();
    public abstract String getTexture(BlockFace blockFace);
}