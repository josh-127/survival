package net.survival.gen.layer;

import net.survival.gen.BiomeType;
import net.survival.util.IntNoise;

@Deprecated
class LandGenLayer extends GenLayer {
    public LandGenLayer(int lengthX, int lengthZ, long seed) {
        super(lengthX, lengthZ, seed);
    }

    @Override
    public void generate(int offsetX, int offsetZ) {
        for (var z = 0; z < lengthZ; ++z) {
            for (var x = 0; x < lengthX; ++x) {
                var dstIndex = x + z * lengthX;

                if (IntNoise.white2D(offsetX + x, offsetZ + z, baseSeed) % 2 == 0) {
                    var noise = IntNoise.white2D(offsetX + x, offsetZ + z, baseSeed + 1L);
                    var value = (noise & 0x7FFFFFFF) % (BiomeType.getCachedValues().length - 1) + 1;
                    map[dstIndex] = (byte) value;
                }
                else {
                    map[dstIndex] = 0;
                }
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