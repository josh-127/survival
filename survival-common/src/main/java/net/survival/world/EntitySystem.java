package net.survival.world;

import net.survival.entity.Entity;
import net.survival.entity.controller.EntityController;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.EntityRelocator;

public class EntitySystem
{
    private final EntityPhysics entityPhysics;
    private final EntityRelocator entityRelocator;
    
    public EntitySystem() {
        entityPhysics = new EntityPhysics();
        entityRelocator = new EntityRelocator();
    }

    public void update(World world, double elapsedTime) {
        for (Chunk chunk : world.iterateChunks()) {
            for (Entity entity : chunk.iterateEntities()) {
                EntityController controller = entity.controllerType.controller;
                controller.tick(entity);
            }
        }
        
        entityPhysics.update(world, elapsedTime);
        entityRelocator.relocateEntities(world);
    }
}