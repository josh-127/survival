package net.survival.block;

import it.unimi.dsi.fastutil.floats.FloatArrayList;

public final class BlockModel {
    public static final int NUM_FACES = 7;
    public static final int FACE_DEFAULT = 0;
    public static final int FACE_NEG_Y = 1;
    public static final int FACE_POS_Y = 2;
    public static final int FACE_NEG_Z = 3;
    public static final int FACE_POS_Z = 4;
    public static final int FACE_NEG_X = 5;
    public static final int FACE_POS_X = 6;

    public static final int NUM_ATTRIBUTES = 5;
    public static final int ATTRIBUTE_POS_X = 0;
    public static final int ATTRIBUTE_POS_Y = 1;
    public static final int ATTRIBUTE_POS_Z = 2;
    public static final int ATTRIBUTE_TEX_U = 3;
    public static final int ATTRIBUTE_TEX_V = 4;

    public static final byte BLOCKING_NEG_Y = (byte) (1 << FACE_NEG_Y);
    public static final byte BLOCKING_POS_Y = (byte) (1 << FACE_POS_Y);
    public static final byte BLOCKING_NEG_Z = (byte) (1 << FACE_NEG_Z);
    public static final byte BLOCKING_POS_Z = (byte) (1 << FACE_POS_Z);
    public static final byte BLOCKING_NEG_X = (byte) (1 << FACE_NEG_X);
    public static final byte BLOCKING_POS_X = (byte) (1 << FACE_POS_X);

    public final float[][] vertexData;
    public final String[] textures;
    public final byte blocking;

    public BlockModel(float[][] vertexData, String[] textures, byte blocking) {
        if (vertexData.length != NUM_FACES) {
            throw new IllegalArgumentException("vertexData");
        }
        if (textures.length != NUM_FACES) {
            throw new IllegalArgumentException("textures");
        }
        for (var data : vertexData) {
            validateVertexData(data);
        }

        this.vertexData = new float[vertexData.length][];
        System.arraycopy(vertexData, 0, this.vertexData, 0, vertexData.length);

        this.textures = new String[textures.length];
        System.arraycopy(textures, 0, this.textures, 0, textures.length);

        this.blocking = blocking;
    }

    private static void validateVertexData(float[] data) {
        if (data != null && (data.length % NUM_ATTRIBUTES) != 0) {
            throw new IllegalArgumentException("data");
        }
    }

    public int getNumVertices(int face) {
        return vertexData[face].length / NUM_ATTRIBUTES;
    }

    public boolean hasFace(int face) {
        return vertexData[face] != null;
    }

    public float[] getVertexData(int face) {
        return vertexData[face];
    }

    public float getVertexAttribute(int face, int index, int attribute) {
        return vertexData[face][index * NUM_ATTRIBUTES + attribute];
    }

    public String getTexture(int face) {
        return textures[face];
    }

    public byte getBlockingFlags() {
        return blocking;
    }

    public boolean isBlocking(int face) {
        return (blocking & (1 << face)) != 0;
    }

    public static final class Builder {
        private static final float[] PLANE_NEG_Y = new VertexDataBuilder()
            .addVertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            .addVertex(1.0f, 0.0f, 0.0f, 1.0f, 0.0f)
            .addVertex(1.0f, 0.0f, 1.0f, 1.0f, 1.0f)
            .addVertex(1.0f, 0.0f, 1.0f, 1.0f, 1.0f)
            .addVertex(0.0f, 0.0f, 1.0f, 0.0f, 1.0f)
            .addVertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            .build();

        private static final float[] PLANE_POS_Y = new VertexDataBuilder()
            .addVertex(0.0f, 1.0f, 1.0f, 0.0f, 0.0f)
            .addVertex(1.0f, 1.0f, 1.0f, 1.0f, 0.0f)
            .addVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
            .addVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
            .addVertex(0.0f, 1.0f, 0.0f, 0.0f, 1.0f)
            .addVertex(0.0f, 1.0f, 1.0f, 0.0f, 0.0f)
            .build();

        private static final float[] PLANE_NEG_Z = new VertexDataBuilder()
            .addVertex(1.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            .addVertex(0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
            .addVertex(0.0f, 1.0f, 0.0f, 1.0f, 1.0f)
            .addVertex(0.0f, 1.0f, 0.0f, 1.0f, 1.0f)
            .addVertex(1.0f, 1.0f, 0.0f, 0.0f, 1.0f)
            .addVertex(1.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            .build();

        private static final float[] PLANE_POS_Z = new VertexDataBuilder()
            .addVertex(0.0f, 0.0f, 1.0f, 0.0f, 0.0f)
            .addVertex(1.0f, 0.0f, 1.0f, 1.0f, 0.0f)
            .addVertex(1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
            .addVertex(1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
            .addVertex(0.0f, 1.0f, 1.0f, 0.0f, 1.0f)
            .addVertex(0.0f, 0.0f, 1.0f, 0.0f, 0.0f)
            .build();

        private static final float[] PLANE_NEG_X = new VertexDataBuilder()
            .addVertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            .addVertex(0.0f, 0.0f, 1.0f, 1.0f, 0.0f)
            .addVertex(0.0f, 1.0f, 1.0f, 1.0f, 1.0f)
            .addVertex(0.0f, 1.0f, 1.0f, 1.0f, 1.0f)
            .addVertex(0.0f, 1.0f, 0.0f, 0.0f, 1.0f)
            .addVertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            .build();

        private static final float[] PLANE_POS_X = new VertexDataBuilder()
            .addVertex(1.0f, 0.0f, 1.0f, 0.0f, 0.0f)
            .addVertex(1.0f, 0.0f, 0.0f, 1.0f, 0.0f)
            .addVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
            .addVertex(1.0f, 1.0f, 0.0f, 1.0f, 1.0f)
            .addVertex(1.0f, 1.0f, 1.0f, 0.0f, 1.0f)
            .addVertex(1.0f, 0.0f, 1.0f, 0.0f, 0.0f)
            .build();

        private static final float[][] PLANES = new float[][] {
            null,
            PLANE_NEG_Y,
            PLANE_POS_Y,
            PLANE_NEG_Z,
            PLANE_POS_Z,
            PLANE_NEG_X,
            PLANE_POS_X,
        };

        private final float[][] vertexData;
        private final String[] textures;
        private byte blocking;

        public Builder() {
            vertexData = new float[NUM_FACES][];
            textures = new String[NUM_FACES];
        }

        public BlockModel build() {
            return new BlockModel(vertexData, textures, blocking);
        }

        public Builder setFace(int face, float[] value, String texture) {
            vertexData[face] = value;
            textures[face] = texture;
            blocking &= ~(1 << face);
            return this;
        }

        public Builder setFaceWithPlane(int face, String texture) {
            if (PLANES[face] == null) {
                throw new IllegalArgumentException("face");
            }

            vertexData[face] = PLANES[face];
            textures[face] = texture;
            blocking |= 1 << face;
            return this;
        }

        public Builder setFaceWithBlocker(int face) {
            if (PLANES[face] == null) {
                throw new IllegalArgumentException("face");
            }

            blocking |= 1 << face;
            return this;
        }
    }

    public static final class VertexDataBuilder {
        private final FloatArrayList vertices = new FloatArrayList(16 * NUM_ATTRIBUTES);

        public float[] build() {
            return vertices.toFloatArray();
        }

        public VertexDataBuilder addVertex(float x, float y, float z, float u, float v) {
            vertices.add(x);
            vertices.add(y);
            vertices.add(z);
            vertices.add(u);
            vertices.add(v);
            return this;
        }
    }
}