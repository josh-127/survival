package net.survival.world.actor;

import net.survival.block.BlockType;
import net.survival.util.HitBox;
import net.survival.world.World;
import net.survival.world.chunk.Chunk;

final class ActorPhysics
{
    private static final double GRAVITY = 32.0;
    private static final double TERMINAL_VELOCITY = 30.0;

    private ActorPhysics() {}

    public static void update(World world, double elapsedTime) {
        for (Actor actor : world.getActors())
            applyGravity(world, elapsedTime, actor);

        for (Actor actor : world.getActors())
            applyVelocities(world, elapsedTime, actor);

        for (Actor actor : world.getActors())
            handleBlockCollisions(world, actor);
    }

    private static void applyGravity(World world, double elapsedTime, Actor actor) {
        actor.velocityY -= GRAVITY * elapsedTime;

        if (actor.velocityY < -TERMINAL_VELOCITY)
            actor.velocityY = -TERMINAL_VELOCITY;
    }

    private static void applyVelocities(World world, double elapsedTime, Actor actor) {
        actor.x += actor.velocityX * elapsedTime;
        actor.y += actor.velocityY * elapsedTime;
        actor.z += actor.velocityZ * elapsedTime;
    }

    private static void handleBlockCollisions(World world, Actor actor) {
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
        if (actor.velocityY >= 0.0)
            return false;

        HitBox actorHitBox = actor.hitBox;

        int startX = (int) Math.floor(actor.x - actorHitBox.radiusX);
        int endX = (int) Math.floor(actor.x + actorHitBox.radiusX);
        int startZ = (int) Math.floor(actor.z - actorHitBox.radiusZ);
        int endZ = (int) Math.floor(actor.z + actorHitBox.radiusZ);

        int floorY = (int) Math.floor(actor.y - actorHitBox.radiusY);

        if (floorY < 0 || floorY + 1 >= Chunk.YLENGTH)
            return false;

        for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short aboveFloorBlockID = world.getBlock(blockX, floorY + 1, blockZ);
                BlockType aboveFloorBlockType = BlockType.byID(aboveFloorBlockID);

                if (aboveFloorBlockType.isSolid())
                    continue;

                short floorBlockID = world.getBlock(blockX, floorY, blockZ);
                BlockType floorBlockType = BlockType.byID(floorBlockID);

                if (floorBlockType.isSolid() && getDominantAxis(actor, blockX, floorY, blockZ) == 1
                        && intersectsFloorPlane(actor, blockX, floorY, blockZ))
                {
                    actor.y = (floorY + 1) + actorHitBox.radiusY;
                    actor.velocityY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleCeilingCollision(Actor actor, World world) {
        if (actor.velocityY <= 0.0)
            return false;

        HitBox actorHitBox = actor.hitBox;

        int startX = (int) Math.floor(actor.x - actorHitBox.radiusX);
        int endX = (int) Math.floor(actor.x + actorHitBox.radiusX);
        int startZ = (int) Math.floor(actor.z - actorHitBox.radiusZ);
        int endZ = (int) Math.floor(actor.z + actorHitBox.radiusZ);

        int ceilingY = (int) Math.floor(actor.y + actorHitBox.radiusY);

        if (ceilingY - 1 < 0 || ceilingY >= Chunk.YLENGTH)
            return false;

        for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short belowCeilingBlockID = world.getBlock(blockX, ceilingY - 1, blockZ);
                BlockType belowCeilingBlockType = BlockType.byID(belowCeilingBlockID);

                if (belowCeilingBlockType.isSolid())
                    continue;

                short ceilingBlockID = world.getBlock(blockX, ceilingY, blockZ);
                BlockType ceilingBlockType = BlockType.byID(ceilingBlockID);

                if (ceilingBlockType.isSolid()
                        && getDominantAxis(actor, blockX, ceilingY, blockZ) == 1
                        && intersectsCeilingPlane(actor, blockX, ceilingY, blockZ))
                {
                    actor.y = ceilingY - actorHitBox.radiusY;
                    actor.velocityY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleLeftWallCollision(Actor actor, World world) {
        HitBox actorHitBox = actor.hitBox;

        int startY = (int) Math.floor(actor.y - actorHitBox.radiusY);
        int endY = (int) Math.floor(actor.y + actorHitBox.radiusY);

        if (startY < 0 || endY >= Chunk.YLENGTH)
            return false;

        int startZ = (int) Math.floor(actor.z - actorHitBox.radiusZ);
        int endZ = (int) Math.floor(actor.z + actorHitBox.radiusZ);

        int wallX = (int) Math.floor(actor.x + actorHitBox.radiusX);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                short adjacentBlockID = world.getBlock(wallX - 1, blockY, blockZ);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(wallX, blockY, blockZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid()
                        && getDominantAxis(actor, wallX, blockY, blockZ) == 0
                        && intersectsLeftPlane(actor, wallX, blockY, blockZ))
                {
                    actor.x = wallX - actorHitBox.radiusX;
                    actor.velocityX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleRightWallCollision(Actor actor, World world) {
        HitBox actorHitBox = actor.hitBox;

        int startY = (int) Math.floor(actor.y - actorHitBox.radiusY);
        int endY = (int) Math.floor(actor.y + actorHitBox.radiusY);

        if (startY < 0 || endY >= Chunk.YLENGTH)
            return false;

        int startZ = (int) Math.floor(actor.z - actorHitBox.radiusZ);
        int endZ = (int) Math.floor(actor.z + actorHitBox.radiusZ);

        int wallX = (int) Math.floor(actor.x - actorHitBox.radiusX);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                short adjacentBlockID = world.getBlock(wallX + 1, blockY, blockZ);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(wallX, blockY, blockZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid()
                        && getDominantAxis(actor, wallX, blockY, blockZ) == 0
                        && intersectsRightPlane(actor, wallX, blockY, blockZ))
                {
                    actor.x = (wallX + 1) + actorHitBox.radiusX;
                    actor.velocityX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleFrontWallCollision(Actor actor, World world) {
        HitBox actorHitBox = actor.hitBox;

        int startY = (int) Math.floor(actor.y - actorHitBox.radiusY);
        int endY = (int) Math.floor(actor.y + actorHitBox.radiusY);

        if (startY < 0 || endY >= Chunk.YLENGTH)
            return false;

        int startX = (int) Math.floor(actor.x - actorHitBox.radiusX);
        int endX = (int) Math.floor(actor.x + actorHitBox.radiusX);

        int wallZ = (int) Math.floor(actor.z - actorHitBox.radiusZ);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short adjacentBlockID = world.getBlock(blockX, blockY, wallZ + 1);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(blockX, blockY, wallZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid()
                        && getDominantAxis(actor, blockX, blockY, wallZ) == 2
                        && intersectsFrontPlane(actor, blockX, blockY, wallZ))
                {
                    actor.z = (wallZ + 1) + actorHitBox.radiusZ;
                    actor.velocityZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean handleBackWallCollision(Actor actor, World world) {
        HitBox actorHitBox = actor.hitBox;

        int startY = (int) Math.floor(actor.y - actorHitBox.radiusY);
        int endY = (int) Math.floor(actor.y + actorHitBox.radiusY);

        if (startY < 0 || endY >= Chunk.YLENGTH)
            return false;

        int startX = (int) Math.floor(actor.x - actorHitBox.radiusX);
        int endX = (int) Math.floor(actor.x + actorHitBox.radiusX);

        int wallZ = (int) Math.floor(actor.z + actorHitBox.radiusZ);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short adjacentBlockID = world.getBlock(blockX, blockY, wallZ - 1);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(blockX, blockY, wallZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid()
                        && getDominantAxis(actor, blockX, blockY, wallZ) == 2
                        && intersectsBackPlane(actor, blockX, blockY, wallZ))
                {
                    actor.z = wallZ - actorHitBox.radiusZ;
                    actor.velocityZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean intersectsFloorPlane(Actor actor, int blockX, int blockY, int blockZ) {
        HitBox hitBox = actor.hitBox;

        return boxIntersectsYPlane(getBlockCollisionBoxTop(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(actor.y), hitBox.getBottom(actor.y),
                hitBox.getLeft(actor.x), hitBox.getRight(actor.x),
                hitBox.getFront(actor.z), hitBox.getBack(actor.z));
    }

    private static boolean intersectsCeilingPlane(Actor actor, int blockX, int blockY, int blockZ) {
        HitBox hitBox = actor.hitBox;

        return boxIntersectsYPlane(getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(actor.y), hitBox.getBottom(actor.y),
                hitBox.getLeft(actor.x), hitBox.getRight(actor.x),
                hitBox.getFront(actor.z), hitBox.getBack(actor.z));
    }

    private static boolean intersectsLeftPlane(Actor actor, int blockX, int blockY, int blockZ) {
        HitBox hitBox = actor.hitBox;

        return boxIntersectsXPlane(getBlockCollisionBoxLeft(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(actor.y), hitBox.getBottom(actor.y),
                hitBox.getLeft(actor.x), hitBox.getRight(actor.x),
                hitBox.getFront(actor.z), hitBox.getBack(actor.z));
    }

    private static boolean intersectsRightPlane(Actor actor, int blockX, int blockY, int blockZ) {
        HitBox hitBox = actor.hitBox;

        return boxIntersectsXPlane(getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(actor.y), hitBox.getBottom(actor.y),
                hitBox.getLeft(actor.x), hitBox.getRight(actor.x),
                hitBox.getFront(actor.z), hitBox.getBack(actor.z));
    }

    private static boolean intersectsFrontPlane(Actor actor, int blockX, int blockY, int blockZ) {
        HitBox hitBox = actor.hitBox;

        return boxIntersectsZPlane(getBlockCollisionBoxFront(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                hitBox.getTop(actor.y), hitBox.getBottom(actor.y),
                hitBox.getLeft(actor.x), hitBox.getRight(actor.x),
                hitBox.getFront(actor.z), hitBox.getBack(actor.z));
    }

    private static boolean intersectsBackPlane(Actor actor, int blockX, int blockY, int blockZ) {
        HitBox hitBox = actor.hitBox;

        return boxIntersectsZPlane(getBlockCollisionBoxBack(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                hitBox.getTop(actor.y), hitBox.getBottom(actor.y),
                hitBox.getLeft(actor.x), hitBox.getRight(actor.x),
                hitBox.getFront(actor.z), hitBox.getBack(actor.z));
    }

    private static boolean boxIntersectsYPlane(double planeY, double planeLeft, double planeRight,
            double planeFront, double planeBack, double boxTop, double boxBottom, double boxLeft,
            double boxRight, double boxFront, double boxBack)
    {
        if (boxBottom >= planeY)
            return false;
        if (boxTop < planeY)
            return false;
        if (boxLeft >= planeRight)
            return false;
        if (boxRight <= planeLeft)
            return false;
        if (boxBack >= planeFront)
            return false;
        if (boxFront <= planeBack)
            return false;

        return true;
    }

    private static boolean boxIntersectsXPlane(double planeX, double planeTop, double planeBottom,
            double planeFront, double planeBack, double boxTop, double boxBottom, double boxLeft,
            double boxRight, double boxFront, double boxBack)
    {
        if (boxBottom >= planeTop)
            return false;
        if (boxTop < planeBottom)
            return false;
        if (boxLeft >= planeX)
            return false;
        if (boxRight <= planeX)
            return false;
        if (boxBack >= planeFront)
            return false;
        if (boxFront <= planeBack)
            return false;

        return true;
    }

    private static boolean boxIntersectsZPlane(double planeZ, double planeTop, double planeBottom,
            double planeLeft, double planeRight, double boxTop, double boxBottom, double boxLeft,
            double boxRight, double boxFront, double boxBack)
    {
        if (boxBottom >= planeTop)
            return false;
        if (boxTop <= planeBottom)
            return false;
        if (boxLeft >= planeRight)
            return false;
        if (boxRight <= planeLeft)
            return false;
        if (boxBack >= planeZ)
            return false;
        if (boxFront < planeZ)
            return false;

        return true;
    }

    private static double getBlockCollisionBoxTop(int blockY) {
        return blockY + 1.0;
    }

    private static double getBlockCollisionBoxBottom(int blockY) {
        return blockY;
    }

    private static double getBlockCollisionBoxLeft(int blockX) {
        return blockX;
    }

    private static double getBlockCollisionBoxRight(int blockX) {
        return blockX + 1.0;
    }

    private static double getBlockCollisionBoxFront(int blockZ) {
        return blockZ + 1.0;
    }

    private static double getBlockCollisionBoxBack(int blockZ) {
        return blockZ;
    }

    private static double getBlockCenterX(int blockX) {
        return blockX + 0.5;
    }

    private static double getBlockCenterY(int blockY) {
        return blockY + 0.5;
    }

    private static double getBlockCenterZ(int blockZ) {
        return blockZ + 0.5;
    }

    private static int getDominantAxis(Actor actor, int blockX, int blockY, int blockZ) {
        double distanceY = Math.abs(actor.y - getBlockCenterY(blockY));
        double distanceX = Math.abs(actor.x - getBlockCenterX(blockX));
        double distanceZ = Math.abs(actor.z - getBlockCenterZ(blockZ));
        double maxDistance = Math.max(Math.max(distanceY, distanceX), distanceZ);

        if (maxDistance == distanceX)
            return 0;
        if (maxDistance == distanceY)
            return 1;

        return 2;
    }
}