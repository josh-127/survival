package net.survival.client

internal class FpvCamera(
    var yaw: Double,
    var pitch: Double
) {
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
}