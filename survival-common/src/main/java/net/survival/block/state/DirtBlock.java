package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(4)
public final class DirtBlock extends BlockState
{
    public static final DirtBlock INSTANCE = new DirtBlock();

    private DirtBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Dirt";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "ProgrammerArt-v3.0/textures/blocks/dirt.png";
    }
}