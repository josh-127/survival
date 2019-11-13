package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(8)
public final class OakLogBlock extends BlockState
{
    public static final OakLogBlock INSTANCE = new OakLogBlock();

    private OakLogBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Oak Log";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        switch (blockFace) {
        case TOP:
        case BOTTOM: return "ProgrammerArt-v3.0/textures/blocks/log_oak_top.png";
        case LEFT:
        case RIGHT:
        case FRONT:
        case BACK: return "ProgrammerArt-v3.0/textures/blocks/log_oak.png";
        default: throw new IllegalArgumentException("blockFace");
        }
    }
}