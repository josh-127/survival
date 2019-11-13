package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(10)
public final class SandBlock extends BlockState
{
    public static final SandBlock INSTANCE = new SandBlock();

    private SandBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Sand";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "ProgrammerArt-v3.0/textures/blocks/sand.png";
    }
}