package net.survival.graphics

import net.survival.graphics.opengl.GLImmediateDrawCall
import net.survival.graphics.opengl.GLMatrixStack
import net.survival.graphics.opengl.GLState
import org.joml.Matrix3f
import org.joml.Matrix4f

internal class SkyboxDisplay {
    companion object {
        const val DEFAULT_BOTTOM_R = 0.8f
        const val DEFAULT_BOTTOM_G = 1.0f
        const val DEFAULT_BOTTOM_B = 1.0f
        const val DEFAULT_MIDDLE_R = 0.8f
        const val DEFAULT_MIDDLE_G = 1.0f
        const val DEFAULT_MIDDLE_B = 1.0f
        const val DEFAULT_TOP_R = 0.25f
        const val DEFAULT_TOP_G = 0.5f
        const val DEFAULT_TOP_B = 1.0f
        private const val MIDDLE_Y = -0.0625f
    }

    var bottomR = DEFAULT_BOTTOM_R; private set
    var bottomG = DEFAULT_BOTTOM_G; private set
    var bottomB = DEFAULT_BOTTOM_B; private set
    var middleR = DEFAULT_MIDDLE_R; private set
    var middleG = DEFAULT_MIDDLE_G; private set
    var middleB = DEFAULT_MIDDLE_B; private set
    var topR = DEFAULT_TOP_R; private set
    var topG = DEFAULT_TOP_G; private set
    var topB = DEFAULT_TOP_B; private set

    private val tempMatrix = Matrix3f()
    private val viewWithoutTranslation = Matrix4f()

    fun setColor(
        br: Float, bg: Float, bb: Float,
        mr: Float, mg: Float, mb: Float,
        tr: Float, tg: Float, tb: Float
    ) {
        bottomR = br
        bottomG = bg
        bottomB = bb
        middleR = mr
        middleG = mg
        middleB = mb
        topR = tr
        topG = tg
        topB = tb
    }

    fun display(view: Matrix4f, projection: Matrix4f?) {
        view.get3x3(tempMatrix)
        viewWithoutTranslation.identity()
        viewWithoutTranslation.set(tempMatrix)

        GLMatrixStack.setProjectionMatrix(projection)
        GLMatrixStack.load(viewWithoutTranslation)
        GLState.pushDepthWriteMask(false)

        GLImmediateDrawCall.beginTriangles(null)
            // Top
            .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
            .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
            .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
            // Bottom
            .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            // Front (Top Half)
            .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            // Front (Bottom Half)
            .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            // Back (Top Half)
            .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
            .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
            .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
            .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            // Back (Bottom Half)
            .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            // Left (Top Half)
            .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
            .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
            .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            // Left (Bottom Half)
            .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            // Right (Top Half)
            .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
            .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
            .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            // Right (Bottom Half)
            .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
            .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
            .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
            .end()

        GLState.popDepthWriteMask()
    }
}