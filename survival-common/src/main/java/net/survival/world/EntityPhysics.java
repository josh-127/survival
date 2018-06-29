package net.survival.world;

import net.survival.entity.Entity;
import net.survival.world.chunk.Chunk;

public class EntityPhysics
{
    private static final double GRAVITY = 9.81;
    private static final double TERMINAL_VELOCITY = 30.0;
    
    private final World world;
    
    public EntityPhysics(World world) {
        this.world = world;
    }
    
    public void tick(double elapsedTime) {
        applyGravity(elapsedTime);
        applyVelocities(elapsedTime);
        handleBlockCollisions();
    }
    
    private void applyGravity(double elapsedTime) {
        for (Chunk chunk : world.iterateChunks()) {
            for (Entity entity : chunk.iterateEntities()) {
                double newVX = entity.getVelocityX();
                double newVY = entity.getVelocityY() - GRAVITY * elapsedTime;
                double newVZ = entity.getVelocityZ();
                
                if (newVY < -TERMINAL_VELOCITY)
                    newVY = -TERMINAL_VELOCITY;
                
                entity.setVelocity(newVX, newVY, newVZ);
            }
        }
    }
    
    private void applyVelocities(double elapsedTime) {
        for (Chunk chunk : world.iterateChunks()) {
            for (Entity entity : chunk.iterateEntities()) {
                double newX = entity.getX() + entity.getVelocityX() * elapsedTime;
                double newY = entity.getY() + entity.getVelocityY() * elapsedTime;
                double newZ = entity.getZ() + entity.getVelocityZ() * elapsedTime;
                entity.moveTo(newX, newY, newZ);
            }
        }
    }
    
    private void handleBlockCollisions() {
        for (Chunk chunk : world.iterateChunks()) {
            for (Entity entity : chunk.iterateEntities()) {
                double x = entity.getX();
                double y = entity.getY();
                double z = entity.getZ();
                if (y < 1.0)
                    y = 1.0;
                
                double newX = x;
                double newY = y;
                double newZ = z;
                entity.moveTo(newX, newY, newZ);
            }
        }
    }
}