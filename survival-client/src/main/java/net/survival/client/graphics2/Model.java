package net.survival.client.graphics2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import net.survival.client.graphics2.Model.Material.TextureEntry;
import net.survival.client.util.ArrayIterators;
import net.survival.client.util.Vec2;
import net.survival.client.util.Vec3;

/**
 * A Model is a collection of textures, materials, and meshes.
 */
public class Model
{
    private final Texture[] textures;
    private final Material[] materials;
    private final Mesh[] meshes;
    private final String texturePath;

    private Model(Texture[] textures, Material[] materials, Mesh[] meshes, String texturePath) {
        this.textures = textures;
        this.materials = materials;
        this.meshes = meshes;
        this.texturePath = texturePath;
    }

    /**
     * Creates a Model builder.
     * 
     * @return a Model builder
     */
    public static Builder createBuilder() {
        return new Builder();
    }

    /**
     * Iterates through the model's textures.
     * 
     * @return an iterator
     */
    public Iterable<Texture> iterateTextures() {
        return ArrayIterators.iterate(textures);
    }

    /**
     * Iterates through the model's materials.
     * 
     * @return an iterator
     */
    public Iterable<Material> iterateMaterials() {
        return ArrayIterators.iterate(materials);
    }

    /**
     * Gets the number of meshes in the model.
     * @return the number of meshes in the model
     */
    public int getMeshCount() {
        return meshes.length;
    }

    /**
     * Iterates through the model's meshes.
     * @return an iterator
     */
    public Iterable<Mesh> iterateMeshes() {
        return ArrayIterators.iterate(meshes);
    }

    /**
     * Gets the texture path.
     * @return the texture path
     */
    public String getTexturePath() {
        return texturePath;
    }

    /**
     * A texture is an image projected onto a mesh.
     */
    public static class Texture
    {
        public final String asset;

        private Texture(String asset) {
            this.asset = asset;
        }

        /**
         * Creates a texture builder.
         * @return a texture builder
         */
        public static Builder createBuilder() {
            return new Builder();
        }

        /**
         * A Texture.Builder is used to build Model textures.
         */
        public static class Builder
        {
            private String asset;

            private Builder() {}

            /**
             * Constructs the Texture from the Texture.Builder.
             * @return the constructed Texture
             */
            public Texture build() {
                if (asset == null)
                    throw new IllegalStateException("asset is required");

                return new Texture(asset);
            }

            /**
             * Sets the texture's asset.
             * 
             * @param asset the asset of the texture
             * @return the builder
             */
            public Builder withAsset(String asset) {
                this.asset = asset;
                return this;
            }
        }
    }

    /**
     * A material contains properties of a Mesh's appearance.
     */
    public static class Material
    {
        public static final Material DEFAULT_MATERIAL = new Material(Type.SHADED, new TextureEntry[0]);

        public final Type type;
        private final TextureEntry[] textureEntries;

        private Material(Type type, TextureEntry[] entries) {
            this.type = type;
            this.textureEntries = entries;
        }

        /**
         * Creates a material builder.
         * 
         * @return a material builder
         */
        public static Builder createBuilder() {
            return new Builder();
        }

        /**
         * Checks if the Material has texture entries.
         * 
         * @return true if the Material has texture entries; otherwise false
         */
        public boolean hasTextureEntries() {
            return textureEntries.length != 0;
        }

        /**
         * Iterates through the Material's texture entries.
         * 
         * @return an iterator
         */
        public Iterable<TextureEntry> iterateTextureEntries() {
            return ArrayIterators.iterate(textureEntries);
        }

        /**
         * Defines the material's shading method.
         */
        public static enum Type
        {
            SHADELESS,
            SHADED
        }

        /**
         * A TextureEntry contains properties of a texture's appearance in a material.
         */
        public static class TextureEntry
        {
            public final int textureId;
            private Model model;

            private TextureEntry(int textureId) {
                this.textureId = textureId;
            }

            /**
             * Creates a texture entry builder.
             * @return a texture entry builder
             */
            public static Builder createBuilder() {
                return new Builder();
            }

            /**
             * Gets the TextureEntry's texture.
             * @return the TextureEntry's texture
             */
            public Texture getTexture() {
                return model.textures[textureId];
            }

            /**
             * A TextureEntry.Builder is used to create materials.
             */
            public static class Builder
            {
                private Integer textureId;

                private Builder() {}

                /**
                 * Constructs a TextureEntry from the TextureEntry.Builder.
                 * 
                 * @return the constructed TextureEntry
                 */
                public TextureEntry build() {
                    if (textureId == null)
                        throw new IllegalStateException("textureId is required");

                    return new TextureEntry(textureId);
                }

