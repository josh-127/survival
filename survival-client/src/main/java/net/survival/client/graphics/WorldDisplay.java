package net.survival.client.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.joml.Matrix4f;

import net.survival.block.BlockFace;
import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;
import net.survival.world.World;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;

class WorldDisplay implements GraphicsResource
{
    //
    // TODO: *FaceDisplays all have bad names, because they also
    // contain entities to be drawn. Rename these variables
    // something else.
    //

    private final World world;
    private HashMap<Chunk, ChunkDisplay> topFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> bottomFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> leftFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> rightFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> frontFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> backFaceDisplays;
    private final BlockTextureAtlas[] blockTextures;
    private final GLTexture overlayTexture;

    private final TreeSet<Long> chunksToRedraw;

    private final Camera camera;
    private final float maxViewRadius;

    public WorldDisplay(World world, Camera camera, float maxViewRadius) {
        this.world = world;

        topFaceDisplays = new HashMap<>();
        bottomFaceDisplays = new HashMap<>();
        leftFaceDisplays = new HashMap<>();
        rightFaceDisplays = new HashMap<>();
        frontFaceDisplays = new HashMap<>();
        backFaceDisplays = new HashMap<>();

        blockTextures = new BlockTextureAtlas[BlockFace.values().length];
        for (int i = 0; i < blockTextures.length; ++i)
            blockTextures[i] = new BlockTextureAtlas(BlockFace.values()[i]);

        Bitmap overlayBitmap = Bitmap.fromFile("../assets/textures/overlays/low_contrast.png");
        overlayTexture = new GLTexture();
        overlayTexture.beginBind().setMinFilter(GLFilterMode.LINEAR_MIPMAP_LINEAR)
                .setMagFilter(GLFilterMode.LINEAR).setWrapS(GLWrapMode.REPEAT)
                .setWrapT(GLWrapMode.REPEAT).setMipmapEnabled(true).setData(overlayBitmap)
                .endBind();

        chunksToRedraw = new TreeSet<>();

        this.camera = camera;
        this.maxViewRadius = maxViewRadius;
    }

    @Override
    public void close() {
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

        for (int i = 0; i < blockTextures.length; ++i)
            blockTextures[i].close();
    }

    public void display() {
        topFaceDisplays = updateFaceDisplays(topFaceDisplays, BlockFace.TOP);
        bottomFaceDisplays = updateFaceDisplays(bottomFaceDisplays, BlockFace.BOTTOM);
        leftFaceDisplays = updateFaceDisplays(leftFaceDisplays, BlockFace.LEFT);
        rightFaceDisplays = updateFaceDisplays(rightFaceDisplays, BlockFace.RIGHT);
        frontFaceDisplays = updateFaceDisplays(frontFaceDisplays, BlockFace.FRONT);
        backFaceDisplays = updateFaceDisplays(backFaceDisplays, BlockFace.BACK);

        GLMatrixStack.setProjectionMatrix(camera.getProjectionMatrix());
        GLMatrixStack.push();
        GLMatrixStack.loadIdentity();

        Matrix4f viewMatrix = camera.getViewMatrix();

        drawFaceDisplays(topFaceDisplays, BlockFace.TOP, true, false, viewMatrix);
        drawFaceDisplays(bottomFaceDisplays, BlockFace.BOTTOM, false, false, viewMatrix);
        drawFaceDisplays(leftFaceDisplays, BlockFace.LEFT, false, false, viewMatrix);
        drawFaceDisplays(rightFaceDisplays, BlockFace.RIGHT, false, false, viewMatrix);
        drawFaceDisplays(frontFaceDisplays, BlockFace.FRONT, false, false, viewMatrix);
        drawFaceDisplays(backFaceDisplays, BlockFace.BACK, false, false, viewMatrix);

        /*//
        try (@SuppressWarnings("resource")
        GLOutputMergerState state = new GLOutputMergerState()
                .withDepthFunction(GLDepthFunction.EQUAL).withDepthWriteMask(false))
        {
            // TODO: Make OpenGL wrapper class.
            org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_BLEND);
            org.lwjgl.opengl.GL11.glBlendFunc(org.lwjgl.opengl.GL11.GL_ZERO,
                    org.lwjgl.opengl.GL11.GL_SRC_COLOR);
        
            drawFaceDisplays(topFaceDisplays, BlockFace.TOP, false, true, viewMatrix);
            drawFaceDisplays(bottomFaceDisplays, BlockFace.BOTTOM, false, true, viewMatrix);
            drawFaceDisplays(leftFaceDisplays, BlockFace.LEFT, false, true, viewMatrix);
            drawFaceDisplays(rightFaceDisplays, BlockFace.RIGHT, false, true, viewMatrix);
            drawFaceDisplays(frontFaceDisplays, BlockFace.FRONT, false, true, viewMatrix);
            drawFaceDisplays(backFaceDisplays, BlockFace.BACK, false, true, viewMatrix);
        
            org.lwjgl.opengl.GL11.glBlendFunc(org.lwjgl.opengl.GL11.GL_ONE,
                    org.lwjgl.opengl.GL11.GL_ZERO);
            org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_BLEND);
        }
        //*/

        GLMatrixStack.pop();

        chunksToRedraw.clear();
    }

