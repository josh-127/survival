package net.survival.graphics

import org.joml.Matrix4f
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

internal class PerspectiveCamera {
    var x = 0f
    var y = 0f
    var z = 0f
    var yaw = 0f
    var pitch = 0f
    var fov = 0f
    var width = 0f
    var height = 0f
    var nearClipPlane = 0f
    var farClipPlane = 0f

    fun getViewMatrix(dest: Matrix4f) {
        dest.lookAt(
            x, y, z,
            x + (sin(yaw.toDouble()) * cos(pitch.toDouble())).toFloat(),
            y + sin(pitch.toDouble()).toFloat(),
            z - (cos(yaw.toDouble()) * cos(pitch.toDouble())).toFloat(),
            0.0f, 1.0f, 0.0f
        )
    }

    fun getProjectionMatrix(dest: Matrix4f) {
        dest.perspective(
            fov,
            width / height,
            nearClipPlane,
            farClipPlane
        )
    }

    val directionX: Float get() = (sin(yaw.toDouble()) * cos(pitch.toDouble())).toFloat()
    val directionY: Float get() = sin(pitch.toDouble()).toFloat()
    val directionZ: Float get() = (-(cos(yaw.toDouble()) * cos(pitch.toDouble()))).toFloat()

    val dominantAxis: Int
        get() {
            val lx = abs(directionX)
            val ly = abs(directionY)
            val lz = abs(directionZ)
            val maxValue = max(max(lx, ly), lz)
            if (maxValue == lx) return 0
            return if (maxValue == ly) 1 else 2
        }
}