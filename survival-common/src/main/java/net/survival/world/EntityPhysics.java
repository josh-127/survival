package net.survival.world;

import net.survival.block.BlockType;
import net.survival.entity.Entity;
import net.survival.world.chunk.Chunk;

public class EntityPhysics
{
    private static final double GRAVITY = 9.81;
    private static final double TERMINAL_VELOCITY = 30.0;

    private final World world;

    public EntityPhysics(World world) {
        this.world = world;
    }

    public void tick(double elapsedTime) {
        applyGravity(elapsedTime);
        applyVelocities(elapsedTime);
        handleBlockCollisions();
    }

    private void applyGravity(double elapsedTime) {
        for (Chunk chunk : world.iterateChunks()) {
            for (Entity entity : chunk.iterateEntities()) {
                double newVX = entity.velocityX;
                double newVY = entity.velocityY - GRAVITY * elapsedTime;
                double newVZ = entity.velocityZ;

                if (newVY < -TERMINAL_VELOCITY)
                    newVY = -TERMINAL_VELOCITY;

                entity.velocityX = newVX;
                entity.velocityY = newVY;
                entity.velocityZ = newVZ;
            }
        }
    }

    private void applyVelocities(double elapsedTime) {
        for (Chunk chunk : world.iterateChunks()) {
            for (Entity entity : chunk.iterateEntities()) {
                entity.x += entity.velocityX * elapsedTime;
                entity.y += entity.velocityY * elapsedTime;
                entity.z += entity.velocityZ * elapsedTime;
            }
        }
    }

    private void handleBlockCollisions() {
        for (Chunk chunk : world.iterateChunks()) {
            for (Entity entity : chunk.iterateEntities()) {
                if (!handleEntityFloorCollision(entity, world))
                    handleEntityCeilingCollision(entity, world);

                if (!handleEntityLeftWallCollision(entity, world))
                    handleEntityRightWallCollision(entity, world);

                if (!handleEntityBackWallCollision(entity, world))
                    handleEntityFrontWallCollision(entity, world);
            }
        }
    }

    //
    // TODO: Remove code duplication.
    //

