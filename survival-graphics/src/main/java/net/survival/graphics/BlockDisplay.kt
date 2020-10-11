package net.survival.graphics

import net.survival.block.Column
import net.survival.block.ColumnPos
import net.survival.graphics.opengl.*
import org.joml.Matrix4f
import java.util.*

internal class BlockDisplay(
    private val camera: PerspectiveCamera
) {
    private val columns: MutableMap<Long, Column?> = HashMap()
    private val columnDisplays = HashMap<Column?, ColumnDisplay>()
    private val invalidatedColumns = HashMap<Long, Column?>()
    private val overlayTexture: GLTexture

    private val cameraViewMatrix = Matrix4f()
    private val cameraProjectionMatrix = Matrix4f()
    private val modelViewMatrix = Matrix4f()

    init {
        val overlayBitmap = Bitmap.fromFile("./assets/textures/overlays/low_contrast.png")
        overlayTexture = GLTexture()
        overlayTexture.beginBind()
            .setMinFilter(GLFilterMode.LINEAR_MIPMAP_LINEAR)
            .setMagFilter(GLFilterMode.LINEAR)
            .setWrapS(GLWrapMode.REPEAT)
            .setWrapT(GLWrapMode.REPEAT)
            .setMipmapEnabled(true)
            .setData(overlayBitmap)
            .endBind()
    }

    fun close() {
        for (columnDisplay in columnDisplays.values) {
            columnDisplay.close()
        }
    }

    fun display() {
        updateColumnDisplays(columnDisplays)

        cameraViewMatrix.identity()
        camera.getViewMatrix(cameraViewMatrix)
        cameraProjectionMatrix.identity()
        camera.getProjectionMatrix(cameraProjectionMatrix)

        GLState.pushAlphaTestEnabled(true)
        GLState.pushAlphaFunction(GLComparisonFunc.EQUAL, 1.0f)

        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix)
        GLMatrixStack.push()
        GLMatrixStack.loadIdentity()

        drawColumnDisplays(columnDisplays, cameraViewMatrix)

        GLMatrixStack.pop()

        GLState.popAlphaFunction()
        GLState.popAlphaTestEnabled()
    }

    private fun drawColumnDisplays(displays: HashMap<Column?, ColumnDisplay>, viewMatrix: Matrix4f) {
        val textureAtlas = Assets.getMipmappedTextureAtlas()
        val textures = textureAtlas.textureObject

        for ((_, columnDisplay) in displays) {
            val globalX = ColumnPos.toGlobalX(columnDisplay.chunkX, 0)
            val globalZ = ColumnPos.toGlobalZ(columnDisplay.chunkZ, 0)

            modelViewMatrix.set(viewMatrix).translate(globalX.toFloat(), 0.0f, globalZ.toFloat())
            GLMatrixStack.load(modelViewMatrix)
            columnDisplay.displayBlocks(textures)
        }
    }

    private fun updateColumnDisplays(columnDisplays: HashMap<Column?, ColumnDisplay>) {
        for ((columnPos, newColumn) in invalidatedColumns) {
            val oldColumn = columns[columnPos]

            val cx = ColumnPos.columnXFromHashedPos(columnPos)
            val cz = ColumnPos.columnZFromHashedPos(columnPos)

            val oldDisplay = columnDisplays[oldColumn]
            oldDisplay?.close()
            columnDisplays.remove(oldColumn)

            if (newColumn == null) {
                columns.remove(columnPos)
            }
            else {
                val leftAdjacentColumnPos = ColumnPos.hashPos(cx - 1, cz)
                val rightAdjacentColumnPos = ColumnPos.hashPos(cx + 1, cz)
                val frontAdjacentColumnPos = ColumnPos.hashPos(cx, cz + 1)
                val backAdjacentColumnPos = ColumnPos.hashPos(cx, cz - 1)

                var leftAdjacentColumn = invalidatedColumns[leftAdjacentColumnPos]
                if (leftAdjacentColumn == null) {
                    leftAdjacentColumn = columns[leftAdjacentColumnPos]
                }

                var rightAdjacentColumn = invalidatedColumns[rightAdjacentColumnPos]
                if (rightAdjacentColumn == null) {
                    rightAdjacentColumn = columns[rightAdjacentColumnPos]
                }

                var frontAdjacentColumn = invalidatedColumns[frontAdjacentColumnPos]
                if (frontAdjacentColumn == null) {
                    frontAdjacentColumn = columns[frontAdjacentColumnPos]
                }

                var backAdjacentColumn = invalidatedColumns[backAdjacentColumnPos]
                if (backAdjacentColumn == null) {
                    backAdjacentColumn = columns[backAdjacentColumnPos]
                }

                columnDisplays[newColumn] = ColumnDisplay.create(
                    cx, cz,
                    newColumn,
                    leftAdjacentColumn,
                    rightAdjacentColumn,
                    frontAdjacentColumn,
                    backAdjacentColumn
                )
            }
        }

        columns.putAll(invalidatedColumns)
        invalidatedColumns.clear()
    }

    fun redrawColumn(columnPos: Long, column: Column?) {
        invalidatedColumns[columnPos] = column
        if (column != null) {
            val cx = ColumnPos.columnXFromHashedPos(columnPos)
            val cz = ColumnPos.columnZFromHashedPos(columnPos)
            redrawAdjacentColumn(cx - 1, cz)
            redrawAdjacentColumn(cx + 1, cz)
            redrawAdjacentColumn(cx, cz - 1)
            redrawAdjacentColumn(cx, cz + 1)
        }
    }

    private fun redrawAdjacentColumn(cx: Int, cz: Int) {
        val columnPos = ColumnPos.hashPos(cx, cz)
        invalidatedColumns.putIfAbsent(columnPos, columns[columnPos])
    }
}