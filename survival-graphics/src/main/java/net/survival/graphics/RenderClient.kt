package net.survival.graphics

import net.survival.block.Column
import net.survival.render.ModelType

interface RenderClient {
    fun send(command: RenderCommand)
    fun present()
}

sealed class RenderCommand {
    data class DrawModel(
        val x: Float, val y: Float, val z: Float,
        val yaw: Float, val pitch: Float, val roll: Float,
        val scaleX: Float, val scaleY: Float, val scaleZ: Float,
        val modelType: ModelType
    ): RenderCommand()

    data class SetColumn(
        val columnPos: Long,
        val column: Column?,
        val invalidationPriority: ColumnInvalidationPriority
    ): RenderCommand()

    data class MoveCamera(
        val x: Float, val y: Float, val z: Float
    ): RenderCommand()

    data class OrientCamera(
        val yaw: Float, val pitch: Float, val roll: Float
    ): RenderCommand()

    data class SetCameraParams(
        val fov: Float,
        val width: Float,
        val height: Float,
        val nearClipPlane: Float,
        val farClipPlane: Float
    ): RenderCommand()

    data class SetCloudParams(
        val seed: Long,
        val density: Float,
        val elevation: Float,
        val speedX: Float,
        val speedZ: Float,
        val alpha: Float,
    ): RenderCommand()

    data class SetSkyColor(
        val br: Float, val bg: Float, val bb: Float,
        val mr: Float, val mg: Float, val mb: Float,
        val tr: Float, val tg: Float, val tb: Float,
    ): RenderCommand()

    data class DrawText(
        val text: String, val fontSize: Float, val x: Float, val z: Float
    ): RenderCommand()
}
