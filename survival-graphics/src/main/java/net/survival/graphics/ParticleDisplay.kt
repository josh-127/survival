package net.survival.graphics

import net.survival.graphics.opengl.GLImmediateDrawCall
import net.survival.graphics.opengl.GLMatrixStack
import net.survival.graphics.particle.ClientParticleSpace
import net.survival.util.MathEx
import org.joml.Matrix4f

internal class ParticleDisplay(
    private val clientParticleSpace: ClientParticleSpace,
    private val camera: PerspectiveCamera
) {
    private val cameraViewMatrix = Matrix4f()
    private val cameraProjectionMatrix = Matrix4f()

    fun display() {
        cameraViewMatrix.identity()
        camera.getViewMatrix(cameraViewMatrix)
        cameraProjectionMatrix.identity()
        camera.getProjectionMatrix(cameraProjectionMatrix)

        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix)
        GLMatrixStack.push()
        GLMatrixStack.load(cameraViewMatrix)
        displayParticles()
        GLMatrixStack.pop()
    }

    private fun displayParticles() {
        var camUpX = 0.0f
        var camUpY = 1.0f
        var camUpZ = 0.0f
        val camForwardX = camera.directionX
        val camForwardY = camera.directionY
        val camForwardZ = camera.directionZ
        var camRightX = MathEx.crossX(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ)
        var camRightY = MathEx.crossY(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ)
        var camRightZ = MathEx.crossZ(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ)
        val length = MathEx.length(camRightX, camRightY, camRightZ)
        camRightX /= length
        camRightY /= length
        camRightZ /= length
        camUpX *= 0.25f
        camUpY *= 0.25f
        camUpZ *= 0.25f
        camRightX *= 0.25f
        camRightY *= 0.25f
        camRightZ *= 0.25f

        val textureAtlas = Assets.getMipmappedTextureAtlas()
        val textures = textureAtlas.textureObject

        val drawCall = GLImmediateDrawCall.beginTriangles(textures)
        drawCall.color(1.0f, 1.0f, 1.0f)

        for ((texture, particleDomain) in clientParticleSpace.getParticleDomains()) {
            val data = particleDomain.data
            val maxParticles = data.maxParticles

            val u1 = textureAtlas.getTexCoordU1(texture)
            val v1 = textureAtlas.getTexCoordV1(texture)
            val u2 = textureAtlas.getTexCoordU2(texture)
            val v2 = textureAtlas.getTexCoordV2(texture)

            val padding = 0.25f
            val uu1 = u1 + padding * (u2 - u1)
            val vv1 = v1 + padding * (v2 - v1)
            val uu2 = u1 + (1.0f - padding) * (u2 - u1)
            val vv2 = v1 + (1.0f - padding) * (v2 - v1)

            for (i in 0 until maxParticles) {
                val x = data.xs[i].toFloat()
                val y = data.ys[i].toFloat()
                val z = data.zs[i].toFloat()
                displayBillboard(
                    drawCall,
                    x, y, z,
                    uu1, vv1, uu2, vv2,
                    camRightX, camRightY, camRightZ,
                    camUpX, camUpY, camUpZ
                )
            }
        }

        drawCall.end()
    }

    private fun displayBillboard(
        drawCall: GLImmediateDrawCall,
        x: Float, y: Float, z: Float,
        u1: Float, v1: Float, u2: Float, v2: Float,
        camRightX: Float, camRightY: Float, camRightZ: Float,
        camUpX: Float, camUpY: Float, camUpZ: Float
    ) {
        val blX = x - camRightX - camUpX
        val blY = y - camRightY - camUpY
        val blZ = z - camRightZ - camUpZ
        val brX = x + camRightX - camUpX
        val brY = y + camRightY - camUpY
        val brZ = z + camRightZ - camUpZ
        val tlX = x - camRightX + camUpX
        val tlY = y - camRightY + camUpY
        val tlZ = z - camRightZ + camUpZ
        val trX = x + camRightX + camUpX
        val trY = y + camRightY + camUpY
        val trZ = z + camRightZ + camUpZ

        drawCall.texCoord(u1, v1)
        drawCall.vertex(blX, blY, blZ)
        drawCall.texCoord(u2, v1)
        drawCall.vertex(brX, brY, brZ)
        drawCall.texCoord(u2, v2)
        drawCall.vertex(trX, trY, trZ)
        drawCall.texCoord(u2, v2)
        drawCall.vertex(trX, trY, trZ)
        drawCall.texCoord(u1, v2)
        drawCall.vertex(tlX, tlY, tlZ)
        drawCall.texCoord(u1, v1)
        drawCall.vertex(blX, blY, blZ)
    }
}