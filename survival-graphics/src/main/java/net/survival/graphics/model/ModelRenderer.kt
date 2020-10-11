package net.survival.graphics.model

import net.survival.graphics.Bitmap
import net.survival.graphics.model.StaticModel.Mesh
import net.survival.graphics.opengl.GLFilterMode
import net.survival.graphics.opengl.GLImmediateDrawCall
import net.survival.graphics.opengl.GLTexture
import net.survival.graphics.opengl.GLWrapMode
import java.util.*

object ModelRenderer {
    // TODO: Need to dispose these resources.
    private val textureCache = HashMap<String, GLTexture>()

    @JvmStatic
    fun displayStaticModel(model: StaticModel) {
        for (i in model.meshes.indices) {
            val mesh = model.meshes[i]
            var texture = textureCache[mesh.texturePath]

            if (texture == null) {
                val bitmap = Bitmap.fromFile(mesh.texturePath)
                texture = GLTexture()
                texture.beginBind()
                    .setMinFilter(GLFilterMode.NEAREST_MIPMAP_NEAREST)
                    .setMagFilter(GLFilterMode.NEAREST)
                    .setWrapS(GLWrapMode.REPEAT)
                    .setWrapT(GLWrapMode.REPEAT)
                    .setMipmapEnabled(true)
                    .setData(bitmap)
                    .endBind()
                textureCache[mesh.texturePath] = texture
            }

            val drawCall = GLImmediateDrawCall.beginTriangles(texture)
            drawCall.color(1.0f, 1.0f, 1.0f)
            makeStaticMeshDrawCall(drawCall, mesh)
            drawCall.end()
        }
    }

    private fun makeStaticMeshDrawCall(drawCall: GLImmediateDrawCall, mesh: Mesh) {
        for (j in 0 until mesh.vertexCount) {
            val baseIndex = j * 8
            val x = mesh.vertices[baseIndex]
            val y = mesh.vertices[baseIndex + 1]
            val z = mesh.vertices[baseIndex + 2]
            val normalX = mesh.vertices[baseIndex + 3]
            val normalY = mesh.vertices[baseIndex + 4]
            val normalZ = mesh.vertices[baseIndex + 5]
            val texCoordU = mesh.vertices[baseIndex + 6]
            val texCoordV = mesh.vertices[baseIndex + 7]
            drawCall.texCoord(texCoordU, texCoordV)
            drawCall.normal(normalX, normalY, normalZ)
            drawCall.vertex(x, y, z)
        }
    }
}