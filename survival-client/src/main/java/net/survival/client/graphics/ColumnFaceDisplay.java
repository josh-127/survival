package net.survival.client.graphics;

import net.survival.block.Chunk;
import net.survival.block.Column;
import net.survival.blocktype.BlockFace;
import net.survival.client.graphics.blockrenderer.BlockRenderer;
import net.survival.client.graphics.opengl.GLDisplayList;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.graphics.opengl.GLTexture;

class ColumnFaceDisplay implements GraphicsResource
{
    private static final float NON_CUBIC_SHADE = 1.0f;
    private static final float TOP_FACE_SHADE = 1.0f;
    private static final float BOTTOM_FACE_SHADE = 0.25f;
    private static final float LEFT_FACE_SHADE = 0.5f;
    private static final float RIGHT_FACE_SHADE = 0.5f;
    private static final float FRONT_FACE_SHADE = 0.75f;
    private static final float BACK_FACE_SHADE = 0.75f;

    public final GLDisplayList displayList;
    public final int cx;
    public final int cz;
    public final GLTexture texture;

    private ColumnFaceDisplay(GLDisplayList displayList, int cx, int cz, GLTexture texture) {
        this.displayList = displayList;
        this.cx = cx;
        this.cz = cz;
        this.texture = texture;
    }

    public static ColumnFaceDisplay create(
            int cx, int cz, Column column, Column adjacentColumn, BlockFace blockFace)
    {
        if (blockFace == null)
            return createNonCubicFaces(cx, cz, column);

        var builder = new GLDisplayList.Builder();

        switch (blockFace) {
        case TOP:
            return new ColumnFaceDisplay(
                    createTopFaces(column, builder),
                    cx, cz,
                    BlockRenderer.topFaceTextures.blockTextures);

        case BOTTOM:
            return new ColumnFaceDisplay(
                    createBottomFaces(column, builder),
                    cx, cz,
                    BlockRenderer.bottomFaceTextures.blockTextures);

        case LEFT:
            return new ColumnFaceDisplay(
                    createLeftFaces(column, adjacentColumn, builder),
                    cx, cz,
                    BlockRenderer.leftFaceTextures.blockTextures);

        case RIGHT:
            return new ColumnFaceDisplay(
                    createRightFaces(column, adjacentColumn, builder),
                    cx, cz,
                    BlockRenderer.rightFaceTextures.blockTextures);

        case FRONT:
            return new ColumnFaceDisplay(
                    createFrontFaces(column, adjacentColumn, builder),
                    cx, cz,
                    BlockRenderer.frontFaceTextures.blockTextures);

        case BACK:
            return new ColumnFaceDisplay(
                    createBackFaces(column, adjacentColumn, builder),
                    cx, cz,
                    BlockRenderer.backFaceTextures.blockTextures);
        }

        throw new IllegalArgumentException("blockFace");
    }

    private static GLDisplayList createTopFaces(Column column, GLDisplayList.Builder builder) {
        builder.setColor(TOP_FACE_SHADE, TOP_FACE_SHADE, TOP_FACE_SHADE);

        for (var i = 0; i < Column.MAX_HEIGHT - 1; ++i) {
            var chunk = column.getChunk(i);
            var adjacentChunk = column.getChunk(i + 1);
            if (chunk != null)
                buildTopFaces(chunk, adjacentChunk, i * Chunk.YLENGTH, builder);
        }

        final var LAST_INDEX = Column.MAX_HEIGHT - 1;
        var topChunk = column.getChunk(LAST_INDEX);
        if (topChunk != null)
            buildTopFaces(topChunk, null, LAST_INDEX * Chunk.YLENGTH, builder);

        return builder.build();
    }

    private static void buildTopFaces(
            Chunk chunk,
            Chunk adjacentChunk,
            int offset,
            GLDisplayList.Builder builder)
    {
        for (var i = 0; i < Chunk.VOLUME - Chunk.BASE_AREA; ++i) {
            var x = i % Chunk.XLENGTH;
            var z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;
            var y = i / Chunk.BASE_AREA;

            var blockID = chunk.getBlockFullID(i);
            var adjacentBlockID = chunk.getBlockFullID(i + Chunk.BASE_AREA);

            BlockRenderer.byFullID(blockID).pushTopFaces(
                    x, y + offset, z, blockID, adjacentBlockID, builder);
        }

        if (adjacentChunk == null) {
            for (var i = 0; i < Chunk.BASE_AREA; ++i) {
                var x = i % Chunk.XLENGTH;
                var z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;

                var blockID = chunk.getBlockFullID(i + Chunk.VOLUME - Chunk.BASE_AREA);

                BlockRenderer.byFullID(blockID).pushTopFaces(
                        x, (Chunk.YLENGTH - 1) + offset, z, blockID, 0, builder);
            }
        }
        else {
            for (var i = 0; i < Chunk.BASE_AREA; ++i) {
                var x = i % Chunk.XLENGTH;
                var z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;

                var blockID = chunk.getBlockFullID(i + Chunk.VOLUME - Chunk.BASE_AREA);
                var adjacentBlockID = adjacentChunk.getBlockFullID(i);

                BlockRenderer.byFullID(blockID).pushTopFaces(
                        x, (Chunk.YLENGTH - 1) + offset, z, blockID, adjacentBlockID, builder);
            }
        }
    }

