package net.survival.block;

final class StandardBlockMeshes {
    public static final float[] SAPLING = createBuilder().build();

    private StandardBlockMeshes() {}

    private static BlockModel.VertexDataBuilder createBuilder() {
        return new BlockModel.VertexDataBuilder();
    }
}
