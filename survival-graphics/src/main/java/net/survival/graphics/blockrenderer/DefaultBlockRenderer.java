package net.survival.graphics.blockrenderer;

import net.survival.block.state.BlockFace;
import net.survival.block.state.BlockState;
import net.survival.graphics.Assets;
import net.survival.graphics.opengl.GLDisplayList;

class DefaultBlockRenderer extends BlockRenderer
{
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

        if (!topAdjBlock.getModel().isBlockingBottom()) {
            var texture = block.getTexture(BlockFace.TOP);
            var u1 = textureAtlas.getTexCoordU1(texture);
            var u2 = textureAtlas.getTexCoordU2(texture);
            var v1 = textureAtlas.getTexCoordV1(texture);
            var v2 = textureAtlas.getTexCoordV2(texture);

            setShade(TOP_FACE_SHADE * shadeFactor, builder);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z       );
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        }

        if (!bottomAdjBlock.getModel().isBlockingTop()) {
            var texture = block.getTexture(BlockFace.BOTTOM);
            var u1 = textureAtlas.getTexCoordU1(texture);
            var u2 = textureAtlas.getTexCoordU2(texture);
            var v1 = textureAtlas.getTexCoordV1(texture);
            var v2 = textureAtlas.getTexCoordV2(texture);

            setShade(BOTTOM_FACE_SHADE, builder);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y, z + 1.0f);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
        }

        if (!leftAdjBlock.getModel().isBlockingRight()) {
            var texture = block.getTexture(BlockFace.LEFT);
            var u1 = textureAtlas.getTexCoordU1(texture);
            var u2 = textureAtlas.getTexCoordU2(texture);
            var v1 = textureAtlas.getTexCoordV1(texture);
            var v2 = textureAtlas.getTexCoordV2(texture);

            setShade(LEFT_FACE_SHADE, builder);
            builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
            builder.setTexCoord(u2, v1); builder.pushVertex(x, y,        z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u1, v2); builder.pushVertex(x, y + 1.0f, z       );
            builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
        }

        if (!rightAdjBlock.getModel().isBlockingLeft()) {
            var texture = block.getTexture(BlockFace.RIGHT);
            var u1 = textureAtlas.getTexCoordU1(texture);
            var u2 = textureAtlas.getTexCoordU2(texture);
            var v1 = textureAtlas.getTexCoordV1(texture);
            var v2 = textureAtlas.getTexCoordV2(texture);

            setShade(RIGHT_FACE_SHADE, builder);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        }

        if (!frontAdjBlock.getModel().isBlockingBack()) {
            var texture = block.getTexture(BlockFace.FRONT);
            var u1 = textureAtlas.getTexCoordU1(texture);
            var u2 = textureAtlas.getTexCoordU2(texture);
            var v1 = textureAtlas.getTexCoordV1(texture);
            var v2 = textureAtlas.getTexCoordV2(texture);

            setShade(FRONT_FACE_SHADE, builder);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
        }

        if (!backAdjBlock.getModel().isBlockingFront()) {
            var texture = block.getTexture(BlockFace.BACK);
            var u1 = textureAtlas.getTexCoordU1(texture);
            var u2 = textureAtlas.getTexCoordU2(texture);
            var v1 = textureAtlas.getTexCoordV1(texture);
            var v2 = textureAtlas.getTexCoordV2(texture);

            setShade(BACK_FACE_SHADE, builder);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
            builder.setTexCoord(u2, v1); builder.pushVertex(x,        y,        z);
            builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
            builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
            builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
        }
    }
}