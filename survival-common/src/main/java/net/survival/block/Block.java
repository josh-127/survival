package net.survival.block;

public abstract class Block
{
    protected final short typeID;

    public Block(short typeID) {
        this.typeID = typeID;
    }

    public int getTypeID() {
        return typeID;
    }

    public int getFullID() {
        return (typeID << 16) | getEncodedState();
    }
    
    public boolean is(Block block) {
        return getFullID() == block.getFullID();
    }

    public boolean isType(Block block) {
        return typeID == block.typeID;
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