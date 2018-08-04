package net.survival.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.entity.Npc;
import net.survival.entity.Player;
import net.survival.world.chunk.Chunk;

public class EntitySystem
{
    private final NpcAI npcAI;

    private final CharacterPhysics characterPhysics;
    private final EntityRelocator entityRelocator;

    public EntitySystem() {
        npcAI = new NpcAI();
        characterPhysics = new CharacterPhysics();
        entityRelocator = new EntityRelocator();
    }

    public void update(World world, double elapsedTime) {
        npcAI.tick(world, elapsedTime);
        characterPhysics.update(world, elapsedTime);
        entityRelocator.relocateEntities(world);

        ObjectIterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Npc npc : chunk.iterateNpcs())
                npc.clearControlState();

            for (Player player : chunk.iteratePlayers())
                player.clearControlState();
        }
    }
}