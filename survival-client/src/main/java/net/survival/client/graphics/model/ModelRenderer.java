package net.survival.client.graphics.model;

import java.util.HashMap;

import net.survival.client.graphics.Bitmap;
import net.survival.client.graphics.opengl.GLFilterMode;
import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLTexture;
import net.survival.client.graphics.opengl.GLWrapMode;

public class ModelRenderer
{
    // TODO: Need to dispose these resources.
    private static final HashMap<String, GLTexture> textureCache = new HashMap<>();

    private ModelRenderer() {}

    public static void displayStaticModel(StaticModel model) {
        for (int i = 0; i < model.meshes.length; ++i) {
            StaticModel.Mesh mesh = model.meshes[i];

            GLTexture texture = textureCache.get(mesh.texturePath);
            if (texture == null) {
                Bitmap bitmap = Bitmap.fromFile(mesh.texturePath);
                texture = new GLTexture();
                texture.beginBind()
                        .setMinFilter(GLFilterMode.NEAREST_MIPMAP_NEAREST)
                        .setMagFilter(GLFilterMode.NEAREST)
                        .setWrapS(GLWrapMode.REPEAT)
                        .setWrapT(GLWrapMode.REPEAT)
                        .setMipmapEnabled(true)
                        .setData(bitmap)
                        .endBind();
                textureCache.put(mesh.texturePath, texture);
            }

            GLImmediateDrawCall drawCall = GLImmediateDrawCall.beginTriangles(texture);
            drawCall.color(1.0f, 1.0f, 1.0f);
            makeStaticMeshDrawCall(drawCall, mesh);
            drawCall.end();
        }
    }

    private static void makeStaticMeshDrawCall(GLImmediateDrawCall drawCall,
            StaticModel.Mesh mesh)
    {
        for (int j = 0; j < mesh.vertexCount; ++j) {
            int baseIndex = j * 8;
            float x = mesh.vertices[baseIndex];
            float y = mesh.vertices[baseIndex + 1];
            float z = mesh.vertices[baseIndex + 2];
            float normalX = mesh.vertices[baseIndex + 3];
            float normalY = mesh.vertices[baseIndex + 4];
            float normalZ = mesh.vertices[baseIndex + 5];
            float texCoordU = mesh.vertices[baseIndex + 6];
            float texCoordV = mesh.vertices[baseIndex + 7];

            drawCall.texCoord(texCoordU, texCoordV);
            drawCall.normal(normalX, normalY, normalZ);
            drawCall.vertex(x, y, z);
        }
    }
}