package net.survival.client.graphics;

import net.survival.block.BlockFace;
import net.survival.client.graphics.blockrenderer.BlockRenderer;
import net.survival.client.graphics.opengl.GLDisplayList;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.world.chunk.Chunk;

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
    public final Chunk chunk;
    public final Chunk adjacentChunk;
    public final BlockFace blockFace;
    public final GLTexture texture;

    private ChunkDisplay(
            GLDisplayList displayList,
            int cx,
            int cz,
            Chunk chunk,
            Chunk adjacentChunk,
            BlockFace blockFace,
            GLTexture texture
    )
    {
        this.displayList = displayList;
        this.chunkX = cx;
        this.chunkZ = cz;
        this.chunk = chunk;
        this.adjacentChunk = adjacentChunk;
        this.texture = texture;
        this.blockFace = blockFace;
    }

    public static ChunkDisplay create(int cx, int cz, Chunk chunk, Chunk adjacentChunk, BlockFace blockFace) {
        if (blockFace == null)
            return createNonCubicFaces(cx, cz, chunk);

        GLDisplayList.Builder builder = new GLDisplayList.Builder();

        switch (blockFace) {
        case TOP:
            return new ChunkDisplay(
                    createTopFaces(chunk, builder),
                    cx,
                    cz,
                    chunk,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.topFaceTextures.blockTextures);

        case BOTTOM:
            return new ChunkDisplay(
                    createBottomFaces(chunk, builder),
                    cx,
                    cz,
                    chunk,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.bottomFaceTextures.blockTextures);

        case LEFT:
            return new ChunkDisplay(
                    createLeftFaces(chunk, adjacentChunk, builder),
                    cx,
                    cz,
                    chunk,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.leftFaceTextures.blockTextures);

        case RIGHT:
            return new ChunkDisplay(
                    createRightFaces(chunk, adjacentChunk, builder),
                    cx,
                    cz,
                    chunk,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.rightFaceTextures.blockTextures);

        case FRONT:
            return new ChunkDisplay(
                    createFrontFaces(chunk, adjacentChunk, builder),
                    cx,
                    cz,
                    chunk,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.frontFaceTextures.blockTextures);

        case BACK:
            return new ChunkDisplay(
                    createBackFaces(chunk, adjacentChunk, builder),
                    cx,
                    cz,
                    chunk,
                    adjacentChunk,
                    blockFace,
                    BlockRenderer.backFaceTextures.blockTextures);
        }

        throw new IllegalArgumentException("blockFace");
    }

    private static GLDisplayList createTopFaces(Chunk chunk, GLDisplayList.Builder builder) {
        builder.setColor(TOP_FACE_SHADE, TOP_FACE_SHADE, TOP_FACE_SHADE);

        for (int i = 0; i < Chunk.VOLUME - Chunk.BASE_AREA; ++i) {
            int x = i % Chunk.XLENGTH;
            int z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;
            int y = i / Chunk.BASE_AREA;

            short blockID = chunk.blockIDs[i];
            short adjacentBlockID = chunk.blockIDs[i + Chunk.BASE_AREA];

            BlockRenderer.byBlockID(blockID).pushTopFaces(
                    x, y, z, blockID, adjacentBlockID, builder);
        }

        for (int i = Chunk.VOLUME - Chunk.BASE_AREA; i < Chunk.VOLUME; ++i) {
            int x = i % Chunk.XLENGTH;
            int z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;
            int y = i / Chunk.BASE_AREA;

            short blockID = chunk.blockIDs[i];

            BlockRenderer.byBlockID(blockID).pushTopFaces(
                    x, y, z, blockID, (short) 0, builder);
        }

        return builder.build();
    }

    private static GLDisplayList createBottomFaces(Chunk chunk, GLDisplayList.Builder builder) {
        builder.setColor(BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE);

        for (int i = Chunk.BASE_AREA; i < Chunk.VOLUME; ++i) {
            int x = i % Chunk.XLENGTH;
            int z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;
            int y = i / Chunk.BASE_AREA;

            short blockID = chunk.blockIDs[i];
            short adjacentBlockID = chunk.blockIDs[i - Chunk.BASE_AREA];

            BlockRenderer.byBlockID(blockID).pushBottomFaces(
                    x, y, z, blockID, adjacentBlockID, builder);
        }

        return builder.build();
    }

    private static GLDisplayList createLeftFaces(Chunk chunk, Chunk adjacentChunk, GLDisplayList.Builder builder) {
        builder.setColor(LEFT_FACE_SHADE, LEFT_FACE_SHADE, LEFT_FACE_SHADE);

        for (int y = 0; y < Chunk.YLENGTH; ++y) {
            int indexY = y * Chunk.BASE_AREA;

            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                int indexYZ = indexY + (z * Chunk.XLENGTH);

                for (int x = 1; x < Chunk.XLENGTH; ++x) {
                    short blockID = chunk.blockIDs[indexYZ + x];
                    short adjacentBlockID = chunk.blockIDs[indexYZ + x - 1];

                    BlockRenderer.byBlockID(blockID).pushLeftFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentChunk != null) {
            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                int indexY = y * Chunk.BASE_AREA;

                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    int indexYZ = indexY + (z * Chunk.XLENGTH);

                    short blockID = chunk.blockIDs[indexYZ];
                    short adjacentBlockID = adjacentChunk.blockIDs[indexYZ + Chunk.XLENGTH - 1];

                    BlockRenderer.byBlockID(blockID).pushLeftFaces(
                            0, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createRightFaces(Chunk chunk, Chunk adjacentChunk, GLDisplayList.Builder builder) {
        builder.setColor(RIGHT_FACE_SHADE, RIGHT_FACE_SHADE, RIGHT_FACE_SHADE);

        for (int y = 0; y < Chunk.YLENGTH; ++y) {
            int indexY = y * Chunk.BASE_AREA;

            for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                int indexYZ = indexY + (z * Chunk.XLENGTH);

                for (int x = 0; x < Chunk.XLENGTH - 1; ++x) {
                    short blockID = chunk.blockIDs[indexYZ + x];
                    short adjacentBlockID = chunk.blockIDs[indexYZ + x + 1];

                    BlockRenderer.byBlockID(blockID).pushRightFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentChunk != null) {
            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                int indexY = y * Chunk.BASE_AREA;

                for (int z = 0; z < Chunk.ZLENGTH; ++z) {
                    int indexYZ = indexY + (z * Chunk.XLENGTH);

                    short blockID = chunk.blockIDs[indexYZ + Chunk.XLENGTH - 1];
                    short adjacentBlockID = adjacentChunk.blockIDs[indexYZ];

                    BlockRenderer.byBlockID(blockID).pushRightFaces(
                            Chunk.XLENGTH - 1, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createFrontFaces(Chunk chunk, Chunk adjacentChunk, GLDisplayList.Builder builder) {
        builder.setColor(FRONT_FACE_SHADE, FRONT_FACE_SHADE, FRONT_FACE_SHADE);

        for (int y = 0; y < Chunk.YLENGTH; ++y) {
            int indexY = y * Chunk.BASE_AREA;

            for (int z = 0; z < Chunk.ZLENGTH - 1; ++z) {
                int indexYZ = indexY + (z * Chunk.XLENGTH);

                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    int indexYZX = indexYZ + x;

                    short blockID = chunk.blockIDs[indexYZX];
                    short adjacentBlockID = chunk.blockIDs[indexYZX + Chunk.XLENGTH];

                    BlockRenderer.byBlockID(blockID).pushFrontFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentChunk != null) {
            int indexZ = Chunk.BASE_AREA - Chunk.XLENGTH;

            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                int indexY = y * Chunk.BASE_AREA;
                int indexYZ = indexZ + indexY;

                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    short blockID = chunk.blockIDs[indexYZ + x];
                    short adjacentBlockID = adjacentChunk.blockIDs[indexY + x];

                    BlockRenderer.byBlockID(blockID).pushFrontFaces(
                            x, y, Chunk.ZLENGTH - 1, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createBackFaces(Chunk chunk, Chunk adjacentChunk, GLDisplayList.Builder builder) {
        builder.setColor(BACK_FACE_SHADE, BACK_FACE_SHADE, BACK_FACE_SHADE);

        for (int y = 0; y < Chunk.YLENGTH; ++y) {
            int indexY = y * Chunk.BASE_AREA;

            for (int z = 1; z < Chunk.ZLENGTH; ++z) {
                int indexYZ = indexY + (z * Chunk.XLENGTH);

                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    int indexYZX = indexYZ + x;

                    short blockID = chunk.blockIDs[indexYZX];
                    short adjacentBlockID = chunk.blockIDs[indexYZX - Chunk.XLENGTH];

                    BlockRenderer.byBlockID(blockID).pushBackFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentChunk != null) {
            for (int y = 0; y < Chunk.YLENGTH; ++y) {
                int indexY = y * Chunk.BASE_AREA;
                int adjacentIndexYZ = indexY + Chunk.BASE_AREA - Chunk.XLENGTH;

                for (int x = 0; x < Chunk.XLENGTH; ++x) {
                    short blockID = chunk.blockIDs[indexY + x];
                    short adjacentBlockID = adjacentChunk.blockIDs[adjacentIndexYZ + x];

                    BlockRenderer.byBlockID(blockID).pushBackFaces(
                            x, y, 0, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static ChunkDisplay createNonCubicFaces(int cx, int cz, Chunk chunk) {
        GLDisplayList.Builder builder = new GLDisplayList.Builder();
        GLTexture texture = BlockRenderer.topFaceTextures.blockTextures;
        boolean shouldCreateDisplayList = false;

        builder.setColor(NON_CUBIC_SHADE, NON_CUBIC_SHADE, NON_CUBIC_SHADE);

        for (int i = 0; i < Chunk.VOLUME; ++i) {
            short blockID = chunk.blockIDs[i];

            if (!BlockRenderer.byBlockID(blockID).nonCubic)
                continue;

            int x = i % Chunk.XLENGTH;
            int z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;
            int y = i / Chunk.BASE_AREA;

            BlockRenderer.byBlockID(blockID).pushNonCubic(x, y, z, blockID, builder);
            shouldCreateDisplayList = true;
        }

        GLDisplayList displayList = null;

        if (shouldCreateDisplayList)
            displayList = builder.build();
        else
            builder.build().close();

        return new ChunkDisplay(
                displayList, cx, cz, chunk, null, null, texture);
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