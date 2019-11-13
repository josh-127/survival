package net.survival.block.state;

import net.survival.block.BlockSerializable;

@BlockSerializable(1)
public final class BedrockBlock extends BlockState
{
    public static final BedrockBlock INSTANCE = new BedrockBlock();

    private BedrockBlock() {}

    @Override
    public void accept(BlockStateVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getDisplayName() {
        return "Bedrock";
    }

    @Override
    public String getTexture(BlockFace blockFace) {
        return "ProgrammerArt-v3.0/textures/blocks/bedrock.png";
    }
}