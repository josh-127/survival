package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(13)
public final class TempSolidBlock extends BlockState {
    public static final TempSolidBlock INSTANCE = new TempSolidBlock();

    private TempSolidBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "<temp_solid>";
    }
}