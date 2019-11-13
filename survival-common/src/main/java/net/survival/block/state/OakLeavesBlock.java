package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(7)
public final class OakLeavesBlock extends BlockState
{
    public static final OakLeavesBlock INSTANCE = new OakLeavesBlock();

    private OakLeavesBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Oak Leaves";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "ProgrammerArt-v3.0/textures/blocks/leaves_oak.png";
    }
}