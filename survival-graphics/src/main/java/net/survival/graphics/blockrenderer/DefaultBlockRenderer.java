package net.survival.graphics.blockrenderer;

import net.survival.blocktype.BlockFace;
import net.survival.blocktype.BlockId;
import net.survival.graphics.opengl.GLDisplayList;

class DefaultBlockRenderer extends BlockRenderer
{
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
            GLDisplayList.Builder builder)
    {
        var blockTypeId = BlockId.typeIdFromFullId(blockId);
        var topAdjacentBlockTypeId = BlockId.typeIdFromFullId(topAdjacentBlockId);
        var bottomAdjacentBlockTypeId = BlockId.typeIdFromFullId(bottomAdjacentBlockId);
        var leftAdjacentBlockTypeId = BlockId.typeIdFromFullId(leftAdjacentBlockId);
        var rightAdjacentBlockTypeId = BlockId.typeIdFromFullId(rightAdjacentBlockId);
        var frontAdjacentBlockTypeId = BlockId.typeIdFromFullId(frontAdjacentBlockId);
        var backAdjacentBlockTypeId = BlockId.typeIdFromFullId(backAdjacentBlockId);

        if (!blockToBlockingBottomTable[topAdjacentBlockTypeId]) {
            var u1 = textures.getTexCoordU1(blockTypeId, BlockFace.TOP);
            var u2 = textures.getTexCoordU2(blockTypeId, BlockFace.TOP);
            var v1 = textures.getTexCoordV1(blockTypeId, BlockFace.TOP);
            var v2 = textures.getTexCoordV2(blockTypeId, BlockFace.TOP);

            builder.setColor(TOP_FACE_SHADE, TOP_FACE_SHADE, TOP_FACE_SHADE);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z       );
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        }

        if (!blockToBlockingTopTable[bottomAdjacentBlockTypeId]) {
            var u1 = textures.getTexCoordU1(blockTypeId, BlockFace.BOTTOM);
            var u2 = textures.getTexCoordU2(blockTypeId, BlockFace.BOTTOM);
            var v1 = textures.getTexCoordV1(blockTypeId, BlockFace.BOTTOM);
            var v2 = textures.getTexCoordV2(blockTypeId, BlockFace.BOTTOM);

            builder.setColor(BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y, z + 1.0f);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
        }

        if (!blockToBlockingRightTable[leftAdjacentBlockTypeId]) {
            var u1 = textures.getTexCoordU1(blockTypeId, BlockFace.LEFT);
            var u2 = textures.getTexCoordU2(blockTypeId, BlockFace.LEFT);
            var v1 = textures.getTexCoordV1(blockTypeId, BlockFace.LEFT);
            var v2 = textures.getTexCoordV2(blockTypeId, BlockFace.LEFT);

            builder.setColor(LEFT_FACE_SHADE, LEFT_FACE_SHADE, LEFT_FACE_SHADE);
            builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
            builder.setTexCoord(u2, v1); builder.pushVertex(x, y,        z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u1, v2); builder.pushVertex(x, y + 1.0f, z       );
            builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
        }

        if (!blockToBlockingLeftTable[rightAdjacentBlockTypeId]) {
            var u1 = textures.getTexCoordU1(blockTypeId, BlockFace.RIGHT);
            var u2 = textures.getTexCoordU2(blockTypeId, BlockFace.RIGHT);
            var v1 = textures.getTexCoordV1(blockTypeId, BlockFace.RIGHT);
            var v2 = textures.getTexCoordV2(blockTypeId, BlockFace.RIGHT);

            builder.setColor(RIGHT_FACE_SHADE, RIGHT_FACE_SHADE, RIGHT_FACE_SHADE);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        }

        if (!blockToBlockingBackTable[frontAdjacentBlockTypeId]) {
            var u1 = textures.getTexCoordU1(blockTypeId, BlockFace.FRONT);
            var u2 = textures.getTexCoordU2(blockTypeId, BlockFace.FRONT);
            var v1 = textures.getTexCoordV1(blockTypeId, BlockFace.FRONT);
            var v2 = textures.getTexCoordV2(blockTypeId, BlockFace.FRONT);

            builder.setColor(FRONT_FACE_SHADE, FRONT_FACE_SHADE, FRONT_FACE_SHADE);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
        }

        if (!blockToBlockingFrontTable[backAdjacentBlockTypeId]) {
            var u1 = textures.getTexCoordU1(blockTypeId, BlockFace.BACK);
            var u2 = textures.getTexCoordU2(blockTypeId, BlockFace.BACK);
            var v1 = textures.getTexCoordV1(blockTypeId, BlockFace.BACK);
            var v2 = textures.getTexCoordV2(blockTypeId, BlockFace.BACK);

            builder.setColor(BACK_FACE_SHADE, BACK_FACE_SHADE, BACK_FACE_SHADE);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
            builder.setTexCoord(u2, v1); builder.pushVertex(x,        y,        z);
            builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
            builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
            builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
        }
    }
}