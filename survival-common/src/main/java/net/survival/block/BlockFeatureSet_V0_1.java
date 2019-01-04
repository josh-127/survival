package net.survival.block;

import static java.lang.String.format;

final class BlockFeatureSet_V0_1 implements BlockFeatureSet
{

    @Override
    public void registerBlocks(BlockRegistry registry) {
        registry.registerBlock(new BlockState.Builder()
                .withInternalName("air")
                .withDisplayName("<air>")
                .withKeywords("air")
                .withSolidEnabled(false)
                .withModel(BlockModel.INVISIBLE)
                .build());

        RockMaterial[] rockMaterials = RockMaterial.values();
        for (RockMaterial rockMaterial : rockMaterials) {
            BlockState.Builder builder = new BlockState.Builder();

            String materialName = rockMaterial.name().toLowerCase();
            builder.withInternalName(format("%s_%s", materialName, "clay"));
            builder.withDisplayName(format("%s %s", materialName, "clay"));
            builder.withKeywords(format("%s %s", materialName, "clay"));

            registry.registerBlock(builder.build());
        }
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