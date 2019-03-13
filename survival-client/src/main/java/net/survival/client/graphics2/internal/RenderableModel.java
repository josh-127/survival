package net.survival.client.graphics2.internal;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;

import net.survival.client.graphics2.Model;
import net.survival.client.util.ArrayIterators;
import net.survival.client.util.Vec2;
import net.survival.client.util.Vec3;

/**
 * A RenderableModel is a model that can be rendered.
 */
public class RenderableModel
{
    private final Mesh[] meshes;

    private RenderableModel(int meshCount) {
        this.meshes = new Mesh[meshCount];
    }

    /**
     * Iterates through the model's meshes.
     * 
     * @return an iterator
     */
    public Iterable<Mesh> iterateMeshes() {
        return ArrayIterators.iterate(meshes);
    }

    /**
     * Converts a Model to a RenderableModel.
     * 
     * @param loader  the RawAssetLoader used to create the RenderableModel
     * @param model   the model to convert
     * @param factory the GraphicsResourceFactory used to create the RenderableModel
     * @return the converted RenderableModel
     */
    public static RenderableModel load(File file, Model model, GraphicsResourceFactory factory) {
        RenderableModel renderableModel = new RenderableModel(model.getMeshCount());
        int meshCounter = 0;

        for (final Model.Mesh modelMesh : model.iterateMeshes()) {
            Model.Material modelMaterial = modelMesh.getMaterial();
            Model.Texture modelTexture = modelMaterial.hasTextureEntries()
                    ? modelMaterial.iterateTextureEntries().iterator().next().getTexture()
                    : null;

            Mesh mesh = new Mesh(
                    Material.fromModelMaterialType(modelMesh.getMaterial().type), modelMesh.getVertexCount(),
                    modelMesh.getIndexCount(),
                    modelTexture != null
                            ? TextureUtil.load(Path.of(model.getTexturePath(), modelTexture.asset).toFile(), factory)
                            : TextureUtil.createEmpty(factory),
                    factory.createVertexBuffer(modelMesh.getVertexCount(), 44),
                    factory.createIndexBuffer(modelMesh.getIndexCount()));

            short[] indices = new short[mesh.indexCount];
            int indexCounter = 0;

            for (short index : modelMesh.iterateIndices())
                indices[indexCounter++] = index;

            mesh.indexBuffer.writeIndices(indices);

            mesh.vertexBuffer.writeVertices(new WritableGraphicsData() {
                @Override
                public void writeGraphicsData(GraphicsDataOutputStream out) {
                    Iterator<Vec3> vertices = modelMesh.iterateVertices().iterator();
                    Iterator<Vec3> colors = modelMesh.hasColorLayers()
                            ? modelMesh.iterateColorLayers().iterator().next()
                            : null;
                    Iterator<Vec2> texCoords = modelMesh.hasTexCoordLayers()
                            ? modelMesh.iterateTexCoordLayers().iterator().next()
                            : null;
                    Iterator<Vec3> normals = modelMesh.hasNormals() ? modelMesh.iterateNormals().iterator() : null;

                    while (vertices.hasNext()) {
                        out.writeVec3(vertices.next());
                        out.writeVec2(texCoords != null ? texCoords.next() : Vec2.ZERO);
                        out.writeVec3(normals != null ? normals.next() : Vec3.ZERO);
                        out.writeVec3(colors != null ? colors.next() : Vec3.ONE);
                    }
                }
            });

            renderableModel.meshes[meshCounter++] = mesh;
        }

        return renderableModel;
    }

    /**
     * Converts a Model animation to a RenderableModel animation.
     * 
     * @param loader    the RawAssetLoader used to create the RenderableModel
     *                  animation
     * @param animation the Model animation to convert
     * @param factory   the GraphicsResource used to create the RenderableModel
     *                  animation
     * @return the frames of the RenderableModel animation
     */
    public static RenderableModel[] loadFrameAnimation(File file, Model[] animation, GraphicsResourceFactory factory) {
        RenderableModel[] frames = new RenderableModel[animation.length];

        for (int i = 0; i < frames.length; i++)
            frames[i] = load(file, animation[i], factory);

        return frames;
    }

    /**
     * Creates a color target model.
     * 
     * @param factory the GraphicsResourceFactory used to create the color target
     *                model.
     * @return a color target model
     */
    public static RenderableModel createColorTargetModel(GraphicsResourceFactory factory) {
        RenderableModel model = new RenderableModel(1);

        model.meshes[0] = new Mesh(
                Material.SHADELESS, 6, 6, TextureUtil.createEmpty(factory), factory.createVertexBuffer(6, 44),
                factory.createIndexBuffer(6));

        model.meshes[0].vertexBuffer.writeVertices(new WritableGraphicsData() {
            @Override
            public void writeGraphicsData(GraphicsDataOutputStream out) {
                out.writeVec3(new Vec3(-1.0f, -1.0f, 0.0f));
                out.writeVec2(new Vec2(0.0f, 1.0f));
                out.writeVec3(Vec3.ZERO);
                out.writeVec3(Vec3.ONE);

                out.writeVec3(new Vec3(1.0f, -1.0f, 0.0f));
                out.writeVec2(new Vec2(1.0f, 1.0f));
                out.writeVec3(Vec3.ZERO);
                out.writeVec3(Vec3.ONE);

                out.writeVec3(new Vec3(1.0f, 1.0f, 0.0f));
                out.writeVec2(new Vec2(1.0f, 0.0f));
                out.writeVec3(Vec3.ZERO);
                out.writeVec3(Vec3.ONE);

                out.writeVec3(new Vec3(1.0f, 1.0f, 0.0f));
                out.writeVec2(new Vec2(1.0f, 0.0f));
                out.writeVec3(Vec3.ZERO);
                out.writeVec3(Vec3.ONE);

                out.writeVec3(new Vec3(-1.0f, 1.0f, 0.0f));
                out.writeVec2(new Vec2(0.0f, 0.0f));
                out.writeVec3(Vec3.ZERO);
                out.writeVec3(Vec3.ONE);

                out.writeVec3(new Vec3(-1.0f, -1.0f, 0.0f));
                out.writeVec2(new Vec2(0.0f, 1.0f));
                out.writeVec3(Vec3.ZERO);
                out.writeVec3(Vec3.ONE);
            }
        });

        model.meshes[0].indexBuffer.writeIndices(new short[] { 0, 1, 2, 3, 4, 5 });

        return model;
    }

    /**
     * Defines the types of materials.
     */
    public static enum Material
    {
        SHADELESS, SHADED;

        /**
         * Converts a Model material to a RenderableModel material.
         * 
         * @param type the type of Model material
         * @return the converted material
         */
        public static Material fromModelMaterialType(Model.Material.Type type) {
            switch (type) {
            case SHADELESS:
                return SHADELESS;
            case SHADED:
                return SHADED;
            default:
                throw new IllegalStateException("unreachable code");
            }
        }
    }

    /**
     * Represents a renderable mesh.
     */
    public static class Mesh
    {
        public final Material material;
        public final int vertexCount;
        public final int indexCount;
        public final Texture texture;
        public final VertexBuffer vertexBuffer;
        public final IndexBuffer indexBuffer;

        private Mesh(
                Material material,
                int vertexCount,
                int indexCount,
                Texture texture,
                VertexBuffer vertexBuffer,
                IndexBuffer indexBuffer)
        {
            this.material = material;
            this.vertexCount = vertexCount;
            this.indexCount = indexCount;
            this.texture = texture;
            this.vertexBuffer = vertexBuffer;
            this.indexBuffer = indexBuffer;
        }
    }
}