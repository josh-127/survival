package net.survival.client.graphics.blockrenderer;

import net.survival.blocktype.BlockId;
import net.survival.client.graphics.opengl.GLDisplayList;

class SaplingRenderer extends BlockRenderer
{
    private static final float PADDING = 0.125f;

    public SaplingRenderer() {
        super(true);
    }

    @Override
    public void pushNonCubic(int x, int y, int z, int blockId, GLDisplayList.Builder builder) {
        var typeId = BlockId.typeIdFromFullId(blockId);

        var u1 = topFaceTextures.getTexCoordU1(typeId);
        var u2 = topFaceTextures.getTexCoordU2(typeId);
        var v1 = topFaceTextures.getTexCoordV1(typeId);
        var v2 = topFaceTextures.getTexCoordV2(typeId);

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