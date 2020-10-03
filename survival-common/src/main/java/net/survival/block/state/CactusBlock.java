package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(2)
public final class CactusBlock extends BlockState {
    public static final CactusBlock INSTANCE = new CactusBlock();

    private CactusBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Cactus";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        switch (blockFace) {
        case TOP: return "ProgrammerArt-v3.0/textures/blocks/cactus_top.png";
        case BOTTOM: return "ProgrammerArt-v3.0/textures/blocks/cactus_bottom.png";
        case LEFT:
        case RIGHT:
        case FRONT:
        case BACK: return "ProgrammerArt-v3.0/textures/blocks/cactus_side.png";
        default: throw new IllegalArgumentException("blockFace");
        }
    }
}