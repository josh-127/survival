package net.survival.server;

import net.survival.world.World;
import net.survival.world.chunk.DefaultChunkProvider;
import net.survival.world.chunk.EntityRelocator;
import net.survival.world.gen.infinite.InfiniteChunkGenerator;

public class Server
{
    private final World world;
    
    private final DefaultChunkProvider chunkProvider;
    private final EntityRelocator entityRelocator;
    
    private Server() {
        world = new World();
        chunkProvider = new DefaultChunkProvider(world, new InfiniteChunkGenerator(0L));
        entityRelocator = new EntityRelocator(world);
    }

    public void tick(double elapsedTime) {
        chunkProvider.tick(elapsedTime);
        entityRelocator.relocateEntities();
    }

    public static void main(String[] args) {
        Server program = new Server();

        final double MILLIS_PER_TICK = World.SECONDS_PER_TICK * 1000.0;
        long now = System.currentTimeMillis();
        long prevTime = now;
        double unprocessedTicks = 0.0;
        
        for (;;) {
            now = System.currentTimeMillis();
            unprocessedTicks += (now - prevTime) / MILLIS_PER_TICK;
            prevTime = now;
            
            if (unprocessedTicks >= 1.0) {
                while (unprocessedTicks >= 1.0) {
                    program.tick(World.SECONDS_PER_TICK);
                    unprocessedTicks -= 1.0;
                }
            }
        }
    }
}