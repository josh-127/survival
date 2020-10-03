package net.survival.graphics;

import net.survival.block.Chunk;
import net.survival.block.Column;
import net.survival.block.state.AirBlock;
import net.survival.graphics.blockrenderer.BlockRenderer;
import net.survival.graphics.opengl.GLDisplayList;
import net.survival.graphics.opengl.GLRenderContext;
import net.survival.graphics.opengl.GLTexture;

class ColumnDisplay implements GraphicsResource {
    private static final Chunk EMPTY_CHUNK = new Chunk();

    private final GLDisplayList displayList;
    private final int cx;
    private final int cz;

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
        var heightMap = genHeightMap(column);

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

            pushInternalBlocks(i, chunk, heightMap, builder);

            if (topAdjChunk != null) {
                pushTopBlocks(i, chunk, topAdjChunk, heightMap, builder);

                if (leftAdjChunk != null) {
                    pushTopLeftEdgeBlocks(
                            i, chunk, topAdjChunk, leftAdjChunk, heightMap, builder);
                }
                if (rightAdjChunk != null) {
                    pushTopRightEdgeBlocks(
                            i, chunk, topAdjChunk, rightAdjChunk, heightMap, builder);
                }
                if (frontAdjChunk != null) {
                    pushTopFrontEdgeBlocks(
                            i, chunk, topAdjChunk, frontAdjChunk, heightMap, builder);
                }
                if (backAdjChunk != null) {
                    pushTopBackEdgeBlocks(
                            i, chunk, topAdjChunk, backAdjChunk, heightMap, builder);
                }

                if (frontAdjChunk != null && leftAdjChunk != null) {
                    pushTopFrontLeftCornerBlock(
                            i, chunk, topAdjChunk, frontAdjChunk, leftAdjChunk, heightMap, builder);
                }
                if (frontAdjChunk != null && rightAdjChunk != null) {
                    pushTopFrontRightCornerBlock(
                            i, chunk, topAdjChunk, frontAdjChunk, rightAdjChunk, heightMap, builder);
                }
                if (backAdjChunk != null && leftAdjChunk != null) {
                    pushTopBackLeftCornerBlock(
                            i, chunk, topAdjChunk, backAdjChunk, leftAdjChunk, heightMap, builder);
                }
                if (backAdjChunk != null && rightAdjChunk != null) {
                    pushTopBackRightCornerBlock(
                            i, chunk, topAdjChunk, backAdjChunk, rightAdjChunk, heightMap, builder);
                }
            }

            if (bottomAdjChunk != null) {
                pushBottomBlocks(i, chunk, bottomAdjChunk, heightMap, builder);

                if (leftAdjChunk != null) {
                    pushBottomLeftEdgeBlocks(
                            i, chunk, bottomAdjChunk, leftAdjChunk, heightMap, builder);
                }
                if (rightAdjChunk != null) {
                    pushBottomRightEdgeBlocks(
                            i, chunk, bottomAdjChunk, rightAdjChunk, heightMap, builder);
                }
                if (frontAdjChunk != null) {
                    pushBottomFrontEdgeBlocks(
                            i, chunk, bottomAdjChunk, frontAdjChunk, heightMap, builder);
                }
                if (backAdjChunk != null) {
                    pushBottomBackEdgeBlocks(
                            i, chunk, bottomAdjChunk, backAdjChunk, heightMap, builder);
                }

                if (frontAdjChunk != null && leftAdjChunk != null) {
                    pushBottomFrontLeftCornerBlock(
                            i, chunk, bottomAdjChunk, frontAdjChunk, leftAdjChunk, heightMap, builder);
                }
                if (frontAdjChunk != null && rightAdjChunk != null) {
                    pushBottomFrontRightCornerBlock(
                            i, chunk, bottomAdjChunk, frontAdjChunk, rightAdjChunk, heightMap, builder);
                }
                if (backAdjChunk != null && leftAdjChunk != null) {
                    pushBottomBackLeftCornerBlock(
                            i, chunk, bottomAdjChunk, backAdjChunk, leftAdjChunk, heightMap, builder);
                }
                if (backAdjChunk != null && rightAdjChunk != null) {
                    pushBottomBackRightCornerBlock(
                            i, chunk, bottomAdjChunk, backAdjChunk, rightAdjChunk, heightMap, builder);
                }
            }

