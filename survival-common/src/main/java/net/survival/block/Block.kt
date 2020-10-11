package net.survival.block

import it.unimi.dsi.fastutil.floats.FloatArrayList

data class Block(
    val hardness: Double = 1.0,
    val resistance: Double = 1.0,
    val solid: Boolean = true,
    val model: BlockModel = StandardBlockModels.STONE
)

class BlockModel(
    vertexData: Array<FloatArray?>,
    textures: Array<String?>,
    blockingFlags: Byte
) {
    companion object {
        const val NUM_FACES = 7
        const val FACE_DEFAULT = 0
        const val FACE_NEG_Y = 1
        const val FACE_POS_Y = 2
        const val FACE_NEG_Z = 3
        const val FACE_POS_Z = 4
        const val FACE_NEG_X = 5
        const val FACE_POS_X = 6

        const val NUM_ATTRIBUTES = 5
        const val ATTRIBUTE_POS_X = 0
        const val ATTRIBUTE_POS_Y = 1
        const val ATTRIBUTE_POS_Z = 2
        const val ATTRIBUTE_TEX_U = 3
        const val ATTRIBUTE_TEX_V = 4

        const val BLOCKING_NEG_Y = (1 shl FACE_NEG_Y).toByte()
        const val BLOCKING_POS_Y = (1 shl FACE_POS_Y).toByte()
        const val BLOCKING_NEG_Z = (1 shl FACE_NEG_Z).toByte()
        const val BLOCKING_POS_Z = (1 shl FACE_POS_Z).toByte()
        const val BLOCKING_NEG_X = (1 shl FACE_NEG_X).toByte()
        const val BLOCKING_POS_X = (1 shl FACE_POS_X).toByte()
    }

    val vertexData: Array<FloatArray?>
    val textures: Array<String?>
    val blockingFlags: Byte

    init {
        require(vertexData.size == NUM_FACES) { "vertexData" }
        require(textures.size == NUM_FACES) { "textures" }
        for (data in vertexData) {
            require(!(data != null && data.size % NUM_ATTRIBUTES != 0)) { "data" }
        }

        this.vertexData = vertexData.clone()
        this.textures = textures.clone()
        this.blockingFlags = blockingFlags
    }

    fun getNumVertices(face: Int): Int {
        val vertexDataAtFace = vertexData[face]
        check(vertexDataAtFace != null) { "vertexData[$face] is null" }
        return vertexDataAtFace.size / NUM_ATTRIBUTES
    }

    fun hasFace(face: Int): Boolean = vertexData[face] != null

    fun getVertexData(face: Int): FloatArray {
        val vertexDataAtFace = vertexData[face]
        check(vertexDataAtFace != null) { "vertexData[$face] is null" }
        return vertexDataAtFace
    }

    fun getVertexAttribute(face: Int, index: Int, attribute: Int): Float =
        vertexData[face]!![index * NUM_ATTRIBUTES + attribute]

    fun getTexture(face: Int): String {
        val textureAtFace = textures[face]
        check(textureAtFace != null) { "textures[$face] is null" }
        return textureAtFace
    }

    fun isBlocking(face: Int): Boolean = (blockingFlags.toInt() and (1 shl face)) != 0

    class Builder {
        private val vertexData: Array<FloatArray?> = arrayOfNulls(NUM_FACES)
        private val textures: Array<String?> = arrayOfNulls(NUM_FACES)
        private var blocking: Byte = 0

        fun build(): BlockModel = BlockModel(vertexData, textures, blocking)

        fun setFace(face: Int, value: FloatArray?, texture: String?): Builder {
            vertexData[face] = value
            textures[face] = texture
            blocking = (blocking.toInt() and (1 shl face).inv()).toByte()
            return this
        }

        fun setFaceWithPlane(face: Int, texture: String?): Builder {
            requireNotNull(PLANES[face]) { "face" }
            vertexData[face] = PLANES[face]
            textures[face] = texture
            blocking = (blocking.toInt() or (1 shl face)).toByte()
            return this
        }

        fun setFaceWithBlocker(face: Int): Builder {
            requireNotNull(PLANES[face]) { "face" }
            blocking = (blocking.toInt() or (1 shl face)).toByte()
            return this
        }

        companion object {
            private val PLANE_NEG_Y = VertexDataBuilder()
                .addVertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                .addVertex(1.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                .addVertex(1.0f, 0.0f, 1.0f, 1.0f, 1.0f)
                .addVertex(1.0f, 0.0f, 1.0f, 1.0f, 1.0f)
                .addVertex(0.0f, 0.0f, 1.0f, 0.0f, 1.0f)
                .addVertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                .build()
            private val PLANE_POS_Y = VertexDataBuilder()
                .addVertex(0.0f, 1.0f, 1.0f, 0.0f, 0.0f)
                .addVertex(1.0f, 1.0f, 1.0f, 1.0f, 0.0f)
                .addVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
                .addVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
                .addVertex(0.0f, 1.0f, 0.0f, 0.0f, 1.0f)
                .addVertex(0.0f, 1.0f, 1.0f, 0.0f, 0.0f)
                .build()
            private val PLANE_NEG_Z = VertexDataBuilder()
                .addVertex(1.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                .addVertex(0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                .addVertex(0.0f, 1.0f, 0.0f, 1.0f, 1.0f)
                .addVertex(0.0f, 1.0f, 0.0f, 1.0f, 1.0f)
                .addVertex(1.0f, 1.0f, 0.0f, 0.0f, 1.0f)
                .addVertex(1.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                .build()
            private val PLANE_POS_Z = VertexDataBuilder()
                .addVertex(0.0f, 0.0f, 1.0f, 0.0f, 0.0f)
                .addVertex(1.0f, 0.0f, 1.0f, 1.0f, 0.0f)
                .addVertex(1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
                .addVertex(1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
                .addVertex(0.0f, 1.0f, 1.0f, 0.0f, 1.0f)
                .addVertex(0.0f, 0.0f, 1.0f, 0.0f, 0.0f)
                .build()
            private val PLANE_NEG_X = VertexDataBuilder()
                .addVertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                .addVertex(0.0f, 0.0f, 1.0f, 1.0f, 0.0f)
                .addVertex(0.0f, 1.0f, 1.0f, 1.0f, 1.0f)
                .addVertex(0.0f, 1.0f, 1.0f, 1.0f, 1.0f)
                .addVertex(0.0f, 1.0f, 0.0f, 0.0f, 1.0f)
                .addVertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
                .build()
            private val PLANE_POS_X = VertexDataBuilder()
                .addVertex(1.0f, 0.0f, 1.0f, 0.0f, 0.0f)
                .addVertex(1.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                .addVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
                .addVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
                .addVertex(1.0f, 1.0f, 1.0f, 0.0f, 1.0f)
                .addVertex(1.0f, 0.0f, 1.0f, 0.0f, 0.0f)
                .build()
            private val PLANES = arrayOf(
                null,
                PLANE_NEG_Y,
                PLANE_POS_Y,
                PLANE_NEG_Z,
                PLANE_POS_Z,
                PLANE_NEG_X,
                PLANE_POS_X)
        }
    }

    class VertexDataBuilder {
        private val vertices = FloatArrayList(16 * NUM_ATTRIBUTES)

        fun build(): FloatArray {
            return vertices.toFloatArray()
        }

        fun addVertex(x: Float, y: Float, z: Float, u: Float, v: Float): VertexDataBuilder {
            vertices.add(x)
            vertices.add(y)
            vertices.add(z)
            vertices.add(u)
            vertices.add(v)
            return this
        }
    }
}

enum class BlockFace {
    TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK;

    companion object {
        val cachedValues = values()
    }
}
