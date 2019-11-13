package net.survival.graphics.blockrenderer;

import net.survival.block.state.BlockFace;
import net.survival.block.state.BlockState;
import net.survival.graphics.Assets;
import net.survival.graphics.opengl.GLDisplayList;

class SaplingRenderer extends BlockRenderer
{
    private static final float PADDING = 0.125f;

    @Override
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
    {
        var textureAtlas = Assets.getMipmappedTextureAtlas();

        var texture = block.getTexture(BlockFace.TOP);
        var u1 = textureAtlas.getTexCoordU1(texture);
        var u2 = textureAtlas.getTexCoordU2(texture);
        var v1 = textureAtlas.getTexCoordV1(texture);
        var v2 = textureAtlas.getTexCoordV2(texture);

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