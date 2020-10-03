package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(14)
public final class WaterBlock extends BlockState {
    public static final WaterBlock INSTANCE = new WaterBlock();

    private WaterBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Water";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "textures/blocks/water.png";
    }
}