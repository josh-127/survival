package net.survival.world;

import java.util.ArrayList;
import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.survival.entity.Entity;
import net.survival.entity.NPC;
import net.survival.entity.Player;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;

public class EntityRelocator
{
    //
    // TODO: Remove code duplication.
    //

    public void relocateEntities(World world) {
        ArrayList<Entity> entitiesToRelocate = new ArrayList<>();

        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            long hashedPos = entry.getLongKey();
            Chunk chunk = entry.getValue();

            int cx = ChunkPos.chunkXFromHashedPos(hashedPos);
            int cz = ChunkPos.chunkZFromHashedPos(hashedPos);

            Iterator<NPC> npcs = chunk.iterateNPCs().iterator();
            while (npcs.hasNext()) {
                Entity entity = npcs.next();

                if (!chunkContainsEntity(cx, cz, entity)) {
                    npcs.remove();
                    entitiesToRelocate.add(entity);
                }
            }

            Iterator<Player> players = chunk.iteratePlayers().iterator();
            while (players.hasNext()) {
                Entity entity = players.next();

                if (!chunkContainsEntity(cx, cz, entity)) {
                    players.remove();
                    entitiesToRelocate.add(entity);
                }
            }
        }

        for (Entity entity : entitiesToRelocate) {
            if (entity instanceof NPC)
                world.addNPC((NPC) entity);
            else if (entity instanceof Player)
                world.addPlayer((Player) entity);
        }
    }

    private boolean chunkContainsEntity(int cx, int cz, Entity entity) {
        return entity.x >= ChunkPos.getGlobalWestBound(cx)
                && entity.x < ChunkPos.getGlobalEastBound(cx)
                && entity.z >= ChunkPos.getGlobalNorthBound(cz)
                && entity.z < ChunkPos.getGlobalSouthBound(cz);
    }
}