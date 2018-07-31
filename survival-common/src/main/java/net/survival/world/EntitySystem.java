package net.survival.world;

import net.survival.world.chunk.EntityRelocator;

public class EntitySystem
{
    private final CharacterPhysics characterPhysics;
    private final EntityRelocator entityRelocator;
    
    public EntitySystem() {
        characterPhysics = new CharacterPhysics();
        entityRelocator = new EntityRelocator();
    }

    public void update(World world, double elapsedTime) {
        characterPhysics.update(world, elapsedTime);
        entityRelocator.relocateEntities(world);
    }
}