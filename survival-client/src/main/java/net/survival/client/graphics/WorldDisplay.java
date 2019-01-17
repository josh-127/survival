package net.survival.client.graphics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.joml.Matrix4f;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.survival.block.BlockFace;
import net.survival.client.graphics.blockrenderer.BlockRenderer;
import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;
import net.survival.world.World;
import net.survival.world.column.Column;
import net.survival.world.column.ColumnPos;

class WorldDisplay implements GraphicsResource
{
    private final World world;
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

    public WorldDisplay(World world, Camera camera, float maxViewRadius) {
        this.world = world;

        nonCubicDisplays = new HashMap<>();
        topFaceDisplays = new HashMap<>();
        bottomFaceDisplays = new HashMap<>();
        leftFaceDisplays = new HashMap<>();
        rightFaceDisplays = new HashMap<>();
        frontFaceDisplays = new HashMap<>();
        backFaceDisplays = new HashMap<>();

        Bitmap overlayBitmap = Bitmap.fromFile("../assets/textures/overlays/low_contrast.png");
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

        int dominantAxis = camera.getDominantAxis();
        BlockFace culledFace = null;

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
        for (Map.Entry<Column, ColumnDisplay> entry : displays.entrySet()) {
            ColumnDisplay display = entry.getValue();

            if (display.isEmpty())
                continue;

            float globalX = ColumnPos.toGlobalX(display.columnX, 0);
            float globalZ = ColumnPos.toGlobalZ(display.columnZ, 0);

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
            for (Map.Entry<Column, ColumnDisplay> entry : faceDisplays.entrySet()) {
                ColumnDisplay display = entry.getValue();

                if (display.isEmpty())
                    continue;

                float globalX = ColumnPos.toGlobalX(display.columnX, 0);
                float globalZ = ColumnPos.toGlobalZ(display.columnZ, 0);

                modelViewMatrix.set(viewMatrix).translate(globalX, 0.0f, globalZ);
                GLMatrixStack.load(modelViewMatrix);
                display.displayBlocks();
            }
        }
    }

    private void updateNonCubicDisplays(HashMap<Column, ColumnDisplay> nonCubicDisplays) {
        float cameraX = camera.x;
        float cameraZ = camera.z;
        float maxViewRadiusSquared = maxViewRadius * maxViewRadius;

        Iterator<Long2ObjectMap.Entry<Column>> columnMapIt = world.getColumnMapFastIterator();
        while (columnMapIt.hasNext()) {
            Long2ObjectMap.Entry<Column> entry = columnMapIt.next();
            long hashedPos = entry.getLongKey();
            Column column = entry.getValue();

            int cx = ColumnPos.columnXFromHashedPos(hashedPos);
            int cz = ColumnPos.columnZFromHashedPos(hashedPos);

            float relativeX = ColumnPos.toGlobalX(cx, Column.XLENGTH / 2) - cameraX;
            float relativeZ = ColumnPos.toGlobalZ(cz, Column.ZLENGTH / 2) - cameraZ;
            float squareDistance = (relativeX * relativeX) + (relativeZ * relativeZ);

            if (squareDistance >= maxViewRadiusSquared)
                continue;

            ColumnDisplay existingDisplay = nonCubicDisplays.get(column);
            boolean needsUpdating = existingDisplay == null || columnsToRedraw.contains(hashedPos);

            if (needsUpdating) {
                if (existingDisplay != null)
                    existingDisplay.close();

                nonCubicDisplays.put(column, ColumnDisplay.create(cx, cz, column, null, null));
            }
        }

        Iterator<Map.Entry<Column, ColumnDisplay>> faceDisplaysIt = nonCubicDisplays.entrySet().iterator();
        while (faceDisplaysIt.hasNext()) {
            Map.Entry<Column, ColumnDisplay> entry = faceDisplaysIt.next();
            ColumnDisplay columnDisplay = entry.getValue();

            if (!world.containsColumn(columnDisplay.columnX, columnDisplay.columnZ)) {
                columnDisplay.close();
                faceDisplaysIt.remove();
            }
        }
    }

    private void updateFaceDisplays(HashMap<Column, ColumnDisplay> faceDisplays, BlockFace blockFace)
    {
        int dx = 0;
        int dz = 0;

        if (blockFace != null) {
            switch (blockFace) {
            case FRONT: dz = 1;  break;
            case BACK:  dz = -1; break;
            case LEFT:  dx = -1; break;
            case RIGHT: dx = 1;  break;
            default:             break;
            }
        }

        float cameraX = camera.x;
        float cameraZ = camera.z;
        float maxViewRadiusSquared = maxViewRadius * maxViewRadius;

        Iterator<Long2ObjectMap.Entry<Column>> columnMapIt = world.getColumnMapFastIterator();
        while (columnMapIt.hasNext()) {
            Long2ObjectMap.Entry<Column> entry = columnMapIt.next();
            long hashedPos = entry.getLongKey();
            Column column = entry.getValue();

            int cx = ColumnPos.columnXFromHashedPos(hashedPos);
            int cz = ColumnPos.columnZFromHashedPos(hashedPos);

            float relativeX = ColumnPos.toGlobalX(cx, Column.XLENGTH / 2) - cameraX;
            float relativeZ = ColumnPos.toGlobalZ(cz, Column.ZLENGTH / 2) - cameraZ;
            float squareDistance = (relativeX * relativeX) + (relativeZ * relativeZ);

            if (squareDistance >= maxViewRadiusSquared)
                continue;

            ColumnDisplay existingDisplay = faceDisplays.get(column);
            Column adjacentColumn = world.getColumn(cx + dx, cz + dz);
            long adjacentHashedPos = ColumnPos.hashPos(cx + dx, cz + dz);

            boolean needsUpdating = existingDisplay == null
                    || (existingDisplay.adjacentColumn == null && adjacentColumn != null)
                    || columnsToRedraw.contains(hashedPos)
                    || columnsToRedraw.contains(adjacentHashedPos);

            if (needsUpdating) {
                if (existingDisplay != null)
                    existingDisplay.close();

                faceDisplays.put(column, ColumnDisplay.create(cx, cz, column, adjacentColumn, blockFace));
            }
        }

        Iterator<Map.Entry<Column, ColumnDisplay>> faceDisplaysIt = faceDisplays.entrySet().iterator();
        while (faceDisplaysIt.hasNext()) {
            Map.Entry<Column, ColumnDisplay> entry = faceDisplaysIt.next();
            ColumnDisplay columnDisplay = entry.getValue();

            if (!world.containsColumn(columnDisplay.columnX, columnDisplay.columnZ)) {
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