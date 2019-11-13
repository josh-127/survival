package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(0x00000000)
public final class AirBlock extends BlockState
{
    public static final AirBlock INSTANCE = new AirBlock();

    private AirBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "<air>";
    }

    @Override
    public double getHardness() {
        return 0.0;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public BlockModel getModel() {
        return BlockModel.INVISIBLE;
    }
}