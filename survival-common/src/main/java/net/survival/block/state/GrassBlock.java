package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(5)
public final class GrassBlock extends BlockState
{
    public static final GrassBlock INSTANCE = new GrassBlock();

    private GrassBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Grass Block";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        switch (blockFace) {
        case TOP: return "ProgrammerArt-v3.0/textures/blocks/grass_top.png";
        case BOTTOM: return "ProgrammerArt-v3.0/textures/blocks/dirt.png";
        case LEFT:
        case RIGHT:
        case FRONT:
        case BACK: return "ProgrammerArt-v3.0/textures/blocks/grass_side.png";
        default: throw new IllegalArgumentException("blockFace");
        }
    }
}