package net.survival.graphics.model;

import java.util.HashMap;

import net.survival.graphics.Bitmap;
import net.survival.graphics.opengl.GLFilterMode;
import net.survival.graphics.opengl.GLImmediateDrawCall;
import net.survival.graphics.opengl.GLTexture;
import net.survival.graphics.opengl.GLWrapMode;

public class ModelRenderer
{
    // TODO: Need to dispose these resources.
    private static final HashMap<String, GLTexture> textureCache = new HashMap<>();

    private ModelRenderer() {}

    public static void displayStaticModel(StaticModel model) {
        for (var i = 0; i < model.meshes.length; ++i) {
            var mesh = model.meshes[i];

            var texture = textureCache.get(mesh.texturePath);
            if (texture == null) {
                var bitmap = Bitmap.fromFile(mesh.texturePath);
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

            var drawCall = GLImmediateDrawCall.beginTriangles(texture);
            drawCall.color(1.0f, 1.0f, 1.0f);
            makeStaticMeshDrawCall(drawCall, mesh);
            drawCall.end();
        }
    }

    private static void makeStaticMeshDrawCall(GLImmediateDrawCall drawCall,
            StaticModel.Mesh mesh)
    {
        for (var j = 0; j < mesh.vertexCount; ++j) {
            var baseIndex = j * 8;
            var x = mesh.vertices[baseIndex];
            var y = mesh.vertices[baseIndex + 1];
            var z = mesh.vertices[baseIndex + 2];
            var normalX = mesh.vertices[baseIndex + 3];
            var normalY = mesh.vertices[baseIndex + 4];
            var normalZ = mesh.vertices[baseIndex + 5];
            var texCoordU = mesh.vertices[baseIndex + 6];
            var texCoordV = mesh.vertices[baseIndex + 7];

            drawCall.texCoord(texCoordU, texCoordV);
            drawCall.normal(normalX, normalY, normalZ);
            drawCall.vertex(x, y, z);
        }
    }
}