    private boolean handleEntityFloorCollision(Entity entity, World world) {
        if (entity.velocityY >= 0.0)
            return false;

        int startX = (int) Math.floor(entity.x - entity.collisionBoxRadiusX);
        int endX = (int) Math.floor(entity.x + entity.collisionBoxRadiusX);
        int startZ = (int) Math.floor(entity.z - entity.collisionBoxRadiusZ);
        int endZ = (int) Math.floor(entity.z + entity.collisionBoxRadiusZ);

        int floorY = (int) Math.floor(entity.y - entity.collisionBoxRadiusY);

        for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short aboveFloorBlockID = world.getBlock(blockX, floorY + 1, blockZ);
                BlockType aboveFloorBlockType = BlockType.byID(aboveFloorBlockID);

                if (aboveFloorBlockType.isSolid())
                    continue;

                short floorBlockID = world.getBlock(blockX, floorY, blockZ);
                BlockType floorBlockType = BlockType.byID(floorBlockID);

                if (floorBlockType.isSolid() && getDominantAxis(entity, blockX, floorY, blockZ) == 1
                        && entityIntersectsFloorPlane(entity, blockX, floorY, blockZ))
                {
                    entity.y = (floorY + 1) + entity.collisionBoxRadiusY;
                    entity.velocityY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleEntityCeilingCollision(Entity entity, World world) {
        if (entity.velocityY <= 0.0)
            return false;

        int startX = (int) Math.floor(entity.x - entity.collisionBoxRadiusX);
        int endX = (int) Math.floor(entity.x + entity.collisionBoxRadiusX);
        int startZ = (int) Math.floor(entity.z - entity.collisionBoxRadiusZ);
        int endZ = (int) Math.floor(entity.z + entity.collisionBoxRadiusZ);

        int ceilingY = (int) Math.floor(entity.y + entity.collisionBoxRadiusY);

        for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short belowCeilingBlockID = world.getBlock(blockX, ceilingY - 1, blockZ);
                BlockType belowCeilingBlockType = BlockType.byID(belowCeilingBlockID);

                if (belowCeilingBlockType.isSolid())
                    continue;

                short ceilingBlockID = world.getBlock(blockX, ceilingY, blockZ);
                BlockType ceilingBlockType = BlockType.byID(ceilingBlockID);

                if (ceilingBlockType.isSolid()
                        && getDominantAxis(entity, blockX, ceilingY, blockZ) == 1
                        && entityIntersectsCeilingPlane(entity, blockX, ceilingY, blockZ))
                {
                    entity.y = ceilingY - entity.collisionBoxRadiusY;
                    entity.velocityY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleEntityLeftWallCollision(Entity entity, World world) {
        int startY = (int) Math.floor(entity.y - entity.collisionBoxRadiusY);
        int endY = (int) Math.floor(entity.y + entity.collisionBoxRadiusY);
        int startZ = (int) Math.floor(entity.z - entity.collisionBoxRadiusZ);
        int endZ = (int) Math.floor(entity.z + entity.collisionBoxRadiusZ);

        int wallX = (int) Math.floor(entity.x + entity.collisionBoxRadiusX);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                short adjacentBlockID = world.getBlock(wallX - 1, blockY, blockZ);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(wallX, blockY, blockZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid() && getDominantAxis(entity, wallX, blockY, blockZ) == 0
                        && entityIntersectsLeftPlane(entity, wallX, blockY, blockZ))
                {
                    entity.x = wallX - entity.collisionBoxRadiusX;
                    entity.velocityX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleEntityRightWallCollision(Entity entity, World world) {
        int startY = (int) Math.floor(entity.y - entity.collisionBoxRadiusY);
        int endY = (int) Math.floor(entity.y + entity.collisionBoxRadiusY);
        int startZ = (int) Math.floor(entity.z - entity.collisionBoxRadiusZ);
        int endZ = (int) Math.floor(entity.z + entity.collisionBoxRadiusZ);

        int wallX = (int) Math.floor(entity.x - entity.collisionBoxRadiusX);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                short adjacentBlockID = world.getBlock(wallX + 1, blockY, blockZ);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(wallX, blockY, blockZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid() && getDominantAxis(entity, wallX, blockY, blockZ) == 0
                        && entityIntersectsRightPlane(entity, wallX, blockY, blockZ))
                {
                    entity.x = (wallX + 1) + entity.collisionBoxRadiusX;
                    entity.velocityX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleEntityFrontWallCollision(Entity entity, World world) {
        int startX = (int) Math.floor(entity.x - entity.collisionBoxRadiusX);
        int endX = (int) Math.floor(entity.x + entity.collisionBoxRadiusX);
        int startY = (int) Math.floor(entity.y - entity.collisionBoxRadiusY);
        int endY = (int) Math.floor(entity.y + entity.collisionBoxRadiusY);

        int wallZ = (int) Math.floor(entity.z - entity.collisionBoxRadiusZ);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short adjacentBlockID = world.getBlock(blockX, blockY, wallZ + 1);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(blockX, blockY, wallZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid() && getDominantAxis(entity, blockX, blockY, wallZ) == 2
                        && entityIntersectsFrontPlane(entity, blockX, blockY, wallZ))
                {
                    entity.z = (wallZ + 1) + entity.collisionBoxRadiusZ;
                    entity.velocityZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleEntityBackWallCollision(Entity entity, World world) {
        int startX = (int) Math.floor(entity.x - entity.collisionBoxRadiusX);
        int endX = (int) Math.floor(entity.x + entity.collisionBoxRadiusX);
        int startY = (int) Math.floor(entity.y - entity.collisionBoxRadiusY);
        int endY = (int) Math.floor(entity.y + entity.collisionBoxRadiusY);

        int wallZ = (int) Math.floor(entity.z + entity.collisionBoxRadiusZ);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short adjacentBlockID = world.getBlock(blockX, blockY, wallZ - 1);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(blockX, blockY, wallZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid() && getDominantAxis(entity, blockX, blockY, wallZ) == 2
                        && entityIntersectsBackPlane(entity, blockX, blockY, wallZ))
                {
                    entity.z = wallZ - entity.collisionBoxRadiusZ;
                    entity.velocityZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean entityIntersectsFloorPlane(Entity entity, int blockX, int blockY, int blockZ) {
        return boxIntersectsYPlane(getBlockCollisionBoxTop(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                getEntityCollisionBoxTop(entity), getEntityCollisionBoxBottom(entity),
                getEntityCollisionBoxLeft(entity), getEntityCollisionBoxRight(entity),
                getEntityCollisionBoxFront(entity), getEntityCollisionBoxBack(entity));
    }

    private boolean entityIntersectsCeilingPlane(Entity entity, int blockX, int blockY,
            int blockZ)
    {
        return boxIntersectsYPlane(getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                getEntityCollisionBoxTop(entity), getEntityCollisionBoxBottom(entity),
                getEntityCollisionBoxLeft(entity), getEntityCollisionBoxRight(entity),
                getEntityCollisionBoxFront(entity), getEntityCollisionBoxBack(entity));
    }

    private boolean entityIntersectsLeftPlane(Entity entity, int blockX, int blockY, int blockZ) {
        return boxIntersectsXPlane(getBlockCollisionBoxLeft(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                getEntityCollisionBoxTop(entity), getEntityCollisionBoxBottom(entity),
                getEntityCollisionBoxLeft(entity), getEntityCollisionBoxRight(entity),
                getEntityCollisionBoxFront(entity), getEntityCollisionBoxBack(entity));
    }

    private boolean entityIntersectsRightPlane(Entity entity, int blockX, int blockY, int blockZ) {
        return boxIntersectsXPlane(getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                getEntityCollisionBoxTop(entity), getEntityCollisionBoxBottom(entity),
                getEntityCollisionBoxLeft(entity), getEntityCollisionBoxRight(entity),
                getEntityCollisionBoxFront(entity), getEntityCollisionBoxBack(entity));
    }

    private boolean entityIntersectsFrontPlane(Entity entity, int blockX, int blockY, int blockZ) {
        return boxIntersectsZPlane(getBlockCollisionBoxFront(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getEntityCollisionBoxTop(entity), getEntityCollisionBoxBottom(entity),
                getEntityCollisionBoxLeft(entity), getEntityCollisionBoxRight(entity),
                getEntityCollisionBoxFront(entity), getEntityCollisionBoxBack(entity));
    }

    private boolean entityIntersectsBackPlane(Entity entity, int blockX, int blockY, int blockZ) {
        return boxIntersectsZPlane(getBlockCollisionBoxBack(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getEntityCollisionBoxTop(entity), getEntityCollisionBoxBottom(entity),
                getEntityCollisionBoxLeft(entity), getEntityCollisionBoxRight(entity),
                getEntityCollisionBoxFront(entity), getEntityCollisionBoxBack(entity));
    }

    private boolean boxIntersectsYPlane(double planeY, double planeLeft, double planeRight,
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

    private boolean boxIntersectsXPlane(double planeX, double planeTop, double planeBottom,
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

    private boolean boxIntersectsZPlane(double planeZ, double planeTop, double planeBottom,
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

    private double getEntityCollisionBoxTop(Entity entity) {
        return entity.y + entity.collisionBoxRadiusY;
    }

    private double getEntityCollisionBoxBottom(Entity entity) {
        return entity.y - entity.collisionBoxRadiusY;
    }

    private double getEntityCollisionBoxLeft(Entity entity) {
        return entity.x - entity.collisionBoxRadiusX;
    }

    private double getEntityCollisionBoxRight(Entity entity) {
        return entity.x + entity.collisionBoxRadiusX;
    }

    private double getEntityCollisionBoxFront(Entity entity) {
        return entity.z + entity.collisionBoxRadiusZ;
    }

    private double getEntityCollisionBoxBack(Entity entity) {
        return entity.z - entity.collisionBoxRadiusZ;
    }

    private double getBlockCollisionBoxTop(int blockY) {
        return blockY + 1.0;
    }

    private double getBlockCollisionBoxBottom(int blockY) {
        return blockY;
    }

    private double getBlockCollisionBoxLeft(int blockX) {
        return blockX;
    }

    private double getBlockCollisionBoxRight(int blockX) {
        return blockX + 1.0;
    }

    private double getBlockCollisionBoxFront(int blockZ) {
        return blockZ + 1.0;
    }

    private double getBlockCollisionBoxBack(int blockZ) {
        return blockZ;
    }

    private double getBlockCenterX(int blockX) {
        return blockX + 0.5;
    }

    private double getBlockCenterY(int blockY) {
        return blockY + 0.5;
    }

    private double getBlockCenterZ(int blockZ) {
        return blockZ + 0.5;
    }

    private int getDominantAxis(Entity entity, int blockX, int blockY, int blockZ) {
        double distanceY = Math.abs(entity.y - getBlockCenterY(blockY));
        double distanceX = Math.abs(entity.x - getBlockCenterX(blockX));
        double distanceZ = Math.abs(entity.z - getBlockCenterZ(blockZ));
        double maxDistance = Math.max(Math.max(distanceY, distanceX), distanceZ);

        if (maxDistance == distanceX)
            return 0;
        if (maxDistance == distanceY)
            return 1;

        return 2;
    }
}