package net.survival.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.entity.Character;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;

public class World implements BlockStorage
{
    private final Long2ObjectOpenHashMap<Chunk> chunks;

    public World() {
        chunks = new Long2ObjectOpenHashMap<>(1024);
    }

    public Chunk getChunk(int cx, int cz) {
        return chunks.get(ChunkPos.hashPos(cx, cz));
    }

    public Chunk getChunk(long hashedPos) {
        return chunks.get(hashedPos);
    }

    public boolean containsChunk(int cx, int cz) {
        return chunks.containsKey(ChunkPos.hashPos(cx, cz));
    }

    public boolean containsChunk(long hashedPos) {
        return chunks.containsKey(hashedPos);
    }

    public Iterable<Long2ObjectMap.Entry<Chunk>> iterateChunkMap() {
        return chunks.long2ObjectEntrySet();
    }

    public ObjectIterator<Long2ObjectMap.Entry<Chunk>> getChunkMapIterator() {
        return chunks.long2ObjectEntrySet().iterator();
    }

    public ObjectIterator<Long2ObjectMap.Entry<Chunk>> getChunkMapFastIterator() {
        return chunks.long2ObjectEntrySet().fastIterator();
    }

    public Iterable<Chunk> iterateChunks() {
        return chunks.values();
    }

    public void addChunk(int cx, int cz, Chunk chunk) {
        chunks.put(ChunkPos.hashPos(cx, cz), chunk);
    }

    public void removeChunk(int cx, int cz) {
        chunks.remove(ChunkPos.hashPos(cx, cz));
    }

    public void removeChunk(long hashedPos) {
        chunks.remove(hashedPos);
    }

    @Override
    public short getBlock(int x, int y, int z) {
        int cx = ChunkPos.toChunkX(x);
        int cz = ChunkPos.toChunkZ(z);

        Chunk chunk = chunks.get(ChunkPos.hashPos(cx, cz));
        if (chunk == null)
            throw new RuntimeException("Cannot query a block in an unloaded chunk.");

        int localX = ChunkPos.toLocalX(cx, x);
        int localZ = ChunkPos.toLocalZ(cz, z);

        return chunk.getBlock(localX, y, localZ);
    }

    @Override
    public void setBlock(int x, int y, int z, short to) {
        Chunk chunk = getChunkFromGlobalPos(x, z, "Cannot place/replace a block in an unloaded chunk.");
        int localX = ChunkPos.toLocalX(ChunkPos.toChunkX(x), x);
        int localZ = ChunkPos.toLocalZ(ChunkPos.toChunkZ(z), z);

        chunk.setBlock(localX, y, localZ, to);
    }

    @Override
    public int getTopLevel(int x, int z) {
        Chunk chunk = getChunkFromGlobalPos(x, z, "Cannot query a block in an unloaded chunk.");
        int localX = ChunkPos.toLocalX(ChunkPos.toChunkX(x), x);
        int localZ = ChunkPos.toLocalZ(ChunkPos.toChunkZ(z), z);

        return chunk.getTopLevel(localX, localZ);
    }

    @Override
    public boolean placeBlockIfEmpty(int x, int y, int z, short to) {
        Chunk chunk = getChunkFromGlobalPos(x, z, "Cannot place a block in an unloaded chunk.");
        int localX = ChunkPos.toLocalX(ChunkPos.toChunkX(x), x);
        int localZ = ChunkPos.toLocalZ(ChunkPos.toChunkZ(z), z);

        return chunk.placeBlockIfEmpty(localX, y, localZ, to);
    }

    @Override
    public boolean replaceBlockIfExists(int x, int y, int z, short replacement) {
        Chunk chunk = getChunkFromGlobalPos(x, z, "Cannot replace a block in an unloaded chunk.");
        int localX = ChunkPos.toLocalX(ChunkPos.toChunkX(x), x);
        int localZ = ChunkPos.toLocalZ(ChunkPos.toChunkZ(z), z);

        return chunk.replaceBlockIfExists(localX, y, localZ, replacement);
    }

    public void addCharacter(Character character) {
        Chunk chunk = getChunkFromGlobalPos((int) character.x, (int) character.z,
                "Cannot place a character in an unloaded chunk.");
        chunk.addCharacter(character);
    }

    private Chunk getChunkFromGlobalPos(int x, int z, String exceptionMessage) {
        int cx = ChunkPos.toChunkX(x);
        int cz = ChunkPos.toChunkZ(z);

        Chunk chunk = chunks.get(ChunkPos.hashPos(cx, cz));
        if (chunk == null)
            throw new RuntimeException(exceptionMessage);
        
        return chunk;
    }
}