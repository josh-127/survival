package net.survival.client.graphics.blockrenderer;

import net.survival.blocktype.BlockID;
import net.survival.client.graphics.opengl.GLDisplayList;

class DefaultBlockRenderer extends BlockRenderer
{
    public DefaultBlockRenderer() {
        super(false);
    }

    @Override
    public void pushTopFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        int typeID = BlockID.typeIDFromFullID(blockID);
        if (blockToBlockingBottomTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float u1 = topFaceTextures.getTexCoordU1(typeID);
        float u2 = topFaceTextures.getTexCoordU2(typeID);
        float v1 = topFaceTextures.getTexCoordV1(typeID);
        float v2 = topFaceTextures.getTexCoordV2(typeID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z       );
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
    }

    @Override
    public void pushBottomFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        int typeID = BlockID.typeIDFromFullID(blockID);
        if (blockToBlockingTopTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float u1 = bottomFaceTextures.getTexCoordU1(typeID);
        float u2 = bottomFaceTextures.getTexCoordU2(typeID);
        float v1 = bottomFaceTextures.getTexCoordV1(typeID);
        float v2 = bottomFaceTextures.getTexCoordV2(typeID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
    }

    @Override
    public void pushLeftFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        int typeID = BlockID.typeIDFromFullID(blockID);
        if (blockToBlockingRightTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float u1 = leftFaceTextures.getTexCoordU1(typeID);
        float u2 = leftFaceTextures.getTexCoordU2(typeID);
        float v1 = leftFaceTextures.getTexCoordV1(typeID);
        float v2 = leftFaceTextures.getTexCoordV2(typeID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
        builder.setTexCoord(u2, v1); builder.pushVertex(x, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x, y + 1.0f, z       );
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
    }

    @Override
    public void pushRightFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        int typeID = BlockID.typeIDFromFullID(blockID);
        if (blockToBlockingLeftTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float u1 = rightFaceTextures.getTexCoordU1(typeID);
        float u2 = rightFaceTextures.getTexCoordU2(typeID);
        float v1 = rightFaceTextures.getTexCoordV1(typeID);
        float v2 = rightFaceTextures.getTexCoordV2(typeID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
    }

    @Override
    public void pushFrontFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        int typeID = BlockID.typeIDFromFullID(blockID);
        if (blockToBlockingBackTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float u1 = frontFaceTextures.getTexCoordU1(typeID);
        float u2 = frontFaceTextures.getTexCoordU2(typeID);
        float v1 = frontFaceTextures.getTexCoordV1(typeID);
        float v2 = frontFaceTextures.getTexCoordV2(typeID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
    }

    @Override
    public void pushBackFaces(int x, int y, int z, int blockID, int adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        int typeID = BlockID.typeIDFromFullID(blockID);
        if (blockToBlockingFrontTable[BlockID.typeIDFromFullID(adjacentBlockID)])
            return;

        float u1 = backFaceTextures.getTexCoordU1(typeID);
        float u2 = backFaceTextures.getTexCoordU2(typeID);
        float v1 = backFaceTextures.getTexCoordV1(typeID);
        float v2 = backFaceTextures.getTexCoordV2(typeID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
        builder.setTexCoord(u2, v1); builder.pushVertex(x,        y,        z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
    }
}