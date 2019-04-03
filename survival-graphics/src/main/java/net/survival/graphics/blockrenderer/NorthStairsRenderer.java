package net.survival.graphics.blockrenderer;

import net.survival.blocktype.BlockId;
import net.survival.graphics.opengl.GLDisplayList.Builder;

class NorthStairsRenderer extends DefaultBlockRenderer
{
    @Override
    public void pushTopFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            Builder builder)
    {
        var typeId = BlockId.typeIdFromFullId(blockId);

        var u1 = topFaceTextures.getTexCoordU1(typeId);
        var u2 = topFaceTextures.getTexCoordU2(typeId);
        var v1 = topFaceTextures.getTexCoordV1(typeId);
        var v2 = topFaceTextures.getTexCoordV2(typeId);

        if (!blockToBlockingBottomTable[BlockId.typeIdFromFullId(adjacentBlockId)]) {
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 0.5f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 1.0f, z + 0.5f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z       );
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 0.5f);
        }

        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 0.5f, z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 0.5f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 0.5f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 0.5f, z + 0.5f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 0.5f, z + 1.0f);
    }

    @Override
    public void pushLeftFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            Builder builder)
    {
        if (blockToBlockingRightTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var typeId = BlockId.typeIdFromFullId(blockId);

        var u1 = leftFaceTextures.getTexCoordU1(typeId);
        var u2 = leftFaceTextures.getTexCoordU2(typeId);
        var v1 = leftFaceTextures.getTexCoordV1(typeId);
        var v2 = leftFaceTextures.getTexCoordV2(typeId);
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
        builder.setTexCoord(u2, v1); builder.pushVertex(x, y,        z + 0.5f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 0.5f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 0.5f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x, y + 1.0f, z       );
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );

        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z + 0.5f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x, y + 0.5f, z + 0.5f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z + 0.5f);
    }

    @Override
    public void pushRightFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            Builder builder)
    {
        if (blockToBlockingLeftTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var typeId = BlockId.typeIdFromFullId(blockId);

        var u1 = rightFaceTextures.getTexCoordU1(typeId);
        var u2 = rightFaceTextures.getTexCoordU2(typeId);
        var v1 = rightFaceTextures.getTexCoordV1(typeId);
        var v2 = rightFaceTextures.getTexCoordV2(typeId);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 0.5f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 0.5f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 0.5f);

        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z + 0.5f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 0.5f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 0.5f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
    }

    @Override
    public void pushFrontFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            Builder builder)
    {
        var typeId = BlockId.typeIdFromFullId(blockId);

        var u1 = frontFaceTextures.getTexCoordU1(typeId);
        var u2 = frontFaceTextures.getTexCoordU2(typeId);
        var v1 = frontFaceTextures.getTexCoordV1(typeId);
        var v2 = frontFaceTextures.getTexCoordV2(typeId);

        if (!blockToBlockingBackTable[BlockId.typeIdFromFullId(adjacentBlockId)]) {
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 1.0f);
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 0.5f, z + 1.0f);
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
        }

        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 0.5f, z + 0.5f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 0.5f, z + 0.5f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 0.5f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 0.5f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z + 0.5f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 0.5f, z + 0.5f);
    }
}