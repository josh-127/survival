package net.survival.client.graphics;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;

import net.survival.block.Column;
import net.survival.block.ColumnPos;
import net.survival.client.graphics.blockrenderer.BlockRenderer;
import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;

class BlockDisplay implements GraphicsResource
{
    private final Map<Long, Column> columns = new HashMap<>();
    private final HashMap<Column, ColumnDisplay> columnDisplays = new HashMap<>();
    private final HashMap<Long, Column> invalidatedColumns = new HashMap<>();
    private final GLTexture overlayTexture;

    private final Camera camera;
    private Matrix4f cameraViewMatrix = new Matrix4f();
    private Matrix4f cameraProjectionMatrix = new Matrix4f();
    private Matrix4f modelViewMatrix = new Matrix4f();

    public BlockDisplay(Camera camera) {
        var overlayBitmap = Bitmap.fromFile("../assets/textures/overlays/low_contrast.png");
        overlayTexture = new GLTexture();
        overlayTexture.beginBind()
                .setMinFilter(GLFilterMode.LINEAR_MIPMAP_LINEAR)
                .setMagFilter(GLFilterMode.LINEAR)
                .setWrapS(GLWrapMode.REPEAT)
                .setWrapT(GLWrapMode.REPEAT)
                .setMipmapEnabled(true)
                .setData(overlayBitmap)
                .endBind();

        this.camera = camera;

        BlockRenderer.initTextures();
    }

    @Override
    public void close() {
        for (var columnDisplay : columnDisplays.values())
            columnDisplay.close();
    }

    public void display() {
        updateColumnDisplays(columnDisplays);

        cameraViewMatrix.identity();
        camera.getViewMatrix(cameraViewMatrix);
        cameraProjectionMatrix.identity();
        camera.getProjectionMatrix(cameraProjectionMatrix);

        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.loadIdentity();

        drawColumnDisplays(columnDisplays, cameraViewMatrix);

        GLMatrixStack.pop();
    }

    private void drawColumnDisplays(HashMap<Column, ColumnDisplay> displays, Matrix4f viewMatrix) {
        for (var entry : displays.entrySet()) {
            var columnDisplay = entry.getValue();

            var globalX = ColumnPos.toGlobalX(columnDisplay.cx, 0);
            var globalZ = ColumnPos.toGlobalZ(columnDisplay.cz, 0);

            modelViewMatrix.set(viewMatrix).translate(globalX, 0.0f, globalZ);
            GLMatrixStack.load(modelViewMatrix);
            columnDisplay.display();
        }
    }

    private void updateColumnDisplays(HashMap<Column, ColumnDisplay> columnDisplays) {
        for (var entry : invalidatedColumns.entrySet()) {
            var columnPos = (long) entry.getKey();
            var newColumn = entry.getValue();
            var oldColumn = columns.get(columnPos);

            var cx = ColumnPos.columnXFromHashedPos(columnPos);
            var cz = ColumnPos.columnZFromHashedPos(columnPos);

            var oldDisplay = columnDisplays.get(oldColumn);
            if (oldDisplay != null) 
                oldDisplay.close();
            columnDisplays.remove(oldColumn);

            if (newColumn == null) {
                columns.remove(columnPos);
            }
            else {
                var leftAdjacentColumnPos = ColumnPos.hashPos(cx - 1, cz);
                var rightAdjacentColumnPos = ColumnPos.hashPos(cx + 1, cz);
                var frontAdjacentColumnPos = ColumnPos.hashPos(cx, cz + 1);
                var backAdjacentColumnPos = ColumnPos.hashPos(cx, cz - 1);

                var leftAdjacentColumn = invalidatedColumns.get(leftAdjacentColumnPos);
                if (leftAdjacentColumn == null)
                    leftAdjacentColumn = columns.get(leftAdjacentColumnPos);

                var rightAdjacentColumn = invalidatedColumns.get(rightAdjacentColumnPos);
                if (rightAdjacentColumn == null)
                    rightAdjacentColumn = columns.get(rightAdjacentColumnPos);

                var frontAdjacentColumn = invalidatedColumns.get(frontAdjacentColumnPos);
                if (frontAdjacentColumn == null)
                    frontAdjacentColumn = columns.get(frontAdjacentColumnPos);

                var backAdjacentColumn = invalidatedColumns.get(backAdjacentColumnPos);
                if (backAdjacentColumn == null)
                    backAdjacentColumn = columns.get(backAdjacentColumnPos);

                columnDisplays.put(
                        newColumn,
                        ColumnDisplay.create(
                                cx, cz,
                                newColumn,
                                leftAdjacentColumn,
                                rightAdjacentColumn,
                                frontAdjacentColumn,
                                backAdjacentColumn));
            }
        }

        columns.putAll(invalidatedColumns);
        invalidatedColumns.clear();
    }

    public void redrawColumn(long columnPos, Column column) {
        invalidatedColumns.put(columnPos, column);

        if (column != null) {
            var cx = ColumnPos.columnXFromHashedPos(columnPos);
            var cz = ColumnPos.columnZFromHashedPos(columnPos);
            redrawAdjacentColumn(cx - 1, cz);
            redrawAdjacentColumn(cx + 1, cz);
            redrawAdjacentColumn(cx, cz - 1);
            redrawAdjacentColumn(cx, cz + 1);
        }
    }

    private void redrawAdjacentColumn(int cx, int cz) {
        var columnPos = ColumnPos.hashPos(cx, cz);
        invalidatedColumns.putIfAbsent(columnPos, columns.get(columnPos));
    }

    public Camera getCamera() {
        return camera;
    }
}