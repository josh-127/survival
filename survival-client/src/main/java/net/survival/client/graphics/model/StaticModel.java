package net.survival.client.graphics.model;

import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;

import net.survival.actor.Actor;

import static org.lwjgl.assimp.Assimp.*;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.file.Paths;

//
// TODO: Pre-process all of this importing code in the build system.
//       The game should just transfer a file's model data directly to the GPU.
//

public class StaticModel
{
    public static final StaticModel chicken = fromFile("../assets/models/entities/chicken.obj");
    public static final StaticModel cow = fromFile("../assets/models/entities/cow.obj");
    public static final StaticModel goat = fromFile("../assets/models/entities/goat.obj");
    public static final StaticModel human = fromFile("../assets/models/entities/human.obj");
    public static final StaticModel pig = fromFile("../assets/models/entities/pig.obj");
    public static final StaticModel slime = fromFile("../assets/models/entities/slime.obj");

    public final Mesh[] meshes;
    public final String absoluteFilePath;

    private StaticModel(int meshCount, String absoluteFilePath) {
        meshes = new Mesh[meshCount];
        this.absoluteFilePath = absoluteFilePath;
    }

    public static StaticModel fromActor(Actor actor) {
        switch (actor.getModel()) {
        case CHICKEN: return chicken;
        case COW:     return cow;
        case GOAT:    return goat;
        case HUMAN:   return human;
        case PIG:     return pig;
        case SLIME:   return slime;
        }

        throw new RuntimeException();
    }

    public static StaticModel fromFile(String filePath) {
        var file = new File(filePath);
        var absoluteFilePath = file.getAbsolutePath();

        try (var scene = aiImportFile(absoluteFilePath, aiProcess_FlipUVs
                | aiProcess_PreTransformVertices | aiProcess_SortByPType | aiProcess_Triangulate))
        {
            var aiMeshes = getMeshes(scene);
            var aiMaterials = getMaterials(scene);

            var model = new StaticModel(aiMeshes.length, absoluteFilePath);
            for (var i = 0; i < model.meshes.length; ++i) {
                var aiMesh = aiMeshes[i];
                var aiMaterial = aiMaterials[aiMesh.mMaterialIndex()];
                model.meshes[i] = Mesh.fromAssimpMeshAndMaterial(aiMesh, aiMaterial,
                        absoluteFilePath);
            }

            return model;
        }
    }

    private static AIMesh[] getMeshes(AIScene scene) {
        var meshPtrs = scene.mMeshes();
        var meshes = new AIMesh[scene.mNumMeshes()];

        for (var i = 0; i < meshes.length; ++i)
            meshes[i] = AIMesh.create(meshPtrs.get());

        return meshes;
    }

    private static AIMaterial[] getMaterials(AIScene scene) {
        var materialPtrs = scene.mMaterials();
        var materials = new AIMaterial[scene.mNumMaterials()];

        for (var i = 0; i < materials.length; ++i)
            materials[i] = AIMaterial.create(materialPtrs.get());

        return materials;
    }

    public static class Mesh
    {
        public final float[] vertices;
        public final int vertexCount;
        public final String texturePath;

        private Mesh(int vertexCount, String texture) {
            vertices = new float[vertexCount * 8];
            this.vertexCount = vertexCount;
            this.texturePath = texture;
        }

        public static Mesh fromAssimpMeshAndMaterial(AIMesh aiMesh, AIMaterial aiMaterial,
                String modelFilePath)
        {
            var totalVertices = getTotalIndexedVertices(aiMesh);
            var texturePath = getTexturePath(aiMaterial, modelFilePath);
            var mesh = new Mesh(totalVertices, texturePath);
            importVertices(aiMesh, mesh);
            return mesh;
        }

        private static int getTotalIndexedVertices(AIMesh mesh) {
            var total = 0;

            var faces = mesh.mFaces();
            while (faces.hasRemaining()) {
                var face = faces.get();
                total += face.mNumIndices();
            }

            return total;
        }

        private static String getTexturePath(AIMaterial material, String modelFilePath) {
            var aiTexturePath = AIString.calloc();
            aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, aiTexturePath,
                    (IntBuffer) null, null, null, null, null, null);
            var texturePath = aiTexturePath.dataString();
            aiTexturePath.close();
            texturePath = Paths.get(new File(modelFilePath).getParent(), texturePath).toString();
            return texturePath;
        }

        private static void importVertices(AIMesh src, Mesh dest) {
            var destIndex = 0;
            var faces = src.mFaces();

            while (faces.hasRemaining()) {
                var face = faces.get();
                var indices = face.mIndices();

                while (indices.hasRemaining()) {
                    var srcIndex = indices.get();
                    var position = src.mVertices().get(srcIndex);
                    var normal = src.mNormals().get(srcIndex);
                    var texCoord = src.mTextureCoords(0).get(srcIndex);

                    dest.vertices[destIndex++] = position.x();
                    dest.vertices[destIndex++] = position.y();
                    dest.vertices[destIndex++] = position.z();
                    dest.vertices[destIndex++] = normal.x();
                    dest.vertices[destIndex++] = normal.y();
                    dest.vertices[destIndex++] = normal.z();
                    dest.vertices[destIndex++] = texCoord.x();
                    dest.vertices[destIndex++] = texCoord.y();
                }
            }
        }
    }
}