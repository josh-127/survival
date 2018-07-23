package net.survival.world.chunk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import net.survival.entity.Entity;
import net.survival.world.World;

public class EntityRelocator
{
    private final World world;

    public EntityRelocator(World world) {
        this.world = world;
    }

    public void relocateEntities() {
        ArrayList<Entity> entitiesToRelocate = new ArrayList<>();

        for (Map.Entry<Long, Chunk> entry : world.iterateChunkMap()) {
            long hashedPos = entry.getKey();
            Chunk chunk = entry.getValue();

            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);
            Iterator<Entity> entities = chunk.iterateEntities().iterator();

            while (entities.hasNext()) {
                Entity entity = entities.next();

                if (!chunkContainsEntity(cx, cz, entity)) {
                    entities.remove();
                    entitiesToRelocate.add(entity);
                }
            }
        }

        for (Entity entity : entitiesToRelocate)
            world.addEntity(entity);
    }

    private boolean chunkContainsEntity(int cx, int cz, Entity entity) {
        return entity.x >= ChunkPos.getGlobalWestBound(cx)
                && entity.x < ChunkPos.getGlobalEastBound(cx)
                && entity.z >= ChunkPos.getGlobalNorthBound(cz)
                && entity.z < ChunkPos.getGlobalSouthBound(cz);
    }
}