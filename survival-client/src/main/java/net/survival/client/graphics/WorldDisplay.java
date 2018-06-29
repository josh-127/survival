package net.survival.client.graphics;

import java.util.HashMap;
import java.util.Map;

import net.survival.block.BlockFace;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.world.World;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;

class WorldDisplay implements GraphicsResource
{
    //
    // TODO: *FaceDisplays all have bad names, because they also
    //        contain entities to be drawn. Rename these variables
    //        something else.
    //
    
    private final World world;
    private HashMap<Chunk, ChunkDisplay> topFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> bottomFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> leftFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> rightFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> frontFaceDisplays;
    private HashMap<Chunk, ChunkDisplay> backFaceDisplays;
    private final BlockTextureAtlas[] blockTextures;
    
    private final Camera camera;
    private final float maxViewRadius;
    
    public WorldDisplay(World world, Camera camera, float maxViewRadius) {
        this.world = world;
        
        topFaceDisplays    = new HashMap<>();
        bottomFaceDisplays = new HashMap<>();
        leftFaceDisplays   = new HashMap<>();
        rightFaceDisplays  = new HashMap<>();
        frontFaceDisplays  = new HashMap<>();
        backFaceDisplays   = new HashMap<>();
        
        blockTextures = new BlockTextureAtlas[BlockFace.values().length];
        for (int i = 0; i < blockTextures.length; ++i)
            blockTextures[i] = new BlockTextureAtlas(BlockFace.values()[i]);
        
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
        GLMatrixStack.load(camera.getViewMatrix());
        
        drawFaceDisplays(topFaceDisplays, BlockFace.TOP, true);
        drawFaceDisplays(bottomFaceDisplays, BlockFace.BOTTOM, false);
        drawFaceDisplays(leftFaceDisplays, BlockFace.LEFT, false);
        drawFaceDisplays(rightFaceDisplays, BlockFace.RIGHT, false);
        drawFaceDisplays(frontFaceDisplays, BlockFace.FRONT, false);
        drawFaceDisplays(backFaceDisplays, BlockFace.BACK, false);
        
        GLMatrixStack.pop();

    }

    private void drawFaceDisplays(HashMap<Chunk, ChunkDisplay> faceDisplays, BlockFace blockFace,
            boolean shouldDrawEntities)
    {
        // Entities
        if (shouldDrawEntities) {
            for (Map.Entry<Chunk, ChunkDisplay> entry : faceDisplays.entrySet()) {
                ChunkDisplay display = entry.getValue();
                display.displayEntities();
            }
        }
        
        // Block Faces
        for (Map.Entry<Chunk, ChunkDisplay> entry : faceDisplays.entrySet()) {
            Chunk chunk = entry.getKey();
            ChunkDisplay display = entry.getValue();
            
            if (display.isEmpty())
                continue;
            
            float globalX = ChunkPos.toGlobalX(display.chunkX, 0);
            float globalY = ChunkPos.toGlobalY(display.chunkY, 0);
            float globalZ = ChunkPos.toGlobalZ(display.chunkZ, 0);
            
            GLMatrixStack.push();
            GLMatrixStack.translate(globalX, globalY, globalZ);
            display.displayBlocks(blockTextures[blockFace.ordinal()].blockTextures);
            GLMatrixStack.pop();
        }
    }
    
    private HashMap<Chunk, ChunkDisplay> updateFaceDisplays(
            HashMap<Chunk, ChunkDisplay> faceDisplays,
            BlockFace blockFace)
    {
        int dx = 0;
        int dy = 0;
        int dz = 0;
        
        switch (blockFace) {
        case TOP:    dy = 1;  break;
        case BOTTOM: dy = -1; break;
        case FRONT:  dz = 1;  break;
        case BACK:   dz = -1; break;
        case LEFT:   dx = -1; break;
        case RIGHT:  dx = 1;  break;
        }
        
        HashMap<Chunk, ChunkDisplay> newFaceDisplays = new HashMap<>();
        
        float cameraX = camera.getX();
        float cameraZ = camera.getZ();
        float maxViewRadiusSquared = maxViewRadius * maxViewRadius;
        
        for (Map.Entry<Long, Chunk> entry : world.iterateChunkMap()) {
            long hashedPos = entry.getKey();
            Chunk chunk = entry.getValue();
            
            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cy = ChunkPos.chunkYFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            
            float relativeX = ChunkPos.toGlobalX(cx, Chunk.XLENGTH / 2) - cameraX;
            float relativeZ = ChunkPos.toGlobalZ(cz, Chunk.ZLENGTH / 2) - cameraZ;
            float squareDistance = (relativeX * relativeX) + (relativeZ * relativeZ);
            
            if (squareDistance >= maxViewRadiusSquared)
                continue;
            
            ChunkDisplay existingDisplay = faceDisplays.remove(chunk);
            Chunk adjacentChunk = world.getChunk(cx + dx, cy + dy, cz + dz);
            
            boolean useExisting = (existingDisplay != null) &&
                    (existingDisplay.adjacentChunk != null || adjacentChunk == null);
            
            if (useExisting) {
                newFaceDisplays.put(chunk, existingDisplay);
            }
            else {
                if (existingDisplay != null)
                    existingDisplay.close();
                
                BlockTextureAtlas atlas = blockTextures[blockFace.ordinal()];
                newFaceDisplays.put(chunk, new ChunkDisplay(cx, cy, cz, chunk, adjacentChunk, blockFace, atlas));
            }
        }
        
        for (ChunkDisplay chunkDisplay : faceDisplays.values())
            chunkDisplay.close();
        
        return newFaceDisplays;
    }
    
    public Camera getCamera() {
        return camera;
    }
}