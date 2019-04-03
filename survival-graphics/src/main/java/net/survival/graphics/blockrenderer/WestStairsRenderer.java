package net.survival.graphics.blockrenderer;

import net.survival.blocktype.BlockId;
import net.survival.graphics.opengl.GLDisplayList.Builder;

class WestStairsRenderer extends DefaultBlockRenderer
{
    @Override
    public void pushTopFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            Builder builder)
    {
        var u1 = topFaceTextures.getTexCoordU1(blockId);
        var u2 = topFaceTextures.getTexCoordU2(blockId);
        var v1 = topFaceTextures.getTexCoordV1(blockId);
        var v2 = topFaceTextures.getTexCoordV2(blockId);

        if (!blockToBlockingBottomTable[BlockId.typeIdFromFullId(adjacentBlockId)]) {
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 0.5f, y + 1.0f, z + 1.0f);
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 0.5f, y + 1.0f, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 0.5f, y + 1.0f, z       );
            builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z       );
            builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        }

        builder.setTexCoord(u1, v1); builder.pushVertex(x + 0.5f, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 0.5f, y + 0.5f, z       );
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 0.5f, y + 0.5f, z + 1.0f);
    }

    @Override
    public void pushRightFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            Builder builder)
    {
        var u1 = rightFaceTextures.getTexCoordU1(blockId);
        var u2 = rightFaceTextures.getTexCoordU2(blockId);
        var v1 = rightFaceTextures.getTexCoordV1(blockId);
        var v2 = rightFaceTextures.getTexCoordV2(blockId);

        if (!blockToBlockingLeftTable[BlockId.typeIdFromFullId(adjacentBlockId)]) {
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
            builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z       );
            builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z       );
            builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 1.0f);
            builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        }

        builder.setTexCoord(u1, v1); builder.pushVertex(x + 0.5f, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 0.5f, y + 0.5f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 0.5f, y + 1.0f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 0.5f, y + 1.0f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 0.5f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 0.5f, y + 0.5f, z + 1.0f);
    }

    @Override
    public void pushFrontFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            Builder builder)
    {
        if (blockToBlockingBackTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var u1 = frontFaceTextures.getTexCoordU1(blockId);
        var u2 = frontFaceTextures.getTexCoordU2(blockId);
        var v1 = frontFaceTextures.getTexCoordV1(blockId);
        var v2 = frontFaceTextures.getTexCoordV2(blockId);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 0.5f, y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 0.5f, y + 0.5f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 0.5f, y,        z + 1.0f);

        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 0.5f, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 0.5f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 0.5f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
    }

    @Override
    public void pushBackFaces(int x, int y, int z, int blockId, int adjacentBlockId,
            Builder builder)
    {
        if (blockToBlockingFrontTable[BlockId.typeIdFromFullId(adjacentBlockId)])
            return;

        var u1 = backFaceTextures.getTexCoordU1(blockId);
        var u2 = backFaceTextures.getTexCoordU2(blockId);
        var v1 = backFaceTextures.getTexCoordV1(blockId);
        var v2 = backFaceTextures.getTexCoordV2(blockId);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 0.5f, y,        z);
        builder.setTexCoord(u2, v1); builder.pushVertex(x,        y,        z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 0.5f, y + 1.0f, z);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 0.5f, y,        z);

        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 0.5f, y,        z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 0.5f, y + 0.5f, z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 0.5f, y + 0.5f, z);
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 0.5f, z);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
    }
}