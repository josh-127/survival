package net.survival.blocktype;

public class StairBlock extends StatefulBlock
{
    public StairBlock(short typeId, short encodedState, BasicBlock baseBlock) {
        super(typeId, encodedState, baseBlock);
    }

    @Override
    protected Block withState(short encodedState) {
        return new StairBlock(typeId, encodedState, baseBlock);
    }

    @Override
    public BlockModel getModel() {
        return getDirection().getModel();
    }

    public StairDirection getDirection() {
        return StairDirection.getCachedValues()[encodedState];
    }

    public StairBlock withDirection(StairDirection as) {
        return new StairBlock(typeId, (short) as.ordinal(), baseBlock);
    }
}