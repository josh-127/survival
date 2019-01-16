package net.survival.block;

public class StatefulBlock extends Block
{
    protected final short encodedState;
    protected final BasicBlock baseBlock;

    public StatefulBlock(short typeID, short encodedState, BasicBlock baseBlock) {
        super(typeID);
        this.encodedState = encodedState;
        this.baseBlock = baseBlock;
    }

    @Override
    protected short getEncodedState() {
        return encodedState;
    }

    @Override
    public Block withState(short encodedState) {
        return new StatefulBlock(typeID, encodedState, baseBlock);
    }

    @Override
    public String getDisplayName() {
        return baseBlock.getDisplayName();
    }

    @Override
    public double getHardness() {
        return baseBlock.getHardness();
    }

    @Override
    public double getResistance() {
        return baseBlock.getResistance();
    }

    @Override
    public boolean isSolid() {
        return baseBlock.isSolid();
    }

    @Override
    public BlockModel getModel() {
        return baseBlock.getModel();
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return baseBlock.getTexture(blockFace);
    }
}