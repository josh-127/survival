package net.survival.client.graphics;

import java.util.HashMap;

import org.joml.Matrix4f;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.survival.block.BlockSpace;
import net.survival.block.column.Column;
import net.survival.block.column.ColumnPos;
import net.survival.blocktype.BlockFace;
import net.survival.client.graphics.blockrenderer.BlockRenderer;
import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;

class BlockDisplay implements GraphicsResource
{
    private final BlockSpace blockSpace;
    private HashMap<Column, ColumnDisplay> nonCubicDisplays;
    private HashMap<Column, ColumnDisplay> topFaceDisplays;
    private HashMap<Column, ColumnDisplay> bottomFaceDisplays;
    private HashMap<Column, ColumnDisplay> leftFaceDisplays;
    private HashMap<Column, ColumnDisplay> rightFaceDisplays;
    private HashMap<Column, ColumnDisplay> frontFaceDisplays;
    private HashMap<Column, ColumnDisplay> backFaceDisplays;
    private final GLTexture overlayTexture;

    private final LongSet columnsToRedraw;

    private final Camera camera;
    private final float maxViewRadius;

    private Matrix4f cameraViewMatrix;
    private Matrix4f cameraProjectionMatrix;
    private Matrix4f modelViewMatrix;

    public BlockDisplay(BlockSpace blockSpace, Camera camera, float maxViewRadius) {
        this.blockSpace = blockSpace;

        nonCubicDisplays = new HashMap<>();
        topFaceDisplays = new HashMap<>();
        bottomFaceDisplays = new HashMap<>();
        leftFaceDisplays = new HashMap<>();
        rightFaceDisplays = new HashMap<>();
        frontFaceDisplays = new HashMap<>();
        backFaceDisplays = new HashMap<>();

        var overlayBitmap = Bitmap.fromFile("../assets/textures/overlays/low_contrast.png");
        overlayTexture = new GLTexture();
        overlayTexture.beginBind().setMinFilter(GLFilterMode.LINEAR_MIPMAP_LINEAR)
                .setMagFilter(GLFilterMode.LINEAR).setWrapS(GLWrapMode.REPEAT)
                .setWrapT(GLWrapMode.REPEAT).setMipmapEnabled(true).setData(overlayBitmap)
                .endBind();

        columnsToRedraw = new LongOpenHashSet();

        this.camera = camera;
        this.maxViewRadius = maxViewRadius;

        cameraViewMatrix = new Matrix4f();
        cameraProjectionMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();

        BlockRenderer.initTextures();
    }

    @Override
    public void close() {
        for (ColumnDisplay display : nonCubicDisplays.values())
            display.close();
        for (ColumnDisplay display : topFaceDisplays.values())
            display.close();
        for (ColumnDisplay display : bottomFaceDisplays.values())
            display.close();
        for (ColumnDisplay display : leftFaceDisplays.values())
            display.close();
        for (ColumnDisplay display : rightFaceDisplays.values())
            display.close();
        for (ColumnDisplay display : frontFaceDisplays.values())
            display.close();
        for (ColumnDisplay display : backFaceDisplays.values())
            display.close();
    }

    public void display() {
        updateNonCubicDisplays(nonCubicDisplays);
        updateFaceDisplays(topFaceDisplays, BlockFace.TOP);
        updateFaceDisplays(bottomFaceDisplays, BlockFace.BOTTOM);
        updateFaceDisplays(leftFaceDisplays, BlockFace.LEFT);
        updateFaceDisplays(rightFaceDisplays, BlockFace.RIGHT);
        updateFaceDisplays(frontFaceDisplays, BlockFace.FRONT);
        updateFaceDisplays(backFaceDisplays, BlockFace.BACK);

        cameraViewMatrix.identity();
        camera.getViewMatrix(cameraViewMatrix);
        cameraProjectionMatrix.identity();
        camera.getProjectionMatrix(cameraProjectionMatrix);

        var dominantAxis = camera.getDominantAxis();
        var culledFace = (BlockFace) null;

        if (dominantAxis == 0) {
            if (camera.getDirectionX() < 0.0f)
                culledFace = BlockFace.LEFT;
            else
                culledFace = BlockFace.RIGHT;
        }
        else if (dominantAxis == 1) {
            if (camera.getDirectionY() < 0.0f)
                culledFace = BlockFace.BOTTOM;
            else
                culledFace = BlockFace.TOP;
        }
        else {
            if (camera.getDirectionZ() < 0.0f)
                culledFace = BlockFace.BACK;
            else
                culledFace = BlockFace.FRONT;
        }

        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.loadIdentity();

        drawNonCubicDisplays(nonCubicDisplays, cameraViewMatrix);

        if (culledFace != BlockFace.TOP)
            drawFaceDisplays(topFaceDisplays, BlockFace.TOP, false, cameraViewMatrix);
        if (culledFace != BlockFace.BOTTOM)
            drawFaceDisplays(bottomFaceDisplays, BlockFace.BOTTOM, false, cameraViewMatrix);
        if (culledFace != BlockFace.LEFT)
        drawFaceDisplays(leftFaceDisplays, BlockFace.LEFT, false, cameraViewMatrix);
        if (culledFace != BlockFace.RIGHT)
            drawFaceDisplays(rightFaceDisplays, BlockFace.RIGHT, false, cameraViewMatrix);
        if (culledFace != BlockFace.FRONT)
            drawFaceDisplays(frontFaceDisplays, BlockFace.FRONT, false, cameraViewMatrix);
        if (culledFace != BlockFace.BACK)
            drawFaceDisplays(backFaceDisplays, BlockFace.BACK, false, cameraViewMatrix);

        GLMatrixStack.pop();

        columnsToRedraw.clear();
    }

