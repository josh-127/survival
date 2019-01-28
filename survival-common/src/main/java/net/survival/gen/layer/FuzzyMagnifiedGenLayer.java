package net.survival.gen.layer;

import java.util.Random;

class FuzzyMagnifiedGenLayer extends GenLayer
{
    private final GenLayer source;
    private Random random;

    private FuzzyMagnifiedGenLayer(int lengthX, int lengthZ, long seed,
            GenLayerFactory sourceFactory)
    {
        super(lengthX, lengthZ, seed);

        source = sourceFactory.create(lengthX / 2 + 3, lengthZ / 2 + 3, seed + 1L);
        addDependency(source);
    }

    @Override
    public void generate(int offsetX, int offsetZ) {
        super.generate(offsetX, offsetZ);

        // TODO: Remove code duplication.
        var coarseOffsetX = offsetX >= 0 ? offsetX / 2 : (offsetX - 1) / 2;
        var coarseOffsetZ = offsetZ >= 0 ? offsetZ / 2 : (offsetZ - 1) / 2;

        source.generate(coarseOffsetX, coarseOffsetZ);

        var srcMap = source.getMap();

        for (var z = 0; z < lengthZ / 2; ++z) {
            for (var x = 0; x < lengthX / 2; ++x) {
                random = rngFromPosition(random, coarseOffsetX + x, coarseOffsetZ + z);

                var srcTL = x + z * source.lengthX;
                var srcTR = srcTL + 1;
                var srcBL = srcTL + source.lengthX;
                var srcBR = srcBL + 1;
                var dstTL = (x * 2) + (z * 2) * lengthX;
                var dstTR = dstTL + 1;
                var dstBL = dstTL + lengthX;
                var dstBR = dstBL + 1;

                map[dstTL] = srcMap[srcTL];
                map[dstTR] = random.nextInt(2) == 0 ? srcMap[srcTL] : srcMap[srcTR];
                map[dstBL] = random.nextInt(2) == 0 ? srcMap[srcTL] : srcMap[srcBL];

                switch (random.nextInt(4)) {
                case 0: map[dstBR] = srcMap[srcTL]; break;
                case 1: map[dstBR] = srcMap[srcTR]; break;
                case 2: map[dstBR] = srcMap[srcBL]; break;
                case 3: map[dstBR] = srcMap[srcBR]; break;
                default:                            break;
                }
            }
        }

        if (offsetX - coarseOffsetX * 2 != 0) {
            for (var z = 0; z < lengthZ; ++z) {
                for (var x = 1; x < lengthX; ++x)
                    map[(x - 1) + z * lengthX] = map[x + z * lengthX];
            }

            for (var z = 0; z < lengthZ / 2; ++z) {
                random = rngFromPosition(random, coarseOffsetX + lengthX / 2, coarseOffsetZ + z);

                var srcTL = (lengthX / 2) + z * source.lengthX;
                var srcBL = srcTL + source.lengthX;
                var dstTL = (lengthX - 1) + (z * 2) * lengthX;
                var dstBL = dstTL + lengthX;

                map[dstTL] = srcMap[srcTL];
                random.nextInt();
                map[dstBL] = random.nextInt(2) == 0 ? srcMap[srcTL] : srcMap[srcBL];
            }
        }

        if (offsetZ - coarseOffsetZ * 2 != 0) {
            for (var z = 1; z < lengthZ; ++z) {
                for (var x = 0; x < lengthX; ++x)
                    map[x + (z - 1) * lengthX] = map[x + z * lengthZ];
            }

            for (var x = 0; x < lengthX / 2; ++x) {
                random = rngFromPosition(random, coarseOffsetX + x, coarseOffsetZ + lengthZ / 2);

                var srcTL = x + (lengthZ / 2) * source.lengthX;
                var srcTR = srcTL + 1;
                var dstTL = (x * 2) + (lengthZ - 1) * lengthX;
                var dstTR = dstTL + 1;

                map[dstTL] = srcMap[srcTL];
                map[dstTR] = random.nextInt(2) == 0 ? srcMap[srcTL] : srcMap[srcTR];
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
            return new FuzzyMagnifiedGenLayer(lengthX, lengthZ, seed, sourceFactory);
        }
    }
}