                /**
                 * Sets the TextureEntry's texture ID, which is an index into the Model's list
                 * of textures.
                 * 
                 * @param id the texture ID of the TextureEntry
                 * @return the builder
                 */
                public Builder withTextureId(int id) {
                    textureId = id;
                    return this;
                }
            }
        }

        public static class Builder
        {
            private Type type;
            private final ArrayList<TextureEntry> textureEntries;

            private Builder() {
                type = Type.SHADED;
                textureEntries = new ArrayList<>();
            }
            
            /**
             * Constructs a Material from the Material.Builder.
             * 
             * @return the constructed Material
             */
            public Material build() {
                return new Material(type, textureEntries.toArray(new TextureEntry[textureEntries.size()]));
            }

            /**
             * Sets the Material's shading type.
             * 
             * @param type the type of the material
             * @return the builder
             */
            public Builder withType(Type type) {
                this.type = type;
                return this;
            }

            /**
             * Adds a TextureEntry to the Material.
             * 
             * @param entry the TextureEntry to add
             * @return the builder
             */
            public Builder addTextureEntry(TextureEntry entry) {
                textureEntries.add(entry);
                return this;
            }
        }
    }

    /**
     * A mesh is a collection of primitives that form a three-dimensional shape.
     * 
     * A vertex is an angular point of a triangle. A vertex can have additional
     * properties, which are called vertex layers. An index is an index into the
     * mesh's vertex array. The index array describes primitive order of vertices.
     */
    public static class Mesh
    {
        public final int materialId;
        private final short[] indices;
        private final Vec3[] vertices;
        private final Vec3[][] colors;
        private final Vec2[][] texCoords;
        private final Vec3[] normals;
        private Model model;

        private Mesh(
                int materialId,
                short[] indices,
                Vec3[] vertices,
                Vec3[][] colors,
                Vec2[][] texCoords,
                Vec3[] normals)
        {
            this.materialId = materialId;
            this.indices = indices;
            this.vertices = vertices;
            this.colors = colors;
            this.texCoords = texCoords;
            this.normals = normals;
        }

        /**
         * Creates a mesh builder.
         * 
         * @return a mesh builder
         */
        public static Builder createMeshBuilder() {
            return new Builder();
        }

        /**
         * Creates a vertex layer builder.
         * 
         * @param clazz the type of vertex layer
         * @return a vertex layer builder
         */
        public static <E> LayerBuilder<E> createLayerBuilder(Class<E> clazz) {
            return new LayerBuilder<>(clazz);
        }
        
        /**
         * Gets the Mesh's material.
         * 
         * @return the Mesh's material
         */
        public Material getMaterial() {
            return model.materials.length != 0
                    ? model.materials[materialId]
                    : Material.DEFAULT_MATERIAL;
        }

        /**
         * Gets the number of indices in the Mesh.
         * 
         * @return the number of indices in the Mesh
         */
        public int getIndexCount() {
            return indices.length;
        }
        
        /**
         * Iterates through the Mesh's indices.
         * 
         * @return an iterator
         */
        public Iterable<Short> iterateIndices() {
            return ArrayIterators.iterate(indices);
        }

        /**
         * Gets a vertex index in the Mesh.
         * 
         * @param index the index of the vertex index
         * @return a vertex index
         */
        public short getIndex(int index) {
            return indices[index];
        }

        /**
         * Gets the number of vertices in the Mesh.
         * 
         * @return the number of vertices in the Mesh
         */
        public int getVertexCount() {
            return vertices.length;
        }

        /**
         * Iterates through the Mesh's vertices
         * 
         * @return an iterator
         */
        public Iterable<Vec3> iterateVertices() {
            return ArrayIterators.iterate(vertices);
        }

        /**
         * Gets a vertex's position in the Mesh.
         * 
         * @param index an index into the Mesh's vertex array
         * @return a vertex position
         */
        public Vec3 getVertex(int index) {
            return vertices[index];
        }

        /**
         * Checks if the Mesh has any color layers.
         * 
         * @return true if the Mesh has one or more color layers; otherwise false
         */
        public boolean hasColorLayers() {
            return colors.length != 0;
        }

        /**
         * Iterates through the Mesh's color layers.
         * 
         * @return an iterator
         */
        public Iterable<Iterator<Vec3>> iterateColorLayers() {
            return ArrayIterators.iterate(colors);
        }

