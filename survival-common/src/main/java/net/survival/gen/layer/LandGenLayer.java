package net.survival.gen.layer;

import java.util.Random;

import net.survival.gen.BiomeType;

class LandGenLayer extends GenLayer
{
    private Random random;

    public LandGenLayer(int lengthX, int lengthZ, long seed) {
        super(lengthX, lengthZ, seed);
    }

    @Override
    public void generate(int offsetX, int offsetZ) {
        super.generate(offsetX, offsetZ);

        for (var z = 0; z < lengthZ; ++z) {
            for (var x = 0; x < lengthX; ++x) {
                random = rngFromPosition(random, offsetX + x, offsetZ + z);
                var dstIndex = x + z * lengthX;
                if (random.nextInt(2) == 0)
                    map[dstIndex] = (byte) (random.nextInt(BiomeType.getCachedValues().length - 1) + 1);
                else
                    map[dstIndex] = 0;
            }
        }
    }

    static class Factory implements GenLayerFactory
    {
        @Override
        public GenLayer create(int lengthX, int lengthZ, long seed) {
            return new LandGenLayer(lengthX, lengthZ, seed);
        }
    }
}