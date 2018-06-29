package net.survival.client.graphics.model;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;

import net.survival.entity.Cow;
import net.survival.entity.Entity;

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
    public static final StaticModel cow = fromFile("../assets/models/entities/cow.obj");
    
    public final Mesh[] meshes;
    public final String absoluteFilePath;
    
    private StaticModel(int meshCount, String absoluteFilePath) {
        meshes = new Mesh[meshCount];
        this.absoluteFilePath = absoluteFilePath;
    }
    
    public static StaticModel fromEntity(Entity entity) {
        if (entity instanceof Cow)
            return cow;
        
        return null;
    }
    
    public static StaticModel fromFile(String filePath) {
        File file = new File(filePath);
        String absoluteFilePath = file.getAbsolutePath();

        try (AIScene scene = aiImportFile(absoluteFilePath,
                aiProcess_FlipUVs | aiProcess_PreTransformVertices | aiProcess_SortByPType | aiProcess_Triangulate))
        {
            AIMesh[] aiMeshes = getMeshes(scene);
            AIMaterial[] aiMaterials = getMaterials(scene);
            
            StaticModel model = new StaticModel(aiMeshes.length, absoluteFilePath);
            for (int i = 0; i < model.meshes.length; ++i) {
                AIMesh aiMesh = aiMeshes[i];
                AIMaterial aiMaterial = aiMaterials[aiMesh.mMaterialIndex()];
                model.meshes[i] = Mesh.fromAssimpMeshAndMaterial(aiMesh, aiMaterial, absoluteFilePath);
            }

            return model;
        }
    }
    
    private static AIMesh[] getMeshes(AIScene scene) {
        PointerBuffer meshPtrs = scene.mMeshes();
        AIMesh[] meshes = new AIMesh[scene.mNumMeshes()];
        
        for (int i = 0; i < meshes.length; ++i)
            meshes[i] = AIMesh.create(meshPtrs.get());
        
        return meshes;
    }
    
    private static AIMaterial[] getMaterials(AIScene scene) {
        PointerBuffer materialPtrs = scene.mMaterials();
        AIMaterial[] materials = new AIMaterial[scene.mNumMaterials()];
        
        for (int i = 0; i < materials.length; ++i)
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
        
        public static Mesh fromAssimpMeshAndMaterial(AIMesh aiMesh, AIMaterial aiMaterial, String modelFilePath) {
            int totalVertices = getTotalIndexedVertices(aiMesh);
            String texturePath = getTexturePath(aiMaterial, modelFilePath);
            Mesh mesh = new Mesh(totalVertices, texturePath);
            importVertices(aiMesh, mesh);
            return mesh;
        }
        
        private static int getTotalIndexedVertices(AIMesh mesh) {
            int total = 0;
            
            AIFace.Buffer faces = mesh.mFaces();
            while (faces.hasRemaining()) {
                AIFace face = faces.get();
                total += face.mNumIndices();
            }
            
            return total;
        }
        
        private static String getTexturePath(AIMaterial material, String modelFilePath) {
            AIString aiTexturePath = AIString.calloc();
            aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, aiTexturePath, (IntBuffer) null, null, null, null,
                    null, null);
            String texturePath = aiTexturePath.dataString();
            aiTexturePath.close();
            texturePath = Paths.get(new File(modelFilePath).getParent(), texturePath).toString();
            return texturePath;
        }
        
        private static void importVertices(AIMesh src, Mesh dest) {
            int destIndex = 0;
            AIFace.Buffer faces = src.mFaces();
            
            while (faces.hasRemaining()) {
                AIFace face = faces.get();
                IntBuffer indices = face.mIndices();
                
                while (indices.hasRemaining()) {
                    int srcIndex = indices.get();
                    AIVector3D position = src.mVertices().get(srcIndex);
                    AIVector3D normal   = src.mNormals().get(srcIndex);
                    AIVector3D texCoord = src.mTextureCoords(0).get(srcIndex);
                    
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