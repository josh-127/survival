package net.survival.world.chunk;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.survival.world.World;

public class ChunkSystem
{
    private static final int DATABASE_LOAD_RATE = 2;
    private static final int GENERATOR_LOAD_RATE = 2;
    
    private final ChunkDatabase chunkDatabase;
    private final ChunkGenerator chunkGenerator;

    public ChunkSystem(ChunkDatabase chunkDatabase, ChunkGenerator chunkGenerator) {
        this.chunkDatabase = chunkDatabase;
        this.chunkGenerator = chunkGenerator;
    }

    public void update(World world, ChunkLoader chunkLoader) {
        Set<Long> chunksToLoad = chunkLoader.getChunkPositions();
        
        Iterator<Map.Entry<Long, Chunk>> chunkMapIt = world.iterateChunkMap().iterator();
        while (chunkMapIt.hasNext()) {
            Map.Entry<Long, Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getKey();
            if (chunksToLoad.contains(hashedPos))
                chunksToLoad.remove(hashedPos);
            else
                chunkMapIt.remove();
        }

        Iterator<Long> chunksToLoadIt = chunksToLoad.iterator();
        for (int i = 0; i < DATABASE_LOAD_RATE && chunksToLoadIt.hasNext(); ++i) {
            long hashedPos = chunksToLoadIt.next();
            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            Chunk loadedChunk = chunkDatabase.loadChunk(cx, cz);
            if (loadedChunk != null) {
                world.addChunk(cx, cz, loadedChunk);
                chunksToLoadIt.remove();
            }
        }

        chunksToLoadIt = chunksToLoad.iterator();
        for (int i = 0; i < GENERATOR_LOAD_RATE && chunksToLoadIt.hasNext(); ++i) {
            long hashedPos = chunksToLoadIt.next();
            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            Chunk generatedChunk = chunkGenerator.generate(cx, cz);
            world.addChunk(cx, cz, generatedChunk);
            chunksToLoadIt.remove();
        }
    }
}