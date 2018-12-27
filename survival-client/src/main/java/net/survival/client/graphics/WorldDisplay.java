package net.survival.client.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.joml.Matrix4f;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.survival.block.BlockFace;
import net.survival.client.graphics.blockrenderer.BlockRenderer;
import net.survival.client.graphics.model.ModelRenderer;
import net.survival.client.graphics.model.StaticModel;
import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;
import net.survival.world.World;
import net.survival.world.actor.Actor;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;

class WorldDisplay implements GraphicsResource
{
    private final World world;
    private HashMap<Chunk, ChunkDisplay> nonCubicDisplays;
    private HashMap<Chunk, ChunkDisplay> topFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> bottomFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> leftFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> rightFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> frontFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> backFaceDisplays;
    private final GLTexture overlayTexture;

    private final LongSet chunksToRedraw;

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

        chunksToRedraw = new LongOpenHashSet();

        this.camera = camera;
        this.maxViewRadius = maxViewRadius;

        cameraViewMatrix = new Matrix4f();
        cameraProjectionMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();

        BlockRenderer.initTextures();
    }

    @Override
    public void close() {
        for (ChunkDisplay display : nonCubicDisplays.values())
            display.close();
        for (ChunkDisplay display : topFaceDisplays.values())
            display.close();
        for (ChunkDisplay display : bottomFaceDisplays.values())
            display.close();
        for (ChunkDisplay display : leftFaceDisplays.values())
            display.close();
        for (ChunkDisplay display : rightFaceDisplays.values())
            display.close();
        for (ChunkDisplay display : frontFaceDisplays.values())
            display.close();
        for (ChunkDisplay display : backFaceDisplays.values())
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

        drawActors(cameraViewMatrix);

        GLMatrixStack.pop();

        chunksToRedraw.clear();
    }

    private void drawNonCubicDisplays(HashMap<Chunk, ChunkDisplay> displays, Matrix4f viewMatrix) {
        for (Map.Entry<Chunk, ChunkDisplay> entry : displays.entrySet()) {
            ChunkDisplay display = entry.getValue();

            if (display.isEmpty())
                continue;

            float globalX = ChunkPos.toGlobalX(display.chunkX, 0);
            float globalZ = ChunkPos.toGlobalZ(display.chunkZ, 0);

            modelViewMatrix.set(viewMatrix).translate(globalX, 0.0f, globalZ);
            GLMatrixStack.load(modelViewMatrix);
            display.displayBlocks();
        }
    }

    private void drawFaceDisplays(HashMap<Chunk, ChunkDisplay> faceDisplays, BlockFace blockFace,
            boolean drawOverlay, Matrix4f viewMatrix)
    {
        // Block Faces
        if (!drawOverlay) {
            for (Map.Entry<Chunk, ChunkDisplay> entry : faceDisplays.entrySet()) {
                ChunkDisplay display = entry.getValue();

                if (display.isEmpty())
                    continue;

                float globalX = ChunkPos.toGlobalX(display.chunkX, 0);
                float globalZ = ChunkPos.toGlobalZ(display.chunkZ, 0);

                modelViewMatrix.set(viewMatrix).translate(globalX, 0.0f, globalZ);
                GLMatrixStack.load(modelViewMatrix);
                display.displayBlocks();
            }
        }
    }

    private void drawActors(Matrix4f viewMatrix) {
        GLMatrixStack.push();
        GLMatrixStack.load(viewMatrix);

        ArrayList<Actor> actors = world.getActors();

        for (Actor actor : actors)
            drawActor(actor);

        GLMatrixStack.pop();
    }

    private void drawActor(Actor actor) {
        GLMatrixStack.push();
        GLMatrixStack.translate((float) actor.getX(), (float) actor.getY(), (float) actor.getZ());
        GLMatrixStack.rotate((float) actor.getYaw(), 0.0f, 1.0f, 0.0f);
        GLMatrixStack.rotate((float) actor.getPitch(), 1.0f, 0.0f, 0.0f);
        GLMatrixStack.rotate((float) actor.getRoll(), 0.0f, 0.0f, 1.0f);

        StaticModel model = StaticModel.fromActor(actor);
        ModelRenderer.displayStaticModel(model);

        GLMatrixStack.pop();
    }

    private void updateNonCubicDisplays(HashMap<Chunk, ChunkDisplay> nonCubicDisplays) {
        float cameraX = camera.x;
        float cameraZ = camera.z;
        float maxViewRadiusSquared = maxViewRadius * maxViewRadius;

        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);

            float relativeX = ChunkPos.toGlobalX(cx, Chunk.XLENGTH / 2) - cameraX;
            float relativeZ = ChunkPos.toGlobalZ(cz, Chunk.ZLENGTH / 2) - cameraZ;
            float squareDistance = (relativeX * relativeX) + (relativeZ * relativeZ);

            if (squareDistance >= maxViewRadiusSquared)
                continue;

            ChunkDisplay existingDisplay = nonCubicDisplays.get(chunk);
            boolean needsUpdating = existingDisplay == null || chunksToRedraw.contains(hashedPos);

            if (needsUpdating) {
                if (existingDisplay != null)
                    existingDisplay.close();

                nonCubicDisplays.put(chunk, ChunkDisplay.create(cx, cz, chunk, null, null));
            }
        }

        Iterator<Map.Entry<Chunk, ChunkDisplay>> faceDisplaysIt = nonCubicDisplays.entrySet().iterator();
        while (faceDisplaysIt.hasNext()) {
            Map.Entry<Chunk, ChunkDisplay> entry = faceDisplaysIt.next();
            ChunkDisplay chunkDisplay = entry.getValue();

            if (!world.containsChunk(chunkDisplay.chunkX, chunkDisplay.chunkZ)) {
                chunkDisplay.close();
                faceDisplaysIt.remove();
            }
        }
    }

    private void updateFaceDisplays(HashMap<Chunk, ChunkDisplay> faceDisplays, BlockFace blockFace)
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

        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);

            float relativeX = ChunkPos.toGlobalX(cx, Chunk.XLENGTH / 2) - cameraX;
            float relativeZ = ChunkPos.toGlobalZ(cz, Chunk.ZLENGTH / 2) - cameraZ;
            float squareDistance = (relativeX * relativeX) + (relativeZ * relativeZ);

            if (squareDistance >= maxViewRadiusSquared)
                continue;

            ChunkDisplay existingDisplay = faceDisplays.get(chunk);
            Chunk adjacentChunk = world.getChunk(cx + dx, cz + dz);
            long adjacentHashedPos = ChunkPos.hashPos(cx + dx, cz + dz);

            boolean needsUpdating = existingDisplay == null
                    || (existingDisplay.adjacentChunk == null && adjacentChunk != null)
                    || chunksToRedraw.contains(hashedPos)
                    || chunksToRedraw.contains(adjacentHashedPos);

            if (needsUpdating) {
                if (existingDisplay != null)
                    existingDisplay.close();

                faceDisplays.put(chunk, ChunkDisplay.create(cx, cz, chunk, adjacentChunk, blockFace));
            }
        }

        Iterator<Map.Entry<Chunk, ChunkDisplay>> faceDisplaysIt = faceDisplays.entrySet().iterator();
        while (faceDisplaysIt.hasNext()) {
            Map.Entry<Chunk, ChunkDisplay> entry = faceDisplaysIt.next();
            ChunkDisplay chunkDisplay = entry.getValue();

            if (!world.containsChunk(chunkDisplay.chunkX, chunkDisplay.chunkZ)) {
                chunkDisplay.close();
                faceDisplaysIt.remove();
            }
        }
    }

    public void redrawChunk(long hashedPos) {
        chunksToRedraw.add(hashedPos);
    }

    public Camera getCamera() {
        return camera;
    }
}