package net.survival.world.gen.layer;

import java.util.Random;

import net.survival.world.gen.BiomeType;

class AdditionalLandGenLayer extends GenLayer
{
    private final GenLayer source;
    private Random random;

    public AdditionalLandGenLayer(int lengthX, int lengthZ, long seed,
            GenLayerFactory sourceFactory)
    {
        super(lengthX, lengthZ, seed);

        source = sourceFactory.create(lengthX, lengthZ, seed + 1L);
        addDependency(source);
    }

    @Override
    public void generate(int offsetX, int offsetZ) {
        super.generate(offsetX, offsetZ);

        source.generate(offsetX, offsetZ);
        System.arraycopy(source.getMap(), 0, map, 0, map.length);

        for (int z = 0; z < lengthZ; ++z) {
            for (int x = 0; x < lengthX; ++x) {
                random = rngFromPosition(random, offsetX + x, offsetZ + z);
                int dstIndex = x + z * lengthX;
                if (map[dstIndex] == 0 && random.nextInt(12) == 0)
                    map[dstIndex] = (byte) (random.nextInt(BiomeType.getCachedValues().length - 1) + 1);
            }
        }
    }

    static class Factory implements GenLayerFactory
    {
        private final GenLayerFactory sourceFactory;

        public Factory(GenLayerFactory sourceFactory) {
            this.sourceFactory = sourceFactory;
        }

        @Override
        public GenLayer create(int lengthX, int lengthZ, long seed) {
            return new AdditionalLandGenLayer(lengthX, lengthZ, seed, sourceFactory);
        }
    }
}