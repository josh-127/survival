package net.survival.gen.layer;

class BoxMagnifiedGenLayer extends GenLayer
{
    // TODO: Implement fine-offset scrolling.

    private final GenLayer source;

    private BoxMagnifiedGenLayer(int lengthX, int lengthZ, long seed,
            GenLayerFactory sourceFactory)
    {
        super(lengthX, lengthZ, seed);

        source = sourceFactory.create(lengthX / 2, lengthZ / 2, seed + 1L);
        addDependency(source);
    }

    @Override
    public void generate(int offsetX, int offsetZ) {
        super.generate(offsetX, offsetZ);

        source.generate(offsetX, offsetZ);

        var srcMap = source.getMap();

        for (var z = 0; z < source.lengthZ; ++z) {
            for (var x = 0; x < source.lengthX; ++x) {
                var srcValue             = srcMap[x + z * source.lengthX];
                var dstTL                = (x * 2) + (z * 2) * lengthX;
                map[dstTL]               = srcValue;
                map[dstTL + 1]           = srcValue;
                map[dstTL + lengthX]     = srcValue;
                map[dstTL + lengthX + 1] = srcValue;
            }
        }
    }

    static class Factory implements GenLayerFactory
    {
        private GenLayerFactory sourceFactory;

        public Factory(GenLayerFactory sourceFactory) {
            this.sourceFactory = sourceFactory;
        }

        @Override
        public GenLayer create(int lengthX, int lengthZ, long seed) {
            return new BoxMagnifiedGenLayer(lengthX, lengthZ, seed, sourceFactory);
        }
    }
}