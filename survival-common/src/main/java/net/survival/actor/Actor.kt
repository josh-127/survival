package net.survival.actor

import net.survival.util.HitBox
import java.nio.ByteBuffer
import kotlin.math.sqrt

class Actor(val id: Long) {
    companion object {
        private const val GRAVITY = 32.0
    }

    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    var velX: Double = 0.0
    var velY: Double = 0.0
    var velZ: Double = 0.0
    var hitBox: HitBox = HitBox.DEFAULT
    var directionX: Double = 0.0
    var directionZ: Double = 0.0
    var movementSpeed: Double = 1.0

    fun update(actor: Actor) {
        x = actor.x
        y = actor.y
        z = actor.z
        velX = actor.velX
        velY = actor.velY
        velZ = actor.velZ
        hitBox = actor.hitBox
        directionX = actor.directionX
        directionZ = actor.directionZ
        movementSpeed = actor.movementSpeed
    }

    fun move(dx: Double, dz: Double) {
        directionX = dx
        directionZ = dz
    }

    fun jump(height: Double) {
        velY = sqrt(2.0 * GRAVITY * height)
    }
}

fun ByteBuffer.putActor(actor: Actor) {
    putLong(actor.id)
    putDouble(actor.x)
    putDouble(actor.y)
    putDouble(actor.z)
    putDouble(actor.velX)
    putDouble(actor.velY)
    putDouble(actor.velZ)
    putShort(actor.hitBox.ordinal.toShort())
    putDouble(actor.directionX)
    putDouble(actor.directionZ)
    putDouble(actor.movementSpeed)
}

fun ByteBuffer.getActor(): Actor {
    val id = long
    val actor = Actor(id)
    actor.x = double
    actor.y = double
    actor.z = double
    actor.velX = double
    actor.velY = double
    actor.velZ = double
    actor.hitBox = HitBox.values()[short.toInt()]
    actor.directionX = double
    actor.directionZ = double
    actor.movementSpeed = double
    return actor
}
