package net.survival.blocktype;

public class SlabBlock extends StatefulBlock
{
    public SlabBlock(short typeID, short encodedState, BasicBlock baseBlock) {
        super(typeID, encodedState, baseBlock);
    }

    @Override
    protected Block withState(short encodedState) {
        return new SlabBlock(typeID, encodedState, baseBlock);
    }

    @Override
    public BlockModel getModel() {
        return getSlabPosition().getModel();
    }

    private SlabPosition getSlabPosition() {
        return SlabPosition.getCachedValues()[encodedState];
    }

    public SlabBlock withSlabPosition(SlabPosition as) {
        return new SlabBlock(typeID, (short) as.ordinal(), baseBlock);
    }
}