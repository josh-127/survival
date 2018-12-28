package net.survival.client.graphics;

import net.survival.block.BlockFace;
import net.survival.client.graphics.blockrenderer.BlockRenderer;
import net.survival.client.graphics.opengl.GLDisplayList;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkColumn;

class ChunkDisplay implements GraphicsResource
{
    private static final float NON_CUBIC_SHADE = 1.0f;
    private static final float TOP_FACE_SHADE = 1.0f;
    private static final float BOTTOM_FACE_SHADE = 0.25f;
    private static final float LEFT_FACE_SHADE = 0.5f;
    private static final float RIGHT_FACE_SHADE = 0.5f;
    private static final float FRONT_FACE_SHADE = 0.75f;
    private static final float BACK_FACE_SHADE = 0.75f;

    public final GLDisplayList displayList;
    public final int chunkX;
    public final int chunkZ;
    public final ChunkColumn chunkColumn;
    public final ChunkColumn adjacentChunk;
    public final BlockFace blockFace;
    public final GLTexture texture;

    private ChunkDisplay(
            GLDisplayList displayList,
            int cx,
            int cz,
            ChunkColumn chunkColumn,
            ChunkColumn adjacentChunk,
            BlockFace blockFace,
            GLTexture texture
    )
    {
        this.displayList = displayList;
        this.chunkX = cx;
        this.chunkZ = cz;
        this.chunkColumn = chunkColumn;
        this.adjacentChunk = adjacentChunk;
        this.texture = texture;
        this.blockFace = blockFace;
    }

    public static ChunkDisplay create(int cx, int cz, ChunkColumn chunkColumn, ChunkColumn adjacentChunk, BlockFace blockFace) {
        if (blockFace == null)
            return createNonCubicFaces(cx, cz, chunkColumn);

        GLDisplayList.Builder builder = new GLDisplayList.Builder();

        switch (blockFace) {
        case TOP:
            return new ChunkDisplay(
                    createTopFaces(chunkColumn, builder),
                    cx,
                    cz,
                    chunkColumn,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.topFaceTextures.blockTextures);

        case BOTTOM:
            return new ChunkDisplay(
                    createBottomFaces(chunkColumn, builder),
                    cx,
                    cz,
                    chunkColumn,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.bottomFaceTextures.blockTextures);

        case LEFT:
            return new ChunkDisplay(
                    createLeftFaces(chunkColumn, adjacentChunk, builder),
                    cx,
                    cz,
                    chunkColumn,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.leftFaceTextures.blockTextures);

        case RIGHT:
            return new ChunkDisplay(
                    createRightFaces(chunkColumn, adjacentChunk, builder),
                    cx,
                    cz,
                    chunkColumn,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.rightFaceTextures.blockTextures);

        case FRONT:
            return new ChunkDisplay(
                    createFrontFaces(chunkColumn, adjacentChunk, builder),
                    cx,
                    cz,
                    chunkColumn,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.frontFaceTextures.blockTextures);

        case BACK:
            return new ChunkDisplay(
                    createBackFaces(chunkColumn, adjacentChunk, builder),
                    cx,
                    cz,
                    chunkColumn,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.backFaceTextures.blockTextures);
        }

        throw new IllegalArgumentException("blockFace");
    }

    private static GLDisplayList createTopFaces(ChunkColumn chunkColumn, GLDisplayList.Builder builder) {
        builder.setColor(TOP_FACE_SHADE, TOP_FACE_SHADE, TOP_FACE_SHADE);

        for (int i = 0; i < ChunkColumn.VOLUME - ChunkColumn.BASE_AREA; ++i) {
            int x = i % ChunkColumn.XLENGTH;
            int z = (i / ChunkColumn.XLENGTH) % ChunkColumn.ZLENGTH;
            int y = i / ChunkColumn.BASE_AREA;

            short blockID = chunkColumn.getBlock(x, y, z);
            short adjacentBlockID = chunkColumn.getBlock(x, y + 1, z);

            BlockRenderer.byBlockID(blockID).pushTopFaces(
                    x, y, z, blockID, adjacentBlockID, builder);
        }

        for (int i = ChunkColumn.VOLUME - ChunkColumn.BASE_AREA; i < ChunkColumn.VOLUME; ++i) {
            int x = i % ChunkColumn.XLENGTH;
            int z = (i / ChunkColumn.XLENGTH) % ChunkColumn.ZLENGTH;
            int y = i / ChunkColumn.BASE_AREA;

            short blockID = chunkColumn.getBlock(x, y, z);

            BlockRenderer.byBlockID(blockID).pushTopFaces(
                    x, y, z, blockID, (short) 0, builder);
        }

        return builder.build();
    }

