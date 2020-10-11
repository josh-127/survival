package net.survival.graphics

import net.survival.graphics.model.ModelRenderer
import net.survival.graphics.model.StaticModel
import net.survival.graphics.opengl.GLMatrixStack
import net.survival.render.ModelType
import org.joml.Matrix4f
import org.joml.Matrix4fStack
import org.joml.Matrix4fc

private const val ASSET_ROOT_PATH = "./assets/"
private const val TEXTURE_PATH = ASSET_ROOT_PATH
private const val FONT_PATH = "textures/fonts/default"
private const val PIXELS_PER_EM = 24

class RenderServer {
    private val projectionMatrix: Matrix4f = Matrix4f()
    private val matrixStack: Matrix4fStack = Matrix4fStack(16)
    private val cloudDisplay: CloudDisplay = CloudDisplay()
    private val skyboxDisplay: SkyboxDisplay = SkyboxDisplay()
    private val textRenderer: TextRenderer = TextRenderer(FONT_PATH)

    fun setProjectionMatrix(matrix: Matrix4fc) {
        projectionMatrix.set(matrix)
    }

    fun pushMatrix(matrix: Matrix4fc) {
        matrixStack.pushMatrix()
        matrixStack.mul(matrix)
    }

    fun popMatrix() {
        matrixStack.popMatrix()
    }

    fun drawModel(type: ModelType) {
        GLMatrixStack.setProjectionMatrix(projectionMatrix)
        GLMatrixStack.push()
        GLMatrixStack.load(matrixStack)
        ModelRenderer.displayStaticModel(StaticModel.fromModelType(type))
        GLMatrixStack.pop()
    }

    fun drawClouds() {
        cloudDisplay.display(matrixStack, projectionMatrix, 0.0f, 0.0f)
    }

    fun drawSkybox(
        br: Float, bg: Float, bb: Float,
        mr: Float, mg: Float, mb: Float,
        tr: Float, tg: Float, tb: Float,
    ) {
        skyboxDisplay.setColor(br, bg, bb, mr, mg, mb, tr, tg, tb)
        skyboxDisplay.display(matrixStack, projectionMatrix)
    }

    fun drawText(text: String, x: Float, z: Float) {
        textRenderer.drawText(text, TextStyle.DEFAULT, x, z, 0.0f)
    }
}