    private static GLDisplayList createBottomFaces(Column column, GLDisplayList.Builder builder) {
        builder.setColor(BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE);

        var bottomChunk = column.getChunk(0);
        if (bottomChunk != null)
            buildBottomFaces(bottomChunk, null, 0, builder);

        for (var i = 1; i < Column.MAX_HEIGHT; ++i) {
            var chunk = column.getChunk(i);
            var adjacentChunk = column.getChunk(i - 1);
            if (chunk != null)
                buildBottomFaces(chunk, adjacentChunk, i * Chunk.YLENGTH, builder);
        }

        return builder.build();
    }

    private static void buildBottomFaces(
            Chunk chunk,
            Chunk adjacentChunk,
            int offset,
            GLDisplayList.Builder builder)
    {
        for (var i = Chunk.BASE_AREA; i < Chunk.VOLUME; ++i) {
            var x = i % Chunk.XLENGTH;
            var z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;
            var y = i / Chunk.BASE_AREA;

            var blockID = chunk.getBlockFullID(i);
            var adjacentBlockID = chunk.getBlockFullID(i - Chunk.BASE_AREA);

            BlockRenderer.byFullID(blockID).pushBottomFaces(
                    x, y + offset, z, blockID, adjacentBlockID, builder);
        }

        if (adjacentChunk == null) {
            for (var i = 0; i < Chunk.BASE_AREA; ++i) {
                var x = i % Chunk.XLENGTH;
                var z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;

                var blockID = chunk.getBlockFullID(i);

                BlockRenderer.byFullID(blockID).pushBottomFaces(
                        x, offset, z, blockID, 0, builder);
            }
        }
        else {
            for (var i = 0; i < Chunk.BASE_AREA; ++i) {
                var x = i % Chunk.XLENGTH;
                var z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;

                var blockID = chunk.getBlockFullID(i);
                var adjacentBlockID = adjacentChunk.getBlockFullID(i + Chunk.VOLUME - Chunk.BASE_AREA);

                BlockRenderer.byFullID(blockID).pushBottomFaces(
                        x, offset, z, blockID, adjacentBlockID, builder);
            }
        }
    }

    private static GLDisplayList createLeftFaces(Column column, Column adjacentColumn, GLDisplayList.Builder builder) {
        builder.setColor(LEFT_FACE_SHADE, LEFT_FACE_SHADE, LEFT_FACE_SHADE);

        for (var i = 0; i < Column.MAX_HEIGHT; ++i) {
            var chunk = column.getChunk(i);
            var cullEdges = adjacentColumn == null;
            var adjacentChunk = cullEdges ? null : adjacentColumn.getChunk(i);
            if (chunk != null)
                buildLeftFaces(chunk, adjacentChunk, cullEdges, i * Chunk.YLENGTH, builder);
        }

        return builder.build();
    }

