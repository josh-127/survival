package net.survival.client.graphics.blockrenderer;

import net.survival.client.graphics.opengl.GLDisplayList;

class SaplingRenderer extends BlockRenderer
{
    private static final float PADDING = 0.125f;

    public SaplingRenderer() {
        super(true);
    }

    @Override
    public void pushNonCubic(int x, int y, int z, int blockID, GLDisplayList.Builder builder) {
        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);

        float left  = x        + PADDING;
        float right = x + 1.0f - PADDING;
        float back  = z        + PADDING;
        float front = z + 1.0f - PADDING;

        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,        front);
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y,        back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, back );
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y + 1.0f, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,        front);

        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,        back );
        builder.setTexCoord(u2, v1); builder.pushVertex(left,  y,        front);
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + 1.0f, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + 1.0f, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(right, y + 1.0f, back );
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,        back );

        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,        back );
        builder.setTexCoord(u2, v1); builder.pushVertex(right, y,        front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, front);
        builder.setTexCoord(u2, v2); builder.pushVertex(right, y + 1.0f, front);
        builder.setTexCoord(u1, v2); builder.pushVertex(left,  y + 1.0f, back );
        builder.setTexCoord(u1, v1); builder.pushVertex(left,  y,        back );

        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,        front);
        builder.setTexCoord(u2, v1); builder.pushVertex(left,  y,        back );
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + 1.0f, back );
        builder.setTexCoord(u2, v2); builder.pushVertex(left,  y + 1.0f, back );
        builder.setTexCoord(u1, v2); builder.pushVertex(right, y + 1.0f, front);
        builder.setTexCoord(u1, v1); builder.pushVertex(right, y,        front);
    }
}