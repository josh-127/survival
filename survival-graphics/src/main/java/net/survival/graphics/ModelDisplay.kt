package net.survival.graphics

import net.survival.graphics.model.ModelRenderer.displayStaticModel
import net.survival.graphics.model.StaticModel.Companion.fromModelType
import net.survival.graphics.opengl.GLMatrixStack
import org.joml.Matrix4f
import java.util.*

internal class ModelDisplay(
    private val camera: PerspectiveCamera
) {
    private val modelsToDraw = ArrayList<DrawModelCommand>()

    private val cameraViewMatrix = Matrix4f()
    private val cameraProjectionMatrix = Matrix4f()

    fun drawModel(model: DrawModelCommand) {
        modelsToDraw.add(model)
    }

    fun display() {
        cameraViewMatrix.identity()
        camera.getViewMatrix(cameraViewMatrix)
        cameraProjectionMatrix.identity()
        camera.getProjectionMatrix(cameraProjectionMatrix)

        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix)
        GLMatrixStack.push()
        GLMatrixStack.loadIdentity()

        displayActors(cameraViewMatrix)

        GLMatrixStack.pop()
        modelsToDraw.clear()
    }

    private fun displayActors(viewMatrix: Matrix4f) {
        GLMatrixStack.push()
        GLMatrixStack.load(viewMatrix)
        for (pawn in modelsToDraw) {
            displayModel(pawn)
        }
        GLMatrixStack.pop()
    }

    private fun displayModel(model: DrawModelCommand) {
        GLMatrixStack.push()

        GLMatrixStack.translate(
            model.x.toFloat(),
            model.y.toFloat(),
            model.z.toFloat()
        )
        GLMatrixStack.rotate(model.yaw.toFloat(), 0.0f, 1.0f, 0.0f)
        GLMatrixStack.rotate(model.pitch.toFloat(), 1.0f, 0.0f, 0.0f)
        GLMatrixStack.rotate(model.roll.toFloat(), 0.0f, 0.0f, 1.0f)

        val displayable = fromModelType(model.modelType)
        displayStaticModel(displayable)

        GLMatrixStack.pop()
    }
}