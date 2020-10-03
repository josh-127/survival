package net.survival.actor;

import net.survival.block.ColumnPos;
import net.survival.util.MathEx;
import net.survival.world.World;

public class Physics {
    private static final double GRAVITY = 32.0;
    private static final double TERMINAL_VELOCITY = 80.0;
    
    private Physics() {}

    public static void tick(Actor actor, World world, double elapsedTime) {
        var x = actor.x;
        var z = actor.z;
        var columnX = ColumnPos.toColumnX((int) Math.floor(x));
        var columnZ = ColumnPos.toColumnZ((int) Math.floor(z));

        if (!world.getColumns().containsKey(ColumnPos.hashPos(columnX, columnZ))) {
            return;
        }

        var dirX = actor.directionX;
        var dirZ = actor.directionZ;
        var sqrLength = MathEx.sqrLength(dirX, dirZ);
        
        var movementSpeed = actor.movementSpeed;

        if (sqrLength > 0.0) {
            var length = Math.sqrt(sqrLength);
            actor.velX = (dirX / length) * movementSpeed;
            actor.velZ = (dirZ / length) * movementSpeed;
        }
        else {
            actor.velX = 0.0;
            actor.velZ = 0.0;
        }

        applyGravity(actor, elapsedTime);
        applyVelocity(actor, elapsedTime);
        handleBlockCollisions(actor, world);
    }

    // ================================================================
    // Physics Code ===================================================
    // ================================================================

    private static void applyGravity(Actor actor, double elapsedTime) {
        actor.velY -= GRAVITY * elapsedTime;
        if (actor.velY < -TERMINAL_VELOCITY) {
            actor.velY = -TERMINAL_VELOCITY;
        }
    }

    private static void applyVelocity(Actor actor, double elapsedTime) {
        actor.x += actor.velX * elapsedTime;
        actor.y += actor.velY * elapsedTime;
        actor.z += actor.velZ * elapsedTime;
    }

    private static void handleBlockCollisions(Actor actor, World world) {
        if (!handleFloorCollision(actor, world)) {
            handleCeilingCollision(actor, world);
        }
        if (!handleLeftWallCollision(actor, world)) {
            handleRightWallCollision(actor, world);
        }
        if (!handleBackWallCollision(actor, world)) {
            handleFrontWallCollision(actor, world);
        }
    }

    //
    // TODO: Remove code duplication.
    //

