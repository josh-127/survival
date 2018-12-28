package net.survival.world.gen.decoration;

import java.util.Random;

import net.survival.block.BlockType;
import net.survival.world.World;
import net.survival.world.chunk.ChunkColumn;
import net.survival.world.chunk.ChunkColumnPos;

public class SmallTreeDecorator implements WorldDecorator
{
    private final Random random;

    public SmallTreeDecorator() {
        random = new Random();
    }

    @Override
    public void decorate(int cx, int cz, ChunkColumn chunkColumn, World world) {
        random.setSeed(ChunkColumnPos.hashPos(cx, cz));

        for (int i = 0; i < 2; ++i)
            generateTree(cx, cz, chunkColumn, world);
    }

    private void generateTree(int cx, int cz, ChunkColumn chunkColumn, World world) {
        int originX = random.nextInt(ChunkColumn.XLENGTH);
        int originZ = random.nextInt(ChunkColumn.ZLENGTH);
        int groundY = chunkColumn.getTopLevel(originX, originZ);

        if (chunkColumn.getBlock(originX, groundY, originZ) != BlockType.GRASS.id)
            return;
        
        chunkColumn.setBlock(originX, groundY, originZ, BlockType.DIRT.id);

        int globalX = ChunkColumnPos.toGlobalX(cx, originX);
        int globalZ = ChunkColumnPos.toGlobalZ(cz, originZ);

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

                    if (gy < ChunkColumn.YLENGTH)
                        world.placeBlockIfEmpty(gx, gy, gz, BlockType.OAK_LEAVES.id);
                }
            }
        }

        for (int y = groundY + 1; y <= groundY + 8; ++y) {
            if (y < ChunkColumn.YLENGTH)
                chunkColumn.setBlock(originX, y, originZ, BlockType.OAK_LOG.id);
        }
    }
}