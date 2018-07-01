package net.survival.world.chunk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.survival.world.World;

public class DefaultChunkProvider implements ChunkProvider
{
    private final World world;
    private final ChunkGenerator chunkGenerator;
    private final Queue<Long> chunksToLoad;
    private final ArrayList<ChunkLoader> chunkLoaders;

    public DefaultChunkProvider(World world, ChunkGenerator chunkGenerator) {
        this.world = world;
        this.chunkGenerator = chunkGenerator;
        chunksToLoad = new LinkedList<>();
        chunkLoaders = new ArrayList<>();
    }
    
    public void addChunkLoader(ChunkLoader chunkLoader) {
        chunkLoaders.add(chunkLoader);
    }
    
    public void removeChunkLoader(ChunkLoader chunkLoader) {
        chunkLoaders.remove(chunkLoader);
    }

    public void tick(double elapsedTime) {
        killDeadChunks();
        enqueueChunksToLoad();
        loadEnqueuedChunks();
    }
    
    private void enqueueChunksToLoad() {
        for (ChunkLoader chunkLoader : chunkLoaders) {
            Set<Long> positions = chunkLoader.getChunkPositions();
            for (long hashedPos : positions) {
                if (world.getChunk(hashedPos) == null && !chunksToLoad.contains(hashedPos))
                    chunksToLoad.add(hashedPos);
            }
        }
        
    }

    private void loadEnqueuedChunks() {
        for (int i = 0; !chunksToLoad.isEmpty() && i < 8; ++i) {
            long hashedPos = chunksToLoad.remove();
            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cy = ChunkPos.chunkYFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            world.addChunk(cx, cy, cz, getChunk(hashedPos));
        }
    }

    private void killDeadChunks() {
        Iterator<Map.Entry<Long, Chunk>> iterator = world.iterateChunkMap().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Long, Chunk> entry = iterator.next();
            long hashedPos = entry.getKey();
            
            boolean isLoaded = false;
            
            for (ChunkLoader chunkLoader : chunkLoaders) {
                Set<Long> positions = chunkLoader.getChunkPositions();
                if (positions.contains(hashedPos)) {
                    isLoaded = true;
                    break;
                }
            }
            
            if (!isLoaded)
                iterator.remove();
        }
    }

    @Override
    public Chunk getChunk(int cx, int cy, int cz) {
        return getChunk(ChunkPos.hashPos(cx, cy, cz));
    }
    
    private Chunk getChunk(long hashedPos) {
        Chunk loadedChunk = world.getChunk(hashedPos);
        if (loadedChunk != null)
            return loadedChunk;

        int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
        int cy = ChunkPos.chunkYFromHashedPos(hashedPos);
        int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
        
        return chunkGenerator.generate(cx, cy, cz);
    }
    
    private String getChunkFilePath(long hashedPos) {
        // TODO: Use proper path concatenation methods
        return String.format("../.world/%s.chunk", Long.toHexString(hashedPos));
    }
}