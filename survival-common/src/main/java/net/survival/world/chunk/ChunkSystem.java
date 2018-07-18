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

        for (Map.Entry<Long, Chunk> entry : world.iterateChunkMap()) {
            long hashedPos = entry.getKey();
            chunksToLoad.remove(hashedPos);
        }

        Iterator<Long> iterator = chunksToLoad.iterator();
        for (int i = 0; i < DATABASE_LOAD_RATE && iterator.hasNext(); ++i) {
            long hashedPos = iterator.next();
            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            Chunk loadedChunk = chunkDatabase.loadChunk(cx, cz);
            if (loadedChunk != null) {
                world.addChunk(cx, cz, loadedChunk);
                iterator.remove();
            }
        }

        iterator = chunksToLoad.iterator();
        for (int i = 0; i < GENERATOR_LOAD_RATE && iterator.hasNext(); ++i) {
            long hashedPos = iterator.next();
            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            Chunk generatedChunk = chunkGenerator.generate(cx, cz);
            world.addChunk(cx, cz, generatedChunk);
            iterator.remove();
        }
    }
}