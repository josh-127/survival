package net.survival.actor;

import net.survival.block.Column;
import net.survival.interaction.InteractionContext;
import net.survival.util.HitBox;
import net.survival.util.MathEx;

public class Locomotion
{
    private static final double GRAVITY = 32.0;
    private static final double TERMINAL_VELOCITY = 30.0;

    private double x;
    private double y;
    private double z;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private final HitBox hitBox;

    private double directionX;
    private double directionZ;
    private double movementSpeed = 1.0;

    public Locomotion(double x, double y, double z, HitBox hitBox) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.hitBox = hitBox;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setMovementDirection(double x, double z) {
        directionX = x;
        directionZ = z;
    }

    public void setMovementSpeed(double to) {
        movementSpeed = to;
    }

    public void jump(double height) {
        velocityY = Math.sqrt(2.0 * GRAVITY * height);
    }

    public void tick(Actor actor, InteractionContext ic) {
        if (!ic.isInStagedColumn(x, y, z))
            return;

        var sqrLength = MathEx.sqrLength(directionX, directionZ);

        if (sqrLength > 0.0) {
            var length = Math.sqrt(sqrLength);
            velocityX = (directionX / length) * movementSpeed;
            velocityZ = (directionZ / length) * movementSpeed;
        }
        else {
            velocityX = 0.0;
            velocityZ = 0.0;
        }

        applyGravity(actor, ic);
        applyVelocity(actor, ic);
        handleBlockCollisions(ic);
    }

    // ================================================================
    // Physics Code ===================================================
    // ================================================================

    private void applyGravity(Actor actor, InteractionContext ic) {
        velocityY -= GRAVITY * ic.getElapsedTime();

        if (velocityY < -TERMINAL_VELOCITY)
            velocityY = -TERMINAL_VELOCITY;
    }

    private void applyVelocity(Actor actor, InteractionContext ic) {
        x += velocityX * ic.getElapsedTime();
        y += velocityY * ic.getElapsedTime();
        z += velocityZ * ic.getElapsedTime();
    }

    private void handleBlockCollisions(InteractionContext ic) {
        if (!handleFloorCollision(ic))
            handleCeilingCollision(ic);

        if (!handleLeftWallCollision(ic))
            handleRightWallCollision(ic);

        if (!handleBackWallCollision(ic))
            handleFrontWallCollision(ic);
    }

    //
    // TODO: Remove code duplication.
    //

