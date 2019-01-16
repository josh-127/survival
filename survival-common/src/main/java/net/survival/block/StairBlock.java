package net.survival.block;

public class StairBlock extends StatefulBlock
{
    public StairBlock(short typeID, short encodedState, BasicBlock baseBlock) {
        super(typeID, encodedState, baseBlock);
    }

    @Override
    public BlockModel getModel() {
        return getDirection().getModel();
    }

    public StairDirection getDirection() {
        return StairDirection.getCachedValues()[encodedState];
    }

    public StairBlock withDirection(StairDirection as) {
        return new StairBlock(typeID, (short) as.ordinal(), baseBlock);
    }
}