        /**
         * Checks if the Mesh has any texture coordinate layers.
         * 
         * @return true if the Mesh has one or more texture coordinate layers;
         *         otherwise false
         */
        public boolean hasTexCoordLayers() {
            return texCoords.length != 0;
        }

        /**
         * Iterates through the Mesh's texture coordinate layers.
         * 
         * @return an iterator
         */
        public Iterable<Iterator<Vec2>> iterateTexCoordLayers() {
            return ArrayIterators.iterate(texCoords);
        }

        /**
         * Checks if the Mesh has normals.
         * 
         * @return true if the Mesh has normals; otherwise false
         */
        public boolean hasNormals() {
            return normals.length != 0;
        }

        /**
         * Iterates through the Mesh's normals.
         * 
         * @return an iterator
         */
        public Iterable<Vec3> iterateNormals() {
            return ArrayIterators.iterate(normals);
        }

        /**
         * Gets the Mesh's primitive topology.
         * 
         * @return the Mesh's primitive topology
         */
        public PrimitiveTopology getPrimitiveTopology() {
            return PrimitiveTopology.TRIANGLE_LIST;
        }

        /**
         * Defines how the Mesh's index array is interpreted.
         */
        public static enum PrimitiveTopology
        {
            TRIANGLE_LIST
        }

        /**
         * A Mesh.LayerBuilder is used to create vertex layers.
         * 
         * @param <E> the type of vertex layer
         */
        public static class LayerBuilder<E>
        {
            private final ArrayList<E> elements;
            private final Class<E> clazz;

            private LayerBuilder(Class<E> clazz) {
                elements = new ArrayList<>();
                this.clazz = clazz;
            }

            /**
             * Adds a vertex's property to the layer.
             * 
             * @param element the vertex's property to add
             * @return the builder
             */
            public LayerBuilder<E> addElement(E element) {
                elements.add(element);
                return this;
            }

            /**
             * Constructs a vertex layer from the LayerBuilder.
             * 
             * @return the constructed vertex layer
             */
            @SuppressWarnings("unchecked")
            public E[] build() {
                return elements.toArray((E[]) Array.newInstance(clazz, elements.size()));
            }
        }

        /**
         * A Mesh.Builder is used to build meshes.
         */
        public static class Builder
        {
            private int materialId;
            private final ArrayList<Short> indices;
            private final ArrayList<Vec3> vertices;
            private final ArrayList<Vec3[]> colorLayers;
            private final ArrayList<Vec2[]> texCoordLayers;
            private final ArrayList<Vec3> normals;

            private Builder() {
                indices = new ArrayList<>();
                vertices = new ArrayList<>();
                colorLayers = new ArrayList<>();
                texCoordLayers = new ArrayList<>();
                normals = new ArrayList<>();
            }

            /**
             * Constructs the Mesh from the Mesh.Builder.
             * 
             * @return the constructed Mesh
             */
            public Mesh build() {
                for (Vec3[] layer : colorLayers) {
                    if (layer.length != vertices.size())
                        throw new IllegalStateException("number of color elements is not equal to vertices.length");
                }

                for (Vec2[] layer : texCoordLayers) {
                    if (layer.length != vertices.size())
                        throw new IllegalStateException(
                                "number of texture coordinate elements is not equal to vertices.length");
                }

                if (normals.size() != 0 && normals.size() != vertices.size())
                    throw new IllegalStateException("normals.length == vertices.length");

                short[] indicesArray = new short[indices.size()];
                for (int i = 0; i < indicesArray.length; i++) {
                    indicesArray[i] = indices.get(i);
                }

                return new Mesh(
                        materialId,
                        indicesArray,
                        vertices.toArray(new Vec3[vertices.size()]),
                        colorLayers.toArray(new Vec3[colorLayers.size()][vertices.size()]),
                        texCoordLayers.toArray(new Vec2[texCoordLayers.size()][vertices.size()]),
                        normals.toArray(new Vec3[normals.size()]));
            }

            /**
             * Sets the Mesh's material ID, which is an index into the Model's material
             * list.
             * 
             * @param materialId the material ID
             * @return the builder
             */
            public Builder withMaterialId(int materialId) {
                this.materialId = materialId;
                return this;
            }

            /**
             * Adds an index to the Mesh.
             * 
             * @param index the index to add
             * @return the builder
             */
            public Builder addIndex(short index) {
                indices.add(index);
                return this;
            }

            /**
             * Adds a vertex to the Mesh.
             * 
             * @param vertex the vertex to add
             * @return the builder
             */
            public Builder addVertex(Vec3 vertex) {
                vertices.add(vertex);
                return this;
            }

