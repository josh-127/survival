package net.survival.client.graphics;

import net.survival.block.BlockFace;
import net.survival.block.BlockType;
import net.survival.client.graphics.model.ModelRenderer;
import net.survival.client.graphics.model.StaticModel;
import net.survival.client.graphics.opengl.GLDisplayList;
import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.entity.Entity;
import net.survival.world.chunk.Chunk;

class ChunkDisplay implements GraphicsResource
{
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
    
    public final ChunkOverlayDisplay overlayDisplay;

    public ChunkDisplay(int cx, int cz, Chunk chunk, Chunk adjacentChunk, BlockFace blockFace,
            BlockTextureAtlas atlas)
    {
        GLDisplayList.Builder builder = new GLDisplayList.Builder();
        
        int faceCount = 0;
        
        switch (blockFace) {
        case TOP:
            builder.setColor(TOP_FACE_SHADE, TOP_FACE_SHADE, TOP_FACE_SHADE);
            break;
            
        case BOTTOM:
            builder.setColor(BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE);
            break;
            
        case LEFT:
            builder.setColor(LEFT_FACE_SHADE, LEFT_FACE_SHADE, LEFT_FACE_SHADE);
            break;
            
        case RIGHT:
            builder.setColor(RIGHT_FACE_SHADE, RIGHT_FACE_SHADE, RIGHT_FACE_SHADE);
            break;
            
        case FRONT:
            builder.setColor(FRONT_FACE_SHADE, FRONT_FACE_SHADE, FRONT_FACE_SHADE);
            break;
            
        case BACK:
            builder.setColor(BACK_FACE_SHADE, BACK_FACE_SHADE, BACK_FACE_SHADE);
            break;
        }
        
        switch (blockFace) {
        case TOP:
            for (int y = 0; y < Chunk.YLENGTH - 1; ++y) {
                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlockID(x, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x, y + 1, z)).isVisible())
                            continue;

                        pushTopFace(x, y, z, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }

            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    short blockID = chunk.getBlockID(x, Chunk.YLENGTH - 1, z);
                    
                    if (!BlockType.byID(blockID).isVisible())
                        continue;
                    
                    if (BlockType.byID(adjacentChunk.getBlockID(x, 0, z)).isVisible())
                        continue;

                    pushTopFace(x, Chunk.YLENGTH - 1, z, atlas, blockID, builder);
                    ++faceCount;
                }
            }
            
            break;
            
        case BOTTOM:
            for (int y = 1; y < Chunk.YLENGTH; ++y) {
                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlockID(x, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x, y - 1, z)).isVisible())
                            continue;

                        pushBottomFace(x, y, z, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }
            
            break;
            
        case FRONT:
            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                for (int z = 0; z < Chunk.ZLENGTH - 1; ++z) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlockID(x, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x, y, z + 1)).isVisible())
                            continue;

                        pushFrontFace(x, y, z, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }

            if (adjacentChunk != null) {
                for (int y = 0; y < Chunk.YLENGTH; ++y) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlockID(x, y, Chunk.ZLENGTH - 1);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(adjacentChunk.getBlockID(x, y, 0)).isVisible())
                            continue;

                        pushFrontFace(x, y, Chunk.ZLENGTH - 1, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }
            
            break;
            
        case BACK:
            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                for (int z = 1; z < Chunk.ZLENGTH; ++z) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlockID(x, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x, y, z - 1)).isVisible())
                            continue;

                        pushBackFace(x, y, z, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }

            if (adjacentChunk != null) {
                for (int y = 0; y < Chunk.YLENGTH; ++y) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlockID(x, y, 0);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(adjacentChunk.getBlockID(x, y, Chunk.ZLENGTH - 1)).isVisible())
                            continue;

                        pushBackFace(x, y, 0, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }
            
            break;
            
        case LEFT:
            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    for (int x = 1; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlockID(x, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x - 1, y, z)).isVisible())
                            continue;

                        pushLeftFace(x, y, z, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }
            
            if (adjacentChunk != null) {
                for (int y = 0; y < Chunk.YLENGTH; ++y) {
                    for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                        short blockID = chunk.getBlockID(0, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(adjacentChunk.getBlockID(Chunk.XLENGTH - 1, y, z)).isVisible())
                            continue;

                        pushLeftFace(0, y, z, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }
            
            break;
            
        case RIGHT:
            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    for (int x = 0; x < Chunk.XLENGTH - 1; ++x) {
                        short blockID = chunk.getBlockID(x, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x + 1, y, z)).isVisible())
                            continue;

                        pushRightFace(x, y, z, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }

            if (adjacentChunk != null) {
                for (int y = 0; y < Chunk.YLENGTH; ++y) {
                    for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                        short blockID = chunk.getBlockID(Chunk.XLENGTH - 1, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;

                        if (BlockType.byID(adjacentChunk.getBlockID(0, y, z)).isVisible())
                            continue;

                        pushRightFace(Chunk.XLENGTH - 1, y, z, atlas, blockID, builder);
                        ++faceCount;
                    }
                }
            }
            
            break;
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
        
        overlayDisplay = new ChunkOverlayDisplay(cx, cz, chunk, adjacentChunk, blockFace);
    }
    
    @Override
    public void close() {
        if (displayList != null)
            displayList.close();
    }
    
    /**
     * Displays the chunk's containing blocks.
     */
    public void displayBlocks(GLTexture texture) {
        if (displayList != null)
            GLRenderContext.callDisplayList(displayList, texture);
    }
    
    /**
     * Displays the chunk's containing entities.
     */
    public void displayEntities() {
        for (Entity entity : chunk.iterateEntities()) {
            if (!entity.visible)
                continue;
            
            GLMatrixStack.push();
            GLMatrixStack.translate((float) entity.x, (float) entity.y, (float) entity.z);
            
            StaticModel model = StaticModel.fromEntity(entity);
            ModelRenderer.displayStaticModel(model);

            final float BOX_R = 1.0f;
            final float BOX_G = 0.0f;
            final float BOX_B = 1.0f;
            
            float cbrX = (float) entity.collisionBoxRadiusX;
            float cbrY = (float) entity.collisionBoxRadiusY;
            float cbrZ = (float) entity.collisionBoxRadiusZ;
            
            GLImmediateDrawCall.beginLines(null)
                    .coloredVertex(-cbrX,  cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX,  cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX,  cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX,  cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX,  cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX,  cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX,  cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX,  cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)

                    .coloredVertex(-cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX, -cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX, -cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX, -cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX, -cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    
                    .coloredVertex(-cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX,  cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX, -cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX,  cbrY, -cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX, -cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex(-cbrX,  cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX, -cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .coloredVertex( cbrX,  cbrY,  cbrZ, BOX_R, BOX_G, BOX_B)
                    .end();
            
            GLMatrixStack.pop();
        }
    }
    
    public boolean isEmpty() {
        return displayList == null;
    }

    private void pushTopFace(int x, int y, int z, BlockTextureAtlas atlas, short blockID, GLDisplayList.Builder builder) {
        float u1 = atlas.getTexCoordU1(blockID);
        float u2 = atlas.getTexCoordU2(blockID);
        float v1 = atlas.getTexCoordV1(blockID);
        float v2 = atlas.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z       );
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
    }

    private void pushBottomFace(int x, int y, int z, BlockTextureAtlas atlas, short blockID, GLDisplayList.Builder builder) {
        float u1 = atlas.getTexCoordU1(blockID);
        float u2 = atlas.getTexCoordU2(blockID);
        float v1 = atlas.getTexCoordV1(blockID);
        float v2 = atlas.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y, z       );
    }

    private void pushFrontFace(int x, int y, int z, BlockTextureAtlas atlas, short blockID, GLDisplayList.Builder builder) {
        float u1 = atlas.getTexCoordU1(blockID);
        float u2 = atlas.getTexCoordU2(blockID);
        float v1 = atlas.getTexCoordV1(blockID);
        float v2 = atlas.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x,        y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x,        y,        z + 1.0f);
    }

    private void pushBackFace(int x, int y, int z, BlockTextureAtlas atlas, short blockID, GLDisplayList.Builder builder) {
        float u1 = atlas.getTexCoordU1(blockID);
        float u2 = atlas.getTexCoordU2(blockID);
        float v1 = atlas.getTexCoordV1(blockID);
        float v2 = atlas.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
        builder.setTexCoord(u2, v1); builder.pushVertex(x,        y,        z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
        builder.setTexCoord(u2, v2); builder.pushVertex(x,        y + 1.0f, z);
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z);
    }

    private void pushLeftFace(int x, int y, int z, BlockTextureAtlas atlas, short blockID, GLDisplayList.Builder builder) {
        float u1 = atlas.getTexCoordU1(blockID);
        float u2 = atlas.getTexCoordU2(blockID);
        float v1 = atlas.getTexCoordV1(blockID);
        float v2 = atlas.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
        builder.setTexCoord(u2, v1); builder.pushVertex(x, y,        z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u2, v2); builder.pushVertex(x, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v2); builder.pushVertex(x, y + 1.0f, z       );
        builder.setTexCoord(u1, v1); builder.pushVertex(x, y,        z       );
    }

    private void pushRightFace(int x, int y, int z, BlockTextureAtlas atlas, short blockID, GLDisplayList.Builder builder) {
        float u1 = atlas.getTexCoordU1(blockID);
        float u2 = atlas.getTexCoordU2(blockID);
        float v1 = atlas.getTexCoordV1(blockID);
        float v2 = atlas.getTexCoordV2(blockID);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
        builder.setTexCoord(u2, v1); builder.pushVertex(x + 1.0f, y,        z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u2, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z       );
        builder.setTexCoord(u1, v2); builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f);
        builder.setTexCoord(u1, v1); builder.pushVertex(x + 1.0f, y,        z + 1.0f);
    }
}