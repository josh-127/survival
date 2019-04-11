package net.survival.graphics.blockrenderer;

import net.survival.blocktype.BlockId;
import net.survival.blocktype.BlockModel;
import net.survival.blocktype.BlockType;
import net.survival.graphics.BlockTextureAtlas;
import net.survival.graphics.opengl.GLDisplayList;

public abstract class BlockRenderer
{
    protected static final float NON_CUBIC_SHADE = 1.0f;
    protected static final float TOP_FACE_SHADE = 1.0f;
    protected static final float BOTTOM_FACE_SHADE = 0.25f;
    protected static final float LEFT_FACE_SHADE = 0.5f;
    protected static final float RIGHT_FACE_SHADE = 0.5f;
    protected static final float FRONT_FACE_SHADE = 0.75f;
    protected static final float BACK_FACE_SHADE = 0.75f;

    // TODO: Make private, and create accessor methods.
    public static BlockTextureAtlas textures = new BlockTextureAtlas();
    private static boolean texturesInit = false;

    private static final BlockRenderer[] blockRenderers = new BlockRenderer[BlockModel.getCachedValues().length];
    private static final BlockRenderer defaultBlockRenderer = new DefaultBlockRenderer();

    protected static final boolean[] blockToBlockingTopTable = new boolean[BlockType.getAllBlocks().length];
    protected static final boolean[] blockToBlockingBottomTable = new boolean[BlockType.getAllBlocks().length];
    protected static final boolean[] blockToBlockingLeftTable = new boolean[BlockType.getAllBlocks().length];
    protected static final boolean[] blockToBlockingRightTable = new boolean[BlockType.getAllBlocks().length];
    protected static final boolean[] blockToBlockingFrontTable = new boolean[BlockType.getAllBlocks().length];
    protected static final boolean[] blockToBlockingBackTable = new boolean[BlockType.getAllBlocks().length];

    static {
        for (var i = 0; i < blockRenderers.length; ++i)
            blockRenderers[i] = defaultBlockRenderer;

        blockRenderers[BlockModel.INVISIBLE.id] = new InvisibleBlockRenderer();
        //blockRenderers[BlockModel.FENCE.id] = new FenceRenderer();
        blockRenderers[BlockModel.SAPLING.id] = new SaplingRenderer();
        //blockRenderers[BlockModel.BOTTOM_SLAB.id] = new BottomSlabRenderer();
        //blockRenderers[BlockModel.TOP_SLAB.id] = new TopSlabRenderer();
        //blockRenderers[BlockModel.NORTH_STAIRS.id] = new NorthStairsRenderer();
        //blockRenderers[BlockModel.SOUTH_STAIRS.id] = new SouthStairsRenderer();
        //blockRenderers[BlockModel.EAST_STAIRS.id] = new EastStairsRenderer();
        //blockRenderers[BlockModel.WEST_STAIRS.id] = new WestStairsRenderer();
        //blockRenderers[BlockModel.NORTH_CEILING_STAIRS.id] = new NorthCeilingStairsRenderer();
        //blockRenderers[BlockModel.SOUTH_CEILING_STAIRS.id] = new SouthCeilingStairsRenderer();
        //blockRenderers[BlockModel.EAST_CEILING_STAIRS.id] = new EastCeilingStairsRenderer();
        //blockRenderers[BlockModel.WEST_CEILING_STAIRS.id] = new WestCeilingStairsRenderer();
        //blockRenderers[BlockModel.FARMLAND.id] = new FarmlandRenderer();
        //blockRenderers[BlockModel.PRESSURE_PLATE_OFF.id] = new PressurePlateRenderer(0.0625f);
        //blockRenderers[BlockModel.PRESSURE_PLATE_ON.id] = new PressurePlateRenderer(0.03125f);

        for (var i = 0; i < blockToBlockingTopTable.length; ++i) {
            var block = BlockType.getAllBlocks()[i];
            if (block != null)
                blockToBlockingTopTable[i] = block.getModel().isBlockingTop();
        }

        for (var i = 0; i < blockToBlockingBottomTable.length; ++i) {
            var block = BlockType.getAllBlocks()[i];
            if (block != null)
                blockToBlockingBottomTable[i] = block.getModel().isBlockingBottom();
        }

        for (var i = 0; i < blockToBlockingLeftTable.length; ++i) {
            var block = BlockType.getAllBlocks()[i];
            if (block != null)
                blockToBlockingLeftTable[i] = block.getModel().isBlockingLeft();
        }

        for (var i = 0; i < blockToBlockingRightTable.length; ++i) {
            var block = BlockType.getAllBlocks()[i];
            if (block != null)
                blockToBlockingRightTable[i] = block.getModel().isBlockingRight();
        }

        for (var i = 0; i < blockToBlockingFrontTable.length; ++i) {
            var block = BlockType.getAllBlocks()[i];
            if (block != null)
                blockToBlockingFrontTable[i] = block.getModel().isBlockingFront();
        }

        for (var i = 0; i < blockToBlockingBackTable.length; ++i) {
            var block = BlockType.getAllBlocks()[i];
            if (block != null)
                blockToBlockingBackTable[i] = block.getModel().isBlockingBack();
        }
    }

    public static final void initTextures() {
        if (!texturesInit) {
            textures = new BlockTextureAtlas();
            texturesInit = true;
        }
    }

    public static BlockRenderer byFullId(int blockId) {
        return blockRenderers[BlockType.getAllBlocks()[BlockId.typeIdFromFullId(blockId)].getModel().id];
    }

    public void pushVertices(
            int x,
            int y,
            int z,
            int blockId,
            int topAdjacentBlockId,
            int bottomAdjacentBlockId,
            int leftAdjacentBlockId,
            int rightAdjacentBlockId,
            int frontAdjacentBlockId,
            int backAdjacentBlockId,
            float shadeFactor,
            GLDisplayList.Builder builder)
    {}

    protected void setShade(float shade, GLDisplayList.Builder builder) {
        builder.setColor(shade, shade, shade);
    }
}