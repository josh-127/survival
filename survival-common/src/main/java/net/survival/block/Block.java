package net.survival.block;

import java.util.Objects;

public final class Block {
    public final double hardness;
    public final double resistance;
    public final boolean solid;
    public final BlockModel model;

    public Block(double hardness,
                 double resistance,
                 boolean solid,
                 BlockModel model
    ) {
        this.hardness = hardness;
        this.resistance = resistance;
        this.solid = solid;
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var that = (Block) o;
        return Double.compare(that.hardness, hardness) == 0 &&
               Double.compare(that.resistance, resistance) == 0 &&
               solid == that.solid &&
               model == that.model;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hardness, resistance, solid, model);
    }

    public static class Builder {
        private static final BlockModel DEFAULT_MODEL = new BlockModel.Builder().build();

        private double hardness;
        private double resistance;
        private boolean solid;
        private BlockModel model;

        public Builder() {
            hardness = 1.0;
            resistance = 1.0;
            solid = true;
            model = DEFAULT_MODEL;
        }

        public Builder(Block block) {
            this.hardness = block.hardness;
            this.resistance = block.resistance;
            this.solid = block.solid;
            this.model = block.model;
        }

        public Block build() {
            return new Block(hardness, resistance, solid, model);
        }

        public Builder setHardness(double hardness) {
            this.hardness = hardness;
            return this;
        }

        public Builder setResistance(double resistance) {
            this.resistance = resistance;
            return this;
        }

        public Builder setSolid(boolean solid) {
            this.solid = solid;
            return this;
        }

        public Builder setModel(BlockModel model) {
            this.model = model;
            return this;
        }
    }
}