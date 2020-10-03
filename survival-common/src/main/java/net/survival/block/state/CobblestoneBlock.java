package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(3)
public final class CobblestoneBlock extends BlockState {
    public static final CobblestoneBlock INSTANCE = new CobblestoneBlock();

    private CobblestoneBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Cobblestone";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "ProgrammerArt-v3.0/textures/blocks/cobblestone.png";
    }
}