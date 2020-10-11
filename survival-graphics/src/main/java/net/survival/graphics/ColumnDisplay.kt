package net.survival.graphics

import net.survival.block.*
import net.survival.graphics.opengl.GLDisplayList
import net.survival.graphics.opengl.GLRenderContext
import net.survival.graphics.opengl.GLTexture
import net.survival.util.MathEx
import java.util.*

internal class ColumnDisplay private constructor(
    private val displayList: GLDisplayList?,
    val chunkX: Int,
    val chunkZ: Int
) {
    fun close() {
        displayList?.close()
    }

    fun displayBlocks(texture: GLTexture?) {
        if (displayList != null) {
            GLRenderContext.callDisplayList(displayList, texture)
        }
    }

    companion object {
        private val EMPTY_CHUNK = Chunk()

        fun create(
            cx: Int,
            cz: Int,
            column: Column,
            leftAdjColumn: Column?,
            rightAdjColumn: Column?,
            frontAdjColumn: Column?,
            backAdjColumn: Column?
        ): ColumnDisplay {
            val heightMap = genHeightMap(column)
            val builder = GLDisplayList.Builder()

            for (i in 0 until column.height) {
                val chunk = column.getChunk(i)

                var topAdjChunk: Chunk?
                var bottomAdjChunk: Chunk? = null
                var leftAdjChunk: Chunk? = null
                var rightAdjChunk: Chunk? = null
                var frontAdjChunk: Chunk? = null
                var backAdjChunk: Chunk? = null

                topAdjChunk = column.getChunk(i + 1)
                if (topAdjChunk == null) {
                    topAdjChunk = EMPTY_CHUNK
                }

                if (i > 0) {
                    bottomAdjChunk = column.getChunk(i - 1)
                    if (bottomAdjChunk == null) {
                        bottomAdjChunk = EMPTY_CHUNK
                    }
                }
                if (leftAdjColumn != null) {
                    leftAdjChunk = leftAdjColumn.getChunk(i)
                    if (leftAdjChunk == null) {
                        leftAdjChunk = EMPTY_CHUNK
                    }
                }
                if (rightAdjColumn != null) {
                    rightAdjChunk = rightAdjColumn.getChunk(i)
                    if (rightAdjChunk == null) {
                        rightAdjChunk = EMPTY_CHUNK
                    }
                }
                if (frontAdjColumn != null) {
                    frontAdjChunk = frontAdjColumn.getChunk(i)
                    if (frontAdjChunk == null) {
                        frontAdjChunk = EMPTY_CHUNK
                    }
                }
                if (backAdjColumn != null) {
                    backAdjChunk = backAdjColumn.getChunk(i)
                    if (backAdjChunk == null) {
                        backAdjChunk = EMPTY_CHUNK
                    }
                }

                pushInternalBlocks(i, chunk, heightMap, builder)

                pushTopBlocks(i, chunk, topAdjChunk, heightMap, builder)
                if (leftAdjChunk != null) {
                    pushTopLeftEdgeBlocks(i, chunk, topAdjChunk, leftAdjChunk, heightMap, builder)
                }
                if (rightAdjChunk != null) {
                    pushTopRightEdgeBlocks(i, chunk, topAdjChunk, rightAdjChunk, heightMap, builder)
                }
                if (frontAdjChunk != null) {
                    pushTopFrontEdgeBlocks(i, chunk, topAdjChunk, frontAdjChunk, heightMap, builder)
                }
                if (backAdjChunk != null) {
                    pushTopBackEdgeBlocks(i, chunk, topAdjChunk, backAdjChunk, heightMap, builder)
                }
                if (frontAdjChunk != null && leftAdjChunk != null) {
                    pushTopFrontLeftCornerBlock(i, chunk, topAdjChunk, frontAdjChunk, leftAdjChunk, heightMap, builder)
                }
                if (frontAdjChunk != null && rightAdjChunk != null) {
                    pushTopFrontRightCornerBlock(i, chunk, topAdjChunk, frontAdjChunk, rightAdjChunk, heightMap, builder)
                }
                if (backAdjChunk != null && leftAdjChunk != null) {
                    pushTopBackLeftCornerBlock(i, chunk, topAdjChunk, backAdjChunk, leftAdjChunk, heightMap, builder)
                }
                if (backAdjChunk != null && rightAdjChunk != null) {
                    pushTopBackRightCornerBlock(i, chunk, topAdjChunk, backAdjChunk, rightAdjChunk, heightMap, builder)
                }
                if (bottomAdjChunk != null) {
                    pushBottomBlocks(i, chunk, bottomAdjChunk, heightMap, builder)
                    if (leftAdjChunk != null) {
                        pushBottomLeftEdgeBlocks(i, chunk, bottomAdjChunk, leftAdjChunk, heightMap, builder)
                    }
                    if (rightAdjChunk != null) {
                        pushBottomRightEdgeBlocks(i, chunk, bottomAdjChunk, rightAdjChunk, heightMap, builder)
                    }
                    if (frontAdjChunk != null) {
                        pushBottomFrontEdgeBlocks(i, chunk, bottomAdjChunk, frontAdjChunk, heightMap, builder)
                    }
                    if (backAdjChunk != null) {
                        pushBottomBackEdgeBlocks(i, chunk, bottomAdjChunk, backAdjChunk, heightMap, builder)
                    }
                    if (frontAdjChunk != null && leftAdjChunk != null) {
                        pushBottomFrontLeftCornerBlock(i, chunk, bottomAdjChunk, frontAdjChunk, leftAdjChunk, heightMap, builder)
                    }
                    if (frontAdjChunk != null && rightAdjChunk != null) {
                        pushBottomFrontRightCornerBlock(i, chunk, bottomAdjChunk, frontAdjChunk, rightAdjChunk, heightMap, builder)
                    }
                    if (backAdjChunk != null && leftAdjChunk != null) {
                        pushBottomBackLeftCornerBlock(i, chunk, bottomAdjChunk, backAdjChunk, leftAdjChunk, heightMap, builder)
                    }
                    if (backAdjChunk != null && rightAdjChunk != null) {
                        pushBottomBackRightCornerBlock(i, chunk, bottomAdjChunk, backAdjChunk, rightAdjChunk, heightMap, builder)
                    }
                }

                if (leftAdjChunk != null) {
                    pushLeftBlocks(i, chunk, leftAdjChunk, heightMap, builder)
                }
                if (rightAdjChunk != null) {
                    pushRightBlocks(i, chunk, rightAdjChunk, heightMap, builder)
                }

                if (frontAdjChunk != null) {
                    pushFrontBlocks(i, chunk, frontAdjChunk, heightMap, builder)
                    if (leftAdjChunk != null) {
                        pushFrontLeftBlocks(i, chunk, leftAdjChunk, frontAdjChunk, heightMap, builder)
                    }
                    if (rightAdjChunk != null) {
                        pushFrontRightBlocks(i, chunk, rightAdjChunk, frontAdjChunk, heightMap, builder)
                    }
                }

                if (backAdjChunk != null) {
                    pushBackBlocks(i, chunk, backAdjChunk, heightMap, builder)
                    if (leftAdjChunk != null) {
                        pushBackLeftBlocks(i, chunk, leftAdjChunk, backAdjChunk, heightMap, builder)
                    }
                    if (rightAdjChunk != null) {
                        pushBackRightBlocks(i, chunk, rightAdjChunk, backAdjChunk, heightMap, builder)
                    }
                }
            }

            val displayList = builder.build()
            return ColumnDisplay(displayList, cx, cz)
        }

        private fun pushInternalBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                for (z in 1 until Chunk.ZLENGTH - 1) {
                    for (x in 1 until Chunk.XLENGTH - 1) {
                        pushSingleBlock(
                            x, y + offsetY, z,
                            block = chunk.getBlock(x, y, z),
                            topAdjBlock = chunk.getBlock(x, y + 1, z),
                            bottomAdjBlock = chunk.getBlock(x, y - 1, z),
                            leftAdjBlock = chunk.getBlock(x - 1, y, z),
                            rightAdjBlock = chunk.getBlock(x + 1, y, z),
                            frontAdjBlock = chunk.getBlock(x, y, z + 1),
                            backAdjBlock = chunk.getBlock(x, y, z - 1),
                            getShade(x, y + offsetY, z, heightMap),
                            builder
                        )
                    }
                }
            }
        }

        private fun pushTopBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            adjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Y = Chunk.YLENGTH - 1
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (z in 1 until Chunk.ZLENGTH - 1) {
                for (x in 1 until Chunk.XLENGTH - 1) {
                    pushSingleBlock(
                        x, Y + offsetY, z,
                        block = chunk.getBlock(x, Y, z),
                        topAdjBlock = adjChunk.getBlock(x, 0, z),
                        bottomAdjBlock = chunk.getBlock(x, Y - 1, z),
                        leftAdjBlock = chunk.getBlock(x - 1, Y, z),
                        rightAdjBlock = chunk.getBlock(x + 1, Y, z),
                        frontAdjBlock = chunk.getBlock(x, Y, z + 1),
                        backAdjBlock = chunk.getBlock(x, Y, z - 1),
                        getShade(x, Y + offsetY, z, heightMap),
                        builder
                    )
                }
            }
        }

        private fun pushTopLeftEdgeBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            topAdjChunk: Chunk,
            leftAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Y = Chunk.YLENGTH - 1
            val XLENGTH = Chunk.XLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (z in 1 until Chunk.ZLENGTH - 1) {
                pushSingleBlock(
                    0, Y + offsetY, z,
                    block = chunk.getBlock(0, Y, z),
                    topAdjBlock = topAdjChunk.getBlock(0, 0, z),
                    bottomAdjBlock = chunk.getBlock(0, Y - 1, z),
                    leftAdjBlock = leftAdjChunk.getBlock(XLENGTH - 1, Y, z),
                    rightAdjBlock = chunk.getBlock(1, Y, z),
                    frontAdjBlock = chunk.getBlock(0, Y, z + 1),
                    backAdjBlock = chunk.getBlock(0, Y, z - 1),
                    getShade(0, Y + offsetY, z, heightMap),
                    builder
                )
            }
        }

        private fun pushTopRightEdgeBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            topAdjChunk: Chunk,
            rightAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val Y = Chunk.YLENGTH - 1
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (z in 1 until Chunk.ZLENGTH - 1) {
                pushSingleBlock(
                    X, Y + offsetY, z,
                    block = chunk.getBlock(X, Y, z),
                    topAdjBlock = topAdjChunk.getBlock(X, 0, z),
                    bottomAdjBlock = chunk.getBlock(X, Y - 1, z),
                    leftAdjBlock = chunk.getBlock(X - 1, Y, z),
                    rightAdjBlock = rightAdjChunk.getBlock(0, Y, z),
                    frontAdjBlock = chunk.getBlock(X, Y, z + 1),
                    backAdjBlock = chunk.getBlock(X, Y, z - 1),
                    getShade(X, Y + offsetY, z, heightMap),
                    builder
                )
            }
        }

        private fun pushTopFrontEdgeBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            topAdjChunk: Chunk,
            frontAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Y = Chunk.YLENGTH - 1
            val Z = Chunk.ZLENGTH - 1
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (x in 1 until Chunk.XLENGTH - 1) {
                pushSingleBlock(
                    x, Y + offsetY, Z,
                    block = chunk.getBlock(x, Y, Z),
                    topAdjBlock = topAdjChunk.getBlock(x, 0, Z),
                    bottomAdjBlock = chunk.getBlock(x, Y - 1, Z),
                    leftAdjBlock = chunk.getBlock(x - 1, Y, Z),
                    rightAdjBlock = chunk.getBlock(x + 1, Y, Z),
                    frontAdjBlock = frontAdjChunk.getBlock(x, Y, 0),
                    backAdjBlock = chunk.getBlock(x, Y, Z - 1),
                    getShade(x, Y + offsetY, Z, heightMap),
                    builder
                )
            }
        }

        private fun pushTopBackEdgeBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            topAdjChunk: Chunk,
            backAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Y = Chunk.YLENGTH - 1
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (x in 1 until Chunk.XLENGTH - 1) {
                pushSingleBlock(
                    x, Y + offsetY, 0,
                    block = chunk.getBlock(x, Y, 0),
                    topAdjBlock = topAdjChunk.getBlock(x, 0, 0),
                    bottomAdjBlock = chunk.getBlock(x, Y - 1, 0),
                    leftAdjBlock = chunk.getBlock(x - 1, Y, 0),
                    rightAdjBlock = chunk.getBlock(x + 1, Y, 0),
                    frontAdjBlock = chunk.getBlock(x, Y, 1),
                    backAdjBlock = backAdjChunk.getBlock(x, Y, ZLENGTH - 1),
                    getShade(x, Y + offsetY, 0, heightMap),
                    builder
                )
            }
        }

        private fun pushTopFrontLeftCornerBlock(
            chunkIndex: Int,
            chunk: Chunk,
            topAdjChunk: Chunk,
            frontAdjChunk: Chunk,
            leftAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Y = Chunk.YLENGTH - 1
            val Z = Chunk.ZLENGTH - 1
            val XLENGTH = Chunk.XLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            pushSingleBlock(
                0, Y + offsetY, Z,
                block = chunk.getBlock(0, Y, Z),
                topAdjBlock = topAdjChunk.getBlock(0, 0, Z),
                bottomAdjBlock = chunk.getBlock(0, Y - 1, Z),
                leftAdjBlock = leftAdjChunk.getBlock(XLENGTH - 1, Y, Z),
                rightAdjBlock = chunk.getBlock(1, Y, Z),
                frontAdjBlock = frontAdjChunk.getBlock(0, Y, 0),
                backAdjBlock = chunk.getBlock(0, Y, Z - 1),
                getShade(0, Y + offsetY, Z, heightMap),
                builder
            )
        }

        private fun pushTopFrontRightCornerBlock(
            chunkIndex: Int,
            chunk: Chunk,
            topAdjChunk: Chunk,
            frontAdjChunk: Chunk,
            rightAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val Y = Chunk.YLENGTH - 1
            val Z = Chunk.ZLENGTH - 1
            val offsetY = chunkIndex * Chunk.YLENGTH

            pushSingleBlock(
                X, Y + offsetY, Z,
                block = chunk.getBlock(X, Y, Z),
                topAdjBlock = topAdjChunk.getBlock(X, 0, Z),
                bottomAdjBlock = chunk.getBlock(X, Y - 1, Z),
                leftAdjBlock = chunk.getBlock(X - 1, Y, Z),
                rightAdjBlock = rightAdjChunk.getBlock(0, Y, Z),
                frontAdjBlock = frontAdjChunk.getBlock(X, Y, 0),
                backAdjBlock = chunk.getBlock(X, Y, Z - 1),
                getShade(X, Y + offsetY, Z, heightMap),
                builder
            )
        }

        private fun pushTopBackLeftCornerBlock(
            chunkIndex: Int,
            chunk: Chunk,
            topAdjChunk: Chunk,
            backAdjChunk: Chunk,
            leftAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Y = Chunk.YLENGTH - 1
            val XLENGTH = Chunk.XLENGTH
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            pushSingleBlock(
                0, Y + offsetY, 0,
                block = chunk.getBlock(0, Y, 0),
                topAdjBlock = topAdjChunk.getBlock(0, 0, 0),
                bottomAdjBlock = chunk.getBlock(0, Y - 1, 0),
                leftAdjBlock = leftAdjChunk.getBlock(XLENGTH - 1, Y, 0),
                rightAdjBlock = chunk.getBlock(1, Y, 0),
                frontAdjBlock = chunk.getBlock(0, Y, 1),
                backAdjBlock = backAdjChunk.getBlock(0, Y, ZLENGTH - 1),
                getShade(0, Y + offsetY, 0, heightMap),
                builder
            )
        }

        private fun pushTopBackRightCornerBlock(
            chunkIndex: Int,
            chunk: Chunk,
            topAdjChunk: Chunk,
            backAdjChunk: Chunk,
            rightAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val Y = Chunk.YLENGTH - 1
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            pushSingleBlock(
                X, Y + offsetY, 0,
                block = chunk.getBlock(X, Y, 0),
                topAdjBlock = topAdjChunk.getBlock(X, 0, 0),
                bottomAdjBlock = chunk.getBlock(X, Y - 1, 0),
                leftAdjBlock = chunk.getBlock(X - 1, Y, 0),
                rightAdjBlock = rightAdjChunk.getBlock(0, Y, 0),
                frontAdjBlock = chunk.getBlock(X, Y, 1),
                backAdjBlock = backAdjChunk.getBlock(X, Y, ZLENGTH - 1),
                getShade(X, Y + offsetY, 0, heightMap),
                builder
            )
        }

        private fun pushBottomBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            adjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val YLENGTH = Chunk.YLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (z in 1 until Chunk.ZLENGTH - 1) {
                for (x in 1 until Chunk.XLENGTH - 1) {
                    pushSingleBlock(
                        x, offsetY, z,
                        block = chunk.getBlock(x, 0, z),
                        topAdjBlock = chunk.getBlock(x, 1, z),
                        bottomAdjBlock = adjChunk.getBlock(x, YLENGTH - 1, z),
                        leftAdjBlock = chunk.getBlock(x - 1, 0, z),
                        rightAdjBlock = chunk.getBlock(x + 1, 0, z),
                        frontAdjBlock = chunk.getBlock(x, 0, z + 1),
                        backAdjBlock = chunk.getBlock(x, 0, z - 1),
                        getShade(x, offsetY, z, heightMap),
                        builder
                    )
                }
            }
        }

        private fun pushBottomLeftEdgeBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            bottomAdjChunk: Chunk,
            leftAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val XLENGTH = Chunk.XLENGTH
            val YLENGTH = Chunk.YLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (z in 1 until Chunk.ZLENGTH - 1) {
                pushSingleBlock(
                    0, offsetY, z,
                    block = chunk.getBlock(0, 0, z),
                    topAdjBlock = chunk.getBlock(0, 1, z),
                    bottomAdjBlock = bottomAdjChunk.getBlock(0, YLENGTH - 1, z),
                    leftAdjBlock = leftAdjChunk.getBlock(XLENGTH - 1, 0, z),
                    rightAdjBlock = chunk.getBlock(1, 0, z),
                    frontAdjBlock = chunk.getBlock(0, 0, z + 1),
                    backAdjBlock = chunk.getBlock(0, 0, z - 1),
                    getShade(0, offsetY, z, heightMap),
                    builder
                )
            }
        }

        private fun pushBottomRightEdgeBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            bottomAdjChunk: Chunk,
            rightAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val YLENGTH = Chunk.YLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (z in 1 until Chunk.ZLENGTH - 1) {
                pushSingleBlock(
                    X, offsetY, z,
                    block = chunk.getBlock(X, 0, z),
                    topAdjBlock = chunk.getBlock(X, 1, z),
                    bottomAdjBlock = bottomAdjChunk.getBlock(X, YLENGTH - 1, z),
                    leftAdjBlock = chunk.getBlock(X - 1, 0, z),
                    rightAdjBlock = rightAdjChunk.getBlock(0, 0, z),
                    frontAdjBlock = chunk.getBlock(X, 0, z + 1),
                    backAdjBlock = chunk.getBlock(X, 0, z - 1),
                    getShade(X, offsetY, z, heightMap),
                    builder
                )
            }
        }

        private fun pushBottomFrontEdgeBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            bottomAdjChunk: Chunk,
            frontAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Z = Chunk.ZLENGTH - 1
            val YLENGTH = Chunk.YLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (x in 1 until Chunk.XLENGTH - 1) {
                pushSingleBlock(
                    x, offsetY, Z,
                    block = chunk.getBlock(x, 0, Z),
                    topAdjBlock = chunk.getBlock(x, 1, Z),
                    bottomAdjBlock = bottomAdjChunk.getBlock(x, YLENGTH - 1, Z),
                    leftAdjBlock = chunk.getBlock(x - 1, 0, Z),
                    rightAdjBlock = chunk.getBlock(x + 1, 0, Z),
                    frontAdjBlock = frontAdjChunk.getBlock(x, 0, 0),
                    backAdjBlock = chunk.getBlock(x, 0, Z - 1),
                    getShade(x, offsetY, Z, heightMap),
                    builder
                )
            }
        }

        private fun pushBottomBackEdgeBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            bottomAdjChunk: Chunk,
            backAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val YLENGTH = Chunk.YLENGTH
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (x in 1 until Chunk.XLENGTH - 1) {
                pushSingleBlock(
                    x, offsetY, 0,
                    block = chunk.getBlock(x, 0, 0),
                    topAdjBlock = chunk.getBlock(x, 1, 0),
                    bottomAdjBlock = bottomAdjChunk.getBlock(x, YLENGTH - 1, 0),
                    leftAdjBlock = chunk.getBlock(x - 1, 0, 0),
                    rightAdjBlock = chunk.getBlock(x + 1, 0, 0),
                    frontAdjBlock = chunk.getBlock(x, 0, 1),
                    backAdjBlock = backAdjChunk.getBlock(x, 0, ZLENGTH - 1),
                    getShade(x, offsetY, 0, heightMap),
                    builder
                )
            }
        }

        private fun pushBottomFrontLeftCornerBlock(
            chunkIndex: Int,
            chunk: Chunk,
            bottomAdjChunk: Chunk,
            frontAdjChunk: Chunk,
            leftAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Z = Chunk.ZLENGTH - 1
            val XLENGTH = Chunk.XLENGTH
            val YLENGTH = Chunk.YLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            pushSingleBlock(
                0, offsetY, Z,
                block = chunk.getBlock(0, 0, Z),
                topAdjBlock = chunk.getBlock(0, 1, Z),
                bottomAdjBlock = bottomAdjChunk.getBlock(0, YLENGTH - 1, Z),
                leftAdjBlock = leftAdjChunk.getBlock(XLENGTH - 1, 0, Z),
                rightAdjBlock = chunk.getBlock(1, 0, Z),
                frontAdjBlock = frontAdjChunk.getBlock(0, 0, 0),
                backAdjBlock = chunk.getBlock(0, 0, Z - 1),
                getShade(0, offsetY, Z, heightMap),
                builder
            )
        }

        private fun pushBottomFrontRightCornerBlock(
            chunkIndex: Int,
            chunk: Chunk,
            bottomAdjChunk: Chunk,
            frontAdjChunk: Chunk,
            rightAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val Z = Chunk.ZLENGTH - 1
            val YLENGTH = Chunk.YLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            pushSingleBlock(
                X, offsetY, Z,
                block = chunk.getBlock(X, 0, Z),
                topAdjBlock = chunk.getBlock(X, 1, Z),
                bottomAdjBlock = bottomAdjChunk.getBlock(X, YLENGTH - 1, Z),
                leftAdjBlock = chunk.getBlock(X - 1, 0, Z),
                rightAdjBlock = rightAdjChunk.getBlock(0, 0, Z),
                frontAdjBlock = frontAdjChunk.getBlock(X, 0, 0),
                backAdjBlock = chunk.getBlock(X, 0, Z - 1),
                getShade(X, offsetY, Z, heightMap),
                builder
            )
        }

        private fun pushBottomBackLeftCornerBlock(
            chunkIndex: Int,
            chunk: Chunk,
            bottomAdjChunk: Chunk,
            backAdjChunk: Chunk,
            leftAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val XLENGTH = Chunk.XLENGTH
            val YLENGTH = Chunk.YLENGTH
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            pushSingleBlock(
                0, offsetY, 0,
                block = chunk.getBlock(0, 0, 0),
                topAdjBlock = chunk.getBlock(0, 1, 0),
                bottomAdjBlock = bottomAdjChunk.getBlock(0, YLENGTH - 1, 0),
                leftAdjBlock = leftAdjChunk.getBlock(XLENGTH - 1, 0, 0),
                rightAdjBlock = chunk.getBlock(1, 0, 0),
                frontAdjBlock = chunk.getBlock(0, 0, 1),
                backAdjBlock = backAdjChunk.getBlock(0, 0, ZLENGTH - 1),
                getShade(0, offsetY, 0, heightMap),
                builder
            )
        }

        private fun pushBottomBackRightCornerBlock(
            chunkIndex: Int,
            chunk: Chunk,
            bottomAdjChunk: Chunk,
            backAdjChunk: Chunk,
            rightAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val YLENGTH = Chunk.YLENGTH
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            pushSingleBlock(
                X, offsetY, 0,
                block = chunk.getBlock(X, 0, 0),
                topAdjBlock = chunk.getBlock(X, 1, 0),
                bottomAdjBlock = bottomAdjChunk.getBlock(X, YLENGTH - 1, 0),
                leftAdjBlock = chunk.getBlock(X - 1, 0, 0),
                rightAdjBlock = rightAdjChunk.getBlock(0, 0, 0),
                frontAdjBlock = chunk.getBlock(X, 0, 1),
                backAdjBlock = backAdjChunk.getBlock(X, 0, ZLENGTH - 1),
                getShade(X, offsetY, 0, heightMap),
                builder
            )
        }

        private fun pushLeftBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            adjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val XLENGTH = Chunk.XLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                for (z in 1 until Chunk.ZLENGTH - 1) {
                    pushSingleBlock(
                        0, y + offsetY, z,
                        block = chunk.getBlock(0, y, z),
                        topAdjBlock = chunk.getBlock(0, y + 1, z),
                        bottomAdjBlock = chunk.getBlock(0, y - 1, z),
                        leftAdjBlock = adjChunk.getBlock(XLENGTH - 1, y, z),
                        rightAdjBlock = chunk.getBlock(1, y, z),
                        frontAdjBlock = chunk.getBlock(0, y, z + 1),
                        backAdjBlock = chunk.getBlock(0, y, z - 1),
                        getShade(0, y + offsetY, z, heightMap),
                        builder
                    )
                }
            }
        }

        private fun pushRightBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            adjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                for (z in 1 until Chunk.ZLENGTH - 1) {
                    pushSingleBlock(
                        X, y + offsetY, z,
                        block = chunk.getBlock(X, y, z),
                        topAdjBlock = chunk.getBlock(X, y + 1, z),
                        bottomAdjBlock = chunk.getBlock(X, y - 1, z),
                        leftAdjBlock = chunk.getBlock(X - 1, y, z),
                        rightAdjBlock = adjChunk.getBlock(0, y, z),
                        frontAdjBlock = chunk.getBlock(X, y, z + 1),
                        backAdjBlock = chunk.getBlock(X, y, z - 1),
                        getShade(X, y + offsetY, z, heightMap),
                        builder
                    )
                }
            }
        }

        private fun pushFrontBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            adjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Z = Chunk.ZLENGTH - 1
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                for (x in 1 until Chunk.XLENGTH - 1) {
                    pushSingleBlock(
                        x, y + offsetY, Z,
                        block = chunk.getBlock(x, y, Z),
                        topAdjBlock = chunk.getBlock(x, y + 1, Z),
                        bottomAdjBlock = chunk.getBlock(x, y - 1, Z),
                        leftAdjBlock = chunk.getBlock(x - 1, y, Z),
                        rightAdjBlock = chunk.getBlock(x + 1, y, Z),
                        frontAdjBlock = adjChunk.getBlock(x, y, 0),
                        backAdjBlock = chunk.getBlock(x, y, Z - 1),
                        getShade(x, y + offsetY, Z, heightMap),
                        builder
                    )
                }
            }
        }

        private fun pushBackBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            adjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                for (x in 1 until Chunk.XLENGTH - 1) {
                    pushSingleBlock(
                        x, y + offsetY, 0,
                        block = chunk.getBlock(x, y, 0),
                        topAdjBlock = chunk.getBlock(x, y + 1, 0),
                        bottomAdjBlock = chunk.getBlock(x, y - 1, 0),
                        leftAdjBlock = chunk.getBlock(x - 1, y, 0),
                        rightAdjBlock = chunk.getBlock(x + 1, y, 0),
                        frontAdjBlock = chunk.getBlock(x, y, 1),
                        backAdjBlock = adjChunk.getBlock(x, y, ZLENGTH - 1),
                        getShade(x, y + offsetY, 0, heightMap),
                        builder
                    )
                }
            }
        }

        private fun pushFrontLeftBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            leftAdjChunk: Chunk,
            frontAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val Z = Chunk.ZLENGTH - 1
            val XLENGTH = Chunk.XLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                pushSingleBlock(
                    0, y + offsetY, Z,
                    block = chunk.getBlock(0, y, Z),
                    topAdjBlock = chunk.getBlock(0, y + 1, Z),
                    bottomAdjBlock = chunk.getBlock(0, y - 1, Z),
                    leftAdjBlock = leftAdjChunk.getBlock(XLENGTH - 1, y, Z),
                    rightAdjBlock = chunk.getBlock(1, y, Z),
                    frontAdjBlock = frontAdjChunk.getBlock(0, y, 0),
                    backAdjBlock = chunk.getBlock(0, y, Z - 1),
                    getShade(0, y + offsetY, Z, heightMap),
                    builder
                )
            }
        }

        private fun pushFrontRightBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            rightAdjChunk: Chunk,
            frontAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val Z = Chunk.ZLENGTH - 1
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                pushSingleBlock(
                    X, y + offsetY, Z,
                    block = chunk.getBlock(X, y, Z),
                    topAdjBlock = chunk.getBlock(X, y + 1, Z),
                    bottomAdjBlock = chunk.getBlock(X, y - 1, Z),
                    leftAdjBlock = chunk.getBlock(X - 1, y, Z),
                    rightAdjBlock = rightAdjChunk.getBlock(0, y, Z),
                    frontAdjBlock = frontAdjChunk.getBlock(X, y, 0),
                    backAdjBlock = chunk.getBlock(X, y, Z - 1),
                    getShade(X, y + offsetY, Z, heightMap),
                    builder
                )
            }
        }

        private fun pushBackLeftBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            leftAdjChunk: Chunk,
            backAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val XLENGTH = Chunk.XLENGTH
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                pushSingleBlock(
                    0, y + offsetY, 0,
                    block = chunk.getBlock(0, y, 0),
                    topAdjBlock = chunk.getBlock(0, y + 1, 0),
                    bottomAdjBlock = chunk.getBlock(0, y - 1, 0),
                    leftAdjBlock = leftAdjChunk.getBlock(XLENGTH - 1, y, 0),
                    rightAdjBlock = chunk.getBlock(1, y, 0),
                    frontAdjBlock = chunk.getBlock(0, y, 1),
                    backAdjBlock = backAdjChunk.getBlock(0, y, ZLENGTH - 1),
                    getShade(0, y + offsetY, 0, heightMap),
                    builder
                )
            }
        }

        private fun pushBackRightBlocks(
            chunkIndex: Int,
            chunk: Chunk,
            rightAdjChunk: Chunk,
            backAdjChunk: Chunk,
            heightMap: IntArray,
            builder: GLDisplayList.Builder
        ) {
            val X = Chunk.XLENGTH - 1
            val ZLENGTH = Chunk.ZLENGTH
            val offsetY = chunkIndex * Chunk.YLENGTH

            for (y in 1 until Chunk.YLENGTH - 1) {
                pushSingleBlock(
                    X, y + offsetY, 0,
                    block = chunk.getBlock(X, y, 0),
                    topAdjBlock = chunk.getBlock(X, y + 1, 0),
                    bottomAdjBlock = chunk.getBlock(X, y - 1, 0),
                    leftAdjBlock = chunk.getBlock(X - 1, y, 0),
                    rightAdjBlock = rightAdjChunk.getBlock(0, y, 0),
                    frontAdjBlock = chunk.getBlock(X, y, 1),
                    backAdjBlock = backAdjChunk.getBlock(X, y, ZLENGTH - 1),
                    getShade(X, y + offsetY, 0, heightMap),
                    builder
                )
            }
        }

        private fun pushSingleBlock(
            x: Int, y: Int, z: Int,
            block: Block,
            topAdjBlock: Block,
            bottomAdjBlock: Block,
            leftAdjBlock: Block,
            rightAdjBlock: Block,
            frontAdjBlock: Block,
            backAdjBlock: Block,
            shade: Float,
            builder: GLDisplayList.Builder
        ) {
            pushSingleModel(
                x, y, z,
                block.model,
                topAdjBlock.model,
                bottomAdjBlock.model,
                leftAdjBlock.model,
                rightAdjBlock.model,
                frontAdjBlock.model,
                backAdjBlock.model,
                shade,
                builder
            )
        }

        private fun pushSingleModel(
            x: Int, y: Int, z: Int,
            model: BlockModel,
            topAdjModel: BlockModel,
            bottomAdjModel: BlockModel,
            leftAdjModel: BlockModel,
            rightAdjModel: BlockModel,
            frontAdjModel: BlockModel,
            backAdjModel: BlockModel,
            shade: Float,
            builder: GLDisplayList.Builder
        ) {
            if (!bottomAdjModel.isBlocking(BlockModel.FACE_POS_Y) && model.hasFace(BlockModel.FACE_NEG_Y)) {
                builder.setColor(0.25f, 0.25f, 0.25f)
                pushModelFace(x, y, z, model, BlockModel.FACE_NEG_Y, builder)
            }
            if (!topAdjModel.isBlocking(BlockModel.FACE_NEG_Y) && model.hasFace(BlockModel.FACE_POS_Y)) {
                builder.setColor(1.0f, 1.0f, 1.0f)
                pushModelFace(x, y, z, model, BlockModel.FACE_POS_Y, builder)
            }
            if (!backAdjModel.isBlocking(BlockModel.FACE_POS_Z) && model.hasFace(BlockModel.FACE_NEG_Z)) {
                builder.setColor(0.75f, 0.75f, 0.75f)
                pushModelFace(x, y, z, model, BlockModel.FACE_NEG_Z, builder)
            }
            if (!frontAdjModel.isBlocking(BlockModel.FACE_NEG_Z) && model.hasFace(BlockModel.FACE_POS_Z)) {
                builder.setColor(0.75f, 0.75f, 0.75f)
                pushModelFace(x, y, z, model, BlockModel.FACE_POS_Z, builder)
            }
            if (!leftAdjModel.isBlocking(BlockModel.FACE_POS_X) && model.hasFace(BlockModel.FACE_NEG_X)) {
                builder.setColor(0.5f, 0.5f, 0.5f)
                pushModelFace(x, y, z, model, BlockModel.FACE_NEG_X, builder)
            }
            if (!rightAdjModel.isBlocking(BlockModel.FACE_NEG_X) && model.hasFace(BlockModel.FACE_POS_X)) {
                builder.setColor(0.5f, 0.5f, 0.5f)
                pushModelFace(x, y, z, model, BlockModel.FACE_POS_X, builder)
            }
        }

        private fun pushModelFace(
            x: Int, y: Int, z: Int,
            model: BlockModel,
            face: Int,
            builder: GLDisplayList.Builder
        ) {
            val textureAtlas = Assets.getMipmappedTextureAtlas()
            val texture = model.getTexture(face)
            for (i in 0 until model.getNumVertices(face)) {
                val vertX = model.getVertexAttribute(face, i, BlockModel.ATTRIBUTE_POS_X)
                val vertY = model.getVertexAttribute(face, i, BlockModel.ATTRIBUTE_POS_Y)
                val vertZ = model.getVertexAttribute(face, i, BlockModel.ATTRIBUTE_POS_Z)
                val vertU = model.getVertexAttribute(face, i, BlockModel.ATTRIBUTE_TEX_U)
                val vertV = model.getVertexAttribute(face, i, BlockModel.ATTRIBUTE_TEX_V)

                val u1 = textureAtlas.getTexCoordU1(texture)
                val u2 = textureAtlas.getTexCoordU2(texture)
                val v1 = textureAtlas.getTexCoordV1(texture)
                val v2 = textureAtlas.getTexCoordV2(texture)

                val mappedU = MathEx.lerp(u1, u2, vertU)
                val mappedV = MathEx.lerp(v1, v2, vertV)
                builder.pushVertex(x + vertX, y + vertY, z + vertZ, mappedU, mappedV)
            }
        }

        private fun genHeightMap(column: Column): IntArray {
            val heightMap = IntArray(Chunk.BASE_AREA)
            var chunkIndex = column.height - 1
            var done = false

            Arrays.fill(heightMap, -1)

            while (!done && chunkIndex >= 0) {
                done = true
                val chunk = column.getChunk(chunkIndex)

                for (z in 0 until Chunk.ZLENGTH) {
                    for (x in 0 until Chunk.XLENGTH) {
                        val index = x + z * Chunk.XLENGTH
                        if (heightMap[index] != -1) {
                            continue
                        }

                        var topY = Chunk.YLENGTH - 1
                        while (topY >= 0 && chunk.getBlock(x, topY, z) == StandardBlocks.AIR) {
                            --topY
                        }

                        if (topY == -1) {
                            done = false
                        }
                        else {
                            heightMap[index] = topY + chunkIndex * Chunk.YLENGTH
                        }
                    }
                }

                --chunkIndex
            }

            return heightMap
        }

        private fun getShade(x: Int, y: Int, z: Int, heightMap: IntArray): Float {
            val height = heightMap[x + z * Chunk.XLENGTH]
            return if (y < height) 0.75f else 1.0f
        }
    }
}