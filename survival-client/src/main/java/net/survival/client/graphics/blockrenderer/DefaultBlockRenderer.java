package net.survival.client.graphics.blockrenderer;

import net.survival.block.BlockModel;
import net.survival.block.BlockType;
import net.survival.client.graphics.opengl.GLDisplayList;

class DefaultBlockRenderer extends BlockRenderer
{
    public DefaultBlockRenderer() {
        super(false);
    }

    @Override
    public void pushTopFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (BlockType.byID(adjacentBlockID).getModel() == BlockModel.DEFAULT)
            return;

        float u1 = topFaceTextures.getTexCoordU1(blockID);
        float u2 = topFaceTextures.getTexCoordU2(blockID);
        float v1 = topFaceTextures.getTexCoordV1(blockID);
        float v2 = topFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z       );
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
    }

    @Override
    public void pushBottomFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (BlockType.byID(adjacentBlockID).getModel() == BlockModel.DEFAULT)
            return;

        float u1 = bottomFaceTextures.getTexCoordU1(blockID);
        float u2 = bottomFaceTextures.getTexCoordU2(blockID);
        float v1 = bottomFaceTextures.getTexCoordV1(blockID);
        float v2 = bottomFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
    }

    @Override
    public void pushLeftFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (BlockType.byID(adjacentBlockID).getModel() == BlockModel.DEFAULT)
            return;

        float u1 = leftFaceTextures.getTexCoordU1(blockID);
        float u2 = leftFaceTextures.getTexCoordU2(blockID);
        float v1 = leftFaceTextures.getTexCoordV1(blockID);
        float v2 = leftFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
        builder.setTexCoord(u2, v1); builder.pushVertex(x, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x, y + 1.0f, z       );
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
    }

    @Override
    public void pushRightFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (BlockType.byID(adjacentBlockID).getModel() == BlockModel.DEFAULT)
            return;

        float u1 = rightFaceTextures.getTexCoordU1(blockID);
        float u2 = rightFaceTextures.getTexCoordU2(blockID);
        float v1 = rightFaceTextures.getTexCoordV1(blockID);
        float v2 = rightFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
    }

    @Override
    public void pushFrontFaces(int x, int y, int z, short blockID, short adjacentBlockID,
            GLDisplayList.Builder builder)
    {
        if (BlockType.byID(adjacentBlockID).getModel() == BlockModel.DEFAULT)
            return;

        float u1 = frontFaceTextures.getTexCoordU1(blockID);
        float u2 = frontFaceTextures.getTexCoordU2(blockID);
        float v1 = frontFaceTextures.getTexCoordV1(blockID);
        float v2 = frontFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
    }

    @Override
    public void pushBackFaces(int x, int y, int z, short blockID,
            short adjacentBlockID, GLDisplayList.Builder builder)
    {
        if (BlockType.byID(adjacentBlockID).getModel() == BlockModel.DEFAULT)
            return;

        float u1 = backFaceTextures.getTexCoordU1(blockID);
        float u2 = backFaceTextures.getTexCoordU2(blockID);
        float v1 = backFaceTextures.getTexCoordV1(blockID);
        float v2 = backFaceTextures.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
        builder.setTexCoord(u2, v1); builder.pushVertex(x,        y,        z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
    }
}