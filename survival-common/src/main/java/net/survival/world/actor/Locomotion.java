package net.survival.world.actor;

import net.survival.block.BlockState;
import net.survival.util.HitBox;
import net.survival.util.MathEx;
import net.survival.world.actor.interaction.InteractionContext;
import net.survival.world.column.Column;

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

    public void tick(Actor actor, InteractionContext ic) {
        double sqrSpeed = MathEx.sqrMagnitude(directionX, directionZ);

        if (sqrSpeed > 0.0) {
            double speed = Math.sqrt(sqrSpeed);
            velocityX = directionX / speed;
            velocityZ = directionZ / speed;
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

        int startX = (int) Math.floor(x - hitBox.radiusX);
        int endX = (int) Math.floor(x + hitBox.radiusX);
        int startZ = (int) Math.floor(z - hitBox.radiusZ);
        int endZ = (int) Math.floor(z + hitBox.radiusZ);

        int floorY = (int) Math.floor(y - hitBox.radiusY);

        if (floorY < 0 || floorY + 1 >= Column.YLENGTH)
            return false;

        for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                BlockState aboveFloorBlock = ic.getBlock(blockX, floorY + 1, blockZ);

                if (aboveFloorBlock.isSolid())
                    continue;

                BlockState floorBlock = ic.getBlock(blockX, floorY, blockZ);

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

        int startX = (int) Math.floor(x - hitBox.radiusX);
        int endX = (int) Math.floor(x + hitBox.radiusX);
        int startZ = (int) Math.floor(z - hitBox.radiusZ);
        int endZ = (int) Math.floor(z + hitBox.radiusZ);

        int ceilingY = (int) Math.floor(y + hitBox.radiusY);

        if (ceilingY - 1 < 0 || ceilingY >= Column.YLENGTH)
            return false;

        for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                BlockState belowCeilingBlock = ic.getBlock(blockX, ceilingY - 1, blockZ);

                if (belowCeilingBlock.isSolid())
                    continue;

                BlockState ceilingBlock = ic.getBlock(blockX, ceilingY, blockZ);

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
        int startY = (int) Math.floor(y - hitBox.radiusY);
        int endY = (int) Math.floor(y + hitBox.radiusY);

        if (startY < 0 || endY >= Column.YLENGTH)
            return false;

        int startZ = (int) Math.floor(z - hitBox.radiusZ);
        int endZ = (int) Math.floor(z + hitBox.radiusZ);

        int wallX = (int) Math.floor(x + hitBox.radiusX);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                BlockState adjacentBlock = ic.getBlock(wallX - 1, blockY, blockZ);

                if (adjacentBlock.isSolid())
                    continue;

                BlockState wallBlock = ic.getBlock(wallX, blockY, blockZ);

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
        int startY = (int) Math.floor(y - hitBox.radiusY);
        int endY = (int) Math.floor(y + hitBox.radiusY);

        if (startY < 0 || endY >= Column.YLENGTH)
            return false;

        int startZ = (int) Math.floor(z - hitBox.radiusZ);
        int endZ = (int) Math.floor(z + hitBox.radiusZ);

        int wallX = (int) Math.floor(x - hitBox.radiusX);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                BlockState adjacentBlock = ic.getBlock(wallX + 1, blockY, blockZ);

                if (adjacentBlock.isSolid())
                    continue;

                BlockState wallBlock = ic.getBlock(wallX, blockY, blockZ);

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
        int startY = (int) Math.floor(y - hitBox.radiusY);
        int endY = (int) Math.floor(y + hitBox.radiusY);

        if (startY < 0 || endY >= Column.YLENGTH)
            return false;

        int startX = (int) Math.floor(x - hitBox.radiusX);
        int endX = (int) Math.floor(x + hitBox.radiusX);

        int wallZ = (int) Math.floor(z - hitBox.radiusZ);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                BlockState adjacentBlock = ic.getBlock(blockX, blockY, wallZ + 1);

                if (adjacentBlock.isSolid())
                    continue;

                BlockState wallBlock = ic.getBlock(blockX, blockY, wallZ);

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
        int startY = (int) Math.floor(y - hitBox.radiusY);
        int endY = (int) Math.floor(y + hitBox.radiusY);

        if (startY < 0 || endY >= Column.YLENGTH)
            return false;

        int startX = (int) Math.floor(x - hitBox.radiusX);
        int endX = (int) Math.floor(x + hitBox.radiusX);

        int wallZ = (int) Math.floor(z + hitBox.radiusZ);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                BlockState adjacentBlock = ic.getBlock(blockX, blockY, wallZ - 1);

                if (adjacentBlock.isSolid())
                    continue;

                BlockState wallBlock = ic.getBlock(blockX, blockY, wallZ);

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
        double distanceY = Math.abs(y - getBlockCenterY(blockY));
        double distanceX = Math.abs(x - getBlockCenterX(blockX));
        double distanceZ = Math.abs(z - getBlockCenterZ(blockZ));
        double maxDistance = Math.max(Math.max(distanceY, distanceX), distanceZ);

        if (maxDistance == distanceX) return 0;
        if (maxDistance == distanceY) return 1;
        return 2;
    }
}