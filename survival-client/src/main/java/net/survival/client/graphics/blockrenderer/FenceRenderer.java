package net.survival.client.graphics.blockrenderer;

import net.survival.client.graphics.opengl.GLDisplayList;

class FenceRenderer extends BlockRenderer
{
    private static final float MODEL_WIDTH = 0.25f;
    private static final float PADDING = (1.0f - MODEL_WIDTH) / 2.0f;

    public FenceRenderer() {
        super(false);
    }

    @Override
    public void pushTopFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);

        float left  = x        + PADDING;
        float right = x + 1.0f - PADDING;
        float back  = z        + PADDING;
        float front = z + 1.0f - PADDING;

        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y + 1.0f, front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y + 1.0f, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, back );
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y + 1.0f, back );
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y + 1.0f, front);
    }

    @Override
    public void pushBottomFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);

        float left  = x        + PADDING;
        float right = x + 1.0f - PADDING;
        float back  = z        + PADDING;
        float front = z + 1.0f - PADDING;

        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y, back );
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y, back );
    }

    @Override
    public void pushLeftFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);

        float left  = x        + PADDING;
        float back  = z        + PADDING;
        float front = z + 1.0f - PADDING;

        builder.setTexCoord(u1, v1); builder.pushVertex(left, y,        back );
        builder.setTexCoord(u2, v1); builder.pushVertex(left, y,        front);
        builder.setTexCoord(u2, v2); builder.pushVertex(left, y + 1.0f, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(left, y + 1.0f, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left, y + 1.0f, back );
        builder.setTexCoord(u1, v1); builder.pushVertex(left, y,        back );
    }

    @Override
    public void pushRightFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);

        float right = x + 1.0f - PADDING;
        float back  = z        + PADDING;
        float front = z + 1.0f - PADDING;

        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,        front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y,        back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, back );
        builder.setTexCoord(u1, v2); builder.pushVertex(right, y + 1.0f, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,        front);
    }

    @Override
    public void pushFrontFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);

        float left  = x        + PADDING;
        float right = x + 1.0f - PADDING;
        float front = z + 1.0f - PADDING;

        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,        front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y,        front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y + 1.0f, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,        front);
    }

    @Override
    public void pushBackFaces(int x, int y, int z, short blockID,
            short adjacentBlockID, GLDisplayList.Builder builder)
    {
        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);

        float left  = x        + PADDING;
        float right = x + 1.0f - PADDING;
        float back  = z        + PADDING;

        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,        back);
        builder.setTexCoord(u2, v1); builder.pushVertex(left,  y,        back);
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + 1.0f, back);
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + 1.0f, back);
        builder.setTexCoord(u1, v2); builder.pushVertex(right, y + 1.0f, back);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,        back);
    }
}