            /**
             * Adds a color vertex layer to the Mesh.
             * 
             * @param layer the color vertex layer to add
             * @return the builder
             */
            public Builder addColorLayer(Vec3[] layer) {
                colorLayers.add(layer);
                return this;
            }

            /**
             * Adds a texture coordinate vertex layer to the Mesh.
             * 
             * @param layer the texture coordinate vertex layer to add
             * @return the builder
             */
            public Builder addTexCoordLayer(Vec2[] layer) {
                texCoordLayers.add(layer);
                return this;
            }

            /**
             * Adds a vertex normal to the Mesh.
             * 
             * @param normal the vertex normal to add.
             * @return
             */
            public Builder addNormal(Vec3 normal) {
                normals.add(normal);
                return this;
            }
        }
    }

    private static Model link(Model model) {
        for (Material material : model.materials) {
            for (TextureEntry entry : material.textureEntries) {
                entry.model = model;
            }
        }

        for (Mesh mesh : model.meshes)
            mesh.model = model;

        return model;
    }

    /**
     * Creates a Model that contains nothing.
     * 
     * @return a model
     */
    public static Model createEmpty() {
        return link(
                createBuilder()
                    .addMesh(
                            Mesh.createMeshBuilder()
                                    .addIndex((short) 0)
                                    .addVertex(Vec3.ZERO)
                                    .build())
                    .build());
    }

    /**
     * Creates a Model with the shape of a finite unit plane.
     * 
     * @param texture the texture of the plane
     * @return a model
     */
    public static Model createPlane(String texture) {
        return link(createBuilder().addTexture(Texture.createBuilder().withAsset(texture).build())
                .addMaterial(
                        Material.createBuilder()
                                .addTextureEntry(
                                        TextureEntry.createBuilder()
                                                .withTextureId(0)
                                                .build())
                                .build())
                .addMesh(
                        Mesh.createMeshBuilder()
                                .withMaterialId(0)
                                .addVertex(new Vec3(-0.5f, -0.5f, 0.0f))
                                .addVertex(new Vec3(0.5f, -0.5f, 0.0f))
                                .addVertex(new Vec3(0.5f, 0.5f, 0.0f))
                                .addVertex(new Vec3(-0.5f, 0.5f, 0.0f))
                                .addIndex((short) 0)
                                .addIndex((short) 1)
                                .addIndex((short) 2)
                                .addIndex((short) 2)
                                .addIndex((short) 3)
                                .addIndex((short) 0)
                                .addTexCoordLayer(
                                        Mesh.createLayerBuilder(Vec2.class)
                                                .addElement(new Vec2(1.0f, 0.0f))
                                                .addElement(new Vec2(0.0f, 0.0f))
                                                .addElement(new Vec2(0.0f, 1.0f))
                                                .addElement(new Vec2(1.0f, 1.0f))
                                                .build())
                                .addNormal(Vec3.UP)
                                .addNormal(Vec3.UP)
                                .addNormal(Vec3.UP)
                                .addNormal(Vec3.UP)
                                .build())
                .build());
    }

    /**
     * A Model.Builder is used to build models.
     */
    public static class Builder
    {
        private String texturePath;
        private final ArrayList<Texture> textures;
        private final ArrayList<Material> materials;
        private final ArrayList<Mesh> meshes;

        private Builder() {
            texturePath = "";
            textures = new ArrayList<>();
            materials = new ArrayList<>();
            meshes = new ArrayList<>();
        }

        /**
         * Constructs the Model from the Model.Builder.
         * 
         * @return the constructed Model
         */
        public Model build() {
            return new Model(
                    textures.toArray(new Texture[textures.size()]), materials.toArray(new Material[materials.size()]),
                    meshes.toArray(new Mesh[meshes.size()]), texturePath);
        }

        /**
         * Sets the Model's texture path.
         * 
         * @param path the texture path
         * @return the builder
         */
        public Builder withTexturePath(String path) {
            texturePath = path;
            return this;
        }

        /**
         * Adds a Texture to the Model.
         * 
         * @param texture the Texture to add
         * @return the builder
         */
        public Builder addTexture(Texture texture) {
            textures.add(texture);
            return this;
        }

        /**
         * Adds a Material to the Model.
         * 
         * @param material the Material to add
         * @return the builder
         */
        public Builder addMaterial(Material material) {
            materials.add(material);
            return this;
        }

        /**
         * Adds a Mesh to the Model.
         * 
         * @param mesh the Mesh to add.
         * @return the builder
         */
        public Builder addMesh(Mesh mesh) {
            meshes.add(mesh);
            return this;
        }
    }
}