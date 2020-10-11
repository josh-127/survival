package net.survival.graphics

import net.survival.block.Column
import net.survival.render.ModelType
import org.joml.Matrix4fc

interface RenderClient {
    fun send(command: RenderCommand)
    fun present()
}

sealed class RenderCommand {
    data class SetProjectionMatrix(val matrix: Matrix4fc): RenderCommand()
    data class PushMatrix(val matrix: Matrix4fc): RenderCommand()
    object PopMatrix: RenderCommand()

    data class DrawModel(val modelType: ModelType): RenderCommand()

    data class SetColumn(
        val columnPos: Long,
        val column: Column?,
        val invalidationPriority: ColumnInvalidationPriority
    ): RenderCommand()

    object DrawClouds: RenderCommand()

    data class DrawSkybox(
        val br: Float, val bg: Float, val bb: Float,
        val mr: Float, val mg: Float, val mb: Float,
        val tr: Float, val tg: Float, val tb: Float,
    ): RenderCommand()

    data class DrawText(
        val text: String, val fontSize: Float, val x: Float, val z: Float
    ): RenderCommand()
}
