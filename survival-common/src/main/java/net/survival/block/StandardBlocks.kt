package net.survival.block

private typealias Models = StandardBlockModels

object StandardBlocks {
    val AIR         = Block(hardness = 0.0, resistance = 0.0, solid = false, model = Models.EMPTY)
    val BEDROCK     = Block(model = Models.BEDROCK)
    val CACTUS      = Block(model = Models.CACTUS)
    val COBBLESTONE = Block(model = Models.COBBLESTONE)
    val DIRT        = Block(model = Models.DIRT)
    val GRASS       = Block(model = Models.GRASS)
    val GRAVEL      = Block(model = Models.GRAVEL)
    val OAK_LEAVES  = Block(model = Models.OAK_LEAVES)
    val OAK_LOG     = Block(model = Models.OAK_LOG)
    val OAK_SAPLING = Block(model = Models.OAK_SAPLING)
    val SAND        = Block(model = Models.SAND)
    val SANDSTONE   = Block(model = Models.SANDSTONE)
    val STONE       = Block(model = Models.STONE)
    val WATER       = Block(model = Models.WATER)
}

object StandardBlockModels {
    private const val PA = "ProgrammerArt-v3.0/textures/blocks/"
    private const val TEX = "textures/blocks/"

    val EMPTY         = createEmptyModel()
    val EMPTY_BLOCKER = createEmptyBlockerModel()
    val BEDROCK       = createCube(PA, "bedrock.png")
    val CACTUS        = createCube(PA, "cactus_bottom.png", "cactus_top.png", "cactus_side.png")
    val COBBLESTONE   = createCube(PA, "stone.png")
    val DIRT          = createCube(PA, "dirt.png")
    val GRASS         = createCube(PA, "dirt.png", "grass_top.png", "grass_side.png")
    val GRAVEL        = createCube(PA, "gravel.png")
    val OAK_LEAVES    = createCube(PA, "leaves_oak.png")
    val OAK_LOG       = createCube(PA, "log_oak_top.png", "log_oak_top.png", "log_oak.png")
    val OAK_SAPLING   = createCustomModel(StandardBlockMeshes.SAPLING, PA, "sapling_oak.png")
    val SAND          = createCube(PA, "sand.png")
    val SANDSTONE     = createCube(PA, "sandstone_bottom.png", "sandstone_top.png", "sandstone_normal.png")
    val STONE         = createCube(PA, "cobblestone.png")
    val WATER         = createCube(TEX, "water.png")

    private fun createBuilder(): BlockModel.Builder = BlockModel.Builder()

    private fun createEmptyModel(): BlockModel = createBuilder().build()

    private fun createEmptyBlockerModel(): BlockModel =
        createBuilder()
            .setFaceWithBlocker(BlockModel.FACE_NEG_Y)
            .setFaceWithBlocker(BlockModel.FACE_POS_Y)
            .setFaceWithBlocker(BlockModel.FACE_NEG_Z)
            .setFaceWithBlocker(BlockModel.FACE_POS_Z)
            .setFaceWithBlocker(BlockModel.FACE_NEG_X)
            .setFaceWithBlocker(BlockModel.FACE_POS_X)
            .build()

    private fun createCustomModel(vertexData: FloatArray, root: String, texture: String): BlockModel =
        createBuilder()
            .setFace(BlockModel.FACE_DEFAULT, vertexData, getTexturePath(root, texture))
            .build()

    private fun createCube(root: String, texture: String): BlockModel =
        createBuilder()
            .setFaceWithPlane(BlockModel.FACE_NEG_Y, getTexturePath(root, texture))
            .setFaceWithPlane(BlockModel.FACE_POS_Y, getTexturePath(root, texture))
            .setFaceWithPlane(BlockModel.FACE_NEG_Z, getTexturePath(root, texture))
            .setFaceWithPlane(BlockModel.FACE_POS_Z, getTexturePath(root, texture))
            .setFaceWithPlane(BlockModel.FACE_NEG_X, getTexturePath(root, texture))
            .setFaceWithPlane(BlockModel.FACE_POS_X, getTexturePath(root, texture))
            .build()

    private fun createCube(
        root: String,
        negYTexture: String,
        posYTexture: String,
        remainingTextures: String
    ): BlockModel {
        return createBuilder()
            .setFaceWithPlane(BlockModel.FACE_NEG_Y, getTexturePath(root, negYTexture))
            .setFaceWithPlane(BlockModel.FACE_POS_Y, getTexturePath(root, posYTexture))
            .setFaceWithPlane(BlockModel.FACE_NEG_Z, getTexturePath(root, remainingTextures))
            .setFaceWithPlane(BlockModel.FACE_POS_Z, getTexturePath(root, remainingTextures))
            .setFaceWithPlane(BlockModel.FACE_NEG_X, getTexturePath(root, remainingTextures))
            .setFaceWithPlane(BlockModel.FACE_POS_X, getTexturePath(root, remainingTextures))
            .build()
    }

    private fun createCube(
        root: String,
        negYTexture: String,
        posYTexture: String,
        negZTexture: String,
        posZTexture: String,
        negXTexture: String,
        posXTexture: String
    ): BlockModel {
        return createBuilder()
            .setFaceWithPlane(BlockModel.FACE_NEG_Y, getTexturePath(root, negYTexture))
            .setFaceWithPlane(BlockModel.FACE_POS_Y, getTexturePath(root, posYTexture))
            .setFaceWithPlane(BlockModel.FACE_NEG_Z, getTexturePath(root, negZTexture))
            .setFaceWithPlane(BlockModel.FACE_POS_Z, getTexturePath(root, posZTexture))
            .setFaceWithPlane(BlockModel.FACE_NEG_X, getTexturePath(root, negXTexture))
            .setFaceWithPlane(BlockModel.FACE_POS_X, getTexturePath(root, posXTexture))
            .build()
    }

    private fun getTexturePath(root: String, texture: String): String = root + texture
}

private object StandardBlockMeshes {
    val SAPLING = createBuilder().build()

    private fun createBuilder(): BlockModel.VertexDataBuilder = BlockModel.VertexDataBuilder()
}
