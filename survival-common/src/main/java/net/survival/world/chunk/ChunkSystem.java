package net.survival.world.chunk;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.survival.concurrent.CoroutineTask;
import net.survival.world.World;
import net.survival.world.gen.decoration.WorldDecorator;

public class ChunkSystem
{
    private static final int DATABASE_LOAD_RATE = 2;
    private static final int GENERATOR_LOAD_RATE = 2;
    private static final int MAX_UNLOADED_CHUNK_CACHE = 64;

    private final World world;
    private final ChunkLoader chunkLoader;

    private final AsyncChunkProvider chunkDatabase;
    private final ChunkProvider chunkGenerator;
    private final WorldDecorator worldDecorator;

    private final Long2ObjectOpenHashMap<Chunk> chunkCache;
    private final Long2ObjectOpenHashMap<CoroutineTask<Chunk>> loadingChunks;

    public ChunkSystem(World world, ChunkLoader chunkLoader, AsyncChunkProvider chunkDatabase,
            ChunkProvider chunkGenerator, WorldDecorator worldDecorator)
    {
        this.world = world;
        this.chunkLoader = chunkLoader;

        this.chunkDatabase = chunkDatabase;
        this.chunkGenerator = chunkGenerator;
        this.worldDecorator = worldDecorator;

        chunkCache = new Long2ObjectOpenHashMap<>(MAX_UNLOADED_CHUNK_CACHE * 4);
        loadingChunks = new Long2ObjectOpenHashMap<>(1024);
    }

    public void update() {
        LongSet chunksToLoad = chunkLoader.getChunkPositions();

        cacheUnlistedChunks(chunksToLoad, chunkCache);
        loadCachedChunks(chunksToLoad.iterator());

        if (chunkCache.size() > MAX_UNLOADED_CHUNK_CACHE)
            unloadCachedChunks();

        generateChunks(chunksToLoad.iterator());
    }

    private void cacheUnlistedChunks(LongSet chunkPosSet, Long2ObjectMap<Chunk> unloadedChunks) {
        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            if (chunkPosSet.contains(hashedPos)) {
                chunkPosSet.remove(hashedPos);
            }
            else {
                unloadedChunks.put(hashedPos, chunk);
                chunkMapIt.remove();
            }
        }
    }

    private void loadCachedChunks(LongIterator chunksToLoadIt) {
        while (chunksToLoadIt.hasNext()) {
            long hashedPos = chunksToLoadIt.nextLong();
            Chunk chunk = chunkCache.remove(hashedPos);

            if (chunk != null) {
                int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
                int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
                world.addChunk(cx, cz, chunk);
                chunksToLoadIt.remove();
            }
        }
    }

    private void unloadCachedChunks() {
        Iterator<Long2ObjectMap.Entry<Chunk>> iterator = chunkCache.long2ObjectEntrySet().fastIterator();

        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = iterator.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            // TODO: Remove hard-coding on file path.
            String filePath = String.format(System.getProperty("user.dir") + "/../.world/%016X", hashedPos);
            ChunkSavingTask.moveChunkAndCreate(chunk, new File(filePath)).start();
        }

        chunkCache.clear();
    }

    private void generateChunks(LongIterator chunksToLoadIt) {
        for (int i = 0; i < GENERATOR_LOAD_RATE && chunksToLoadIt.hasNext(); ++i) {
            long hashedPos = chunksToLoadIt.nextLong();
            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            Chunk generatedChunk = chunkGenerator.provideChunk(cx, cz);
            world.addChunk(cx, cz, generatedChunk);
            chunksToLoadIt.remove();
        }

        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            Chunk chunk = entry.getValue();

            boolean isFullySurrounded = true;
            for (int z = -1; z <= 1 && isFullySurrounded; ++z) {
                for (int x = -1; x <= 1 && isFullySurrounded; ++x) {
                    if (world.getChunk(cx + x, cz + z) == null)
                        isFullySurrounded = false;
                }
            }

            if (!chunk.isDecorated() && isFullySurrounded) {
                worldDecorator.decorate(cx, cz, chunk, world);
                chunk.markDecorated();
            }
        }
    }
}