package net.survival.client.graphics;

import net.survival.block.BlockFace;
import net.survival.client.graphics.blockrenderer.BlockRenderer;
import net.survival.client.graphics.model.ModelRenderer;
import net.survival.client.graphics.model.StaticModel;
import net.survival.client.graphics.opengl.GLDisplayList;
import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.entity.Character;
import net.survival.world.chunk.Chunk;

class ChunkDisplay implements GraphicsResource
{
    private static final float NON_CUBIC_SHADE   = 1.0f;
    private static final float TOP_FACE_SHADE    = 1.0f;
    private static final float BOTTOM_FACE_SHADE = 0.25f;
    private static final float LEFT_FACE_SHADE   = 0.5f;
    private static final float RIGHT_FACE_SHADE  = 0.5f;
    private static final float FRONT_FACE_SHADE  = 0.75f;
    private static final float BACK_FACE_SHADE   = 0.75f;

    public final GLDisplayList displayList;
    public final int chunkX;
    public final int chunkZ;
    public final Chunk chunk;
    public final Chunk adjacentChunk;
    public final BlockFace blockFace;
    public final GLTexture texture;

    public ChunkDisplay(int cx, int cz, Chunk chunk, Chunk adjacentChunk, BlockFace blockFace)
    {
        GLDisplayList.Builder builder = new GLDisplayList.Builder();
        GLTexture texture = null;

        int faceCount = 0;

        if (blockFace != null) {
            switch (blockFace) {
            case TOP:
                texture = BlockRenderer.topFaceTextures.blockTextures;

                builder.setColor(TOP_FACE_SHADE, TOP_FACE_SHADE, TOP_FACE_SHADE);

                for (int y = 0; y < Chunk.YLENGTH - 1; ++y) {
                    for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                        for (int x = 0; x < Chunk.XLENGTH; ++x) {
                            short blockID = chunk.getBlock(x, y, z);
                            short adjacentBlockID = chunk.getBlock(x, y + 1, z);

                            BlockRenderer.byBlockID(blockID).pushTopFaces(x, y, z, blockID,
                                    adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlock(x, Chunk.YLENGTH - 1, z);
                        short adjacentBlockID = adjacentChunk.getBlock(x, 0, z);

                        BlockRenderer.byBlockID(blockID).pushTopFaces(x, Chunk.YLENGTH - 1, z,
                                blockID, adjacentBlockID, builder);

                        ++faceCount;
                    }
                }

                break;

            case BOTTOM:
                texture = BlockRenderer.bottomFaceTextures.blockTextures;

                builder.setColor(BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE);

                for (int y = 1; y < Chunk.YLENGTH; ++y) {
                    for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                        for (int x = 0; x < Chunk.XLENGTH; ++x) {
                            short blockID = chunk.getBlock(x, y, z);
                            short adjacentBlockID = chunk.getBlock(x, y - 1, z);

                            BlockRenderer.byBlockID(blockID).pushBottomFaces(x, y, z, blockID,
                                    adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                break;

            case LEFT:
                texture = BlockRenderer.leftFaceTextures.blockTextures;

                builder.setColor(LEFT_FACE_SHADE, LEFT_FACE_SHADE, LEFT_FACE_SHADE);

                for (int y = 0; y < Chunk.YLENGTH; ++y) {
                    for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                        for (int x = 1; x < Chunk.XLENGTH; ++x) {
                            short blockID = chunk.getBlock(x, y, z);
                            short adjacentBlockID = chunk.getBlock(x - 1, y, z);

                            BlockRenderer.byBlockID(blockID).pushLeftFaces(x, y, z, blockID,
                                    adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                if (adjacentChunk != null) {
                    for (int y = 0; y < Chunk.YLENGTH; ++y) {
                        for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                            short blockID = chunk.getBlock(0, y, z);
                            short adjacentBlockID = adjacentChunk.getBlock(Chunk.XLENGTH - 1, y, z);

                            BlockRenderer.byBlockID(blockID).pushLeftFaces(0, y, z, blockID,
                                    adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                break;

            case RIGHT:
                texture = BlockRenderer.rightFaceTextures.blockTextures;

                builder.setColor(RIGHT_FACE_SHADE, RIGHT_FACE_SHADE, RIGHT_FACE_SHADE);

                for (int y = 0; y < Chunk.YLENGTH; ++y) {
                    for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                        for (int x = 0; x < Chunk.XLENGTH - 1; ++x) {
                            short blockID = chunk.getBlock(x, y, z);
                            short adjacentBlockID = chunk.getBlock(x + 1, y, z);

                            BlockRenderer.byBlockID(blockID).pushRightFaces(x, y, z, blockID,
                                    adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                if (adjacentChunk != null) {
                    for (int y = 0; y < Chunk.YLENGTH; ++y) {
                        for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                            short blockID = chunk.getBlock(Chunk.XLENGTH - 1, y, z);
                            short adjacentBlockID = adjacentChunk.getBlock(0, y, z);

                            BlockRenderer.byBlockID(blockID).pushRightFaces(Chunk.XLENGTH - 1, y, z,
                                    blockID, adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                break;

            case FRONT:
                texture = BlockRenderer.frontFaceTextures.blockTextures;

                builder.setColor(FRONT_FACE_SHADE, FRONT_FACE_SHADE, FRONT_FACE_SHADE);

                for (int y = 0; y < Chunk.YLENGTH; ++y) {
                    for (int z = 0; z < Chunk.ZLENGTH - 1; ++z) {
                        for (int x = 0; x < Chunk.XLENGTH; ++x) {
                            short blockID = chunk.getBlock(x, y, z);
                            short adjacentBlockID = chunk.getBlock(x, y, z + 1);

                            BlockRenderer.byBlockID(blockID).pushFrontFaces(x, y, z, blockID,
                                    adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                if (adjacentChunk != null) {
                    for (int y = 0; y < Chunk.YLENGTH; ++y) {
                        for (int x = 0; x < Chunk.XLENGTH; ++x) {
                            short blockID = chunk.getBlock(x, y, Chunk.ZLENGTH - 1);
                            short adjacentBlockID = adjacentChunk.getBlock(x, y, 0);

                            BlockRenderer.byBlockID(blockID).pushFrontFaces(x, y, Chunk.ZLENGTH - 1,
                                    blockID, adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                break;

            case BACK:
                texture = BlockRenderer.backFaceTextures.blockTextures;

                builder.setColor(BACK_FACE_SHADE, BACK_FACE_SHADE, BACK_FACE_SHADE);

                for (int y = 0; y < Chunk.YLENGTH; ++y) {
                    for (int z = 1; z < Chunk.ZLENGTH; ++z) {
                        for (int x = 0; x < Chunk.XLENGTH; ++x) {
                            short blockID = chunk.getBlock(x, y, z);
                            short adjacentBlockID = chunk.getBlock(x, y, z - 1);

                            BlockRenderer.byBlockID(blockID).pushBackFaces(x, y, z, blockID,
                                    adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                if (adjacentChunk != null) {
                    for (int y = 0; y < Chunk.YLENGTH; ++y) {
                        for (int x = 0; x < Chunk.XLENGTH; ++x) {
                            short blockID = chunk.getBlock(x, y, 0);
                            short adjacentBlockID = adjacentChunk.getBlock(x, y, Chunk.ZLENGTH - 1);

                            BlockRenderer.byBlockID(blockID).pushBackFaces(x, y, 0, blockID,
                                    adjacentBlockID, builder);

                            ++faceCount;
                        }
                    }
                }

                break;
            }
        }
        else {
            texture = BlockRenderer.topFaceTextures.blockTextures;

            builder.setColor(NON_CUBIC_SHADE, NON_CUBIC_SHADE, NON_CUBIC_SHADE);

            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlock(x, y, z);

                        if (!BlockRenderer.byBlockID(blockID).nonCubic)
                            continue;

                        BlockRenderer.byBlockID(blockID).pushNonCubic(x, y, z, blockID, builder);

                        ++faceCount;
                    }
                }
            }
        }

        if (faceCount > 0) {
            displayList = builder.build();
        }
        else {
            displayList = null;
            builder.build().close();
        }

        chunkX = cx;
        chunkZ = cz;
        this.chunk = chunk;
        this.adjacentChunk = adjacentChunk;
        this.blockFace = blockFace;
        this.texture = texture;
    }

    @Override
    public void close() {
        if (displayList != null)
            displayList.close();
    }

    /**
     * Displays the chunk's containing blocks.
     */
    public void displayBlocks() {
        if (displayList != null)
            GLRenderContext.callDisplayList(displayList, texture);
    }

    /**
     * Displays the chunk's containing entities.
     */
    public void displayEntities() {
        for (Character character : chunk.iterateCharacters()) {
            if (!character.visible)
                continue;

            displayCharacter(character);
            displayCharacterHitBox(character);
        }
    }

    private void displayCharacter(Character character) {
        GLMatrixStack.push();
        GLMatrixStack.translate((float) character.x, (float) character.y, (float) character.z);
        GLMatrixStack.rotate((float) character.yaw, 0.0f, 1.0f, 0.0f);
        GLMatrixStack.rotate((float) character.pitch, 1.0f, 0.0f, 0.0f);
        GLMatrixStack.rotate((float) character.roll, 0.0f, 0.0f, 1.0f);

        StaticModel model = StaticModel.fromCharacter(character);
        ModelRenderer.displayStaticModel(model);

        GLMatrixStack.pop();
    }

    private void displayCharacterHitBox(Character character) {
        GLMatrixStack.push();
        GLMatrixStack.translate((float) character.x, (float) character.y, (float) character.z);

        final float BOX_R = 1.0f;
        final float BOX_G = 0.0f;
        final float BOX_B = 1.0f;

        float cbrX = (float) character.collisionBoxRadiusX;
        float cbrY = (float) character.collisionBoxRadiusY;
        float cbrZ = (float) character.collisionBoxRadiusZ;

        GLImmediateDrawCall.beginLines(null)
                .coloredVertex(-cbrX, cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, cbrY, cbrZ, BOX_R, BOX_G, BOX_B)

                .coloredVertex(-cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, -cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, -cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, -cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, -cbrY, cbrZ, BOX_R, BOX_G, BOX_B)

                .coloredVertex(-cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, -cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(-cbrX, cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, -cbrY, cbrZ, BOX_R, BOX_G, BOX_B)
                .coloredVertex(cbrX, cbrY, cbrZ, BOX_R, BOX_G, BOX_B).end();

        GLMatrixStack.pop();
    }

    public boolean isEmpty() {
        return displayList == null;
    }
}