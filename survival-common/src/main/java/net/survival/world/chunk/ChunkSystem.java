package net.survival.world.chunk;

import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.world.World;
import net.survival.world.gen.decoration.WorldDecorator;

public class ChunkSystem
{
    private static final int GENERATOR_LOAD_RATE = 2;
    private static final int DATABASE_LOAD_RATE = 4;
    private static final double SAVE_RATE = 10.0;

    private final World world;
    private final ChunkStageMask chunkStageMask;

    private final ChunkDbPipe.ClientSide chunkDbPipe;
    private final ChunkProvider chunkGenerator;
    private final WorldDecorator worldDecorator;

    private final LongArraySet loadingChunks;
    private double saveTimer;

    public ChunkSystem(
            World world,
            ChunkStageMask chunkStageMask,
            ChunkDbPipe.ClientSide chunkDbPipe,
            ChunkProvider chunkGenerator,
            WorldDecorator worldDecorator)
    {
        this.world = world;
        this.chunkStageMask = chunkStageMask;

        this.chunkDbPipe = chunkDbPipe;
        this.chunkGenerator = chunkGenerator;
        this.worldDecorator = worldDecorator;

        loadingChunks = new LongArraySet();
        saveTimer = SAVE_RATE;
    }

    public void saveAllChunks() {
        saveChunks();
    }

    public void update(double elapsedTime) {
        saveTimer -= elapsedTime;
        if (saveTimer <= 0.0) {
            saveChunks();
            maskOutChunks();
            saveTimer = SAVE_RATE;
        }

        LongSet missingChunks = getMissingChunkPosSet();
        loadMissingChunksFromDb(missingChunks);
        generateChunks(missingChunks.iterator());
    }

    private LongSet getMissingChunkPosSet() {
        LongSet missingChunks = chunkStageMask.getChunkPositions();

        ObjectIterator<Long2ObjectMap.Entry<ChunkColumn>> iterator = world.getChunkMapFastIterator();
        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<ChunkColumn> entry = iterator.next();
            long hashedPos = entry.getLongKey();

            if (missingChunks.contains(hashedPos))
                missingChunks.remove(hashedPos);
        }

        return missingChunks;
    }

    private void saveChunks() {
        // Save all loaded chunks.
        ObjectIterator<Long2ObjectMap.Entry<ChunkColumn>> iterator = world.getChunkMapFastIterator();
        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<ChunkColumn> entry = iterator.next();
            long hashedPos = entry.getLongKey();
            ChunkColumn chunkColumn = entry.getValue();

            chunkDbPipe.request(ChunkRequest.createPostRequest(hashedPos, chunkColumn));
        }
    }

    private void maskOutChunks() {
        ObjectIterator<Long2ObjectMap.Entry<ChunkColumn>> iterator = world.getChunkMapFastIterator();
        LongSet mask = chunkStageMask.getChunkPositions();

        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<ChunkColumn> entry = iterator.next();
            long hashedPos = entry.getLongKey();

            if (!mask.contains(hashedPos))
                iterator.remove();
        }
    }

    private void loadMissingChunksFromDb(LongSet missingChunks) {
        LongIterator iterator = missingChunks.iterator();

        for (int i = 0; iterator.hasNext() && i < DATABASE_LOAD_RATE; ++i) {
            long hashedPos = iterator.nextLong();

            if (!loadingChunks.contains(hashedPos)) {
                loadingChunks.add(hashedPos);
                chunkDbPipe.request(ChunkRequest.createGetRequest(hashedPos));
            }
        }

        missingChunks.clear();

        for (
                ChunkResponse response = chunkDbPipe.pollResponse();
                response != null;
                response = chunkDbPipe.pollResponse())
        {
            long hashedPos = response.chunkPos;
            ChunkColumn chunkColumn = response.chunkColumn;

            loadingChunks.remove(hashedPos);

            if (chunkColumn != null)
                world.addChunk(hashedPos, chunkColumn);
            else
                missingChunks.add(hashedPos);
        }
    }

    private void generateChunks(LongIterator missingChunksIt) {
        for (int i = 0; i < GENERATOR_LOAD_RATE && missingChunksIt.hasNext(); ++i) {
            long hashedPos = missingChunksIt.nextLong();
            ChunkColumn generatedChunk = chunkGenerator.provideChunk(hashedPos);
            world.addChunk(hashedPos, generatedChunk);
            missingChunksIt.remove();
        }

        Iterator<Long2ObjectMap.Entry<ChunkColumn>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<ChunkColumn> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            int cx = ChunkColumnPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkColumnPos.chunkZFromHashedPos(hashedPos);
            ChunkColumn chunkColumn = entry.getValue();

            boolean isFullySurrounded = true;
            for (int z = -1; z <= 1 && isFullySurrounded; ++z) {
                for (int x = -1; x <= 1 && isFullySurrounded; ++x) {
                    if (world.getChunk(cx + x, cz + z) == null)
                        isFullySurrounded = false;
                }
            }

            if (!chunkColumn.isDecorated() && isFullySurrounded) {
                worldDecorator.decorate(cx, cz, chunkColumn, world);
                chunkColumn.markDecorated();
            }
        }
    }
}