package net.survival.actor

import net.survival.util.HitBox
import kotlin.math.sqrt

class Actor {
    companion object {
        private const val GRAVITY = 32.0
    }

    var x = 0.0
    var y = 0.0
    var z = 0.0
    var velX = 0.0
    var velY = 0.0
    var velZ = 0.0
    var hitBox = HitBox.DEFAULT
    var directionX = 0.0
    var directionZ = 0.0
    var movementSpeed = 1.0

    fun move(dx: Double, dz: Double) {
        directionX = dx
        directionZ = dz
    }

    fun jump(height: Double) {
        velY = sqrt(2.0 * GRAVITY * height)
    }
}