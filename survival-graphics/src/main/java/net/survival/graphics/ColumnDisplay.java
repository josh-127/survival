package net.survival.graphics;

import net.survival.block.Chunk;
import net.survival.block.Column;
import net.survival.graphics.blockrenderer.BlockRenderer;
import net.survival.graphics.opengl.GLDisplayList;
import net.survival.graphics.opengl.GLRenderContext;
import net.survival.graphics.opengl.GLTexture;

class ColumnDisplay implements GraphicsResource
{
    private static final Chunk EMPTY_CHUNK = new Chunk();

    public final GLDisplayList displayList;
    public final int cx;
    public final int cz;

    private ColumnDisplay(GLDisplayList displayList, int cx, int cz) {
        this.displayList = displayList;
        this.cx = cx;
        this.cz = cz;
    }

    public static ColumnDisplay create(
            int cx,
            int cz,
            Column column,
            Column leftAdjColumn,
            Column rightAdjColumn,
            Column frontAdjColumn,
            Column backAdjColumn)
    {
        var builder = new GLDisplayList.Builder();

        for (var i = 0; i < column.getHeight(); ++i) {
            var chunk = column.getChunk(i);

            var topAdjChunk = (Chunk) null;
            var bottomAdjChunk = (Chunk) null;
            var leftAdjChunk = (Chunk) null;
            var rightAdjChunk = (Chunk) null;
            var frontAdjChunk = (Chunk) null;
            var backAdjChunk = (Chunk) null;

            topAdjChunk = column.getChunk(i + 1);
            if (topAdjChunk == null) {
                topAdjChunk = EMPTY_CHUNK;
            }

            if (i > 0) {
                bottomAdjChunk = column.getChunk(i - 1);
                if (bottomAdjChunk == null) {
                    bottomAdjChunk = EMPTY_CHUNK;
                }
            }
            if (leftAdjColumn != null) {
                leftAdjChunk = leftAdjColumn.getChunk(i);
                if (leftAdjChunk == null) {
                    leftAdjChunk = EMPTY_CHUNK;
                }
            }
            if (rightAdjColumn != null) {
                rightAdjChunk = rightAdjColumn.getChunk(i);
                if (rightAdjChunk == null) {
                    rightAdjChunk = EMPTY_CHUNK;
                }
            }
            if (frontAdjColumn != null) {
                frontAdjChunk = frontAdjColumn.getChunk(i);
                if (frontAdjChunk == null) {
                    frontAdjChunk = EMPTY_CHUNK;
                }
            }
            if (backAdjColumn != null) {
                backAdjChunk = backAdjColumn.getChunk(i);
                if (backAdjChunk == null) {
                    backAdjChunk = EMPTY_CHUNK;
                }
            }

            pushInternalBlocks(i, chunk, builder);

            if (topAdjChunk != null) {
                pushTopBlocks(i, chunk, topAdjChunk, builder);

                if (leftAdjChunk != null) {
                    pushTopLeftEdgeBlocks(i, chunk, topAdjChunk, leftAdjChunk, builder);
                }
                if (rightAdjChunk != null) {
                    pushTopRightEdgeBlocks(i, chunk, topAdjChunk, rightAdjChunk, builder);
                }
                if (frontAdjChunk != null) {
                    pushTopFrontEdgeBlocks(i, chunk, topAdjChunk, frontAdjChunk, builder);
                }
                if (backAdjChunk != null) {
                    pushTopBackEdgeBlocks(i, chunk, topAdjChunk, backAdjChunk, builder);
                }

                if (frontAdjChunk != null && leftAdjChunk != null) {
                    pushTopFrontLeftCornerBlock(
                            i, chunk, topAdjChunk, frontAdjChunk, leftAdjChunk, builder);
                }
                if (frontAdjChunk != null && rightAdjChunk != null) {
                    pushTopFrontRightCornerBlock(
                            i, chunk, topAdjChunk, frontAdjChunk, rightAdjChunk, builder);
                }
                if (backAdjChunk != null && leftAdjChunk != null) {
                    pushTopBackLeftCornerBlock(
                            i, chunk, topAdjChunk, backAdjChunk, leftAdjChunk, builder);
                }
                if (backAdjChunk != null && rightAdjChunk != null) {
                    pushTopBackRightCornerBlock(
                            i, chunk, topAdjChunk, backAdjChunk, rightAdjChunk, builder);
                }
            }

            if (bottomAdjChunk != null) {
                pushBottomBlocks(i, chunk, bottomAdjChunk, builder);

                if (leftAdjChunk != null) {
                    pushBottomLeftEdgeBlocks(
                            i, chunk, bottomAdjChunk, leftAdjChunk, builder);
                }
                if (rightAdjChunk != null) {
                    pushBottomRightEdgeBlocks(
                            i, chunk, bottomAdjChunk, rightAdjChunk, builder);
                }
                if (frontAdjChunk != null) {
                    pushBottomFrontEdgeBlocks(
                            i, chunk, bottomAdjChunk, frontAdjChunk, builder);
                }
                if (backAdjChunk != null) {
                    pushBottomBackEdgeBlocks(
                            i, chunk, bottomAdjChunk, backAdjChunk, builder);
                }

                if (frontAdjChunk != null && leftAdjChunk != null) {
                    pushBottomFrontLeftCornerBlock(
                            i, chunk, bottomAdjChunk, frontAdjChunk, leftAdjChunk, builder);
                }
                if (frontAdjChunk != null && rightAdjChunk != null) {
                    pushBottomFrontRightCornerBlock(
                            i, chunk, bottomAdjChunk, frontAdjChunk, rightAdjChunk, builder);
                }
                if (backAdjChunk != null && leftAdjChunk != null) {
                    pushBottomBackLeftCornerBlock(
                            i, chunk, bottomAdjChunk, backAdjChunk, leftAdjChunk, builder);
                }
                if (backAdjChunk != null && rightAdjChunk != null) {
                    pushBottomBackRightCornerBlock(
                            i, chunk, bottomAdjChunk, backAdjChunk, rightAdjChunk, builder);
                }
            }

            if (leftAdjChunk != null) {
                pushLeftBlocks(i, chunk, leftAdjChunk, builder);
            }
            if (rightAdjChunk != null) {
                pushRightBlocks(i, chunk, rightAdjChunk, builder);
            }
            if (frontAdjChunk != null) {
                pushFrontBlocks(i, chunk, frontAdjChunk, builder);

                if (leftAdjChunk != null) {
                    pushFrontLeftBlocks(i, chunk, leftAdjChunk, frontAdjChunk, builder);
                }
                if (rightAdjChunk != null) {
                    pushFrontRightBlocks(i, chunk, rightAdjChunk, frontAdjChunk, builder);
                }
            }
            if (backAdjChunk != null) {
                pushBackBlocks(i, chunk, backAdjChunk, builder);

                if (leftAdjChunk != null) {
                    pushBackLeftBlocks(i, chunk, leftAdjChunk, backAdjChunk, builder);
                }
                if (rightAdjChunk != null) {
                    pushBackRightBlocks(i, chunk, rightAdjChunk, backAdjChunk, builder);
                }
            }
        }

        var displayList = builder.build();

        return new ColumnDisplay(displayList, cx, cz);
    }

