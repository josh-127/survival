package net.survival.actor

import net.survival.block.ColumnPos
import net.survival.util.MathEx
import net.survival.world.World
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sqrt

object Physics {
    private const val GRAVITY = 32.0
    private const val TERMINAL_VELOCITY = 80.0

    fun tick(actor: Actor, world: World, elapsedTime: Double) {
        val x = actor.x
        val z = actor.z
        val columnX = ColumnPos.toColumnX(floor(x).toInt())
        val columnZ = ColumnPos.toColumnZ(floor(z).toInt())

        if (!world.columns.containsKey(ColumnPos.hashPos(columnX, columnZ))) {
            return
        }

        val dirX = actor.directionX
        val dirZ = actor.directionZ
        val sqrLength = MathEx.sqrLength(dirX, dirZ)
        val movementSpeed = actor.movementSpeed

        if (sqrLength > 0.0) {
            val length = sqrt(sqrLength)
            actor.velX = dirX / length * movementSpeed
            actor.velZ = dirZ / length * movementSpeed
        }
        else {
            actor.velX = 0.0
            actor.velZ = 0.0
        }

        applyGravity(actor, elapsedTime)
        applyVelocity(actor, elapsedTime)
        handleBlockCollisions(actor, world)
    }

    // ================================================================
    // Physics Code ===================================================
    // ================================================================
    private fun applyGravity(actor: Actor, elapsedTime: Double) {
        actor.velY -= GRAVITY * elapsedTime
        if (actor.velY < -TERMINAL_VELOCITY) {
            actor.velY = -TERMINAL_VELOCITY
        }
    }

    private fun applyVelocity(actor: Actor, elapsedTime: Double) {
        actor.x += actor.velX * elapsedTime
        actor.y += actor.velY * elapsedTime
        actor.z += actor.velZ * elapsedTime
    }

    private fun handleBlockCollisions(actor: Actor, world: World) {
        if (!handleFloorCollision(actor, world)) {
            handleCeilingCollision(actor, world)
        }
        if (!handleLeftWallCollision(actor, world)) {
            handleRightWallCollision(actor, world)
        }
        if (!handleBackWallCollision(actor, world)) {
            handleFrontWallCollision(actor, world)
        }
    }

    //
    // TODO: Remove code duplication.
    //
    private fun handleFloorCollision(actor: Actor, world: World): Boolean {
        val hitBox = actor.hitBox
        if (actor.velY >= 0.0) {
            return false
        }

        val startX = floor(actor.x - hitBox.radiusX).toInt()
        val endX = floor(actor.x + hitBox.radiusX).toInt()
        val startZ = floor(actor.z - hitBox.radiusZ).toInt()
        val endZ = floor(actor.z + hitBox.radiusZ).toInt()
        val floorY = floor(actor.y - hitBox.radiusY).toInt()
        if (floorY < 0) {
            return false
        }

        for (blockZ in startZ..endZ) {
            for (blockX in startX..endX) {
                val aboveFloorBlock = world.getBlock(blockX, floorY + 1, blockZ)
                if (aboveFloorBlock.solid) {
                    continue
                }

                val floorBlock = world.getBlock(blockX, floorY, blockZ)
                if (floorBlock.solid && getDominantAxis(actor, blockX, floorY, blockZ) == 1 &&
                    intersectsFloorPlane(actor, blockX, floorY, blockZ)
                ) {
                    actor.y = floorY + 1 + hitBox.radiusY
                    actor.velY = 0.0
                    return true
                }
            }
        }

        return false
    }

    private fun handleCeilingCollision(actor: Actor, world: World): Boolean {
        val hitBox = actor.hitBox
        if (actor.velY <= 0.0) {
            return false
        }

        val startX = floor(actor.x - hitBox.radiusX).toInt()
        val endX = floor(actor.x + hitBox.radiusX).toInt()
        val startZ = floor(actor.z - hitBox.radiusZ).toInt()
        val endZ = floor(actor.z + hitBox.radiusZ).toInt()
        val ceilingY = floor(actor.y + hitBox.radiusY).toInt()
        if (ceilingY - 1 < 0) {
            return false
        }

        for (blockZ in startZ..endZ) {
            for (blockX in startX..endX) {
                val belowCeilingBlock = world.getBlock(blockX, ceilingY - 1, blockZ)
                if (belowCeilingBlock.solid) {
                    continue
                }

                val ceilingBlock = world.getBlock(blockX, ceilingY, blockZ)
                if (ceilingBlock.solid && getDominantAxis(actor, blockX, ceilingY, blockZ) == 1 &&
                    intersectsCeilingPlane(actor, blockX, ceilingY, blockZ)
                ) {
                    actor.y = ceilingY - hitBox.radiusY
                    actor.velY = 0.0
                    return true
                }
            }
        }

        return false
    }

