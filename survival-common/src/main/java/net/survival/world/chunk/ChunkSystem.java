package net.survival.world.chunk;

import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.world.World;
import net.survival.world.gen.decoration.WorldDecorator;

public class ChunkSystem
{
    private static final int GENERATOR_LOAD_RATE = 2;

    private final World world;
    private final ChunkLoader chunkLoader;

    private final ChunkDbPipe.ClientSide chunkDbPipe;
    private final ChunkProvider chunkGenerator;
    private final WorldDecorator worldDecorator;

    public ChunkSystem(
            World world,
            ChunkLoader chunkLoader,
            ChunkDbPipe.ClientSide chunkDbPipe,
            ChunkProvider chunkGenerator,
            WorldDecorator worldDecorator)
    {
        this.world = world;
        this.chunkLoader = chunkLoader;

        this.chunkDbPipe = chunkDbPipe;
        this.chunkGenerator = chunkGenerator;
        this.worldDecorator = worldDecorator;
    }

    public void update() {
        LongSet requestedChunks = chunkLoader.getChunkPositions();

        ObjectIterator<Long2ObjectMap.Entry<Chunk>> iterator = world.getChunkMapFastIterator();
        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = iterator.next();
            long hashedPos = entry.getLongKey();

            if (requestedChunks.contains(hashedPos))
                requestedChunks.remove(hashedPos);
            else
                iterator.remove();
        }

        generateChunks(requestedChunks.iterator());
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