package net.survival.world.chunk;

import java.util.ArrayList;
import java.util.Iterator;

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
        
        for (Chunk chunk : world.iterateChunks()) {
            Iterator<Entity> entities = chunk.iterateEntities().iterator();
            
            while (entities.hasNext()) {
                Entity entity = entities.next();
                
                if (!chunkContainsEntity(chunk, entity)) {
                    entities.remove();
                    entitiesToRelocate.add(entity);
                }
            }
        }
        
        for (Entity entity : entitiesToRelocate)
            world.addEntity(entity);
    }
    
    private boolean chunkContainsEntity(Chunk chunk, Entity entity) {
        return  entity.getX() >= chunk.getGlobalWestBound() &&
                entity.getX() <  chunk.getGlobalEastBound() &&
                entity.getZ() >= chunk.getGlobalNorthBound() &&
                entity.getZ() <  chunk.getGlobalSouthBound() &&
                entity.getY() >= chunk.getGlobalBottomBound() &&
                entity.getY() <  chunk.getGlobalNorthBound();
    }
}