    private static void buildLeftFaces(
            Chunk chunk,
            Chunk adjacentChunk,
            boolean cullEdges,
            int offset,
            GLDisplayList.Builder builder)
    {
        for (var y = 0; y < Chunk.YLENGTH; ++y) {
            for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                for (var x = 1; x < Chunk.XLENGTH; ++x) {
                    var blockID = chunk.getBlockFullID(x, y, z);
                    var adjacentBlockID = chunk.getBlockFullID(x - 1, y, z);

                    BlockRenderer.byFullID(blockID).pushLeftFaces(
                            x, y + offset, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (!cullEdges) {
            if (adjacentChunk == null) {
                for (var y = 0; y < Chunk.YLENGTH; ++y) {
                    for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                        var blockID = chunk.getBlockFullID(0, y, z);

                        BlockRenderer.byFullID(blockID).pushLeftFaces(
                                0, y + offset, z, blockID, 0, builder);
                    }
                }
            }
            else {
                for (var y = 0; y < Chunk.YLENGTH; ++y) {
                    for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                        var blockID = chunk.getBlockFullID(0, y, z);
                        var adjacentBlockID = adjacentChunk.getBlockFullID(Column.XLENGTH - 1, y, z);

                        BlockRenderer.byFullID(blockID).pushLeftFaces(
                                0, y + offset, z, blockID, adjacentBlockID, builder);
                    }
                }
            }
        }
    }

    private static GLDisplayList createRightFaces(Column column, Column adjacentColumn, GLDisplayList.Builder builder) {
        builder.setColor(RIGHT_FACE_SHADE, RIGHT_FACE_SHADE, RIGHT_FACE_SHADE);

        for (var i = 0; i < Column.MAX_HEIGHT; ++i) {
            var chunk = column.getChunk(i);
            var cullEdges = adjacentColumn == null;
            var adjacentChunk = cullEdges ? null : adjacentColumn.getChunk(i);
            if (chunk != null)
                buildRightFaces(chunk, adjacentChunk, cullEdges, i * Chunk.YLENGTH, builder);
        }

        return builder.build();
    }

    private static void buildRightFaces(
            Chunk chunk,
            Chunk adjacentChunk,
            boolean cullEdges,
            int offset,
            GLDisplayList.Builder builder)
    {
        for (var y = 0; y < Chunk.YLENGTH; ++y) {
            for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                for (var x = 0; x < Chunk.XLENGTH - 1; ++x) {
                    var blockID = chunk.getBlockFullID(x, y, z);
                    var adjacentBlockID = chunk.getBlockFullID(x + 1, y, z);

                    BlockRenderer.byFullID(blockID).pushRightFaces(
                            x, y + offset, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (!cullEdges) {
            if (adjacentChunk == null) {
                for (var y = 0; y < Chunk.YLENGTH; ++y) {
                    for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                        var blockID = chunk.getBlockFullID(Column.XLENGTH - 1, y, z);

                        BlockRenderer.byFullID(blockID).pushRightFaces(
                                Column.XLENGTH - 1, y + offset, z, blockID, 0, builder);
                    }
                }
            }
            else {
                for (var y = 0; y < Chunk.YLENGTH; ++y) {
                    for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                        var blockID = chunk.getBlockFullID(Column.XLENGTH - 1, y, z);
                        var adjacentBlockID = adjacentChunk.getBlockFullID(0, y, z);

                        BlockRenderer.byFullID(blockID).pushRightFaces(
                                Column.XLENGTH - 1, y + offset, z, blockID, adjacentBlockID, builder);
                    }
                }
            }
        }
    }

    private static GLDisplayList createFrontFaces(Column column, Column adjacentColumn, GLDisplayList.Builder builder) {
        builder.setColor(FRONT_FACE_SHADE, FRONT_FACE_SHADE, FRONT_FACE_SHADE);

        for (var i = 0; i < Column.MAX_HEIGHT; ++i) {
            var chunk = column.getChunk(i);
            var cullEdges = adjacentColumn == null;
            var adjacentChunk = cullEdges ? null : adjacentColumn.getChunk(i);
            if (chunk != null)
                buildFrontFaces(chunk, adjacentChunk, cullEdges, i * Chunk.YLENGTH, builder);
        }

        return builder.build();
    }

    private static void buildFrontFaces(
            Chunk chunk,
            Chunk adjacentChunk,
            boolean cullEdges,
            int offset,
            GLDisplayList.Builder builder)
    {
        for (var y = 0; y < Chunk.YLENGTH; ++y) {
            for (var z = 0; z < Chunk.ZLENGTH - 1; ++z) {
                for (var x = 0; x < Chunk.XLENGTH; ++x) {
                    var blockID = chunk.getBlockFullID(x, y, z);
                    var adjacentBlockID = chunk.getBlockFullID(x, y, z + 1);

                    BlockRenderer.byFullID(blockID).pushFrontFaces(
                            x, y + offset, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (!cullEdges) {
            if (adjacentChunk == null) {
                for (var y = 0; y < Chunk.YLENGTH; ++y) {
                    for (var x = 0; x < Chunk.XLENGTH; ++x) {
                        var blockID = chunk.getBlockFullID(x, y, Column.ZLENGTH - 1);

                        BlockRenderer.byFullID(blockID).pushFrontFaces(
                                x, y + offset, Column.ZLENGTH - 1, blockID, 0, builder);
                    }
                }
            }
            else {
                for (var y = 0; y < Chunk.YLENGTH; ++y) {
                    for (var x = 0; x < Chunk.XLENGTH; ++x) {
                        var blockID = chunk.getBlockFullID(x, y, Column.ZLENGTH - 1);
                        var adjacentBlockID = adjacentChunk.getBlockFullID(x, y, 0);

                        BlockRenderer.byFullID(blockID).pushFrontFaces(
                                x, y + offset, Column.ZLENGTH - 1, blockID, adjacentBlockID, builder);
                    }
                }
            }
        }
    }

    private static GLDisplayList createBackFaces(Column column, Column adjacentColumn, GLDisplayList.Builder builder) {
        builder.setColor(BACK_FACE_SHADE, BACK_FACE_SHADE, BACK_FACE_SHADE);

        for (var i = 0; i < Column.MAX_HEIGHT; ++i) {
            var chunk = column.getChunk(i);
            var cullEdges = adjacentColumn == null;
            var adjacentChunk = cullEdges ? null : adjacentColumn.getChunk(i);
            if (chunk != null)
                buildBackFaces(chunk, adjacentChunk, cullEdges, i * Chunk.YLENGTH, builder);
        }

        return builder.build();
    }

    private static void buildBackFaces(
            Chunk chunk,
            Chunk adjacentChunk,
            boolean cullEdges,
            int offset,
            GLDisplayList.Builder builder)
    {
        for (var y = 0; y < Chunk.YLENGTH; ++y) {
            for (var z = 1; z < Chunk.ZLENGTH; ++z) {
                for (var x = 0; x < Chunk.XLENGTH; ++x) {
                    var blockID = chunk.getBlockFullID(x, y, z);
                    var adjacentBlockID = chunk.getBlockFullID(x, y, z - 1);

                    BlockRenderer.byFullID(blockID).pushBackFaces(
                            x, y + offset, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (!cullEdges) {
            if (adjacentChunk == null) {
                for (var y = 0; y < Chunk.YLENGTH; ++y) {
                    for (var x = 0; x < Chunk.XLENGTH; ++x) {
                        var blockID = chunk.getBlockFullID(x, y, 0);

                        BlockRenderer.byFullID(blockID).pushBackFaces(
                                x, y + offset, 0, blockID, 0, builder);
                    }
                }
            }
            else {
                for (var y = 0; y < Chunk.YLENGTH; ++y) {
                    for (var x = 0; x < Chunk.XLENGTH; ++x) {
                        var blockID = chunk.getBlockFullID(x, y, 0);
                        var adjacentBlockID = adjacentChunk.getBlockFullID(x, y, Column.ZLENGTH - 1);

                        BlockRenderer.byFullID(blockID).pushBackFaces(
                                x, y + offset, 0, blockID, adjacentBlockID, builder);
                    }
                }
            }
        }
    }

    private static ColumnFaceDisplay createNonCubicFaces(int cx, int cz, Column column) {
        var builder = new GLDisplayList.Builder();
        var texture = BlockRenderer.topFaceTextures.blockTextures;
        var shouldCreateDisplayList = false;

        builder.setColor(NON_CUBIC_SHADE, NON_CUBIC_SHADE, NON_CUBIC_SHADE);

        for (var chunkIndex = 0; chunkIndex < Column.MAX_HEIGHT; ++chunkIndex) {
            var chunk = column.getChunk(chunkIndex);
            if (chunk == null)
                continue;

            var offset = chunkIndex * Chunk.YLENGTH;

            for (var i = 0; i < Chunk.VOLUME; ++i) {
                var x = i % Chunk.XLENGTH;
                var z = (i / Chunk.XLENGTH) % Chunk.ZLENGTH;
                var y = i / Chunk.BASE_AREA;
                var blockID = chunk.getBlockFullID(x, y, z);

                if (!BlockRenderer.byFullID(blockID).nonCubic)
                    continue;

                BlockRenderer.byFullID(blockID).pushNonCubic(x, y + offset, z, blockID, builder);
                shouldCreateDisplayList = true;
            }
        }

        @SuppressWarnings("resource")
        var displayList = (GLDisplayList) null;

        if (shouldCreateDisplayList)
            displayList = builder.build();
        else
            builder.build().close();

        return new ColumnFaceDisplay(displayList, cx, cz, texture);
    }

    @Override
    public void close() {
        if (displayList != null)
            displayList.close();
    }

    /**
     * Displays the column's containing blocks.
     */
    public void displayBlocks() {
        if (displayList != null)
            GLRenderContext.callDisplayList(displayList, texture);
    }

    public boolean isEmpty() {
        return displayList == null;
    }
}