    private fun handleLeftWallCollision(actor: Actor, world: World): Boolean {
        val hitBox = actor.hitBox
        val startY = floor(actor.y - hitBox.radiusY).toInt()
        val endY = floor(actor.y + hitBox.radiusY).toInt()
        if (startY < 0) {
            return false
        }

        val startZ = floor(actor.z - hitBox.radiusZ).toInt()
        val endZ = floor(actor.z + hitBox.radiusZ).toInt()
        val wallX = floor(actor.x + hitBox.radiusX).toInt()

        for (blockY in startY..endY) {
            for (blockZ in startZ..endZ) {
                val adjacentBlock = world.getBlock(wallX - 1, blockY, blockZ)
                if (adjacentBlock.solid) {
                    continue
                }

                val wallBlock = world.getBlock(wallX, blockY, blockZ)
                if (wallBlock.solid && getDominantAxis(actor, wallX, blockY, blockZ) == 0 &&
                    intersectsLeftPlane(actor, wallX, blockY, blockZ)
                ) {
                    actor.x = wallX - hitBox.radiusX
                    actor.velX = 0.0
                    return true
                }
            }
        }

        return false
    }

    private fun handleRightWallCollision(actor: Actor, world: World): Boolean {
        val hitBox = actor.hitBox
        val startY = floor(actor.y - hitBox.radiusY).toInt()
        val endY = floor(actor.y + hitBox.radiusY).toInt()
        if (startY < 0) {
            return false
        }

        val startZ = floor(actor.z - hitBox.radiusZ).toInt()
        val endZ = floor(actor.z + hitBox.radiusZ).toInt()
        val wallX = floor(actor.x - hitBox.radiusX).toInt()

        for (blockY in startY..endY) {
            for (blockZ in startZ..endZ) {
                val adjacentBlock = world.getBlock(wallX + 1, blockY, blockZ)
                if (adjacentBlock.solid) {
                    continue
                }

                val wallBlock = world.getBlock(wallX, blockY, blockZ)
                if (wallBlock.solid && getDominantAxis(actor, wallX, blockY, blockZ) == 0 &&
                    intersectsRightPlane(actor, wallX, blockY, blockZ)
                ) {
                    actor.x = wallX + 1 + hitBox.radiusX
                    actor.velX = 0.0
                    return true
                }
            }
        }

        return false
    }

    private fun handleFrontWallCollision(actor: Actor, world: World): Boolean {
        val hitBox = actor.hitBox
        val startY = floor(actor.y - hitBox.radiusY).toInt()
        val endY = floor(actor.y + hitBox.radiusY).toInt()
        if (startY < 0) {
            return false
        }

        val startX = floor(actor.x - hitBox.radiusX).toInt()
        val endX = floor(actor.x + hitBox.radiusX).toInt()
        val wallZ = floor(actor.z - hitBox.radiusZ).toInt()

        for (blockY in startY..endY) {
            for (blockX in startX..endX) {
                val adjacentBlock = world.getBlock(blockX, blockY, wallZ + 1)
                if (adjacentBlock.solid) {
                    continue
                }

                val wallBlock = world.getBlock(blockX, blockY, wallZ)
                if (wallBlock.solid && getDominantAxis(actor, blockX, blockY, wallZ) == 2 &&
                    intersectsFrontPlane(actor, blockX, blockY, wallZ)
                ) {
                    actor.z = wallZ + 1 + hitBox.radiusZ
                    actor.velZ = 0.0
                    return true
                }
            }
        }

        return false
    }

    private fun handleBackWallCollision(actor: Actor, world: World): Boolean {
        val hitBox = actor.hitBox
        val startY = floor(actor.y - hitBox.radiusY).toInt()
        val endY = floor(actor.y + hitBox.radiusY).toInt()
        if (startY < 0) {
            return false
        }

        val startX = floor(actor.x - hitBox.radiusX).toInt()
        val endX = floor(actor.x + hitBox.radiusX).toInt()
        val wallZ = floor(actor.z + hitBox.radiusZ).toInt()

        for (blockY in startY..endY) {
            for (blockX in startX..endX) {
                val adjacentBlock = world.getBlock(blockX, blockY, wallZ - 1)
                if (adjacentBlock.solid) {
                    continue
                }

                val wallBlock = world.getBlock(blockX, blockY, wallZ)
                if (wallBlock.solid && getDominantAxis(actor, blockX, blockY, wallZ) == 2 &&
                    intersectsBackPlane(actor, blockX, blockY, wallZ)
                ) {
                    actor.z = wallZ - hitBox.radiusZ
                    actor.velZ = 0.0
                    return true
                }
            }
        }

        return false
    }

