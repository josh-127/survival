package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(11)
public final class SandstoneBlock extends BlockState
{
    public static final SandstoneBlock INSTANCE = new SandstoneBlock();

    private SandstoneBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Sandstone";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        switch (blockFace) {
        case TOP: return "ProgrammerArt-v3.0/textures/blocks/sandstone_top.png";
        case BOTTOM: return "ProgrammerArt-v3.0/textures/blocks/sandstone_bottom.png";
        case LEFT:
        case RIGHT:
        case FRONT:
        case BACK: return "ProgrammerArt-v3.0/textures/blocks/sandstone_normal.png";
        default: throw new IllegalArgumentException("blockFace");
        }
    }
}