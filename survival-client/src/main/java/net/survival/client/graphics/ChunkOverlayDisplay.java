package net.survival.client.graphics;

import net.survival.block.BlockFace;
import net.survival.block.BlockType;
import net.survival.client.graphics.opengl.GLDisplayList;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;

//
// Temporary code.
//
class ChunkOverlayDisplay implements GraphicsResource
{
    private static final int OVERLAY_SIZE = 64;
    private static final float OVERLAY_SIZE_F = OVERLAY_SIZE;
    private static final float INV_OVERLAY_SIZE_F = 1.0f / OVERLAY_SIZE_F;
    
    public final GLDisplayList displayList;
    public final int chunkX;
    public final int chunkZ;
    public final Chunk chunk;
    public final Chunk adjacentChunk;
    public final BlockFace blockFace;

    public ChunkOverlayDisplay(int cx, int cz, Chunk chunk, Chunk adjacentChunk, BlockFace blockFace)
    {
        GLDisplayList.Builder builder = new GLDisplayList.Builder();
        
        int faceCount = 0;
        
        switch (blockFace) {
        case TOP:
            for (int y = 0; y < Chunk.YLENGTH - 1; ++y) {
                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    for (int x = 0; x < Chunk.XLENGTH; ++x) {
                        short blockID = chunk.getBlockID(x, y, z);
                        
                        if (!BlockType.byID(blockID).isVisible())
                            continue;
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x, y + 1, z)).isVisible())
                            continue;

                        pushTopFace(x, y, z, ChunkPos.toGlobalX(cx, x), ChunkPos.toGlobalZ(cz, z),
                                blockID, builder);
                        ++faceCount;
                    }
                }
            }

            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    short blockID = chunk.getBlockID(x, Chunk.YLENGTH - 1, z);
                    
                    if (!BlockType.byID(blockID).isVisible())
                        continue;
                    
                    if (blockID == BlockType.WATER.id)
                        continue;
                    
                    if (BlockType.byID(adjacentChunk.getBlockID(x, 0, z)).isVisible())
                        continue;

                    pushTopFace(x, Chunk.YLENGTH - 1, z, ChunkPos.toGlobalX(cx, x),
                            ChunkPos.toGlobalZ(cz, z), blockID, builder);
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
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x, y - 1, z)).isVisible())
                            continue;

                        pushBottomFace(x, y, z, ChunkPos.toGlobalX(cx, x),
                                ChunkPos.toGlobalZ(cz, z), blockID, builder);
                        ++faceCount;
                    }
                }
            }
            
            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    short blockID = chunk.getBlockID(x, 0, z);
                    
                    if (!BlockType.byID(blockID).isVisible())
                        continue;
                    
                    if (blockID == BlockType.WATER.id)
                        continue;
                    
                    if (BlockType.byID(adjacentChunk.getBlockID(x, Chunk.YLENGTH - 1, z)).isVisible())
                        continue;

                    pushBottomFace(x, 0, z, ChunkPos.toGlobalX(cx, x),
                            ChunkPos.toGlobalZ(cz, z), blockID, builder);
                    ++faceCount;
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
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x, y, z + 1)).isVisible())
                            continue;

                        pushFrontFace(x, y, z, ChunkPos.toGlobalX(cx, x), blockID, builder);
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

                        pushFrontFace(x, y, Chunk.ZLENGTH - 1, ChunkPos.toGlobalX(cx, x), blockID, builder);
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
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x, y, z - 1)).isVisible())
                            continue;

                        pushBackFace(x, y, z, ChunkPos.toGlobalX(cx, x), blockID, builder);
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
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(adjacentChunk.getBlockID(x, y, Chunk.ZLENGTH - 1)).isVisible())
                            continue;

                        pushBackFace(x, y, 0, ChunkPos.toGlobalX(cx, x), blockID, builder);
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
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x - 1, y, z)).isVisible())
                            continue;

                        pushLeftFace(x, y, z, ChunkPos.toGlobalZ(cz, z), blockID, builder);
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
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(adjacentChunk.getBlockID(Chunk.XLENGTH - 1, y, z)).isVisible())
                            continue;

                        pushLeftFace(0, y, z, ChunkPos.toGlobalZ(cz, z), blockID, builder);
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
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(chunk.getBlockID(x + 1, y, z)).isVisible())
                            continue;

                        pushRightFace(x, y, z, ChunkPos.toGlobalZ(cz, z), blockID, builder);
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
                        
                        if (blockID == BlockType.WATER.id)
                            continue;

                        if (BlockType.byID(adjacentChunk.getBlockID(0, y, z)).isVisible())
                            continue;

                        pushRightFace(Chunk.XLENGTH - 1, y, z, ChunkPos.toGlobalZ(cz, z), blockID, builder);
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
    
    public boolean isEmpty() {
        return displayList == null;
    }

    private void pushTopFace(int x, int y, int z, float gx, float gz, short blockID,
            GLDisplayList.Builder builder)
    {
        float u1 = gx * INV_OVERLAY_SIZE_F;
        float u2 = (gx + 1) * INV_OVERLAY_SIZE_F;
        float v1 = (gz + 1) * INV_OVERLAY_SIZE_F;
        float v2 = gz * INV_OVERLAY_SIZE_F;
        builder.pushVertex(x, y + 1.0f, z + 1.0f, u1, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f, u2, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y + 1.0f, z, u1, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y + 1.0f, z + 1.0f, u1, v1, 1.0f, 1.0f, 1.0f);
    }

    private void pushBottomFace(int x, int y, int z, float gx, float gz, short blockID,
            GLDisplayList.Builder builder)
    {
        float u1 = gx * INV_OVERLAY_SIZE_F;
        float u2 = (gx + 1) * INV_OVERLAY_SIZE_F;
        float v1 = (gz + 1) * INV_OVERLAY_SIZE_F;
        float v2 = gz * INV_OVERLAY_SIZE_F;
        builder.pushVertex(x, y, z, u1, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y, z, u2, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y, z + 1.0f, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y, z + 1.0f, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y, z + 1.0f, u1, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y, z, u1, v1, 1.0f, 1.0f, 1.0f);
    }

    private void pushFrontFace(int x, int y, int z, float gx, short blockID, GLDisplayList.Builder builder) {
        float u1 = gx * INV_OVERLAY_SIZE_F;
        float u2 = (gx + 1) * INV_OVERLAY_SIZE_F;
        float v1 = y * INV_OVERLAY_SIZE_F;
        float v2 = (y + 1) * INV_OVERLAY_SIZE_F;
        builder.pushVertex(x,        y,        z + 1.0f, u1, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y,        z + 1.0f, u2, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x,        y + 1.0f, z + 1.0f, u1, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x,        y,        z + 1.0f, u1, v1, 1.0f, 1.0f, 1.0f);
    }

    private void pushBackFace(int x, int y, int z, float gx, short blockID, GLDisplayList.Builder builder) {
        float u1 = gx * INV_OVERLAY_SIZE_F;
        float u2 = (gx + 1) * INV_OVERLAY_SIZE_F;
        float v1 = y * INV_OVERLAY_SIZE_F;
        float v2 = (y + 1) * INV_OVERLAY_SIZE_F;
        builder.pushVertex(x + 1.0f, y,        z, u1, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x,        y,        z, u2, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x,        y + 1.0f, z, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x,        y + 1.0f, z, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z, u1, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y,        z, u1, v1, 1.0f, 1.0f, 1.0f);
    }

    private void pushLeftFace(int x, int y, int z, float gz, short blockID, GLDisplayList.Builder builder) {
        float u1 = gz * INV_OVERLAY_SIZE_F;
        float u2 = (gz + 1) * INV_OVERLAY_SIZE_F;
        float v1 = y * INV_OVERLAY_SIZE_F;
        float v2 = (y + 1) * INV_OVERLAY_SIZE_F;
        builder.pushVertex(x, y,        z,        u1, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y,        z + 1.0f, u2, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y + 1.0f, z + 1.0f, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y + 1.0f, z + 1.0f, u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y + 1.0f, z,        u1, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x, y,        z,        u1, v1, 1.0f, 1.0f, 1.0f);
    }

    private void pushRightFace(int x, int y, int z, float gz, short blockID, GLDisplayList.Builder builder) {
        float u1 = gz * INV_OVERLAY_SIZE_F;
        float u2 = (gz + 1) * INV_OVERLAY_SIZE_F;
        float v1 = y * INV_OVERLAY_SIZE_F;
        float v2 = (y + 1) * INV_OVERLAY_SIZE_F;
        builder.pushVertex(x + 1.0f, y,        z + 1.0f, u1, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y,        z,        u2, v1, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z,        u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z,        u2, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y + 1.0f, z + 1.0f, u1, v2, 1.0f, 1.0f, 1.0f);
        builder.pushVertex(x + 1.0f, y,        z + 1.0f, u1, v1, 1.0f, 1.0f, 1.0f);
    }
}