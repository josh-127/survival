package net.survival.world.gen.decoration;

import java.util.Random;

import net.survival.block.BlockType;
import net.survival.world.World;
import net.survival.world.chunk.Chunk;
import net.survival.world.chunk.ChunkPos;

public class SmallTreeDecorator implements WorldDecorator
{
    private final Random random;
    
    public SmallTreeDecorator() {
        random = new Random();
    }
    
    @Override
    public void decorate(int cx, int cz, Chunk chunk, World world) {
        random.setSeed(ChunkPos.hashPos(cx, cz));
        
        for (int i = 0; i < 12; ++i)
            generateTree(cx, cz, chunk, world);
    }
    
    private void generateTree(int cx, int cz, Chunk chunk, World world) {
        int originX = random.nextInt(Chunk.XLENGTH);
        int originZ = random.nextInt(Chunk.ZLENGTH);
        int groundY = chunk.getTopBlockY(originX, originZ);
        
        if (chunk.getBlockID(originX, groundY, originZ) != BlockType.GRASS.id)
            return;
        
        int globalX = ChunkPos.toGlobalX(cx, originX);
        int globalZ = ChunkPos.toGlobalZ(cz, originZ);
        
        final int RADIUS = 5;
        final int HEIGHT = 10;
        
        for (int y = 0; y <= HEIGHT; ++y) {
            for (int z = -RADIUS; z <= RADIUS; ++z) {
                for (int x = -RADIUS; x <= RADIUS; ++x) {
                    if (x * x + y * y + z * z > RADIUS * RADIUS)
                        continue;

                    int gx = globalX + x;
                    int gy = groundY + y + 4;
                    int gz = globalZ + z;
                    world.setBlockID(gx, gy, gz, BlockType.OAK_LEAVES.id);
                }
            }
        }
        
        for (int y = groundY + 1; y <= groundY + 8; ++y)
            chunk.setBlockID(originX, y, originZ, BlockType.OAK_LOG.id);
    }
}