    private void drawNonCubicDisplays(HashMap<Column, ColumnDisplay> displays, Matrix4f viewMatrix) {
        for (var entry : displays.entrySet()) {
            var display = entry.getValue();

            if (display.isEmpty())
                continue;

            var globalX = ColumnPos.toGlobalX(display.columnX, 0);
            var globalZ = ColumnPos.toGlobalZ(display.columnZ, 0);

            modelViewMatrix.set(viewMatrix).translate(globalX, 0.0f, globalZ);
            GLMatrixStack.load(modelViewMatrix);
            display.displayBlocks();
        }
    }

    private void drawFaceDisplays(HashMap<Column, ColumnDisplay> faceDisplays, BlockFace blockFace,
            boolean drawOverlay, Matrix4f viewMatrix)
    {
        // Block Faces
        if (!drawOverlay) {
            for (var entry : faceDisplays.entrySet()) {
                var display = entry.getValue();

                if (display.isEmpty())
                    continue;

                var globalX = ColumnPos.toGlobalX(display.columnX, 0);
                var globalZ = ColumnPos.toGlobalZ(display.columnZ, 0);

                modelViewMatrix.set(viewMatrix).translate(globalX, 0.0f, globalZ);
                GLMatrixStack.load(modelViewMatrix);
                display.displayBlocks();
            }
        }
    }

    private void updateNonCubicDisplays(HashMap<Column, ColumnDisplay> nonCubicDisplays) {
        var cameraX = camera.x;
        var cameraZ = camera.z;
        var maxViewRadiusSquared = maxViewRadius * maxViewRadius;

        var columnMapIt = blockSpace.iterateColumnMap().iterator();
        while (columnMapIt.hasNext()) {
            var entry = columnMapIt.next();
            var hashedPos = (long) entry.getKey();
            var column = entry.getValue();

            var cx = ColumnPos.columnXFromHashedPos(hashedPos);
            var cz = ColumnPos.columnZFromHashedPos(hashedPos);

            var relativeX = ColumnPos.toGlobalX(cx, Column.XLENGTH / 2) - cameraX;
            var relativeZ = ColumnPos.toGlobalZ(cz, Column.ZLENGTH / 2) - cameraZ;
            var squareDistance = (relativeX * relativeX) + (relativeZ * relativeZ);

            if (squareDistance >= maxViewRadiusSquared)
                continue;

            var existingDisplay = nonCubicDisplays.get(column);
            var needsUpdating = existingDisplay == null || columnsToRedraw.contains(hashedPos);

            if (needsUpdating) {
                if (existingDisplay != null)
                    existingDisplay.close();

                nonCubicDisplays.put(column, ColumnDisplay.create(cx, cz, column, null, null));
            }
        }

        var faceDisplaysIt = nonCubicDisplays.entrySet().iterator();
        while (faceDisplaysIt.hasNext()) {
            var entry = faceDisplaysIt.next();
            var columnDisplay = entry.getValue();

            if (!blockSpace.containsColumn(columnDisplay.columnX, columnDisplay.columnZ)) {
                columnDisplay.close();
                faceDisplaysIt.remove();
            }
        }
    }

    private void updateFaceDisplays(HashMap<Column, ColumnDisplay> faceDisplays, BlockFace blockFace)
    {
        var dx = 0;
        var dz = 0;

        if (blockFace != null) {
            switch (blockFace) {
            case FRONT: dz = 1;  break;
            case BACK:  dz = -1; break;
            case LEFT:  dx = -1; break;
            case RIGHT: dx = 1;  break;
            default:             break;
            }
        }

        var cameraX = camera.x;
        var cameraZ = camera.z;
        var maxViewRadiusSquared = maxViewRadius * maxViewRadius;

        var columnMapIt = blockSpace.iterateColumnMap().iterator();
        while (columnMapIt.hasNext()) {
            var entry = columnMapIt.next();
            var hashedPos = (long) entry.getKey();
            var column = entry.getValue();

            var cx = ColumnPos.columnXFromHashedPos(hashedPos);
            var cz = ColumnPos.columnZFromHashedPos(hashedPos);

            var relativeX = ColumnPos.toGlobalX(cx, Column.XLENGTH / 2) - cameraX;
            var relativeZ = ColumnPos.toGlobalZ(cz, Column.ZLENGTH / 2) - cameraZ;
            var squareDistance = (relativeX * relativeX) + (relativeZ * relativeZ);

            if (squareDistance >= maxViewRadiusSquared)
                continue;

            var existingDisplay = faceDisplays.get(column);
            var adjacentColumn = blockSpace.getColumn(cx + dx, cz + dz);
            var adjacentHashedPos = ColumnPos.hashPos(cx + dx, cz + dz);

            var needsUpdating = existingDisplay == null
                    || (existingDisplay.adjacentColumn == null && adjacentColumn != null)
                    || columnsToRedraw.contains(hashedPos)
                    || columnsToRedraw.contains(adjacentHashedPos);

            if (needsUpdating) {
                if (existingDisplay != null)
                    existingDisplay.close();

                faceDisplays.put(column, ColumnDisplay.create(cx, cz, column, adjacentColumn, blockFace));
            }
        }

        var faceDisplaysIt = faceDisplays.entrySet().iterator();
        while (faceDisplaysIt.hasNext()) {
            var entry = faceDisplaysIt.next();
            var columnDisplay = entry.getValue();

            if (!blockSpace.containsColumn(columnDisplay.columnX, columnDisplay.columnZ)) {
                columnDisplay.close();
                faceDisplaysIt.remove();
            }
        }
    }

    public void redrawColumn(long hashedPos) {
        columnsToRedraw.add(hashedPos);
    }

    public Camera getCamera() {
        return camera;
    }
}