    private void drawFaceDisplays(HashMap<Chunk, ChunkDisplay> faceDisplays, BlockFace blockFace,
            boolean shouldDrawEntities, boolean drawOverlay, Matrix4f viewMatrix)
    {
        // Entities
        if (shouldDrawEntities) {
            GLMatrixStack.push();
            GLMatrixStack.load(viewMatrix);

            for (Map.Entry<Chunk, ChunkDisplay> entry : faceDisplays.entrySet()) {
                ChunkDisplay display = entry.getValue();
                display.displayEntities();
            }

            GLMatrixStack.pop();
        }

        // Block Faces
        Matrix4f modelView = new Matrix4f();

        if (!drawOverlay) {
            for (Map.Entry<Chunk, ChunkDisplay> entry : faceDisplays.entrySet()) {
                ChunkDisplay display = entry.getValue();

                if (display.isEmpty())
                    continue;

                float globalX = ChunkPos.toGlobalX(display.chunkX, 0);
                float globalZ = ChunkPos.toGlobalZ(display.chunkZ, 0);

                modelView.set(viewMatrix).translate(globalX, 0.0f, globalZ);
                GLMatrixStack.load(modelView);
                display.displayBlocks(blockTextures[blockFace.ordinal()].blockTextures);
            }
        }
        /*//
        else {
            for (Map.Entry<Chunk, ChunkDisplay> entry : faceDisplays.entrySet()) {
                ChunkDisplay display = entry.getValue();
                
                if (display.isEmpty())
                    continue;
                
                float globalX = ChunkPos.toGlobalX(display.chunkX, 0);
                float globalZ = ChunkPos.toGlobalZ(display.chunkZ, 0);
                
                modelView.set(viewMatrix).translate(globalX, 0.0f, globalZ);
                GLMatrixStack.load(modelView);
                display.overlayDisplay.displayBlocks(overlayTexture);
            }
        }
        //*/
    }

    private HashMap<Chunk, ChunkDisplay> updateFaceDisplays(
            HashMap<Chunk, ChunkDisplay> faceDisplays, BlockFace blockFace)
    {
        int dx = 0;
        int dz = 0;

        switch (blockFace) {
        case FRONT:
            dz = 1;
            break;
        case BACK:
            dz = -1;
            break;
        case LEFT:
            dx = -1;
            break;
        case RIGHT:
            dx = 1;
            break;
        default:
            break;
        }

        HashMap<Chunk, ChunkDisplay> newFaceDisplays = new HashMap<>();

        float cameraX = camera.getX();
        float cameraZ = camera.getZ();
        float maxViewRadiusSquared = maxViewRadius * maxViewRadius;

        for (Map.Entry<Long, Chunk> entry : world.iterateChunkMap()) {
            long hashedPos = entry.getKey();
            Chunk chunk = entry.getValue();

            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);

            float relativeX = ChunkPos.toGlobalX(cx, Chunk.XLENGTH / 2) - cameraX;
            float relativeZ = ChunkPos.toGlobalZ(cz, Chunk.ZLENGTH / 2) - cameraZ;
            float squareDistance = (relativeX * relativeX) + (relativeZ * relativeZ);

            if (squareDistance >= maxViewRadiusSquared)
                continue;

            ChunkDisplay existingDisplay = faceDisplays.remove(chunk);
            Chunk adjacentChunk = world.getChunk(cx + dx, cz + dz);

            boolean needsUpdating = existingDisplay == null
                    || (existingDisplay.adjacentChunk == null && adjacentChunk != null)
                    || chunksToRedraw.contains(hashedPos);

            if (needsUpdating) {
                if (existingDisplay != null)
                    existingDisplay.close();

                BlockTextureAtlas atlas = blockTextures[blockFace.ordinal()];
                newFaceDisplays.put(chunk,
                        new ChunkDisplay(cx, cz, chunk, adjacentChunk, blockFace, atlas));
            }
            else {
                newFaceDisplays.put(chunk, existingDisplay);
            }
        }

        for (ChunkDisplay chunkDisplay : faceDisplays.values())
            chunkDisplay.close();

        return newFaceDisplays;
    }

    public void redrawChunk(long hashedPos) {
        chunksToRedraw.add(hashedPos);
    }

    public Camera getCamera() {
        return camera;
    }
}