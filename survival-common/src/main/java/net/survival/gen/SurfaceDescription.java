package net.survival.gen;

import java.util.ArrayList;

public class SurfaceDescription
{
    private final ArrayList<SurfaceLayer> layers;
    private final BiomeType biomeType;

    private SurfaceDescription(ArrayList<SurfaceLayer> layers, BiomeType biomeType) {
        this.layers = layers;
        this.biomeType = biomeType;
    }

    public SurfaceLayer getLayer(int index) {
        return layers.get(index);
    }

    public int getLayerCount() {
        return layers.size();
    }

    public BiomeType getBiomeType() {
        return biomeType;
    }

    public static class Builder
    {
        private final ArrayList<SurfaceLayer> layers = new ArrayList<>();
        private BiomeType biomeType = BiomeType.OCEAN;

        public SurfaceDescription build() {
            return new SurfaceDescription(layers, biomeType);
        }

        public Builder addLayer(SurfaceLayer layer) {
            layers.add(layer);
            return this;
        }

        public Builder withBiomeType(BiomeType as) {
            biomeType = as;
            return this;
        }
    }
}