package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(12)
public final class StoneBlock extends BlockState {
    public static final StoneBlock INSTANCE = new StoneBlock();

    private StoneBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Stone";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "ProgrammerArt-v3.0/textures/blocks/stone.png";
    }
}