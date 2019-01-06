package net.survival.block;

import static java.lang.String.format;

final class BlockFeatureSet_V0_1 implements BlockFeatureSet
{

    @Override
    public void registerBlocks(BlockRegistry registry) {
        registry.registerBlock(new BlockState.Builder(registry)
                .withInternalName("air")
                .withDisplayName("<air>")
                .withKeywords("air")
                .withSolidEnabled(false)
                .withModel(BlockModel.INVISIBLE)
                .build());

        registry.registerBlock(new BlockState.Builder(registry)
                .withInternalName("bedrock")
                .withDisplayName("Bedrock")
                .withKeywords("bedrock")
                .withHardness(Double.POSITIVE_INFINITY)
                .withResistance(Double.POSITIVE_INFINITY)
                .build());

        registry.registerBlock(new BlockState.Builder(registry)
                .withInternalName("<temp_solid>")
                .withDisplayName("<TEMP_SOLID>")
                .withKeywords("<temp_solid>")
                .withHardness(Double.POSITIVE_INFINITY)
                .withResistance(Double.POSITIVE_INFINITY)
                .withModel(BlockModel.INVISIBLE)
                .build());

        RockMaterial[] rockMaterials = RockMaterial.values();
        for (RockMaterial rockMaterial : rockMaterials) {
            BlockState.Builder builder = new BlockState.Builder(registry);

            String materialName = rockMaterial.name().toLowerCase();
            builder.withInternalName(format("%s_%s", materialName, "clay"));
            builder.withDisplayName(format("%s %s", materialName, "clay"));
            builder.withKeywords(format("%s %s", materialName, "clay"));
            registry.registerBlock(builder.build());

            builder.withInternalName(format("%s_%s", materialName, "raw stone"));
            builder.withDisplayName(format("%s %s", materialName, "raw stone"));
            builder.withKeywords(format("%s %s", materialName, "raw stone"));
            registry.registerBlock(builder.build());

            builder.withInternalName(format("%s_%s", materialName, "dirt"));
            builder.withDisplayName(format("%s %s", materialName, "dirt"));
            builder.withKeywords(format("%s %s", materialName, "dirt"));
            registry.registerBlock(builder.build());

            builder.withInternalName(format("%s_%s", materialName, "sand"));
            builder.withDisplayName(format("%s %s", materialName, "sand"));
            builder.withKeywords(format("%s %s", materialName, "sand"));
            registry.registerBlock(builder.build());
        }

        registry.registerBlock(new BlockState.Builder(registry)
                .withInternalName("grass")
                .withDisplayName("Grass")
                .withKeywords("grass")
                .build());

        registry.registerBlock(new BlockState.Builder(registry)
                .withInternalName("water")
                .withDisplayName("Water")
                .withKeywords("water")
                .build());
    }

    private static enum RockMaterial {
        ANDESITE,
        BASALT,
        CHALK,
        CHERT,
        CLAYSTONE,
        CONGLOMERATE,
        DACITE,
        DIORITE,
        DOLOMITE,
        GABBRO,
        GNEISS,
        GRANITE,
        LIMESTONE,
        MARBLE,
        PHYLLITE,
        QUARTZITE,
        RHYOLITE,
        ROCK_SALT,
        SCHIST,
        SHALE,
        SLATE,
    }
}