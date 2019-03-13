package net.survival.client.graphics.blockrenderer;

import net.survival.blocktype.BlockID;
import net.survival.client.graphics.opengl.GLDisplayList;

class SaplingRenderer extends BlockRenderer
{
    private static final float PADDING = 0.125f;

    public SaplingRenderer() {
        super(true);
    }

    @Override
    public void pushNonCubic(int x, int y, int z, int blockID, GLDisplayList.Builder builder) {
        var typeID = BlockID.typeIDFromFullID(blockID);

        var u1 = topFaceTextures.getTexCoordU1(typeID);
        var u2 = topFaceTextures.getTexCoordU2(typeID);
        var v1 = topFaceTextures.getTexCoordV1(typeID);
        var v2 = topFaceTextures.getTexCoordV2(typeID);

        var left  = x        + PADDING;
        var right = x + 1.0f - PADDING;
        var back  = z        + PADDING;
        var front = z + 1.0f - PADDING;

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