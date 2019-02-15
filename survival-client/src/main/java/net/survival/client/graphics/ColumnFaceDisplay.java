package net.survival.client.graphics;

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

        for (var i = 0; i < Column.VOLUME - Column.BASE_AREA; ++i) {
            var x = i % Column.XLENGTH;
            var z = (i / Column.XLENGTH) % Column.ZLENGTH;
            var y = i / Column.BASE_AREA;

            var blockID = column.getBlockFullID(x, y, z);
            var adjacentBlockID = column.getBlockFullID(x, y + 1, z);

            BlockRenderer.byFullID(blockID).pushTopFaces(
                    x, y, z, blockID, adjacentBlockID, builder);
        }

        for (var i = Column.VOLUME - Column.BASE_AREA; i < Column.VOLUME; ++i) {
            var x = i % Column.XLENGTH;
            var z = (i / Column.XLENGTH) % Column.ZLENGTH;
            var y = i / Column.BASE_AREA;

            int blockID = column.getBlockFullID(x, y, z);

            BlockRenderer.byFullID(blockID).pushTopFaces(
                    x, y, z, blockID, (short) 0, builder);
        }

        return builder.build();
    }

    private static GLDisplayList createBottomFaces(Column column, GLDisplayList.Builder builder) {
        builder.setColor(BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE, BOTTOM_FACE_SHADE);

        for (var i = Column.BASE_AREA; i < Column.VOLUME; ++i) {
            var x = i % Column.XLENGTH;
            var z = (i / Column.XLENGTH) % Column.ZLENGTH;
            var y = i / Column.BASE_AREA;

            var blockID = column.getBlockFullID(x, y, z);
            var adjacentBlockID = column.getBlockFullID(x, y - 1, z);

            BlockRenderer.byFullID(blockID).pushBottomFaces(
                    x, y, z, blockID, adjacentBlockID, builder);
        }

        return builder.build();
    }

    private static GLDisplayList createLeftFaces(Column column, Column adjacentColumn, GLDisplayList.Builder builder) {
        builder.setColor(LEFT_FACE_SHADE, LEFT_FACE_SHADE, LEFT_FACE_SHADE);

        for (var y = 0; y < Column.YLENGTH; ++y) {
            for (var z = 0; z < Column.ZLENGTH; ++z) {
                for (var x = 1; x < Column.XLENGTH; ++x) {
                    var blockID = column.getBlockFullID(x, y, z);
                    var adjacentBlockID = column.getBlockFullID(x - 1, y, z);

                    BlockRenderer.byFullID(blockID).pushLeftFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentColumn != null) {
            for (var y = 0; y < Column.YLENGTH; ++y) {
                for (var z = 0; z < Column.ZLENGTH; ++z) {
                    var blockID = column.getBlockFullID(0, y, z);
                    var adjacentBlockID = adjacentColumn.getBlockFullID(Column.XLENGTH - 1, y, z);

                    BlockRenderer.byFullID(blockID).pushLeftFaces(
                            0, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createRightFaces(Column column, Column adjacentColumn, GLDisplayList.Builder builder) {
        builder.setColor(RIGHT_FACE_SHADE, RIGHT_FACE_SHADE, RIGHT_FACE_SHADE);

        for (var y = 0; y < Column.YLENGTH; ++y) {
            for (var z = 0; z < Column.ZLENGTH; ++z) {
                for (var x = 0; x < Column.XLENGTH - 1; ++x) {
                    var blockID = column.getBlockFullID(x, y, z);
                    var adjacentBlockID = column.getBlockFullID(x + 1, y, z);

                    BlockRenderer.byFullID(blockID).pushRightFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentColumn != null) {
            for (var y = 0; y < Column.YLENGTH; ++y) {
                for (var z = 0; z < Column.ZLENGTH; ++z) {
                    var blockID = column.getBlockFullID(Column.XLENGTH - 1, y, z);
                    var adjacentBlockID = adjacentColumn.getBlockFullID(0, y, z);

                    BlockRenderer.byFullID(blockID).pushRightFaces(
                            Column.XLENGTH - 1, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createFrontFaces(Column column, Column adjacentColumn, GLDisplayList.Builder builder) {
        builder.setColor(FRONT_FACE_SHADE, FRONT_FACE_SHADE, FRONT_FACE_SHADE);

        for (var y = 0; y < Column.YLENGTH; ++y) {
            for (var z = 0; z < Column.ZLENGTH - 1; ++z) {
                for (var x = 0; x < Column.XLENGTH; ++x) {
                    var blockID = column.getBlockFullID(x, y, z);
                    var adjacentBlockID = column.getBlockFullID(x, y, z + 1);

                    BlockRenderer.byFullID(blockID).pushFrontFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentColumn != null) {
            for (var y = 0; y < Column.YLENGTH; ++y) {
                for (var x = 0; x < Column.XLENGTH; ++x) {
                    var blockID = column.getBlockFullID(x, y, Column.ZLENGTH - 1);
                    var adjacentBlockID = adjacentColumn.getBlockFullID(x, y, 0);

                    BlockRenderer.byFullID(blockID).pushFrontFaces(
                            x, y, Column.ZLENGTH - 1, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static GLDisplayList createBackFaces(Column column, Column adjacentColumn, GLDisplayList.Builder builder) {
        builder.setColor(BACK_FACE_SHADE, BACK_FACE_SHADE, BACK_FACE_SHADE);

        for (var y = 0; y < Column.YLENGTH; ++y) {
            for (var z = 1; z < Column.ZLENGTH; ++z) {
                for (var x = 0; x < Column.XLENGTH; ++x) {
                    var blockID = column.getBlockFullID(x, y, z);
                    var adjacentBlockID = column.getBlockFullID(x, y, z - 1);

                    BlockRenderer.byFullID(blockID).pushBackFaces(
                            x, y, z, blockID, adjacentBlockID, builder);
                }
            }
        }

        if (adjacentColumn != null) {
            for (var y = 0; y < Column.YLENGTH; ++y) {
                for (var x = 0; x < Column.XLENGTH; ++x) {
                    var blockID = column.getBlockFullID(x, y, 0);
                    var adjacentBlockID = adjacentColumn.getBlockFullID(x, y, Column.ZLENGTH - 1);

                    BlockRenderer.byFullID(blockID).pushBackFaces(
                            x, y, 0, blockID, adjacentBlockID, builder);
                }
            }
        }

        return builder.build();
    }

    private static ColumnFaceDisplay createNonCubicFaces(int cx, int cz, Column column) {
        var builder = new GLDisplayList.Builder();
        var texture = BlockRenderer.topFaceTextures.blockTextures;
        var shouldCreateDisplayList = false;

        builder.setColor(NON_CUBIC_SHADE, NON_CUBIC_SHADE, NON_CUBIC_SHADE);

        for (var i = 0; i < Column.VOLUME; ++i) {
            var x = i % Column.XLENGTH;
            var z = (i / Column.XLENGTH) % Column.ZLENGTH;
            var y = i / Column.BASE_AREA;
            var blockID = column.getBlockFullID(x, y, z);

            if (!BlockRenderer.byFullID(blockID).nonCubic)
                continue;

            BlockRenderer.byFullID(blockID).pushNonCubic(x, y, z, blockID, builder);
            shouldCreateDisplayList = true;
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