    private fun intersectsFloorPlane(actor: Actor, blockX: Int, blockY: Int, blockZ: Int): Boolean {
        return boxIntersectsYPlane(getBlockCollisionBoxTop(blockY),
            getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
            getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z)
        )
    }

    private fun intersectsCeilingPlane(actor: Actor, blockX: Int, blockY: Int, blockZ: Int): Boolean {
        return boxIntersectsYPlane(getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
            getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z)
        )
    }

    private fun intersectsLeftPlane(actor: Actor, blockX: Int, blockY: Int, blockZ: Int): Boolean {
        return boxIntersectsXPlane(getBlockCollisionBoxLeft(blockX),
            getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z)
        )
    }

    private fun intersectsRightPlane(actor: Actor, blockX: Int, blockY: Int, blockZ: Int): Boolean {
        return boxIntersectsXPlane(getBlockCollisionBoxRight(blockX),
            getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z)
        )
    }

    private fun intersectsFrontPlane(actor: Actor, blockX: Int, blockY: Int, blockZ: Int): Boolean {
        return boxIntersectsZPlane(getBlockCollisionBoxFront(blockZ),
            getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z)
        )
    }

    private fun intersectsBackPlane(actor: Actor, blockX: Int, blockY: Int, blockZ: Int): Boolean {
        return boxIntersectsZPlane(getBlockCollisionBoxBack(blockZ),
            getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z)
        )
    }

    private fun boxIntersectsYPlane(
        planeY: Double, planeLeft: Double, planeRight: Double,
        planeFront: Double, planeBack: Double, boxTop: Double, boxBottom: Double, boxLeft: Double,
        boxRight: Double, boxFront: Double, boxBack: Double
    ): Boolean {
        if (boxBottom >= planeY) return false
        if (boxTop < planeY) return false
        if (boxLeft >= planeRight) return false
        if (boxRight <= planeLeft) return false
        if (boxBack >= planeFront) return false
        return boxFront > planeBack
    }

    private fun boxIntersectsXPlane(
        planeX: Double, planeTop: Double, planeBottom: Double,
        planeFront: Double, planeBack: Double, boxTop: Double, boxBottom: Double, boxLeft: Double,
        boxRight: Double, boxFront: Double, boxBack: Double
    ): Boolean {
        if (boxBottom >= planeTop) return false
        if (boxTop < planeBottom) return false
        if (boxLeft >= planeX) return false
        if (boxRight <= planeX) return false
        if (boxBack >= planeFront) return false
        return boxFront > planeBack
    }

    private fun boxIntersectsZPlane(
        planeZ: Double, planeTop: Double, planeBottom: Double,
        planeLeft: Double, planeRight: Double, boxTop: Double, boxBottom: Double, boxLeft: Double,
        boxRight: Double, boxFront: Double, boxBack: Double
    ): Boolean {
        if (boxBottom >= planeTop) return false
        if (boxTop <= planeBottom) return false
        if (boxLeft >= planeRight) return false
        if (boxRight <= planeLeft) return false
        if (boxBack >= planeZ) return false
        return boxFront >= planeZ
    }

    private fun getBlockCollisionBoxTop(blockY: Int): Double = blockY + 1.0
    private fun getBlockCollisionBoxBottom(blockY: Int): Double = blockY.toDouble()
    private fun getBlockCollisionBoxLeft(blockX: Int): Double = blockX.toDouble()
    private fun getBlockCollisionBoxRight(blockX: Int): Double = blockX + 1.0
    private fun getBlockCollisionBoxFront(blockZ: Int): Double = blockZ + 1.0
    private fun getBlockCollisionBoxBack(blockZ: Int): Double = blockZ.toDouble()

    private fun getBlockCenterX(blockX: Int): Double = blockX + 0.5
    private fun getBlockCenterY(blockY: Int): Double = blockY + 0.5
    private fun getBlockCenterZ(blockZ: Int): Double = blockZ + 0.5

    private fun getDominantAxis(actor: Actor, blockX: Int, blockY: Int, blockZ: Int): Int {
        val distanceY = abs(actor.y - getBlockCenterY(blockY))
        val distanceX = abs(actor.x - getBlockCenterX(blockX))
        val distanceZ = abs(actor.z - getBlockCenterZ(blockZ))
        val maxDistance = max(max(distanceY, distanceX), distanceZ)
        if (maxDistance == distanceX) return 0
        return if (maxDistance == distanceY) 1 else 2
    }
}