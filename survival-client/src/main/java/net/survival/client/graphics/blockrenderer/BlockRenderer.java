package net.survival.client.graphics.blockrenderer;

import net.survival.block.BlockFace;
import net.survival.block.BlockModel;
import net.survival.block.BlockType;
import net.survival.client.graphics.BlockTextureAtlas;
import net.survival.client.graphics.opengl.GLDisplayList;

public abstract class BlockRenderer
{
    // TODO: Make private, and create accessor methods.
    public static BlockTextureAtlas topFaceTextures = new BlockTextureAtlas(BlockFace.TOP);
    public static BlockTextureAtlas bottomFaceTextures = new BlockTextureAtlas(BlockFace.BOTTOM);
    public static BlockTextureAtlas leftFaceTextures = new BlockTextureAtlas(BlockFace.LEFT);
    public static BlockTextureAtlas rightFaceTextures = new BlockTextureAtlas(BlockFace.RIGHT);
    public static BlockTextureAtlas frontFaceTextures = new BlockTextureAtlas(BlockFace.FRONT);
    public static BlockTextureAtlas backFaceTextures = new BlockTextureAtlas(BlockFace.BACK);
    private static boolean texturesInit = false;

    private static final BlockRenderer[] blockRenderers = new BlockRenderer[BlockModel.getCachedValues().length];
    private static final BlockRenderer defaultBlockRenderer = new DefaultBlockRenderer();

    static {
        for (int i = 0; i < blockRenderers.length; ++i)
            blockRenderers[i] = defaultBlockRenderer;

        blockRenderers[BlockModel.INVISIBLE.id] = new InvisibleBlockRenderer();
        blockRenderers[BlockModel.FENCE.id] = new FenceRenderer();
        blockRenderers[BlockModel.SAPLING.id] = new SaplingRenderer();
    }

    public static final void initTextures() {
        if (!texturesInit) {
            topFaceTextures = new BlockTextureAtlas(BlockFace.TOP);
            bottomFaceTextures = new BlockTextureAtlas(BlockFace.BOTTOM);
            leftFaceTextures = new BlockTextureAtlas(BlockFace.LEFT);
            rightFaceTextures = new BlockTextureAtlas(BlockFace.RIGHT);
            frontFaceTextures = new BlockTextureAtlas(BlockFace.FRONT);
            backFaceTextures = new BlockTextureAtlas(BlockFace.BACK);
            texturesInit = true;
        }
    }

    public final boolean nonCubic;

    public BlockRenderer(boolean nonCubic) {
        this.nonCubic = nonCubic;
    }

    public static BlockRenderer byBlockID(short blockID) {
        return blockRenderers[BlockType.byID(blockID).getModel().id];
    }

    public void pushNonCubic(int x, int y, int z, short blockID, GLDisplayList.Builder builder) {}

    public void pushTopFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {}

    public void pushBottomFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {}

    public void pushLeftFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {}

    public void pushRightFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {}

    public void pushFrontFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {}

    public void pushBackFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {}
}