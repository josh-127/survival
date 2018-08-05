package net.survival.world;

import java.util.ArrayList;
import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.survival.entity.Entity;
import net.survival.entity.Character;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;

public class EntityRelocator
{
    public void relocateEntities(World world) {
        ArrayList<Entity> entitiesToRelocate = new ArrayList<>();

        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);

            Iterator<Character> characterIt = chunk.iterateCharacters().iterator();
            while (characterIt.hasNext()) {
                Entity entity = characterIt.next();

                if (!chunkContainsEntity(cx, cz, entity)) {
                    characterIt.remove();
                    entitiesToRelocate.add(entity);
                }
            }
        }

        for (Entity entity : entitiesToRelocate) {
            if (entity instanceof Character)
                world.addCharacter((Character) entity);
        }
    }

    private boolean chunkContainsEntity(int cx, int cz, Entity entity) {
        return entity.x >= ChunkPos.getGlobalWestBound(cx)
                && entity.x < ChunkPos.getGlobalEastBound(cx)
                && entity.z >= ChunkPos.getGlobalNorthBound(cz)
                && entity.z < ChunkPos.getGlobalSouthBound(cz);
    }
}