    private static boolean handleFloorCollision(Actor actor, World world) {
        var hitBox = actor.hitBox;
        if (actor.velY >= 0.0)
            return false;

        var startX = (int) Math.floor(actor.x - hitBox.getRadiusX());
        var endX = (int) Math.floor(actor.x + hitBox.getRadiusX());
        var startZ = (int) Math.floor(actor.z - hitBox.getRadiusZ());
        var endZ = (int) Math.floor(actor.z + hitBox.getRadiusZ());

        var floorY = (int) Math.floor(actor.y - hitBox.getRadiusY());
        if (floorY < 0) {
            return false;
        }

        for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var aboveFloorBlock = world.getBlock(blockX, floorY + 1, blockZ);
                if (aboveFloorBlock.solid) {
                    continue;
                }

                var floorBlock = world.getBlock(blockX, floorY, blockZ);

                if (floorBlock.solid &&
                    getDominantAxis(actor, blockX, floorY, blockZ) == 1 &&
                    intersectsFloorPlane(actor, blockX, floorY, blockZ)
                ) {
                    actor.y = (floorY + 1) + hitBox.getRadiusY();
                    actor.velY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleCeilingCollision(Actor actor, World world) {
        var hitBox = actor.hitBox;
        if (actor.velY <= 0.0) {
            return false;
        }

        var startX = (int) Math.floor(actor.x - hitBox.getRadiusX());
        var endX = (int) Math.floor(actor.x + hitBox.getRadiusX());
        var startZ = (int) Math.floor(actor.z - hitBox.getRadiusZ());
        var endZ = (int) Math.floor(actor.z + hitBox.getRadiusZ());

        var ceilingY = (int) Math.floor(actor.y + hitBox.getRadiusY());
        if (ceilingY - 1 < 0) {
            return false;
        }

        for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var belowCeilingBlock = world.getBlock(blockX, ceilingY - 1, blockZ);
                if (belowCeilingBlock.solid) {
                    continue;
                }

                var ceilingBlock = world.getBlock(blockX, ceilingY, blockZ);

                if (ceilingBlock.solid &&
                    getDominantAxis(actor, blockX, ceilingY, blockZ) == 1 &&
                    intersectsCeilingPlane(actor, blockX, ceilingY, blockZ)
                ) {
                    actor.y = ceilingY - hitBox.getRadiusY();
                    actor.velY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleLeftWallCollision(Actor actor, World world) {
        var hitBox = actor.hitBox;
        var startY = (int) Math.floor(actor.y - hitBox.getRadiusY());
        var endY = (int) Math.floor(actor.y + hitBox.getRadiusY());

        if (startY < 0) {
            return false;
        }

        var startZ = (int) Math.floor(actor.z - hitBox.getRadiusZ());
        var endZ = (int) Math.floor(actor.z + hitBox.getRadiusZ());

        var wallX = (int) Math.floor(actor.x + hitBox.getRadiusX());

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
                var adjacentBlock = world.getBlock(wallX - 1, blockY, blockZ);
                if (adjacentBlock.solid) {
                    continue;
                }

                var wallBlock = world.getBlock(wallX, blockY, blockZ);

                if (wallBlock.solid &&
                    getDominantAxis(actor, wallX, blockY, blockZ) == 0 &&
                    intersectsLeftPlane(actor, wallX, blockY, blockZ)
                ) {
                    actor.x = wallX - hitBox.getRadiusX();
                    actor.velX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleRightWallCollision(Actor actor, World world) {
        var hitBox = actor.hitBox;
        var startY = (int) Math.floor(actor.y - hitBox.getRadiusY());
        var endY = (int) Math.floor(actor.y + hitBox.getRadiusY());

        if (startY < 0) {
            return false;
        }

        var startZ = (int) Math.floor(actor.z - hitBox.getRadiusZ());
        var endZ = (int) Math.floor(actor.z + hitBox.getRadiusZ());

        var wallX = (int) Math.floor(actor.x - hitBox.getRadiusX());

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
                var adjacentBlock = world.getBlock(wallX + 1, blockY, blockZ);
                if (adjacentBlock.solid) {
                    continue;
                }

                var wallBlock = world.getBlock(wallX, blockY, blockZ);

                if (wallBlock.solid &&
                    getDominantAxis(actor, wallX, blockY, blockZ) == 0 &&
                    intersectsRightPlane(actor, wallX, blockY, blockZ)
                ) {
                    actor.x = (wallX + 1) + hitBox.getRadiusX();
                    actor.velX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleFrontWallCollision(Actor actor, World world) {
        var hitBox = actor.hitBox;
        var startY = (int) Math.floor(actor.y - hitBox.getRadiusY());
        var endY = (int) Math.floor(actor.y + hitBox.getRadiusY());

        if (startY < 0) {
            return false;
        }

        var startX = (int) Math.floor(actor.x - hitBox.getRadiusX());
        var endX = (int) Math.floor(actor.x + hitBox.getRadiusX());

        var wallZ = (int) Math.floor(actor.z - hitBox.getRadiusZ());

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var adjacentBlock = world.getBlock(blockX, blockY, wallZ + 1);
                if (adjacentBlock.solid) {
                    continue;
                }

                var wallBlock = world.getBlock(blockX, blockY, wallZ);

                if (wallBlock.solid &&
                    getDominantAxis(actor, blockX, blockY, wallZ) == 2 &&
                    intersectsFrontPlane(actor, blockX, blockY, wallZ)
                ) {
                    actor.z = (wallZ + 1) + hitBox.getRadiusZ();
                    actor.velZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleBackWallCollision(Actor actor, World world) {
        var hitBox = actor.hitBox;
        var startY = (int) Math.floor(actor.y - hitBox.getRadiusY());
        var endY = (int) Math.floor(actor.y + hitBox.getRadiusY());

        if (startY < 0) {
            return false;
        }

        var startX = (int) Math.floor(actor.x - hitBox.getRadiusX());
        var endX = (int) Math.floor(actor.x + hitBox.getRadiusX());

        var wallZ = (int) Math.floor(actor.z + hitBox.getRadiusZ());

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var adjacentBlock = world.getBlock(blockX, blockY, wallZ - 1);
                if (adjacentBlock.solid) {
                    continue;
                }

                var wallBlock = world.getBlock(blockX, blockY, wallZ);

                if (wallBlock.solid &&
                    getDominantAxis(actor, blockX, blockY, wallZ) == 2 &&
                    intersectsBackPlane(actor, blockX, blockY, wallZ)
                ) {
                    actor.z = wallZ - hitBox.getRadiusZ();
                    actor.velZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean intersectsFloorPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsYPlane(getBlockCollisionBoxTop(blockY),
            getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
            getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z));
    }

    private static boolean intersectsCeilingPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsYPlane(getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
            getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z));
    }

    private static boolean intersectsLeftPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsXPlane(getBlockCollisionBoxLeft(blockX),
            getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z));
    }

    private static boolean intersectsRightPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsXPlane(getBlockCollisionBoxRight(blockX),
            getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z));
    }

    private static boolean intersectsFrontPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsZPlane(getBlockCollisionBoxFront(blockZ),
            getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z));
    }

    private static boolean intersectsBackPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsZPlane(getBlockCollisionBoxBack(blockZ),
            getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
            getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
            actor.hitBox.getTop(actor.y), actor.hitBox.getBottom(actor.y),
            actor.hitBox.getLeft(actor.x), actor.hitBox.getRight(actor.x),
            actor.hitBox.getFront(actor.z), actor.hitBox.getBack(actor.z));
    }

    private static boolean boxIntersectsYPlane(double planeY, double planeLeft, double planeRight,
            double planeFront, double planeBack, double boxTop, double boxBottom, double boxLeft,
            double boxRight, double boxFront, double boxBack)
    {
        if (boxBottom >= planeY) return false;
        if (boxTop < planeY) return false;
        if (boxLeft >= planeRight) return false;
        if (boxRight <= planeLeft) return false;
        if (boxBack >= planeFront) return false;
        if (boxFront <= planeBack) return false;
        return true;
    }

    private static boolean boxIntersectsXPlane(double planeX, double planeTop, double planeBottom,
            double planeFront, double planeBack, double boxTop, double boxBottom, double boxLeft,
            double boxRight, double boxFront, double boxBack)
    {
        if (boxBottom >= planeTop) return false;
        if (boxTop < planeBottom) return false;
        if (boxLeft >= planeX) return false;
        if (boxRight <= planeX) return false;
        if (boxBack >= planeFront) return false;
        if (boxFront <= planeBack) return false;
        return true;
    }

    private static boolean boxIntersectsZPlane(double planeZ, double planeTop, double planeBottom,
            double planeLeft, double planeRight, double boxTop, double boxBottom, double boxLeft,
            double boxRight, double boxFront, double boxBack)
    {
        if (boxBottom >= planeTop) return false;
        if (boxTop <= planeBottom) return false;
        if (boxLeft >= planeRight) return false;
        if (boxRight <= planeLeft) return false;
        if (boxBack >= planeZ) return false;
        if (boxFront < planeZ) return false;
        return true;
    }

    private static double getBlockCollisionBoxTop(int blockY) { return blockY + 1.0; }
    private static double getBlockCollisionBoxBottom(int blockY) { return blockY; }
    private static double getBlockCollisionBoxLeft(int blockX) { return blockX; }
    private static double getBlockCollisionBoxRight(int blockX) { return blockX + 1.0; }
    private static double getBlockCollisionBoxFront(int blockZ) { return blockZ + 1.0; }
    private static double getBlockCollisionBoxBack(int blockZ) { return blockZ; }

    private static double getBlockCenterX(int blockX) { return blockX + 0.5; }
    private static double getBlockCenterY(int blockY) { return blockY + 0.5; }
    private static double getBlockCenterZ(int blockZ) { return blockZ + 0.5; }

    private static int getDominantAxis(Actor actor, int blockX, int blockY, int blockZ) {
        var distanceY = Math.abs(actor.y - getBlockCenterY(blockY));
        var distanceX = Math.abs(actor.x - getBlockCenterX(blockX));
        var distanceZ = Math.abs(actor.z - getBlockCenterZ(blockZ));
        var maxDistance = Math.max(Math.max(distanceY, distanceX), distanceZ);

        if (maxDistance == distanceX) return 0;
        if (maxDistance == distanceY) return 1;
        return 2;
    }
}