package net.survival.world;

import java.util.ArrayList;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.entity.Entity;
import net.survival.world.chunk.Chunk;

public class EntitySystem
{
    private final ArrayList<EntityBehavior> entityBehaviors;

    private final CharacterPhysics characterPhysics;
    private final EntityRelocator entityRelocator;

    public EntitySystem() {
        entityBehaviors = new ArrayList<>();
        entityBehaviors.add(new CharacterBehavior());
        entityBehaviors.add(new NpcBehavior());

        characterPhysics = new CharacterPhysics();
        entityRelocator = new EntityRelocator();
    }

    public void update(World world, double elapsedTime) {
        for (EntityBehavior entityBehavior : entityBehaviors)
            entityBehavior.preTick(world, elapsedTime);

        for (EntityBehavior entityBehavior : entityBehaviors)
            entityBehavior.tick(world, elapsedTime);

        characterPhysics.update(world, elapsedTime);
        entityRelocator.relocateEntities(world);

        for (EntityBehavior entityBehavior : entityBehaviors)
            entityBehavior.postTick(world, elapsedTime);

        clearEntityControlStates(world);
    }

    private void clearEntityControlStates(World world) {
        ObjectIterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Entity entity : chunk.iterateCharacters())
                entity.clearControlState();
        }
    }
}