            if (leftAdjChunk != null) {
                pushLeftBlocks(i, chunk, leftAdjChunk, heightMap, builder);
            }
            if (rightAdjChunk != null) {
                pushRightBlocks(i, chunk, rightAdjChunk, heightMap, builder);
            }
            if (frontAdjChunk != null) {
                pushFrontBlocks(i, chunk, frontAdjChunk, heightMap, builder);

                if (leftAdjChunk != null) {
                    pushFrontLeftBlocks(i, chunk, leftAdjChunk, frontAdjChunk, heightMap, builder);
                }
                if (rightAdjChunk != null) {
                    pushFrontRightBlocks(i, chunk, rightAdjChunk, frontAdjChunk, heightMap, builder);
                }
            }
            if (backAdjChunk != null) {
                pushBackBlocks(i, chunk, backAdjChunk, heightMap, builder);

                if (leftAdjChunk != null) {
                    pushBackLeftBlocks(i, chunk, leftAdjChunk, backAdjChunk, heightMap, builder);
                }
                if (rightAdjChunk != null) {
                    pushBackRightBlocks(i, chunk, rightAdjChunk, backAdjChunk, heightMap, builder);
                }
            }
        }

        var displayList = builder.build();

        return new ColumnDisplay(displayList, cx, cz);
    }

    private static void pushInternalBlocks(
            int chunkIndex,
            Chunk chunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
                for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                    var block          = chunk.getBlock(x,     y,     z);
                    var topAdjBlock    = chunk.getBlock(x,     y + 1, z);
                    var bottomAdjBlock = chunk.getBlock(x,     y - 1, z);
                    var leftAdjBlock   = chunk.getBlock(x - 1, y,     z);
                    var rightAdjBlock  = chunk.getBlock(x + 1, y,     z);
                    var frontAdjBlock  = chunk.getBlock(x,     y,     z + 1);
                    var backAdjBlock   = chunk.getBlock(x,     y,     z - 1);

                    BlockRenderer.byBlock(block).pushVertices(
                            x,
                            y + offsetY,
                            z,
                            block,
                            topAdjBlock,
                            bottomAdjBlock,
                            leftAdjBlock,
                            rightAdjBlock,
                            frontAdjBlock,
                            backAdjBlock,
                            getShade(x, y + offsetY, z, heightMap),
                            builder);
                }
            }
        }
    }

    private static void pushTopBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                var block          = chunk.getBlock   (x,     Y,     z);
                var topAdjBlock    = adjChunk.getBlock(x,     0,     z);
                var bottomAdjBlock = chunk.getBlock   (x,     Y - 1, z);
                var leftAdjBlock   = chunk.getBlock   (x - 1, Y,     z);
                var rightAdjBlock  = chunk.getBlock   (x + 1, Y,     z);
                var frontAdjBlock  = chunk.getBlock   (x,     Y,     z + 1);
                var backAdjBlock   = chunk.getBlock   (x,     Y,     z - 1);

                BlockRenderer.byBlock(block).pushVertices(
                        x,
                        Y + offsetY,
                        z,
                        block,
                        topAdjBlock,
                        bottomAdjBlock,
                        leftAdjBlock,
                        rightAdjBlock,
                        frontAdjBlock,
                        backAdjBlock,
                        getShade(x, Y + offsetY, z, heightMap),
                        builder);
            }
        }
    }

    private static void pushTopLeftEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk leftAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            var block          = chunk.getBlock       (0,           Y,     z);
            var topAdjBlock    = topAdjChunk.getBlock (0,           0,     z);
            var bottomAdjBlock = chunk.getBlock       (0,           Y - 1, z);
            var leftAdjBlock   = leftAdjChunk.getBlock(XLENGTH - 1, Y,     z);
            var rightAdjBlock  = chunk.getBlock       (1,           Y,     z);
            var frontAdjBlock  = chunk.getBlock       (0,           Y,     z + 1);
            var backAdjBlock   = chunk.getBlock       (0,           Y,     z - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    0,
                    Y + offsetY,
                    z,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(0, Y + offsetY, z, heightMap),
                    builder);
        }
    }

    private static void pushTopRightEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk rightAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Y = Chunk.YLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            var block          = chunk.getBlock        (X,     Y,     z);
            var topAdjBlock    = topAdjChunk.getBlock  (X,     0,     z);
            var bottomAdjBlock = chunk.getBlock        (X,     Y - 1, z);
            var leftAdjBlock   = chunk.getBlock        (X - 1, Y,     z);
            var rightAdjBlock  = rightAdjChunk.getBlock(0,     Y,     z);
            var frontAdjBlock  = chunk.getBlock        (X,     Y,     z + 1);
            var backAdjBlock   = chunk.getBlock        (X,     Y,     z - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    X,
                    Y + offsetY,
                    z,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(X, Y + offsetY, z, heightMap),
                    builder);
        }
    }

    private static void pushTopFrontEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk frontAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
            var block          = chunk.getBlock        (x,     Y,     Z);
            var topAdjBlock    = topAdjChunk.getBlock  (x,     0,     Z);
            var bottomAdjBlock = chunk.getBlock        (x,     Y - 1, Z);
            var leftAdjBlock   = chunk.getBlock        (x - 1, Y,     Z);
            var rightAdjBlock  = chunk.getBlock        (x + 1, Y,     Z);
            var frontAdjBlock  = frontAdjChunk.getBlock(x,     Y,     0);
            var backAdjBlock   = chunk.getBlock        (x,     Y,     Z - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    x,
                    Y + offsetY,
                    Z,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(x, Y + offsetY, Z, heightMap),
                    builder);
        }
    }

    private static void pushTopBackEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk backAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
            var block          = chunk.getBlock       (x,     Y,     0);
            var topAdjBlock    = topAdjChunk.getBlock (x,     0,     0);
            var bottomAdjBlock = chunk.getBlock       (x,     Y - 1, 0);
            var leftAdjBlock   = chunk.getBlock       (x - 1, Y,     0);
            var rightAdjBlock  = chunk.getBlock       (x + 1, Y,     0);
            var frontAdjBlock  = chunk.getBlock       (x,     Y,     1);
            var backAdjBlock   = backAdjChunk.getBlock(x,     Y,     ZLENGTH - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    x,
                    Y + offsetY,
                    0,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(x, Y + offsetY, 0, heightMap),
                    builder);
        }
    }

    private static void pushTopFrontLeftCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk frontAdjChunk,
            Chunk leftAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var block          = chunk.getBlock        (0,           Y,     Z);
        var topAdjBlock    = topAdjChunk.getBlock  (0,           0,     Z);
        var bottomAdjBlock = chunk.getBlock        (0,           Y - 1, Z);
        var leftAdjBlock   = leftAdjChunk.getBlock (XLENGTH - 1, Y,     Z);
        var rightAdjBlock  = chunk.getBlock        (1,           Y,     Z);
        var frontAdjBlock  = frontAdjChunk.getBlock(0,           Y,     0);
        var backAdjBlock   = chunk.getBlock        (0,           Y,     Z - 1);

        BlockRenderer.byBlock(block).pushVertices(
                0,
                Y + offsetY,
                Z,
                block,
                topAdjBlock,
                bottomAdjBlock,
                leftAdjBlock,
                rightAdjBlock,
                frontAdjBlock,
                backAdjBlock,
                getShade(0, Y + offsetY, Z, heightMap),
                builder);
    }

    private static void pushTopFrontRightCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk frontAdjChunk,
            Chunk rightAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Y = Chunk.YLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var block          = chunk.getBlock        (X,     Y,     Z);
        var topAdjBlock    = topAdjChunk.getBlock  (X,     0,     Z);
        var bottomAdjBlock = chunk.getBlock        (X,     Y - 1, Z);
        var leftAdjBlock   = chunk.getBlock        (X - 1, Y,     Z);
        var rightAdjBlock  = rightAdjChunk.getBlock(0,     Y,     Z);
        var frontAdjBlock  = frontAdjChunk.getBlock(X,     Y,     0);
        var backAdjBlock   = chunk.getBlock        (X,     Y,     Z - 1);

        BlockRenderer.byBlock(block).pushVertices(
                X,
                Y + offsetY,
                Z,
                block,
                topAdjBlock,
                bottomAdjBlock,
                leftAdjBlock,
                rightAdjBlock,
                frontAdjBlock,
                backAdjBlock,
                getShade(X, Y + offsetY, Z, heightMap),
                builder);
    }

    private static void pushTopBackLeftCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk backAdjChunk,
            Chunk leftAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Y = Chunk.YLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var block          = chunk.getBlock       (0,           Y,     0);
        var topAdjBlock    = topAdjChunk.getBlock (0,           0,     0);
        var bottomAdjBlock = chunk.getBlock       (0,           Y - 1, 0);
        var leftAdjBlock   = leftAdjChunk.getBlock(XLENGTH - 1, Y,     0);
        var rightAdjBlock  = chunk.getBlock       (1,           Y,     0);
        var frontAdjBlock  = chunk.getBlock       (0,           Y,     1);
        var backAdjBlock   = backAdjChunk.getBlock(0,           Y,     ZLENGTH - 1);

        BlockRenderer.byBlock(block).pushVertices(
                0,
                Y + offsetY,
                0,
                block,
                topAdjBlock,
                bottomAdjBlock,
                leftAdjBlock,
                rightAdjBlock,
                frontAdjBlock,
                backAdjBlock,
                getShade(0, Y + offsetY, 0, heightMap),
                builder);
    }

    private static void pushTopBackRightCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk topAdjChunk,
            Chunk backAdjChunk,
            Chunk rightAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Y = Chunk.YLENGTH - 1;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var block          = chunk.getBlock        (X,     Y,     0);
        var topAdjBlock    = topAdjChunk.getBlock  (X,     0,     0);
        var bottomAdjBlock = chunk.getBlock        (X,     Y - 1, 0);
        var leftAdjBlock   = chunk.getBlock        (X - 1, Y,     0);
        var rightAdjBlock  = rightAdjChunk.getBlock(0,     Y,     0);
        var frontAdjBlock  = chunk.getBlock        (X,     Y,     1);
        var backAdjBlock   = backAdjChunk.getBlock (X,     Y,     ZLENGTH - 1);

        BlockRenderer.byBlock(block).pushVertices(
                X,
                Y + offsetY,
                0,
                block,
                topAdjBlock,
                bottomAdjBlock,
                leftAdjBlock,
                rightAdjBlock,
                frontAdjBlock,
                backAdjBlock,
                getShade(X, Y + offsetY, 0, heightMap),
                builder);
    }

    private static void pushBottomBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                var block          = chunk.getBlock   (x,     0,           z);
                var topAdjBlock    = chunk.getBlock   (x,     1,           z);
                var bottomAdjBlock = adjChunk.getBlock(x,     YLENGTH - 1, z);
                var leftAdjBlock   = chunk.getBlock   (x - 1, 0,           z);
                var rightAdjBlock  = chunk.getBlock   (x + 1, 0,           z);
                var frontAdjBlock  = chunk.getBlock   (x,     0,           z + 1);
                var backAdjBlock   = chunk.getBlock   (x,     0,           z - 1);

                BlockRenderer.byBlock(block).pushVertices(
                        x,
                        offsetY,
                        z,
                        block,
                        topAdjBlock,
                        bottomAdjBlock,
                        leftAdjBlock,
                        rightAdjBlock,
                        frontAdjBlock,
                        backAdjBlock,
                        getShade(x, offsetY, z, heightMap),
                        builder);
            }
        }
    }

    private static void pushBottomLeftEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk leftAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var XLENGTH = Chunk.XLENGTH;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            var block          = chunk.getBlock         (0,           0,           z);
            var topAdjBlock    = chunk.getBlock         (0,           1,           z);
            var bottomAdjBlock = bottomAdjChunk.getBlock(0,           YLENGTH - 1, z);
            var leftAdjBlock   = leftAdjChunk.getBlock  (XLENGTH - 1, 0,           z);
            var rightAdjBlock  = chunk.getBlock         (1,           0,           z);
            var frontAdjBlock  = chunk.getBlock         (0,           0,           z + 1);
            var backAdjBlock   = chunk.getBlock         (0,           0,           z - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    0,
                    offsetY,
                    z,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(0, offsetY, z, heightMap),
                    builder);
        }
    }

    private static void pushBottomRightEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk rightAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
            var block          = chunk.getBlock         (X,     0,           z);
            var topAdjBlock    = chunk.getBlock         (X,     1,           z);
            var bottomAdjBlock = bottomAdjChunk.getBlock(X,     YLENGTH - 1, z);
            var leftAdjBlock   = chunk.getBlock         (X - 1, 0,           z);
            var rightAdjBlock  = rightAdjChunk.getBlock (0,     0,           z);
            var frontAdjBlock  = chunk.getBlock         (X,     0,           z + 1);
            var backAdjBlock   = chunk.getBlock         (X,     0,           z - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    X,
                    offsetY,
                    z,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(X, offsetY, z, heightMap),
                    builder);
        }
    }

    private static void pushBottomFrontEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk frontAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Z = Chunk.ZLENGTH - 1;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
            var block          = chunk.getBlock         (x,     0,           Z);
            var topAdjBlock    = chunk.getBlock         (x,     1,           Z);
            var bottomAdjBlock = bottomAdjChunk.getBlock(x,     YLENGTH - 1, Z);
            var leftAdjBlock   = chunk.getBlock         (x - 1, 0,           Z);
            var rightAdjBlock  = chunk.getBlock         (x + 1, 0,           Z);
            var frontAdjBlock  = frontAdjChunk.getBlock (x,     0,           0);
            var backAdjBlock   = chunk.getBlock         (x,     0,           Z - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    x,
                    offsetY,
                    Z,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(x, offsetY, Z, heightMap),
                    builder);
        }
    }

    private static void pushBottomBackEdgeBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk backAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var YLENGTH = Chunk.YLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
            var block          = chunk.getBlock         (x,     0,           0);
            var topAdjBlock    = chunk.getBlock         (x,     1,           0);
            var bottomAdjBlock = bottomAdjChunk.getBlock(x,     YLENGTH - 1, 0);
            var leftAdjBlock   = chunk.getBlock         (x - 1, 0,           0);
            var rightAdjBlock  = chunk.getBlock         (x + 1, 0,           0);
            var frontAdjBlock  = chunk.getBlock         (x,     0,           1);
            var backAdjBlock   = backAdjChunk.getBlock  (x,     0,           ZLENGTH - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    x,
                    offsetY,
                    0,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(x, offsetY, 0, heightMap),
                    builder);
        }
    }

    private static void pushBottomFrontLeftCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk frontAdjChunk,
            Chunk leftAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Z = Chunk.ZLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var block          = chunk.getBlock         (0,           0,           Z);
        var topAdjBlock    = chunk.getBlock         (0,           1,           Z);
        var bottomAdjBlock = bottomAdjChunk.getBlock(0,           YLENGTH - 1, Z);
        var leftAdjBlock   = leftAdjChunk.getBlock  (XLENGTH - 1, 0,           Z);
        var rightAdjBlock  = chunk.getBlock         (1,           0,           Z);
        var frontAdjBlock  = frontAdjChunk.getBlock (0,           0,           0);
        var backAdjBlock   = chunk.getBlock         (0,           0,           Z - 1);

        BlockRenderer.byBlock(block).pushVertices(
                0,
                offsetY,
                Z,
                block,
                topAdjBlock,
                bottomAdjBlock,
                leftAdjBlock,
                rightAdjBlock,
                frontAdjBlock,
                backAdjBlock,
                getShade(0, offsetY, Z, heightMap),
                builder);
    }

    private static void pushBottomFrontRightCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk frontAdjChunk,
            Chunk rightAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        final var YLENGTH = Chunk.YLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var block          = chunk.getBlock         (X,     0,           Z);
        var topAdjBlock    = chunk.getBlock         (X,     1,           Z);
        var bottomAdjBlock = bottomAdjChunk.getBlock(X,     YLENGTH - 1, Z);
        var leftAdjBlock   = chunk.getBlock         (X - 1, 0,           Z);
        var rightAdjBlock  = rightAdjChunk.getBlock (0,     0,           Z);
        var frontAdjBlock  = frontAdjChunk.getBlock (X,     0,           0);
        var backAdjBlock   = chunk.getBlock         (X,     0,           Z - 1);

        BlockRenderer.byBlock(block).pushVertices(
                X,
                offsetY,
                Z,
                block,
                topAdjBlock,
                bottomAdjBlock,
                leftAdjBlock,
                rightAdjBlock,
                frontAdjBlock,
                backAdjBlock,
                getShade(X, offsetY, Z, heightMap),
                builder);
    }

    private static void pushBottomBackLeftCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk backAdjChunk,
            Chunk leftAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var XLENGTH = Chunk.XLENGTH;
        final var YLENGTH = Chunk.YLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var block          = chunk.getBlock         (0,           0,           0);
        var topAdjBlock    = chunk.getBlock         (0,           1,           0);
        var bottomAdjBlock = bottomAdjChunk.getBlock(0,           YLENGTH - 1, 0);
        var leftAdjBlock   = leftAdjChunk.getBlock  (XLENGTH - 1, 0,           0);
        var rightAdjBlock  = chunk.getBlock         (1,           0,           0);
        var frontAdjBlock  = chunk.getBlock         (0,           0,           1);
        var backAdjBlock   = backAdjChunk.getBlock  (0,           0,           ZLENGTH - 1);

        BlockRenderer.byBlock(block).pushVertices(
                0,
                offsetY,
                0,
                block,
                topAdjBlock,
                bottomAdjBlock,
                leftAdjBlock,
                rightAdjBlock,
                frontAdjBlock,
                backAdjBlock,
                getShade(0, offsetY, 0, heightMap),
                builder);
    }

    private static void pushBottomBackRightCornerBlock(
            int chunkIndex,
            Chunk chunk,
            Chunk bottomAdjChunk,
            Chunk backAdjChunk,
            Chunk rightAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var YLENGTH = Chunk.YLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        var block          = chunk.getBlock         (X,     0,           0);
        var topAdjBlock    = chunk.getBlock         (X,     1,           0);
        var bottomAdjBlock = bottomAdjChunk.getBlock(X,     YLENGTH - 1, 0);
        var leftAdjBlock   = chunk.getBlock         (X - 1, 0,           0);
        var rightAdjBlock  = rightAdjChunk.getBlock (0,     0,           0);
        var frontAdjBlock  = chunk.getBlock         (X,     0,           1);
        var backAdjBlock   = backAdjChunk.getBlock  (X,     0,           ZLENGTH - 1);

        BlockRenderer.byBlock(block).pushVertices(
                X,
                offsetY,
                0,
                block,
                topAdjBlock,
                bottomAdjBlock,
                leftAdjBlock,
                rightAdjBlock,
                frontAdjBlock,
                backAdjBlock,
                getShade(X, offsetY, 0, heightMap),
                builder);
    }

    private static void pushLeftBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var XLENGTH = Chunk.XLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
                var block          = chunk.getBlock   (0,           y,     z);
                var topAdjBlock    = chunk.getBlock   (0,           y + 1, z);
                var bottomAdjBlock = chunk.getBlock   (0,           y - 1, z);
                var leftAdjBlock   = adjChunk.getBlock(XLENGTH - 1, y,     z);
                var rightAdjBlock  = chunk.getBlock   (1,           y,     z);
                var frontAdjBlock  = chunk.getBlock   (0,           y, z + 1);
                var backAdjBlock   = chunk.getBlock   (0,           y, z - 1);

                BlockRenderer.byBlock(block).pushVertices(
                        0,
                        y + offsetY,
                        z,
                        block,
                        topAdjBlock,
                        bottomAdjBlock,
                        leftAdjBlock,
                        rightAdjBlock,
                        frontAdjBlock,
                        backAdjBlock,
                        getShade(0, y + offsetY, z, heightMap),
                        builder);
            }
        }
    }

    private static void pushRightBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var z = 1; z < Chunk.ZLENGTH - 1; ++z) {
                var block          = chunk.getBlock   (X,     y,     z);
                var topAdjBlock    = chunk.getBlock   (X,     y + 1, z);
                var bottomAdjBlock = chunk.getBlock   (X,     y - 1, z);
                var leftAdjBlock   = chunk.getBlock   (X - 1, y,     z);
                var rightAdjBlock  = adjChunk.getBlock(0,     y,     z);
                var frontAdjBlock  = chunk.getBlock   (X,     y,     z + 1);
                var backAdjBlock   = chunk.getBlock   (X,     y,     z - 1);

                BlockRenderer.byBlock(block).pushVertices(
                        X,
                        y + offsetY,
                        z,
                        block,
                        topAdjBlock,
                        bottomAdjBlock,
                        leftAdjBlock,
                        rightAdjBlock,
                        frontAdjBlock,
                        backAdjBlock,
                        getShade(X, y + offsetY, z, heightMap),
                        builder);
            }
        }
    }

    private static void pushFrontBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Z = Chunk.ZLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                var block          = chunk.getBlock   (x,     y,     Z);
                var topAdjBlock    = chunk.getBlock   (x,     y + 1, Z);
                var bottomAdjBlock = chunk.getBlock   (x,     y - 1, Z);
                var leftAdjBlock   = chunk.getBlock   (x - 1, y,     Z);
                var rightAdjBlock  = chunk.getBlock   (x + 1, y,     Z);
                var frontAdjBlock  = adjChunk.getBlock(x,     y,     0);
                var backAdjBlock   = chunk.getBlock   (x,     y,     Z - 1);

                BlockRenderer.byBlock(block).pushVertices(
                        x,
                        y + offsetY,
                        Z,
                        block,
                        topAdjBlock,
                        bottomAdjBlock,
                        leftAdjBlock,
                        rightAdjBlock,
                        frontAdjBlock,
                        backAdjBlock,
                        getShade(x, y + offsetY, Z, heightMap),
                        builder);
            }
        }
    }

    private static void pushBackBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk adjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            for (var x = 1; x < Chunk.XLENGTH - 1; ++x) {
                var block          = chunk.getBlock   (x,     y,     0);
                var topAdjBlock    = chunk.getBlock   (x,     y + 1, 0);
                var bottomAdjBlock = chunk.getBlock   (x,     y - 1, 0);
                var leftAdjBlock   = chunk.getBlock   (x - 1, y,     0);
                var rightAdjBlock  = chunk.getBlock   (x + 1, y,     0);
                var frontAdjBlock  = chunk.getBlock   (x,     y,     1);
                var backAdjBlock   = adjChunk.getBlock(x,     y,     ZLENGTH - 1);

                BlockRenderer.byBlock(block).pushVertices(
                        x,
                        y + offsetY,
                        0,
                        block,
                        topAdjBlock,
                        bottomAdjBlock,
                        leftAdjBlock,
                        rightAdjBlock,
                        frontAdjBlock,
                        backAdjBlock,
                        getShade(x, y + offsetY, 0, heightMap),
                        builder);
            }
        }
    }

    private static void pushFrontLeftBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk leftAdjChunk,
            Chunk frontAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var Z = Chunk.ZLENGTH - 1;
        final var XLENGTH = Chunk.XLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            var block          = chunk.getBlock        (0,           y,     Z);
            var topAdjBlock    = chunk.getBlock        (0,           y + 1, Z);
            var bottomAdjBlock = chunk.getBlock        (0,           y - 1, Z);
            var leftAdjBlock   = leftAdjChunk.getBlock (XLENGTH - 1, y,     Z);
            var rightAdjBlock  = chunk.getBlock        (1,           y,     Z);
            var frontAdjBlock  = frontAdjChunk.getBlock(0,           y,     0);
            var backAdjBlock   = chunk.getBlock        (0,           y,     Z - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    0,
                    y + offsetY,
                    Z,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(0, y + offsetY, Z, heightMap),
                    builder);
        }
    }

    private static void pushFrontRightBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk rightAdjChunk,
            Chunk frontAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var Z = Chunk.ZLENGTH - 1;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            var block          = chunk.getBlock        (X,     y,     Z);
            var topAdjBlock    = chunk.getBlock        (X,     y + 1, Z);
            var bottomAdjBlock = chunk.getBlock        (X,     y - 1, Z);
            var leftAdjBlock   = chunk.getBlock        (X - 1, y,     Z);
            var rightAdjBlock  = rightAdjChunk.getBlock(0,     y,     Z);
            var frontAdjBlock  = frontAdjChunk.getBlock(X,     y,     0);
            var backAdjBlock   = chunk.getBlock        (X,     y,     Z - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    X,
                    y + offsetY,
                    Z,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(X, y + offsetY, Z, heightMap),
                    builder);
        }
    }

    private static void pushBackLeftBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk leftAdjChunk,
            Chunk backAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var XLENGTH = Chunk.XLENGTH;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            var block          = chunk.getBlock       (0,           y,     0);
            var topAdjBlock    = chunk.getBlock       (0,           y + 1, 0);
            var bottomAdjBlock = chunk.getBlock       (0,           y - 1, 0);
            var leftAdjBlock   = leftAdjChunk.getBlock(XLENGTH - 1, y,     0);
            var rightAdjBlock  = chunk.getBlock       (1,           y,     0);
            var frontAdjBlock  = chunk.getBlock       (0,           y,     1);
            var backAdjBlock   = backAdjChunk.getBlock(0,           y,     ZLENGTH - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    0,
                    y + offsetY,
                    0,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(0, y + offsetY, 0, heightMap),
                    builder);
        }
    }

    private static void pushBackRightBlocks(
            int chunkIndex,
            Chunk chunk,
            Chunk rightAdjChunk,
            Chunk backAdjChunk,
            int[] heightMap,
            GLDisplayList.Builder builder)
    {
        final var X = Chunk.XLENGTH - 1;
        final var ZLENGTH = Chunk.ZLENGTH;
        var offsetY = chunkIndex * Chunk.YLENGTH;

        for (var y = 1; y < Chunk.YLENGTH - 1; ++y) {
            var block          = chunk.getBlock        (X,     y,     0);
            var topAdjBlock    = chunk.getBlock        (X,     y + 1, 0);
            var bottomAdjBlock = chunk.getBlock        (X,     y - 1, 0);
            var leftAdjBlock   = chunk.getBlock        (X - 1, y,     0);
            var rightAdjBlock  = rightAdjChunk.getBlock(0,     y,     0);
            var frontAdjBlock  = chunk.getBlock        (X,     y,     1);
            var backAdjBlock   = backAdjChunk.getBlock (X,     y,     ZLENGTH - 1);

            BlockRenderer.byBlock(block).pushVertices(
                    X,
                    y + offsetY,
                    0,
                    block,
                    topAdjBlock,
                    bottomAdjBlock,
                    leftAdjBlock,
                    rightAdjBlock,
                    frontAdjBlock,
                    backAdjBlock,
                    getShade(X, y + offsetY, 0, heightMap),
                    builder);
        }
    }

    private static int[] genHeightMap(Column column) {
        var heightMap = new int[Chunk.BASE_AREA];
        var chunkIndex = column.getHeight() - 1;
        var done = false;

        for (var i = 0; i < heightMap.length; ++i) {
            heightMap[i] = -1;
        }

        while (!done && chunkIndex >= 0) {
            done = true;

            var chunk = column.getChunk(chunkIndex);

            for (var z = 0; z < Chunk.ZLENGTH; ++z) {
                for (var x = 0; x < Chunk.XLENGTH; ++x) {
                    var index = x + z * Chunk.XLENGTH;
                    if (heightMap[index] != -1) {
                        continue;
                    }

                    var topY = Chunk.YLENGTH - 1;

                    while (topY >= 0 && chunk.getBlock(x, topY, z) instanceof AirBlock) {
                        --topY;
                    }

                    if (topY == -1) {
                        done = false;
                    }
                    else {
                        heightMap[index] = topY + chunkIndex * Chunk.YLENGTH;
                    }
                }
            }

            --chunkIndex;
        }

        return heightMap;
    }

    private static float getShade(int x, int y, int z, int[] heightMap) {
        var height = heightMap[x + z * Chunk.XLENGTH];
        if (y < height) {
            return 0.75f;
        }

        return 1.0f;
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

    public int getChunkX() {
        return cx;
    }

    public int getChunkZ() {
        return cz;
    }

    public boolean isEmpty() {
        return displayList == null;
    }
}