package net.survival.graphics.blockrenderer;

import net.survival.block.state.BlockModel;
import net.survival.block.state.BlockState;
import net.survival.graphics.opengl.GLDisplayList;

public abstract class BlockRenderer {
    protected static final float NON_CUBIC_SHADE = 1.0f;
    protected static final float TOP_FACE_SHADE = 1.0f;
    protected static final float BOTTOM_FACE_SHADE = 0.25f;
    protected static final float LEFT_FACE_SHADE = 0.5f;
    protected static final float RIGHT_FACE_SHADE = 0.5f;
    protected static final float FRONT_FACE_SHADE = 0.75f;
    protected static final float BACK_FACE_SHADE = 0.75f;

    private static final BlockRenderer[] blockRenderers = new BlockRenderer[BlockModel.getCachedValues().length];
    private static final BlockRenderer defaultBlockRenderer = new DefaultBlockRenderer();

    static {
        for (var i = 0; i < blockRenderers.length; ++i)
            blockRenderers[i] = defaultBlockRenderer;

        blockRenderers[BlockModel.INVISIBLE.id] = new InvisibleBlockRenderer();
        blockRenderers[BlockModel.SAPLING.id] = new SaplingRenderer();
    }

    public static BlockRenderer byBlock(BlockState block) {
        return blockRenderers[block.getModel().id];
    }

    public void pushVertices(
            int x,
            int y,
            int z,
            BlockState block,
            BlockState topAdjBlock,
            BlockState bottomAdjBlock,
            BlockState leftAdjBlock,
            BlockState rightAdjBlock,
            BlockState frontAdjBlock,
            BlockState backAdjBlock,
            float shadeFactor,
            GLDisplayList.Builder builder)
    {}

    protected void setShade(float shade, GLDisplayList.Builder builder) {
        builder.setColor(shade, shade, shade);
    }
}