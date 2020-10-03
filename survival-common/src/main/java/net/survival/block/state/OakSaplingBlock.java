package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(9)
public final class OakSaplingBlock extends BlockState {
    public static final OakSaplingBlock INSTANCE = new OakSaplingBlock();

    private OakSaplingBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Oak Sapling";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "ProgrammerArt-v3.0/textures/blocks/sapling_oak.png";
    }
}