    private static void pushInternalBlocks(
            int chunkIndex,
            Chunk chunk,
            GLDisplayList.Builder builder)
    {
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
                for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                    var blockId          = chunk.getBlockFullId(x,     y,     z);
                    var topAdjBlockId    = chunk.getBlockFullId(x,     y + 1, z);
                    var bottomAdjBlockId = chunk.getBlockFullId(x,     y - 1, z);
                    var leftAdjBlockId   = chunk.getBlockFullId(x - 1, y,     z);
                    var rightAdjBlockId  = chunk.getBlockFullId(x + 1, y,     z);
                    var frontAdjBlockId  = chunk.getBlockFullId(x,     y,     z + 1);
                    var backAdjBlockId   = chunk.getBlockFullId(x,     y,     z - 1);

                    BlockRenderer.byFullId(blockId).pushVertices(
                            x,
                            y + offsetY,
                            z,
                            blockId,
                            topAdjBlockId,
                            bottomAdjBlockId,
                            leftAdjBlockId,
                            rightAdjBlockId,
                            frontAdjBlockId,
                            backAdjBlockId,
                            builder);
                }
            }
        }
    }

    private static void pushTopBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                var blockId          = chunk.getBlockFullId   (x,     Y,     z);
                var topAdjBlockId    = adjChunk.getBlockFullId(x,     0,     z);
                var bottomAdjBlockId = chunk.getBlockFullId   (x,     Y - 1, z);
                var leftAdjBlockId   = chunk.getBlockFullId   (x - 1, Y,     z);
                var rightAdjBlockId  = chunk.getBlockFullId   (x + 1, Y,     z);
                var frontAdjBlockId  = chunk.getBlockFullId   (x,     Y,     z + 1);
                var backAdjBlockId   = chunk.getBlockFullId   (x,     Y,     z - 1);

                BlockRenderer.byFullId(blockId).pushVertices(
                        x,
                        Y + offsetY,
                        z,
                        blockId,
                        topAdjBlockId,
                        bottomAdjBlockId,
                        leftAdjBlockId,
                        rightAdjBlockId,
                        frontAdjBlockId,
                        backAdjBlockId,
                        builder);
            }
        }
    }

    private static void pushTopLeftEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk leftAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            var blockId          = chunk.getBlockFullId       (0,           Y,     z);
            var topAdjBlockId    = topAdjChunk.getBlockFullId (0,           0,     z);
            var bottomAdjBlockId = chunk.getBlockFullId       (0,           Y - 1, z);
            var leftAdjBlockId   = leftAdjChunk.getBlockFullId(XLENGTH - 1, Y,     z);
            var rightAdjBlockId  = chunk.getBlockFullId       (1,           Y,     z);
            var frontAdjBlockId  = chunk.getBlockFullId       (0,           Y,     z + 1);
            var backAdjBlockId   = chunk.getBlockFullId       (0,           Y,     z - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    0,
                    Y + offsetY,
                    z,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushTopRightEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk rightAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Y = Chunk.YLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            var blockId          = chunk.getBlockFullId        (X,     Y,     z);
            var topAdjBlockId    = topAdjChunk.getBlockFullId  (X,     0,     z);
            var bottomAdjBlockId = chunk.getBlockFullId        (X,     Y - 1, z);
            var leftAdjBlockId   = chunk.getBlockFullId        (X - 1, Y,     z);
            var rightAdjBlockId  = rightAdjChunk.getBlockFullId(0,     Y,     z);
            var frontAdjBlockId  = chunk.getBlockFullId        (X,     Y,     z + 1);
            var backAdjBlockId   = chunk.getBlockFullId        (X,     Y,     z - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    X,
                    Y + offsetY,
                    z,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushTopFrontEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk frontAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
            var blockId          = chunk.getBlockFullId        (x,     Y,     Z);
            var topAdjBlockId    = topAdjChunk.getBlockFullId  (x,     0,     Z);
            var bottomAdjBlockId = chunk.getBlockFullId        (x,     Y - 1, Z);
            var leftAdjBlockId   = chunk.getBlockFullId        (x - 1, Y,     Z);
            var rightAdjBlockId  = chunk.getBlockFullId        (x + 1, Y,     Z);
            var frontAdjBlockId  = frontAdjChunk.getBlockFullId(x,     Y,     0);
            var backAdjBlockId   = chunk.getBlockFullId        (x,     Y,     Z - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    x,
                    Y + offsetY,
                    Z,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushTopBackEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk backAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
            var blockId          = chunk.getBlockFullId       (x,     Y,     0);
            var topAdjBlockId    = topAdjChunk.getBlockFullId (x,     0,     0);
            var bottomAdjBlockId = chunk.getBlockFullId       (x,     Y - 1, 0);
            var leftAdjBlockId   = chunk.getBlockFullId       (x - 1, Y,     0);
            var rightAdjBlockId  = chunk.getBlockFullId       (x + 1, Y,     0);
            var frontAdjBlockId  = chunk.getBlockFullId       (x,     Y,     1);
            var backAdjBlockId   = backAdjChunk.getBlockFullId(x,     Y,     ZLENGTH - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    x,
                    Y + offsetY,
                    0,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushTopFrontLeftCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk frontAdjChunk,
            Chunk leftAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var blockId          = chunk.getBlockFullId        (0,           Y,     Z);
        var topAdjBlockId    = topAdjChunk.getBlockFullId  (0,           0,     Z);
        var bottomAdjBlockId = chunk.getBlockFullId        (0,           Y - 1, Z);
        var leftAdjBlockId   = leftAdjChunk.getBlockFullId (XLENGTH - 1, Y,     Z);
        var rightAdjBlockId  = chunk.getBlockFullId        (1,           Y,     Z);
        var frontAdjBlockId  = frontAdjChunk.getBlockFullId(0,           Y,     0);
        var backAdjBlockId   = chunk.getBlockFullId        (0,           Y,     Z - 1);

        BlockRenderer.byFullId(blockId).pushVertices(
                0,
                Y + offsetY,
                Z,
                blockId,
                topAdjBlockId,
                bottomAdjBlockId,
                leftAdjBlockId,
                rightAdjBlockId,
                frontAdjBlockId,
                backAdjBlockId,
                builder);
    }

    private static void pushTopFrontRightCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk frontAdjChunk,
            Chunk rightAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Y = Chunk.YLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var blockId          = chunk.getBlockFullId        (X,     Y,     Z);
        var topAdjBlockId    = topAdjChunk.getBlockFullId  (X,     0,     Z);
        var bottomAdjBlockId = chunk.getBlockFullId        (X,     Y - 1, Z);
        var leftAdjBlockId   = chunk.getBlockFullId        (X - 1, Y,     Z);
        var rightAdjBlockId  = rightAdjChunk.getBlockFullId(0,     Y,     Z);
        var frontAdjBlockId  = frontAdjChunk.getBlockFullId(X,     Y,     0);
        var backAdjBlockId   = chunk.getBlockFullId        (X,     Y,     Z - 1);

        BlockRenderer.byFullId(blockId).pushVertices(
                X,
                Y + offsetY,
                Z,
                blockId,
                topAdjBlockId,
                bottomAdjBlockId,
                leftAdjBlockId,
                rightAdjBlockId,
                frontAdjBlockId,
                backAdjBlockId,
                builder);
    }

    private static void pushTopBackLeftCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk backAdjChunk,
            Chunk leftAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var blockId          = chunk.getBlockFullId       (0,           Y,     0);
        var topAdjBlockId    = topAdjChunk.getBlockFullId (0,           0,     0);
        var bottomAdjBlockId = chunk.getBlockFullId       (0,           Y - 1, 0);
        var leftAdjBlockId   = leftAdjChunk.getBlockFullId(XLENGTH - 1, Y,     0);
        var rightAdjBlockId  = chunk.getBlockFullId       (1,           Y,     0);
        var frontAdjBlockId  = chunk.getBlockFullId       (0,           Y,     1);
        var backAdjBlockId   = backAdjChunk.getBlockFullId(0,           Y,     ZLENGTH - 1);

        BlockRenderer.byFullId(blockId).pushVertices(
                0,
                Y + offsetY,
                0,
                blockId,
                topAdjBlockId,
                bottomAdjBlockId,
                leftAdjBlockId,
                rightAdjBlockId,
                frontAdjBlockId,
                backAdjBlockId,
                builder);
    }

    private static void pushTopBackRightCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk backAdjChunk,
            Chunk rightAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Y = Chunk.YLENGTH - 1;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var blockId          = chunk.getBlockFullId        (X,     Y,     0);
        var topAdjBlockId    = topAdjChunk.getBlockFullId  (X,     0,     0);
        var bottomAdjBlockId = chunk.getBlockFullId        (X,     Y - 1, 0);
        var leftAdjBlockId   = chunk.getBlockFullId        (X - 1, Y,     0);
        var rightAdjBlockId  = rightAdjChunk.getBlockFullId(0,     Y,     0);
        var frontAdjBlockId  = chunk.getBlockFullId        (X,     Y,     1);
        var backAdjBlockId   = backAdjChunk.getBlockFullId (X,     Y,     ZLENGTH - 1);

        BlockRenderer.byFullId(blockId).pushVertices(
                X,
                Y + offsetY,
                0,
                blockId,
                topAdjBlockId,
                bottomAdjBlockId,
                leftAdjBlockId,
                rightAdjBlockId,
                frontAdjBlockId,
                backAdjBlockId,
                builder);
    }

    private static void pushBottomBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            GLDisplayList.Builder builder)
    {
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                var blockId          = chunk.getBlockFullId   (x,     0,           z);
                var topAdjBlockId    = chunk.getBlockFullId   (x,     1,           z);
                var bottomAdjBlockId = adjChunk.getBlockFullId(x,     YLENGTH - 1, z);
                var leftAdjBlockId   = chunk.getBlockFullId   (x - 1, 0,           z);
                var rightAdjBlockId  = chunk.getBlockFullId   (x + 1, 0,           z);
                var frontAdjBlockId  = chunk.getBlockFullId   (x,     0,           z + 1);
                var backAdjBlockId   = chunk.getBlockFullId   (x,     0,           z - 1);

                BlockRenderer.byFullId(blockId).pushVertices(
                        x,
                        offsetY,
                        z,
                        blockId,
                        topAdjBlockId,
                        bottomAdjBlockId,
                        leftAdjBlockId,
                        rightAdjBlockId,
                        frontAdjBlockId,
                        backAdjBlockId,
                        builder);
            }
        }
    }

    private static void pushBottomLeftEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk leftAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var XLENGTH = Chunk.XLENGTH;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            var blockId          = chunk.getBlockFullId         (0,           0,           z);
            var topAdjBlockId    = chunk.getBlockFullId         (0,           1,           z);
            var bottomAdjBlockId = bottomAdjChunk.getBlockFullId(0,           YLENGTH - 1, z);
            var leftAdjBlockId   = leftAdjChunk.getBlockFullId  (XLENGTH - 1, 0,           z);
            var rightAdjBlockId  = chunk.getBlockFullId         (1,           0,           z);
            var frontAdjBlockId  = chunk.getBlockFullId         (0,           0,           z + 1);
            var backAdjBlockId   = chunk.getBlockFullId         (0,           0,           z - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    0,
                    offsetY,
                    z,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushBottomRightEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk rightAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            var blockId          = chunk.getBlockFullId         (X,     0,           z);
            var topAdjBlockId    = chunk.getBlockFullId         (X,     1,           z);
            var bottomAdjBlockId = bottomAdjChunk.getBlockFullId(X,     YLENGTH - 1, z);
            var leftAdjBlockId   = chunk.getBlockFullId         (X - 1, 0,           z);
            var rightAdjBlockId  = rightAdjChunk.getBlockFullId (0,     0,           z);
            var frontAdjBlockId  = chunk.getBlockFullId         (X,     0,           z + 1);
            var backAdjBlockId   = chunk.getBlockFullId         (X,     0,           z - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    X,
                    offsetY,
                    z,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushBottomFrontEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk frontAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var Z = Chunk.ZLENGTH - 1;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
            var blockId          = chunk.getBlockFullId         (x,     0,           Z);
            var topAdjBlockId    = chunk.getBlockFullId         (x,     1,           Z);
            var bottomAdjBlockId = bottomAdjChunk.getBlockFullId(x,     YLENGTH - 1, Z);
            var leftAdjBlockId   = chunk.getBlockFullId         (x - 1, 0,           Z);
            var rightAdjBlockId  = chunk.getBlockFullId         (x + 1, 0,           Z);
            var frontAdjBlockId  = frontAdjChunk.getBlockFullId (x,     0,           0);
            var backAdjBlockId   = chunk.getBlockFullId         (x,     0,           Z - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    x,
                    offsetY,
                    Z,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushBottomBackEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk backAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var YLENGTH = Chunk.YLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
            var blockId          = chunk.getBlockFullId         (x,     0,           0);
            var topAdjBlockId    = chunk.getBlockFullId         (x,     1,           0);
            var bottomAdjBlockId = bottomAdjChunk.getBlockFullId(x,     YLENGTH - 1, 0);
            var leftAdjBlockId   = chunk.getBlockFullId         (x - 1, 0,           0);
            var rightAdjBlockId  = chunk.getBlockFullId         (x + 1, 0,           0);
            var frontAdjBlockId  = chunk.getBlockFullId         (x,     0,           1);
            var backAdjBlockId   = backAdjChunk.getBlockFullId  (x,     0,           ZLENGTH - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    x,
                    offsetY,
                    0,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushBottomFrontLeftCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk frontAdjChunk,
            Chunk leftAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var Z = Chunk.ZLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var blockId          = chunk.getBlockFullId         (0,           0,           Z);
        var topAdjBlockId    = chunk.getBlockFullId         (0,           1,           Z);
        var bottomAdjBlockId = bottomAdjChunk.getBlockFullId(0,           YLENGTH - 1, Z);
        var leftAdjBlockId   = leftAdjChunk.getBlockFullId  (XLENGTH - 1, 0,           Z);
        var rightAdjBlockId  = chunk.getBlockFullId         (1,           0,           Z);
        var frontAdjBlockId  = frontAdjChunk.getBlockFullId (0,           0,           0);
        var backAdjBlockId   = chunk.getBlockFullId         (0,           0,           Z - 1);

        BlockRenderer.byFullId(blockId).pushVertices(
                0,
                offsetY,
                Z,
                blockId,
                topAdjBlockId,
                bottomAdjBlockId,
                leftAdjBlockId,
                rightAdjBlockId,
                frontAdjBlockId,
                backAdjBlockId,
                builder);
    }

    private static void pushBottomFrontRightCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk frontAdjChunk,
            Chunk rightAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var blockId          = chunk.getBlockFullId         (X,     0,           Z);
        var topAdjBlockId    = chunk.getBlockFullId         (X,     1,           Z);
        var bottomAdjBlockId = bottomAdjChunk.getBlockFullId(X,     YLENGTH - 1, Z);
        var leftAdjBlockId   = chunk.getBlockFullId         (X - 1, 0,           Z);
        var rightAdjBlockId  = rightAdjChunk.getBlockFullId (0,     0,           Z);
        var frontAdjBlockId  = frontAdjChunk.getBlockFullId (X,     0,           0);
        var backAdjBlockId   = chunk.getBlockFullId         (X,     0,           Z - 1);

        BlockRenderer.byFullId(blockId).pushVertices(
                X,
                offsetY,
                Z,
                blockId,
                topAdjBlockId,
                bottomAdjBlockId,
                leftAdjBlockId,
                rightAdjBlockId,
                frontAdjBlockId,
                backAdjBlockId,
                builder);
    }

    private static void pushBottomBackLeftCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk backAdjChunk,
            Chunk leftAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var XLENGTH = Chunk.XLENGTH;
        final var YLENGTH = Chunk.YLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var blockId          = chunk.getBlockFullId         (0,           0,           0);
        var topAdjBlockId    = chunk.getBlockFullId         (0,           1,           0);
        var bottomAdjBlockId = bottomAdjChunk.getBlockFullId(0,           YLENGTH - 1, 0);
        var leftAdjBlockId   = leftAdjChunk.getBlockFullId  (XLENGTH - 1, 0,           0);
        var rightAdjBlockId  = chunk.getBlockFullId         (1,           0,           0);
        var frontAdjBlockId  = chunk.getBlockFullId         (0,           0,           1);
        var backAdjBlockId   = backAdjChunk.getBlockFullId  (0,           0,           ZLENGTH - 1);

        BlockRenderer.byFullId(blockId).pushVertices(
                0,
                offsetY,
                0,
                blockId,
                topAdjBlockId,
                bottomAdjBlockId,
                leftAdjBlockId,
                rightAdjBlockId,
                frontAdjBlockId,
                backAdjBlockId,
                builder);
    }

    private static void pushBottomBackRightCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk backAdjChunk,
            Chunk rightAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var YLENGTH = Chunk.YLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var blockId          = chunk.getBlockFullId         (X,     0,           0);
        var topAdjBlockId    = chunk.getBlockFullId         (X,     1,           0);
        var bottomAdjBlockId = bottomAdjChunk.getBlockFullId(X,     YLENGTH - 1, 0);
        var leftAdjBlockId   = chunk.getBlockFullId         (X - 1, 0,           0);
        var rightAdjBlockId  = rightAdjChunk.getBlockFullId (0,     0,           0);
        var frontAdjBlockId  = chunk.getBlockFullId         (X,     0,           1);
        var backAdjBlockId   = backAdjChunk.getBlockFullId  (X,     0,           ZLENGTH - 1);

        BlockRenderer.byFullId(blockId).pushVertices(
                X,
                offsetY,
                0,
                blockId,
                topAdjBlockId,
                bottomAdjBlockId,
                leftAdjBlockId,
                rightAdjBlockId,
                frontAdjBlockId,
                backAdjBlockId,
                builder);
    }

    private static void pushLeftBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            GLDisplayList.Builder builder)
    {
        final var XLENGTH = Chunk.XLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
                var blockId          = chunk.getBlockFullId   (0,           y,     z);
                var topAdjBlockId    = chunk.getBlockFullId   (0,           y + 1, z);
                var bottomAdjBlockId = chunk.getBlockFullId   (0,           y - 1, z);
                var leftAdjBlockId   = adjChunk.getBlockFullId(XLENGTH - 1, y,     z);
                var rightAdjBlockId  = chunk.getBlockFullId   (1,           y,     z);
                var frontAdjBlockId  = chunk.getBlockFullId   (0,           y, z + 1);
                var backAdjBlockId   = chunk.getBlockFullId   (0,           y, z - 1);

                BlockRenderer.byFullId(blockId).pushVertices(
                        0,
                        y + offsetY,
                        z,
                        blockId,
                        topAdjBlockId,
                        bottomAdjBlockId,
                        leftAdjBlockId,
                        rightAdjBlockId,
                        frontAdjBlockId,
                        backAdjBlockId,
                        builder);
            }
        }
    }

    private static void pushRightBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
                var blockId          = chunk.getBlockFullId   (X,     y,     z);
                var topAdjBlockId    = chunk.getBlockFullId   (X,     y + 1, z);
                var bottomAdjBlockId = chunk.getBlockFullId   (X,     y - 1, z);
                var leftAdjBlockId   = chunk.getBlockFullId   (X - 1, y,     z);
                var rightAdjBlockId  = adjChunk.getBlockFullId(0,     y,     z);
                var frontAdjBlockId  = chunk.getBlockFullId   (X,     y,     z + 1);
                var backAdjBlockId   = chunk.getBlockFullId   (X,     y,     z - 1);

                BlockRenderer.byFullId(blockId).pushVertices(
                        X,
                        y + offsetY,
                        z,
                        blockId,
                        topAdjBlockId,
                        bottomAdjBlockId,
                        leftAdjBlockId,
                        rightAdjBlockId,
                        frontAdjBlockId,
                        backAdjBlockId,
                        builder);
            }
        }
    }

    private static void pushFrontBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            GLDisplayList.Builder builder)
    {
        final var Z = Chunk.ZLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                var blockId          = chunk.getBlockFullId   (x,     y,     Z);
                var topAdjBlockId    = chunk.getBlockFullId   (x,     y + 1, Z);
                var bottomAdjBlockId = chunk.getBlockFullId   (x,     y - 1, Z);
                var leftAdjBlockId   = chunk.getBlockFullId   (x - 1, y,     Z);
                var rightAdjBlockId  = chunk.getBlockFullId   (x + 1, y,     Z);
                var frontAdjBlockId  = adjChunk.getBlockFullId(x,     y,     0);
                var backAdjBlockId   = chunk.getBlockFullId   (x,     y,     Z - 1);

                BlockRenderer.byFullId(blockId).pushVertices(
                        x,
                        y + offsetY,
                        Z,
                        blockId,
                        topAdjBlockId,
                        bottomAdjBlockId,
                        leftAdjBlockId,
                        rightAdjBlockId,
                        frontAdjBlockId,
                        backAdjBlockId,
                        builder);
            }
        }
    }

    private static void pushBackBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            GLDisplayList.Builder builder)
    {
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                var blockId          = chunk.getBlockFullId   (x,     y,     0);
                var topAdjBlockId    = chunk.getBlockFullId   (x,     y + 1, 0);
                var bottomAdjBlockId = chunk.getBlockFullId   (x,     y - 1, 0);
                var leftAdjBlockId   = chunk.getBlockFullId   (x - 1, y,     0);
                var rightAdjBlockId  = chunk.getBlockFullId   (x + 1, y,     0);
                var frontAdjBlockId  = chunk.getBlockFullId   (x,     y,     1);
                var backAdjBlockId   = adjChunk.getBlockFullId(x,     y,     ZLENGTH - 1);

                BlockRenderer.byFullId(blockId).pushVertices(
                        x,
                        y + offsetY,
                        0,
                        blockId,
                        topAdjBlockId,
                        bottomAdjBlockId,
                        leftAdjBlockId,
                        rightAdjBlockId,
                        frontAdjBlockId,
                        backAdjBlockId,
                        builder);
            }
        }
    }

    private static void pushFrontLeftBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk leftAdjChunk,
            Chunk frontAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var Z = Chunk.ZLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            var blockId          = chunk.getBlockFullId        (0,           y,     Z);
            var topAdjBlockId    = chunk.getBlockFullId        (0,           y + 1, Z);
            var bottomAdjBlockId = chunk.getBlockFullId        (0,           y - 1, Z);
            var leftAdjBlockId   = leftAdjChunk.getBlockFullId (XLENGTH - 1, y,     Z);
            var rightAdjBlockId  = chunk.getBlockFullId        (1,           y,     Z);
            var frontAdjBlockId  = frontAdjChunk.getBlockFullId(0,           y,     0);
            var backAdjBlockId   = chunk.getBlockFullId        (0,           y,     Z - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    0,
                    y + offsetY,
                    Z,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushFrontRightBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk rightAdjChunk,
            Chunk frontAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            var blockId          = chunk.getBlockFullId        (X,     y,     Z);
            var topAdjBlockId    = chunk.getBlockFullId        (X,     y + 1, Z);
            var bottomAdjBlockId = chunk.getBlockFullId        (X,     y - 1, Z);
            var leftAdjBlockId   = chunk.getBlockFullId        (X - 1, y,     Z);
            var rightAdjBlockId  = rightAdjChunk.getBlockFullId(0,     y,     Z);
            var frontAdjBlockId  = frontAdjChunk.getBlockFullId(X,     y,     0);
            var backAdjBlockId   = chunk.getBlockFullId        (X,     y,     Z - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    X,
                    y + offsetY,
                    Z,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushBackLeftBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk leftAdjChunk,
            Chunk backAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var XLENGTH = Chunk.XLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            var blockId          = chunk.getBlockFullId       (0,           y,     0);
            var topAdjBlockId    = chunk.getBlockFullId       (0,           y + 1, 0);
            var bottomAdjBlockId = chunk.getBlockFullId       (0,           y - 1, 0);
            var leftAdjBlockId   = leftAdjChunk.getBlockFullId(XLENGTH - 1, y,     0);
            var rightAdjBlockId  = chunk.getBlockFullId       (1,           y,     0);
            var frontAdjBlockId  = chunk.getBlockFullId       (0,           y,     1);
            var backAdjBlockId   = backAdjChunk.getBlockFullId(0,           y,     ZLENGTH - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    0,
                    y + offsetY,
                    0,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    private static void pushBackRightBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk rightAdjChunk,
            Chunk backAdjChunk,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            var blockId          = chunk.getBlockFullId        (X,     y,     0);
            var topAdjBlockId    = chunk.getBlockFullId        (X,     y + 1, 0);
            var bottomAdjBlockId = chunk.getBlockFullId        (X,     y - 1, 0);
            var leftAdjBlockId   = chunk.getBlockFullId        (X - 1, y,     0);
            var rightAdjBlockId  = rightAdjChunk.getBlockFullId(0,     y,     0);
            var frontAdjBlockId  = chunk.getBlockFullId        (X,     y,     1);
            var backAdjBlockId   = backAdjChunk.getBlockFullId (X,     y,     ZLENGTH - 1);

            BlockRenderer.byFullId(blockId).pushVertices(
                    X,
                    y + offsetY,
                    0,
                    blockId,
                    topAdjBlockId,
                    bottomAdjBlockId,
                    leftAdjBlockId,
                    rightAdjBlockId,
                    frontAdjBlockId,
                    backAdjBlockId,
                    builder);
        }
    }

    @Override
    public void close() {
        if (displayList != null)
            displayList.close();
    }

    /**
     * Displays the column's containing blocks.
     */
    public void displayBlocks(GLTexture texture) {
        if (displayList != null) {
            GLRenderContext.callDisplayList(displayList, texture);
        }
    }

    public boolean isEmpty() {
        return displayList == null;
    }
}