package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(6)
public final class GravelBlock extends BlockState {
    public static final GravelBlock INSTANCE = new GravelBlock();

    private GravelBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Gravel";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "ProgrammerArt-v3.0/textures/blocks/gravel.png";
    }
}