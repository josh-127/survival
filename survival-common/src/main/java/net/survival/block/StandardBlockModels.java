package net.survival.block;

import static net.survival.block.BlockModel.FACE_DEFAULT;
import static net.survival.block.BlockModel.FACE_NEG_Y;
import static net.survival.block.BlockModel.FACE_POS_Y;
import static net.survival.block.BlockModel.FACE_NEG_Z;
import static net.survival.block.BlockModel.FACE_POS_Z;
import static net.survival.block.BlockModel.FACE_NEG_X;
import static net.survival.block.BlockModel.FACE_POS_X;

public final class StandardBlockModels {
    private static final String PA = "ProgrammerArt-v3.0/textures/blocks/";
    private static final String TEX = "textures/blocks/";

    public static final BlockModel EMPTY         = createEmptyModel();
    public static final BlockModel EMPTY_BLOCKER = createEmptyBlockerModel();
    public static final BlockModel BEDROCK       = createCube(PA, "bedrock.png");
    public static final BlockModel CACTUS        = createCube(PA, "cactus_bottom.png", "cactus_top.png", "cactus_side.png");
    public static final BlockModel COBBLESTONE   = createCube(PA, "stone.png");
    public static final BlockModel DIRT          = createCube(PA, "dirt.png");
    public static final BlockModel GRASS         = createCube(PA, "dirt.png", "grass_top.png", "grass_side.png");
    public static final BlockModel GRAVEL        = createCube(PA, "gravel.png");
    public static final BlockModel OAK_LEAVES    = createCube(PA, "leaves_oak.png");
    public static final BlockModel OAK_LOG       = createCube(PA, "log_oak_top.png", "log_oak_top.png", "log_oak.png");
    public static final BlockModel OAK_SAPLING   = createCustomModel(StandardBlockMeshes.SAPLING, PA, "sapling_oak.png");
    public static final BlockModel SAND          = createCube(PA, "sand.png");
    public static final BlockModel SANDSTONE     = createCube(PA, "sandstone_bottom.png", "sandstone_top.png", "sandstone_normal.png");
    public static final BlockModel STONE         = createCube(PA, "cobblestone.png");
    public static final BlockModel WATER         = createCube(TEX, "water.png");

    private StandardBlockModels() {}

    private static BlockModel.Builder createBuilder() {
        return new BlockModel.Builder();
    }

    private static BlockModel createEmptyModel() {
        return createBuilder().build();
    }

    private static BlockModel createEmptyBlockerModel() {
        return createBuilder().setFaceWithBlocker(FACE_NEG_Y)
                              .setFaceWithBlocker(FACE_POS_Y)
                              .setFaceWithBlocker(FACE_NEG_Z)
                              .setFaceWithBlocker(FACE_POS_Z)
                              .setFaceWithBlocker(FACE_NEG_X)
                              .setFaceWithBlocker(FACE_POS_X)
                              .build();
    }

    private static BlockModel createCustomModel(float[] vertexData, String root, String texture) {
        return createBuilder()
            .setFace(FACE_DEFAULT, vertexData, getTexturePath(root, texture))
            .build();
    }

    private static BlockModel createCube(String root, String texture) {
        return createBuilder()
            .setFaceWithPlane(FACE_NEG_Y, getTexturePath(root, texture))
            .setFaceWithPlane(FACE_POS_Y, getTexturePath(root, texture))
            .setFaceWithPlane(FACE_NEG_Z, getTexturePath(root, texture))
            .setFaceWithPlane(FACE_POS_Z, getTexturePath(root, texture))
            .setFaceWithPlane(FACE_NEG_X, getTexturePath(root, texture))
            .setFaceWithPlane(FACE_POS_X, getTexturePath(root, texture))
            .build();
    }

    private static BlockModel createCube(String root,
                                         String negYTexture,
                                         String posYTexture,
                                         String remainingTextures)
    {
        return createBuilder()
            .setFaceWithPlane(FACE_NEG_Y, getTexturePath(root, negYTexture))
            .setFaceWithPlane(FACE_POS_Y, getTexturePath(root, posYTexture))
            .setFaceWithPlane(FACE_NEG_Z, getTexturePath(root, remainingTextures))
            .setFaceWithPlane(FACE_POS_Z, getTexturePath(root, remainingTextures))
            .setFaceWithPlane(FACE_NEG_X, getTexturePath(root, remainingTextures))
            .setFaceWithPlane(FACE_POS_X, getTexturePath(root, remainingTextures))
            .build();
    }

    private static BlockModel createCube(String root,
                                         String negYTexture,
                                         String posYTexture,
                                         String negZTexture,
                                         String posZTexture,
                                         String negXTexture,
                                         String posXTexture)
    {
        return createBuilder()
            .setFaceWithPlane(FACE_NEG_Y, getTexturePath(root, negYTexture))
            .setFaceWithPlane(FACE_POS_Y, getTexturePath(root, posYTexture))
            .setFaceWithPlane(FACE_NEG_Z, getTexturePath(root, negZTexture))
            .setFaceWithPlane(FACE_POS_Z, getTexturePath(root, posZTexture))
            .setFaceWithPlane(FACE_NEG_X, getTexturePath(root, negXTexture))
            .setFaceWithPlane(FACE_POS_X, getTexturePath(root, posXTexture))
            .build();
    }

    private static String getTexturePath(String root, String texture) {
        return root + texture;
    }
}
