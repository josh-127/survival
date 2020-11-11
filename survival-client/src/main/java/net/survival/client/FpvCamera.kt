package net.survival.client

import org.joml.Matrix4f
import kotlin.math.cos
import kotlin.math.sin

internal class FpvCamera(
    var x: Double,
    var y: Double,
    var z: Double,
    var yaw: Double,
    var pitch: Double,
    var fov: Double,
    var width: Double,
    var height: Double,
    var nearClipPlane: Double,
    var farClipPlane: Double,
) {
    fun moveTo(newX: Double, newY: Double, newZ: Double) {
        x = newX
        y = newY
        z = newZ
    }

    fun rotate(dYaw: Double, dPitch: Double) {
        yaw -= dYaw
        pitch += dPitch

        if (yaw < 0.0) {
            yaw += Math.PI * 2.0
        }
        else if (yaw >= Math.PI * 2.0) {
            yaw -= Math.PI * 2.0
        }

        if (pitch < -Math.PI / 2.03125) {
            pitch = -Math.PI / 2.03125
        }
        else if (pitch > Math.PI / 2.03125) {
            pitch = Math.PI / 2.03125
        }
    }

    fun getViewMatrix(): Matrix4f = Matrix4f().apply { getViewMatrix(this) }

    fun getViewMatrix(dest: Matrix4f) {
        dest.lookAt(
            x.toFloat(), y.toFloat(), z.toFloat(),
            (x + (sin(yaw) * cos(pitch)).toFloat()).toFloat(),
            (y + sin(pitch).toFloat()).toFloat(),
            (z - (cos(yaw) * cos(pitch)).toFloat()).toFloat(),
            0.0f, 1.0f, 0.0f
        )
    }

    fun getProjectionMatrix(): Matrix4f = Matrix4f().apply { getProjectionMatrix(this) }

    fun getProjectionMatrix(dest: Matrix4f) {
        dest.perspective(
            fov.toFloat(),
            (width / height).toFloat(),
            nearClipPlane.toFloat(),
            farClipPlane.toFloat()
        )
    }

    val directionX: Float get() = (sin(yaw) * cos(pitch)).toFloat()
    val directionY: Float get() = sin(pitch).toFloat()
    val directionZ: Float get() = (-(cos(yaw) * cos(pitch))).toFloat()
}