    private static GLDisplayList createBottomFaces(ChunkColumn chunkColumn, GLDisplayList.Builder builder) {
        builder.setColor(BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE);

        for (int i = ChunkColumn.BASE_AREA; i < ChunkColumn.VOLUME; ++i) {
            int x = i % ChunkColumn.XLENGTH;
            int z = (i / ChunkColumn.XLENGTH) % ChunkColumn.ZLENGTH;
            int y = i / ChunkColumn.BASE_AREA;

            short blockID = chunkColumn.getBlock(x, y, z);
            short adjacentBlockID = chunkColumn.getBlock(x, y - 1, z);

            BlockRenderer.byBlockID(blockID).pushBottomFaces(
                    x, y, z, blockID, adjacentBlockID, builder);
        }

        return builder.build();
    }

    private static GLDisplayList createLeftFaces(ChunkColumn chunkColumn, ChunkColumn adjacentChunk, GLDisplayList.Builder builder) {
        builder.setColor(LEFT_FACE_SHADE, LEFT_FACE_SHADE, LEFT_FACE_SHADE);

        for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
            int indexY = y * ChunkColumn.BASE_AREA;

            for (int z = 0; z < ChunkColumn.ZLENGTH; ++z) {
                int indexYZ = indexY + (z * ChunkColumn.XLENGTH);

                for (int x = 1; x < ChunkColumn.XLENGTH; ++x) {
                    short blockID = chunkColumn.getBlock(x, y, z);
                    short adjacentBlockID = chunkColumn.getBlock(x - 1, y, z);

                    BlockRenderer.byBlockID(blockID).pushLeftFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentChunk != null) {
            for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
                int indexY = y * ChunkColumn.BASE_AREA;

                for (int z = 0; z < ChunkColumn.ZLENGTH; ++z) {
                    int indexYZ = indexY + (z * ChunkColumn.XLENGTH);

                    short blockID = chunkColumn.getBlock(0, y, z);
                    short adjacentBlockID = adjacentChunk.getBlock(Chunk.XLENGTH - 1, y, z);

                    BlockRenderer.byBlockID(blockID).pushLeftFaces(
                            0, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createRightFaces(ChunkColumn chunkColumn, ChunkColumn adjacentChunk, GLDisplayList.Builder builder) {
        builder.setColor(RIGHT_FACE_SHADE, RIGHT_FACE_SHADE, RIGHT_FACE_SHADE);

        for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
            int indexY = y * ChunkColumn.BASE_AREA;

            for (int z = 0; z < ChunkColumn.ZLENGTH; ++z) {
                int indexYZ = indexY + (z * ChunkColumn.XLENGTH);

                for (int x = 0; x < ChunkColumn.XLENGTH - 1; ++x) {
                    short blockID = chunkColumn.getBlock(x, y, z);
                    short adjacentBlockID = chunkColumn.getBlock(x + 1, y, z);

                    BlockRenderer.byBlockID(blockID).pushRightFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentChunk != null) {
            for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
                int indexY = y * ChunkColumn.BASE_AREA;

                for (int z = 0; z < ChunkColumn.ZLENGTH; ++z) {
                    int indexYZ = indexY + (z * ChunkColumn.XLENGTH);

                    short blockID = chunkColumn.getBlock(Chunk.XLENGTH - 1, y, z);
                    short adjacentBlockID = adjacentChunk.getBlock(0, y, z);

                    BlockRenderer.byBlockID(blockID).pushRightFaces(
                            ChunkColumn.XLENGTH - 1, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createFrontFaces(ChunkColumn chunkColumn, ChunkColumn adjacentChunk, GLDisplayList.Builder builder) {
        builder.setColor(FRONT_FACE_SHADE, FRONT_FACE_SHADE, FRONT_FACE_SHADE);

        for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
            int indexY = y * ChunkColumn.BASE_AREA;

            for (int z = 0; z < ChunkColumn.ZLENGTH - 1; ++z) {
                int indexYZ = indexY + (z * ChunkColumn.XLENGTH);

                for (int x = 0; x < ChunkColumn.XLENGTH; ++x) {
                    int indexYZX = indexYZ + x;

                    short blockID = chunkColumn.getBlock(x, y, z);
                    short adjacentBlockID = chunkColumn.getBlock(x, y, z + 1);

                    BlockRenderer.byBlockID(blockID).pushFrontFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentChunk != null) {
            int indexZ = ChunkColumn.BASE_AREA - ChunkColumn.XLENGTH;

            for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
                int indexY = y * ChunkColumn.BASE_AREA;
                int indexYZ = indexZ + indexY;

                for (int x = 0; x < ChunkColumn.XLENGTH; ++x) {
                    short blockID = chunkColumn.getBlock(x, y, Chunk.ZLENGTH - 1);
                    short adjacentBlockID = adjacentChunk.getBlock(x, y, 0);

                    BlockRenderer.byBlockID(blockID).pushFrontFaces(
                            x, y, ChunkColumn.ZLENGTH - 1, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createBackFaces(ChunkColumn chunkColumn, ChunkColumn adjacentChunk, GLDisplayList.Builder builder) {
        builder.setColor(BACK_FACE_SHADE, BACK_FACE_SHADE, BACK_FACE_SHADE);

        for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
            int indexY = y * ChunkColumn.BASE_AREA;

            for (int z = 1; z < ChunkColumn.ZLENGTH; ++z) {
                int indexYZ = indexY + (z * ChunkColumn.XLENGTH);

                for (int x = 0; x < ChunkColumn.XLENGTH; ++x) {
                    int indexYZX = indexYZ + x;

                    short blockID = chunkColumn.getBlock(x, y, z);
                    short adjacentBlockID = chunkColumn.getBlock(x, y, z - 1);

                    BlockRenderer.byBlockID(blockID).pushBackFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentChunk != null) {
            for (int y = 0; y < ChunkColumn.YLENGTH; ++y) {
                int indexY = y * ChunkColumn.BASE_AREA;
                int adjacentIndexYZ = indexY + ChunkColumn.BASE_AREA - ChunkColumn.XLENGTH;

                for (int x = 0; x < ChunkColumn.XLENGTH; ++x) {
                    short blockID = chunkColumn.getBlock(x, y, 0);
                    short adjacentBlockID = adjacentChunk.getBlock(x, y, Chunk.ZLENGTH - 1);

                    BlockRenderer.byBlockID(blockID).pushBackFaces(
                            x, y, 0, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static ChunkDisplay createNonCubicFaces(int cx, int cz, ChunkColumn chunkColumn) {
        GLDisplayList.Builder builder = new GLDisplayList.Builder();
        GLTexture texture = BlockRenderer.topFaceTextures.blockTextures;
        boolean shouldCreateDisplayList = false;

        builder.setColor(NON_CUBIC_SHADE, NON_CUBIC_SHADE, NON_CUBIC_SHADE);

        for (int i = 0; i < ChunkColumn.VOLUME; ++i) {
            int x = i % ChunkColumn.XLENGTH;
            int z = (i / ChunkColumn.XLENGTH) % ChunkColumn.ZLENGTH;
            int y = i / ChunkColumn.BASE_AREA;
            short blockID = chunkColumn.getBlock(x, y, z);

            if (!BlockRenderer.byBlockID(blockID).nonCubic)
                continue;

            BlockRenderer.byBlockID(blockID).pushNonCubic(x, y, z, blockID, builder);
            shouldCreateDisplayList = true;
        }

        GLDisplayList displayList = null;

        if (shouldCreateDisplayList)
            displayList = builder.build();
        else
            builder.build().close();

        return new ChunkDisplay(
                displayList, cx, cz, chunkColumn, null, null, texture);
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

    public boolean isEmpty() {
        return displayList == null;
    }
}