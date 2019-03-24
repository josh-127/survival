package net.survival.blocktype;

public class SlabBlock extends StatefulBlock
{
    public SlabBlock(short typeId, short encodedState, BasicBlock baseBlock) {
        super(typeId, encodedState, baseBlock);
    }

    @Override
    protected Block withState(short encodedState) {
        return new SlabBlock(typeId, encodedState, baseBlock);
    }

    @Override
    public BlockModel getModel() {
        return getSlabPosition().getModel();
    }

    private SlabPosition getSlabPosition() {
        return SlabPosition.getCachedValues()[encodedState];
    }

    public SlabBlock withSlabPosition(SlabPosition as) {
        return new SlabBlock(typeId, (short) as.ordinal(), baseBlock);
    }
}