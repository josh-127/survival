package net.survival.gen.layer;

import net.survival.gen.BiomeType;
import net.survival.util.IntNoise;

class AdditionalLandGenLayer extends GenLayer
{
    private final GenLayer source;

    public AdditionalLandGenLayer(int lengthX, int lengthZ, long seed,
            GenLayerFactory sourceFactory)
    {
        super(lengthX, lengthZ, seed);
        source = sourceFactory.create(lengthX, lengthZ, seed + 1000L);
    }

    @Override
    public void generate(int offsetX, int offsetZ) {
        source.generate(offsetX, offsetZ);
        System.arraycopy(source.getMap(), 0, map, 0, map.length);

        for (var z = 0; z < lengthZ; ++z) {
            for (var x = 0; x < lengthX; ++x) {
                var dstIndex = x + z * lengthX;

                if (map[dstIndex] == 0 && IntNoise.white2D(offsetX + x, offsetZ + z, baseSeed) == 0) {
                    var noise = IntNoise.white2D(offsetX + x, offsetZ + z, baseSeed + 1L);
                    var value = (noise & 0x7FFFFFFF) % (BiomeType.getCachedValues().length - 1) + 1;
                    map[dstIndex] = (byte) value;
                }
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