package net.survival.world.gen.v1.layer;

import java.util.Random;

import net.survival.block.BlockType;

class StoneGenLayer extends GenLayer
{
    private Random random;
    
    private StoneGenLayer(int lengthX, int lengthZ, long baseSeed) {
        super(lengthX, lengthZ, baseSeed);
    }
    
    @Override
    public void generate(int offsetX, int offsetZ) {
        super.generate(offsetX, offsetZ);
        
        for (int z = 0; z < lengthZ; ++z) {
            for (int x = 0; x < lengthX; ++x) {
                random = rngFromPosition(random, offsetX + x, offsetZ + z);
                map[x + z * lengthX] = (byte) (random.nextInt(BlockType.getStoneTypes().length - 1) + 1);
            }
        }
    }
    
    static class Factory implements GenLayerFactory
    {
        @Override
        public GenLayer create(int lengthX, int lengthZ, long seed) {
            return new StoneGenLayer(lengthX, lengthZ, seed);
        }
    }
}