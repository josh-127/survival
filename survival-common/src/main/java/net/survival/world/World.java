package net.survival.world;

import java.util.ArrayList;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.world.actor.Actor;
import net.survival.world.chunk.ChunkColumn;
import net.survival.world.chunk.ChunkColumnPos;

public class World implements BlockStorage
{
    private final Long2ObjectOpenHashMap<ChunkColumn> chunkColumns;
    private final ArrayList<Actor> actors;

    public World() {
        chunkColumns = new Long2ObjectOpenHashMap<>(1024);
        actors = new ArrayList<>(1024);
    }

    public ChunkColumn getChunk(int cx, int cz) {
        return chunkColumns.get(ChunkColumnPos.hashPos(cx, cz));
    }

    public ChunkColumn getChunk(long hashedPos) {
        return chunkColumns.get(hashedPos);
    }

    public boolean containsChunk(int cx, int cz) {
        return chunkColumns.containsKey(ChunkColumnPos.hashPos(cx, cz));
    }

    public boolean containsChunk(long hashedPos) {
        return chunkColumns.containsKey(hashedPos);
    }

    public Iterable<Long2ObjectMap.Entry<ChunkColumn>> iterateChunkMap() {
        return chunkColumns.long2ObjectEntrySet();
    }

    public ObjectIterator<Long2ObjectMap.Entry<ChunkColumn>> getChunkMapIterator() {
        return chunkColumns.long2ObjectEntrySet().iterator();
    }

    public ObjectIterator<Long2ObjectMap.Entry<ChunkColumn>> getChunkMapFastIterator() {
        return chunkColumns.long2ObjectEntrySet().fastIterator();
    }

    public Iterable<ChunkColumn> iterateChunks() {
        return chunkColumns.values();
    }

    public void addChunk(int cx, int cz, ChunkColumn chunkColumn) {
        chunkColumns.put(ChunkColumnPos.hashPos(cx, cz), chunkColumn);
    }

    public void addChunk(long hashedPos, ChunkColumn chunkColumn) {
        chunkColumns.put(hashedPos, chunkColumn);
    }

    public void removeChunk(int cx, int cz) {
        chunkColumns.remove(ChunkColumnPos.hashPos(cx, cz));
    }

    public void removeChunk(long hashedPos) {
        chunkColumns.remove(hashedPos);
    }

    @Override
    public short getBlock(int x, int y, int z) {
        int cx = ChunkColumnPos.toChunkX(x);
        int cz = ChunkColumnPos.toChunkZ(z);

        ChunkColumn chunkColumn = chunkColumns.get(ChunkColumnPos.hashPos(cx, cz));
        if (chunkColumn == null)
            throw new RuntimeException("Cannot query a block in an unloaded chunk.");

        int localX = ChunkColumnPos.toLocalX(cx, x);
        int localZ = ChunkColumnPos.toLocalZ(cz, z);

        return chunkColumn.getBlock(localX, y, localZ);
    }

    @Override
    public void setBlock(int x, int y, int z, short to) {
        ChunkColumn chunkColumn = getChunkFromGlobalPos(x, z, "Cannot place/replace a block in an unloaded chunk.");
        int localX = ChunkColumnPos.toLocalX(ChunkColumnPos.toChunkX(x), x);
        int localZ = ChunkColumnPos.toLocalZ(ChunkColumnPos.toChunkZ(z), z);

        chunkColumn.setBlock(localX, y, localZ, to);
    }

    @Override
    public int getTopLevel(int x, int z) {
        ChunkColumn chunkColumn = getChunkFromGlobalPos(x, z, "Cannot query a block in an unloaded chunk.");
        int localX = ChunkColumnPos.toLocalX(ChunkColumnPos.toChunkX(x), x);
        int localZ = ChunkColumnPos.toLocalZ(ChunkColumnPos.toChunkZ(z), z);

        return chunkColumn.getTopLevel(localX, localZ);
    }

    @Override
    public boolean placeBlockIfEmpty(int x, int y, int z, short to) {
        ChunkColumn chunkColumn = getChunkFromGlobalPos(x, z, "Cannot place a block in an unloaded chunk.");
        int localX = ChunkColumnPos.toLocalX(ChunkColumnPos.toChunkX(x), x);
        int localZ = ChunkColumnPos.toLocalZ(ChunkColumnPos.toChunkZ(z), z);

        return chunkColumn.placeBlockIfEmpty(localX, y, localZ, to);
    }

    @Override
    public boolean replaceBlockIfExists(int x, int y, int z, short replacement) {
        ChunkColumn chunkColumn = getChunkFromGlobalPos(x, z, "Cannot replace a block in an unloaded chunk.");
        int localX = ChunkColumnPos.toLocalX(ChunkColumnPos.toChunkX(x), x);
        int localZ = ChunkColumnPos.toLocalZ(ChunkColumnPos.toChunkZ(z), z);

        return chunkColumn.replaceBlockIfExists(localX, y, localZ, replacement);
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    private ChunkColumn getChunkFromGlobalPos(int x, int z, String exceptionMessage) {
        int cx = ChunkColumnPos.toChunkX(x);
        int cz = ChunkColumnPos.toChunkZ(z);

        ChunkColumn chunkColumn = chunkColumns.get(ChunkColumnPos.hashPos(cx, cz));
        if (chunkColumn == null)
            throw new RuntimeException(exceptionMessage);
        
        return chunkColumn;
    }
}