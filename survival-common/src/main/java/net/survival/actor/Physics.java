package net.survival.actor;

import net.survival.block.ColumnPos;
import net.survival.util.MathEx;
import net.survival.world.World;

public class Physics {
    private static final double GRAVITY = 32.0;
    private static final double TERMINAL_VELOCITY = 80.0;
    
    private Physics() {}

    public static void dispatch(Actor actor, World world, double elapsedTime) {
        var x = actor.getX();
        var z = actor.getZ();
        var columnX = ColumnPos.toColumnX((int) Math.floor(x));
        var columnZ = ColumnPos.toColumnZ((int) Math.floor(z));

        if (!world.getColumns().containsKey(ColumnPos.hashPos(columnX, columnZ))) {
            return;
        }

        var dirX = actor.getDirectionX();
        var dirZ = actor.getDirectionZ();
        var sqrLength = MathEx.sqrLength(dirX, dirZ);
        
        var movementSpeed = actor.getMovementSpeed();

        if (sqrLength > 0.0) {
            var length = Math.sqrt(sqrLength);
            actor.setVelX((dirX / length) * movementSpeed);
            actor.setVelZ((dirZ / length) * movementSpeed);
        }
        else {
            actor.setVelX(0.0);
            actor.setVelZ(0.0);
        }

        applyGravity(actor, elapsedTime);
        applyVelocity(actor, elapsedTime);
        handleBlockCollisions(actor, world);
    }

    // ================================================================
    // Physics Code ===================================================
    // ================================================================

    private static void applyGravity(Actor actor, double elapsedTime) {
        var velY = actor.getVelY();
        velY -= GRAVITY * elapsedTime;

        if (velY < -TERMINAL_VELOCITY)
            velY = -TERMINAL_VELOCITY;
        
        actor.setVelY(velY);
    }

    private static void applyVelocity(Actor actor, double elapsedTime) {
        var x = actor.getX();
        var y = actor.getY();
        var z = actor.getZ();
        var velX = actor.getVelX();
        var velY = actor.getVelY();
        var velZ = actor.getVelZ();
        
        x += velX * elapsedTime;
        y += velY * elapsedTime;
        z += velZ * elapsedTime;
        
        actor.setX(x);
        actor.setY(y);
        actor.setZ(z);
    }

    private static void handleBlockCollisions(Actor actor, World world) {
        if (!handleFloorCollision(actor, world))
            handleCeilingCollision(actor, world);

        if (!handleLeftWallCollision(actor, world))
            handleRightWallCollision(actor, world);

        if (!handleBackWallCollision(actor, world))
            handleFrontWallCollision(actor, world);
    }

    //
    // TODO: Remove code duplication.
    //

