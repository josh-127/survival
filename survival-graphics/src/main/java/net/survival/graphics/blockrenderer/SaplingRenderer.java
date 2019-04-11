package net.survival.graphics.blockrenderer;

import net.survival.blocktype.BlockFace;
import net.survival.blocktype.BlockId;
import net.survival.graphics.opengl.GLDisplayList;

class SaplingRenderer extends BlockRenderer
{
    private static final float PADDING = 0.125f;

    @Override
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
    {
        var blockTypeId = BlockId.typeIdFromFullId(blockId);

        var u1 = textures.getTexCoordU1(blockTypeId, BlockFace.TOP);
        var u2 = textures.getTexCoordU2(blockTypeId, BlockFace.TOP);
        var v1 = textures.getTexCoordV1(blockTypeId, BlockFace.TOP);
        var v2 = textures.getTexCoordV2(blockTypeId, BlockFace.TOP);

        var left  = x        + PADDING;
        var right = x + 1.0f - PADDING;
        var back  = z        + PADDING;
        var front = z + 1.0f - PADDING;

        setShade(shadeFactor, builder);
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