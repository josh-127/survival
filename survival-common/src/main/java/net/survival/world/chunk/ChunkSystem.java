package net.survival.world.chunk;

import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.survival.concurrent.DeferredResult;
import net.survival.world.World;
import net.survival.world.gen.decoration.WorldDecorator;

public class ChunkSystem
{
    private static final int GENERATOR_LOAD_RATE = 2;
    private static final int MAX_UNLOADED_CHUNK_CACHE = 64;

    private final World world;
    private final ChunkLoader chunkLoader;

    private final PersistentChunkStorage chunkStorage;
    private final ChunkProvider chunkGenerator;
    private final WorldDecorator worldDecorator;

    private final Long2ObjectOpenHashMap<Chunk> chunkCache;
    private final Long2ObjectOpenHashMap<DeferredResult<Chunk>> loadingChunks;

    public ChunkSystem(World world, ChunkLoader chunkLoader, PersistentChunkStorage chunkStorage,
            ChunkProvider chunkGenerator, WorldDecorator worldDecorator)
    {
        this.world = world;
        this.chunkLoader = chunkLoader;

        this.chunkStorage = chunkStorage;
        this.chunkGenerator = chunkGenerator;
        this.worldDecorator = worldDecorator;

        chunkCache = new Long2ObjectOpenHashMap<>(MAX_UNLOADED_CHUNK_CACHE);
        loadingChunks = new Long2ObjectOpenHashMap<>();
    }

    public void update() {
        LongSet requestedChunks = chunkLoader.getChunkPositions();

        cacheUnlistedChunks(requestedChunks, chunkCache);
        loadCachedChunks(requestedChunks.iterator());

        if (chunkCache.size() > MAX_UNLOADED_CHUNK_CACHE)
            saveCachedChunks();

        loadSavedChunks(requestedChunks);
        generateChunks(requestedChunks.iterator());
    }

    public void saveAllChunks() {
        saveCachedChunks();

        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            chunkCache.put(hashedPos, chunk);
        }

        saveCachedChunks();
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

    private void loadCachedChunks(LongIterator requestedChunksIt) {
        while (requestedChunksIt.hasNext()) {
            long hashedPos = requestedChunksIt.nextLong();
            Chunk chunk = chunkCache.remove(hashedPos);

            if (chunk != null) {
                int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
                int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
                world.addChunk(cx, cz, chunk);
                requestedChunksIt.remove();
            }
        }
    }

    private void saveChunks(Iterator<Long2ObjectMap.Entry<Chunk>> chunksToSaveIt) {
        while (chunksToSaveIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunksToSaveIt.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            chunkStorage.saveChunkAsync(cx, cz, chunk);
        }
    }

    public void saveCachedChunks() {
        saveChunks(chunkCache.long2ObjectEntrySet().fastIterator());
        chunkCache.clear();
    }

    private void loadSavedChunks(LongSet chunksToLoad) {
        LongIterator chunksToLoadIt = chunksToLoad.iterator();

        while (chunksToLoadIt.hasNext()) {
            long hashedPos = chunksToLoadIt.nextLong();
            if (loadingChunks.containsKey(hashedPos)) {
                chunksToLoadIt.remove();
                continue;
            }

            DeferredResult<Chunk> deferredResult = chunkStorage.provideChunkAsync(hashedPos);

            if (deferredResult != null) {
                loadingChunks.put(hashedPos, deferredResult);
                chunksToLoadIt.remove();
            }
        }

        Iterator<Long2ObjectMap.Entry<DeferredResult<Chunk>>> loadingChunksIt =
                loadingChunks.long2ObjectEntrySet().fastIterator();
        while (loadingChunksIt.hasNext()) {
            Long2ObjectMap.Entry<DeferredResult<Chunk>> entry = loadingChunksIt.next();
            long hashedPos = entry.getLongKey();
            DeferredResult<Chunk> coroutine = entry.getValue();
            Chunk chunk = coroutine.pollResult();

            if (chunk != null) {
                world.addChunk(hashedPos, chunk);
                loadingChunksIt.remove();
            }
        }
    }

    private void generateChunks(LongIterator requestedChunksIt) {
        for (int i = 0; i < GENERATOR_LOAD_RATE && requestedChunksIt.hasNext(); ++i) {
            long hashedPos = requestedChunksIt.nextLong();
            Chunk generatedChunk = chunkGenerator.provideChunk(hashedPos);
            world.addChunk(hashedPos, generatedChunk);
            requestedChunksIt.remove();
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