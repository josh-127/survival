package net.survival.client.graphics.blockrenderer;

import net.survival.blocktype.BlockId;
import net.survival.client.graphics.opengl.GLDisplayList;

class PressurePlateRenderer extends BlockRenderer
{
    private static final float PADDING = 0.0625f;

    private final float height;

    public PressurePlateRenderer(float height) {
        super(false);

        this.height = height;
    }

    @Override
    public void pushTopFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingBottomTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var left = x + PADDING;
        var right = x + 1.0f - PADDING;
        var back = z + PADDING;
        var front = z + 1.0f - PADDING;
        var u1 = topFaceTextures.getTexCoordU1(blockId);
        var u2 = topFaceTextures.getTexCoordU2(blockId);
        var v1 = topFaceTextures.getTexCoordV1(blockId);
        var v2 = topFaceTextures.getTexCoordV2(blockId);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y + height, front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y + height, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, back );
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y + height, back );
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y + height, front);
    }

    @Override
    public void pushBottomFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingTopTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var left = x + PADDING;
        var right = x + 1.0f - PADDING;
        var back = z + PADDING;
        var front = z + 1.0f - PADDING;
        var u1 = bottomFaceTextures.getTexCoordU1(blockId);
        var u2 = bottomFaceTextures.getTexCoordU2(blockId);
        var v1 = bottomFaceTextures.getTexCoordV1(blockId);
        var v2 = bottomFaceTextures.getTexCoordV2(blockId);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y, back );
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y, back );
    }

    @Override
    public void pushLeftFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingRightTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var left = x + PADDING;
        var back = z + PADDING;
        var front = z + 1.0f - PADDING;
        var u1 = leftFaceTextures.getTexCoordU1(blockId);
        var u2 = leftFaceTextures.getTexCoordU2(blockId);
        var v1 = leftFaceTextures.getTexCoordV1(blockId);
        var v2 = leftFaceTextures.getTexCoordV2(blockId);
        builder.setTexCoord(u1, v1); builder.pushVertex(left, y,          back );
        builder.setTexCoord(u2, v1); builder.pushVertex(left, y,          front);
        builder.setTexCoord(u2, v2); builder.pushVertex(left, y + height, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(left, y + height, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left, y + height, back );
        builder.setTexCoord(u1, v1); builder.pushVertex(left, y,          back );
    }

    @Override
    public void pushRightFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingLeftTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var right = x + 1.0f - PADDING;
        var back = z + PADDING;
        var front = z + 1.0f - PADDING;
        var u1 = rightFaceTextures.getTexCoordU1(blockId);
        var u2 = rightFaceTextures.getTexCoordU2(blockId);
        var v1 = rightFaceTextures.getTexCoordV1(blockId);
        var v2 = rightFaceTextures.getTexCoordV2(blockId);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,          front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y,          back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, back );
        builder.setTexCoord(u1, v2); builder.pushVertex(right, y + height, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,          front);
    }

    @Override
    public void pushFrontFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingBackTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var left = x + PADDING;
        var right = x + 1.0f - PADDING;
        var front = z + 1.0f - PADDING;
        var u1 = frontFaceTextures.getTexCoordU1(blockId);
        var u2 = frontFaceTextures.getTexCoordU2(blockId);
        var v1 = frontFaceTextures.getTexCoordV1(blockId);
        var v2 = frontFaceTextures.getTexCoordV2(blockId);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,          front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y,          front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y + height, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,          front);
    }

    @Override
    public void pushBackFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingFrontTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var left = x + PADDING;
        var right = x + 1.0f - PADDING;
        var back = z + PADDING;
        var u1 = backFaceTextures.getTexCoordU1(blockId);
        var u2 = backFaceTextures.getTexCoordU2(blockId);
        var v1 = backFaceTextures.getTexCoordV1(blockId);
        var v2 = backFaceTextures.getTexCoordV2(blockId);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,          back);
        builder.setTexCoord(u2, v1); builder.pushVertex(left,  y,          back);
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + height, back);
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + height, back);
        builder.setTexCoord(u1, v2); builder.pushVertex(right, y + height, back);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,          back);
    }
}