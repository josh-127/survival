package net.survival.client.graphics.blockrenderer;

import net.survival.block.BlockID;
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
    public void pushTopFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingBottomTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float left = x + PADDING;
        float right = x + 1.0f - PADDING;
        float back = z + PADDING;
        float front = z + 1.0f - PADDING;
        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y + height, front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y + height, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, back );
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y + height, back );
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y + height, front);
    }

    @Override
    public void pushBottomFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingTopTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float left = x + PADDING;
        float right = x + 1.0f - PADDING;
        float back = z + PADDING;
        float front = z + 1.0f - PADDING;
        float u1 = bottomFaceTextures.getTexCoordU1(blockID);
        float u2 = bottomFaceTextures.getTexCoordU2(blockID);
        float v1 = bottomFaceTextures.getTexCoordV1(blockID);
        float v2 = bottomFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y, back );
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y, back );
    }

    @Override
    public void pushLeftFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingRightTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float left = x + PADDING;
        float back = z + PADDING;
        float front = z + 1.0f - PADDING;
        float u1 = leftFaceTextures.getTexCoordU1(blockID);
        float u2 = leftFaceTextures.getTexCoordU2(blockID);
        float v1 = leftFaceTextures.getTexCoordV1(blockID);
        float v2 = leftFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(left, y,          back );
        builder.setTexCoord(u2, v1); builder.pushVertex(left, y,          front);
        builder.setTexCoord(u2, v2); builder.pushVertex(left, y + height, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(left, y + height, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left, y + height, back );
        builder.setTexCoord(u1, v1); builder.pushVertex(left, y,          back );
    }

    @Override
    public void pushRightFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingLeftTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float right = x + 1.0f - PADDING;
        float back = z + PADDING;
        float front = z + 1.0f - PADDING;
        float u1 = rightFaceTextures.getTexCoordU1(blockID);
        float u2 = rightFaceTextures.getTexCoordU2(blockID);
        float v1 = rightFaceTextures.getTexCoordV1(blockID);
        float v2 = rightFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,          front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y,          back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, back );
        builder.setTexCoord(u1, v2); builder.pushVertex(right, y + height, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,          front);
    }

    @Override
    public void pushFrontFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingBackTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float left = x + PADDING;
        float right = x + 1.0f - PADDING;
        float front = z + 1.0f - PADDING;
        float u1 = frontFaceTextures.getTexCoordU1(blockID);
        float u2 = frontFaceTextures.getTexCoordU2(blockID);
        float v1 = frontFaceTextures.getTexCoordV1(blockID);
        float v2 = frontFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,          front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y,          front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + height, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y + height, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,          front);
    }

    @Override
    public void pushBackFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (blockToBlockingFrontTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float left = x + PADDING;
        float right = x + 1.0f - PADDING;
        float back = z + PADDING;
        float u1 = backFaceTextures.getTexCoordU1(blockID);
        float u2 = backFaceTextures.getTexCoordU2(blockID);
        float v1 = backFaceTextures.getTexCoordV1(blockID);
        float v2 = backFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,          back);
        builder.setTexCoord(u2, v1); builder.pushVertex(left,  y,          back);
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + height, back);
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + height, back);
        builder.setTexCoord(u1, v2); builder.pushVertex(right, y + height, back);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,          back);
    }
}