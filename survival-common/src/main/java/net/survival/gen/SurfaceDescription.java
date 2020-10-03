package net.survival.gen;

import java.util.ArrayList;

public class SurfaceDescription {
    private final ArrayList<SurfaceLayer> layers;

    private SurfaceDescription(ArrayList<SurfaceLayer> layers) {
        this.layers = layers;
    }

    public SurfaceLayer getLayer(int index) {
        return layers.get(index);
    }

    public int getLayerCount() {
        return layers.size();
    }

    public static class Builder
    {
        private final ArrayList<SurfaceLayer> layers = new ArrayList<>();

        public SurfaceDescription build() {
            return new SurfaceDescription(layers);
        }

        public Builder addLayer(SurfaceLayer layer) {
            layers.add(layer);
            return this;
        }
    }
}