    private boolean handleFloorCollision(InteractionContext ic) {
        if (velocityY >= 0.0)
            return false;

        var startX = (int) Math.floor(x - hitBox.radiusX);
        var endX = (int) Math.floor(x + hitBox.radiusX);
        var startZ = (int) Math.floor(z - hitBox.radiusZ);
        var endZ = (int) Math.floor(z + hitBox.radiusZ);

        var floorY = (int) Math.floor(y - hitBox.radiusY);

        if (floorY < 0 || floorY + 1 >= Column.YLENGTH)
            return false;

        for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var aboveFloorBlock = ic.getBlock(blockX, floorY + 1, blockZ);

                if (aboveFloorBlock.isSolid())
                    continue;

                var floorBlock = ic.getBlock(blockX, floorY, blockZ);

                if (floorBlock.isSolid()
                        && getDominantAxis(blockX, floorY, blockZ) == 1
                        && intersectsFloorPlane(blockX, floorY, blockZ))
                {
                    y = (floorY + 1) + hitBox.radiusY;
                    velocityY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleCeilingCollision(InteractionContext ic) {
        if (velocityY <= 0.0)
            return false;

        var startX = (int) Math.floor(x - hitBox.radiusX);
        var endX = (int) Math.floor(x + hitBox.radiusX);
        var startZ = (int) Math.floor(z - hitBox.radiusZ);
        var endZ = (int) Math.floor(z + hitBox.radiusZ);

        var ceilingY = (int) Math.floor(y + hitBox.radiusY);

        if (ceilingY - 1 < 0 || ceilingY >= Column.YLENGTH)
            return false;

        for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var belowCeilingBlock = ic.getBlock(blockX, ceilingY - 1, blockZ);

                if (belowCeilingBlock.isSolid())
                    continue;

                var ceilingBlock = ic.getBlock(blockX, ceilingY, blockZ);

                if (ceilingBlock.isSolid()
                        && getDominantAxis(blockX, ceilingY, blockZ) == 1
                        && intersectsCeilingPlane(blockX, ceilingY, blockZ))
                {
                    y = ceilingY - hitBox.radiusY;
                    velocityY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleLeftWallCollision(InteractionContext ic) {
        var startY = (int) Math.floor(y - hitBox.radiusY);
        var endY = (int) Math.floor(y + hitBox.radiusY);

        if (startY < 0 || endY >= Column.YLENGTH)
            return false;

        var startZ = (int) Math.floor(z - hitBox.radiusZ);
        var endZ = (int) Math.floor(z + hitBox.radiusZ);

        var wallX = (int) Math.floor(x + hitBox.radiusX);

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
                var adjacentBlock = ic.getBlock(wallX - 1, blockY, blockZ);

                if (adjacentBlock.isSolid())
                    continue;

                var wallBlock = ic.getBlock(wallX, blockY, blockZ);

                if (wallBlock.isSolid()
                        && getDominantAxis(wallX, blockY, blockZ) == 0
                        && intersectsLeftPlane(wallX, blockY, blockZ))
                {
                    x = wallX - hitBox.radiusX;
                    velocityX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleRightWallCollision(InteractionContext ic) {
        var startY = (int) Math.floor(y - hitBox.radiusY);
        var endY = (int) Math.floor(y + hitBox.radiusY);

        if (startY < 0 || endY >= Column.YLENGTH)
            return false;

        var startZ = (int) Math.floor(z - hitBox.radiusZ);
        var endZ = (int) Math.floor(z + hitBox.radiusZ);

        var wallX = (int) Math.floor(x - hitBox.radiusX);

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockZ = startZ; blockZ <= endZ; ++blockZ) {
                var adjacentBlock = ic.getBlock(wallX + 1, blockY, blockZ);

                if (adjacentBlock.isSolid())
                    continue;

                var wallBlock = ic.getBlock(wallX, blockY, blockZ);

                if (wallBlock.isSolid()
                        && getDominantAxis(wallX, blockY, blockZ) == 0
                        && intersectsRightPlane(wallX, blockY, blockZ))
                {
                    x = (wallX + 1) + hitBox.radiusX;
                    velocityX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleFrontWallCollision(InteractionContext ic) {
        var startY = (int) Math.floor(y - hitBox.radiusY);
        var endY = (int) Math.floor(y + hitBox.radiusY);

        if (startY < 0 || endY >= Column.YLENGTH)
            return false;

        var startX = (int) Math.floor(x - hitBox.radiusX);
        var endX = (int) Math.floor(x + hitBox.radiusX);

        var wallZ = (int) Math.floor(z - hitBox.radiusZ);

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var adjacentBlock = ic.getBlock(blockX, blockY, wallZ + 1);

                if (adjacentBlock.isSolid())
                    continue;

                var wallBlock = ic.getBlock(blockX, blockY, wallZ);

                if (wallBlock.isSolid()
                        && getDominantAxis(blockX, blockY, wallZ) == 2
                        && intersectsFrontPlane(blockX, blockY, wallZ))
                {
                    z = (wallZ + 1) + hitBox.radiusZ;
                    velocityZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleBackWallCollision(InteractionContext ic) {
        var startY = (int) Math.floor(y - hitBox.radiusY);
        var endY = (int) Math.floor(y + hitBox.radiusY);

        if (startY < 0 || endY >= Column.YLENGTH)
            return false;

        var startX = (int) Math.floor(x - hitBox.radiusX);
        var endX = (int) Math.floor(x + hitBox.radiusX);

        var wallZ = (int) Math.floor(z + hitBox.radiusZ);

        for (var blockY = startY; blockY <= endY; ++blockY) {
            for (var blockX = startX; blockX <= endX; ++blockX) {
                var adjacentBlock = ic.getBlock(blockX, blockY, wallZ - 1);

                if (adjacentBlock.isSolid())
                    continue;

                var wallBlock = ic.getBlock(blockX, blockY, wallZ);

                if (wallBlock.isSolid()
                        && getDominantAxis(blockX, blockY, wallZ) == 2
                        && intersectsBackPlane(blockX, blockY, wallZ))
                {
                    z = wallZ - hitBox.radiusZ;
                    velocityZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean intersectsFloorPlane(int blockX, int blockY, int blockZ) {
        return boxIntersectsYPlane(getBlockCollisionBoxTop(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(y), hitBox.getBottom(y),
                hitBox.getLeft(x), hitBox.getRight(x),
                hitBox.getFront(z), hitBox.getBack(z));
    }

    private boolean intersectsCeilingPlane(int blockX, int blockY, int blockZ) {
        return boxIntersectsYPlane(getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(y), hitBox.getBottom(y),
                hitBox.getLeft(x), hitBox.getRight(x),
                hitBox.getFront(z), hitBox.getBack(z));
    }

    private boolean intersectsLeftPlane(int blockX, int blockY, int blockZ) {
        return boxIntersectsXPlane(getBlockCollisionBoxLeft(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(y), hitBox.getBottom(y),
                hitBox.getLeft(x), hitBox.getRight(x),
                hitBox.getFront(z), hitBox.getBack(z));
    }

    private boolean intersectsRightPlane(int blockX, int blockY, int blockZ) {
        return boxIntersectsXPlane(getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(y), hitBox.getBottom(y),
                hitBox.getLeft(x), hitBox.getRight(x),
                hitBox.getFront(z), hitBox.getBack(z));
    }

    private boolean intersectsFrontPlane(int blockX, int blockY, int blockZ) {
        return boxIntersectsZPlane(getBlockCollisionBoxFront(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                hitBox.getTop(y), hitBox.getBottom(y),
                hitBox.getLeft(x), hitBox.getRight(x),
                hitBox.getFront(z), hitBox.getBack(z));
    }

    private boolean intersectsBackPlane(int blockX, int blockY, int blockZ) {
        return boxIntersectsZPlane(getBlockCollisionBoxBack(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                hitBox.getTop(y), hitBox.getBottom(y),
                hitBox.getLeft(x), hitBox.getRight(x),
                hitBox.getFront(z), hitBox.getBack(z));
    }

    private boolean boxIntersectsYPlane(double planeY, double planeLeft, double planeRight,
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

    private boolean boxIntersectsXPlane(double planeX, double planeTop, double planeBottom,
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

    private boolean boxIntersectsZPlane(double planeZ, double planeTop, double planeBottom,
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

    private double getBlockCollisionBoxTop(int blockY) { return blockY + 1.0; }
    private double getBlockCollisionBoxBottom(int blockY) { return blockY; }
    private double getBlockCollisionBoxLeft(int blockX) { return blockX; }
    private double getBlockCollisionBoxRight(int blockX) { return blockX + 1.0; }
    private double getBlockCollisionBoxFront(int blockZ) { return blockZ + 1.0; }
    private double getBlockCollisionBoxBack(int blockZ) { return blockZ; }

    private double getBlockCenterX(int blockX) { return blockX + 0.5; }
    private double getBlockCenterY(int blockY) { return blockY + 0.5; }
    private double getBlockCenterZ(int blockZ) { return blockZ + 0.5; }

    private int getDominantAxis(int blockX, int blockY, int blockZ) {
        var distanceY = Math.abs(y - getBlockCenterY(blockY));
        var distanceX = Math.abs(x - getBlockCenterX(blockX));
        var distanceZ = Math.abs(z - getBlockCenterZ(blockZ));
        var maxDistance = Math.max(Math.max(distanceY, distanceX), distanceZ);

        if (maxDistance == distanceX) return 0;
        if (maxDistance == distanceY) return 1;
        return 2;
    }
}