    private static boolean handleFloorCollision(Actor actor, World world) {
        var hitBox = actor.getHitBox();
        if (actor.getVelY() >= 0.0)
            return false;

        var startX = (int) Math.floor(actor.getX() - hitBox.getRadiusX());
        var endX = (int) Math.floor(actor.getX() + hitBox.getRadiusX());
        var startZ = (int) Math.floor(actor.getZ() - hitBox.getRadiusZ());
        var endZ = (int) Math.floor(actor.getZ() + hitBox.getRadiusZ());

        var floorY = (int) Math.floor(actor.getY() - hitBox.getRadiusY());

        if (floorY < 0)
            return false;

        for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var aboveFloorBlock = world.getBlock(blockX, floorY + 1, blockZ);

                if (aboveFloorBlock.solid)
                    continue;

                var floorBlock = world.getBlock(blockX, floorY, blockZ);

                if (floorBlock.solid
                        && getDominantAxis(actor, blockX, floorY, blockZ) == 1
                        && intersectsFloorPlane(actor, blockX, floorY, blockZ))
                {
                    actor.setY((floorY + 1) + hitBox.getRadiusY());
                    actor.setVelY(0.0);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleCeilingCollision(Actor actor, World world) {
        var hitBox = actor.getHitBox();
        if (actor.getVelY() <= 0.0)
            return false;

        var startX = (int) Math.floor(actor.getX() - hitBox.getRadiusX());
        var endX = (int) Math.floor(actor.getX() + hitBox.getRadiusX());
        var startZ = (int) Math.floor(actor.getZ() - hitBox.getRadiusZ());
        var endZ = (int) Math.floor(actor.getZ() + hitBox.getRadiusZ());

        var ceilingY = (int) Math.floor(actor.getY() + hitBox.getRadiusY());

        if (ceilingY - 1 < 0)
            return false;

        for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var belowCeilingBlock = world.getBlock(blockX, ceilingY - 1, blockZ);

                if (belowCeilingBlock.solid)
                    continue;

                var ceilingBlock = world.getBlock(blockX, ceilingY, blockZ);

                if (ceilingBlock.solid
                        && getDominantAxis(actor, blockX, ceilingY, blockZ) == 1
                        && intersectsCeilingPlane(actor, blockX, ceilingY, blockZ))
                {
                    actor.setY(ceilingY - hitBox.getRadiusY());
                    actor.setVelY(0.0);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleLeftWallCollision(Actor actor, World world) {
        var hitBox = actor.getHitBox();
        var startY = (int) Math.floor(actor.getY() - hitBox.getRadiusY());
        var endY = (int) Math.floor(actor.getY() + hitBox.getRadiusY());

        if (startY < 0)
            return false;

        var startZ = (int) Math.floor(actor.getZ() - hitBox.getRadiusZ());
        var endZ = (int) Math.floor(actor.getZ() + hitBox.getRadiusZ());

        var wallX = (int) Math.floor(actor.getX() + hitBox.getRadiusX());

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
                var adjacentBlock = world.getBlock(wallX - 1, blockY, blockZ);

                if (adjacentBlock.solid)
                    continue;

                var wallBlock = world.getBlock(wallX, blockY, blockZ);

                if (wallBlock.solid
                        && getDominantAxis(actor, wallX, blockY, blockZ) == 0
                        && intersectsLeftPlane(actor, wallX, blockY, blockZ))
                {
                    actor.setX(wallX - hitBox.getRadiusX());
                    actor.setVelX(0.0);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleRightWallCollision(Actor actor, World world) {
        var hitBox = actor.getHitBox();
        var startY = (int) Math.floor(actor.getY() - hitBox.getRadiusY());
        var endY = (int) Math.floor(actor.getY() + hitBox.getRadiusY());

        if (startY < 0)
            return false;

        var startZ = (int) Math.floor(actor.getZ() - hitBox.getRadiusZ());
        var endZ = (int) Math.floor(actor.getZ() + hitBox.getRadiusZ());

        var wallX = (int) Math.floor(actor.getX() - hitBox.getRadiusX());

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
                var adjacentBlock = world.getBlock(wallX + 1, blockY, blockZ);

                if (adjacentBlock.solid)
                    continue;

                var wallBlock = world.getBlock(wallX, blockY, blockZ);

                if (wallBlock.solid
                        && getDominantAxis(actor, wallX, blockY, blockZ) == 0
                        && intersectsRightPlane(actor, wallX, blockY, blockZ))
                {
                    actor.setX((wallX + 1) + hitBox.getRadiusX());
                    actor.setVelX(0.0);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleFrontWallCollision(Actor actor, World world) {
        var hitBox = actor.getHitBox();
        var startY = (int) Math.floor(actor.getY() - hitBox.getRadiusY());
        var endY = (int) Math.floor(actor.getY() + hitBox.getRadiusY());

        if (startY < 0)
            return false;

        var startX = (int) Math.floor(actor.getX() - hitBox.getRadiusX());
        var endX = (int) Math.floor(actor.getX() + hitBox.getRadiusX());

        var wallZ = (int) Math.floor(actor.getZ() - hitBox.getRadiusZ());

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var adjacentBlock = world.getBlock(blockX, blockY, wallZ + 1);

                if (adjacentBlock.solid)
                    continue;

                var wallBlock = world.getBlock(blockX, blockY, wallZ);

                if (wallBlock.solid
                        && getDominantAxis(actor, blockX, blockY, wallZ) == 2
                        && intersectsFrontPlane(actor, blockX, blockY, wallZ))
                {
                    actor.setZ((wallZ + 1) + hitBox.getRadiusZ());
                    actor.setVelZ(0.0);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleBackWallCollision(Actor actor, World world) {
        var hitBox = actor.getHitBox();
        var startY = (int) Math.floor(actor.getY() - hitBox.getRadiusY());
        var endY = (int) Math.floor(actor.getY() + hitBox.getRadiusY());

        if (startY < 0)
            return false;

        var startX = (int) Math.floor(actor.getX() - hitBox.getRadiusX());
        var endX = (int) Math.floor(actor.getX() + hitBox.getRadiusX());

        var wallZ = (int) Math.floor(actor.getZ() + hitBox.getRadiusZ());

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var adjacentBlock = world.getBlock(blockX, blockY, wallZ - 1);

                if (adjacentBlock.solid)
                    continue;

                var wallBlock = world.getBlock(blockX, blockY, wallZ);

                if (wallBlock.solid
                        && getDominantAxis(actor, blockX, blockY, wallZ) == 2
                        && intersectsBackPlane(actor, blockX, blockY, wallZ))
                {
                    actor.setZ(wallZ - hitBox.getRadiusZ());
                    actor.setVelZ(0.0);
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
                actor.getHitBox().getTop(actor.getY()), actor.getHitBox().getBottom(actor.getY()),
                actor.getHitBox().getLeft(actor.getX()), actor.getHitBox().getRight(actor.getX()),
                actor.getHitBox().getFront(actor.getZ()), actor.getHitBox().getBack(actor.getZ()));
    }

    private static boolean intersectsCeilingPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsYPlane(getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                actor.getHitBox().getTop(actor.getY()), actor.getHitBox().getBottom(actor.getY()),
                actor.getHitBox().getLeft(actor.getX()), actor.getHitBox().getRight(actor.getX()),
                actor.getHitBox().getFront(actor.getZ()), actor.getHitBox().getBack(actor.getZ()));
    }

    private static boolean intersectsLeftPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsXPlane(getBlockCollisionBoxLeft(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                actor.getHitBox().getTop(actor.getY()), actor.getHitBox().getBottom(actor.getY()),
                actor.getHitBox().getLeft(actor.getX()), actor.getHitBox().getRight(actor.getX()),
                actor.getHitBox().getFront(actor.getZ()), actor.getHitBox().getBack(actor.getZ()));
    }

    private static boolean intersectsRightPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsXPlane(getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                actor.getHitBox().getTop(actor.getY()), actor.getHitBox().getBottom(actor.getY()),
                actor.getHitBox().getLeft(actor.getX()), actor.getHitBox().getRight(actor.getX()),
                actor.getHitBox().getFront(actor.getZ()), actor.getHitBox().getBack(actor.getZ()));
    }

    private static boolean intersectsFrontPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsZPlane(getBlockCollisionBoxFront(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                actor.getHitBox().getTop(actor.getY()), actor.getHitBox().getBottom(actor.getY()),
                actor.getHitBox().getLeft(actor.getX()), actor.getHitBox().getRight(actor.getX()),
                actor.getHitBox().getFront(actor.getZ()), actor.getHitBox().getBack(actor.getZ()));
    }

    private static boolean intersectsBackPlane(Actor actor, int blockX, int blockY, int blockZ) {
        return boxIntersectsZPlane(getBlockCollisionBoxBack(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                actor.getHitBox().getTop(actor.getY()), actor.getHitBox().getBottom(actor.getY()),
                actor.getHitBox().getLeft(actor.getX()), actor.getHitBox().getRight(actor.getX()),
                actor.getHitBox().getFront(actor.getZ()), actor.getHitBox().getBack(actor.getZ()));
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
        var distanceY = Math.abs(actor.getY() - getBlockCenterY(blockY));
        var distanceX = Math.abs(actor.getX() - getBlockCenterX(blockX));
        var distanceZ = Math.abs(actor.getZ() - getBlockCenterZ(blockZ));
        var maxDistance = Math.max(Math.max(distanceY, distanceX), distanceZ);

        if (maxDistance == distanceX) return 0;
        if (maxDistance == distanceY) return 1;
        return 2;
    }
}