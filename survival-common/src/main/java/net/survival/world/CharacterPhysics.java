package net.survival.world;

import java.util.Iterator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.survival.block.BlockType;
import net.survival.entity.Character;
import net.survival.util.HitBox;
import net.survival.world.chunk.Chunk;

public class CharacterPhysics
{
    private static final double GRAVITY = 32.0;
    private static final double TERMINAL_VELOCITY = 30.0;

    public void update(World world, double elapsedTime) {
        applyGravity(world, elapsedTime);
        applyVelocities(world, elapsedTime);
        handleBlockCollisions(world);
    }

    private void applyGravity(World world, double elapsedTime) {
        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Character character : chunk.iterateCharacters())
                applyGravity(world, elapsedTime, character);
        }
    }

    private void applyGravity(World world, double elapsedTime, Character character)
    {
        character.velocityY -= GRAVITY * elapsedTime;

        if (character.velocityY < -TERMINAL_VELOCITY)
            character.velocityY = -TERMINAL_VELOCITY;
    }

    private void applyVelocities(World world, double elapsedTime) {
        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Character character : chunk.iterateCharacters())
                applyVelocities(world, elapsedTime, character);
        }
    }

    private void applyVelocities(World world, double elapsedTime, Character character)
    {
        character.x += character.velocityX * elapsedTime;
        character.y += character.velocityY * elapsedTime;
        character.z += character.velocityZ * elapsedTime;
    }

    private void handleBlockCollisions(World world) {
        Iterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Character character : chunk.iterateCharacters())
                handleBlockCollisions(world, character);
        }
    }

    private void handleBlockCollisions(World world, Character character) {
        if (!handleFloorCollision(character, world))
            handleCeilingCollision(character, world);

        if (!handleLeftWallCollision(character, world))
            handleRightWallCollision(character, world);

        if (!handleBackWallCollision(character, world))
            handleFrontWallCollision(character, world);
    }

    //
    // TODO: Remove code duplication.
    //

    private boolean handleFloorCollision(Character character, World world) {
        if (character.velocityY >= 0.0)
            return false;

        HitBox characterHitBox = character.hitBox;

        int startX = (int) Math.floor(character.x - characterHitBox.radiusX);
        int endX = (int) Math.floor(character.x + characterHitBox.radiusX);
        int startZ = (int) Math.floor(character.z - characterHitBox.radiusZ);
        int endZ = (int) Math.floor(character.z + characterHitBox.radiusZ);

        int floorY = (int) Math.floor(character.y - characterHitBox.radiusY);

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

                if (floorBlockType.isSolid()
                        && getDominantAxis(character, blockX, floorY, blockZ) == 1
                        && intersectsFloorPlane(character, blockX, floorY, blockZ))
                {
                    character.y = (floorY + 1) + characterHitBox.radiusY;
                    character.velocityY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleCeilingCollision(Character character, World world) {
        if (character.velocityY <= 0.0)
            return false;

        HitBox characterHitBox = character.hitBox;

        int startX = (int) Math.floor(character.x - characterHitBox.radiusX);
        int endX = (int) Math.floor(character.x + characterHitBox.radiusX);
        int startZ = (int) Math.floor(character.z - characterHitBox.radiusZ);
        int endZ = (int) Math.floor(character.z + characterHitBox.radiusZ);

        int ceilingY = (int) Math.floor(character.y + characterHitBox.radiusY);

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
                        && getDominantAxis(character, blockX, ceilingY, blockZ) == 1
                        && intersectsCeilingPlane(character, blockX, ceilingY, blockZ))
                {
                    character.y = ceilingY - characterHitBox.radiusY;
                    character.velocityY = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleLeftWallCollision(Character character, World world) {
        HitBox characterHitBox = character.hitBox;

        int startY = (int) Math.floor(character.y - characterHitBox.radiusY);
        int endY = (int) Math.floor(character.y + characterHitBox.radiusY);

        if (startY < 0 || endY >= Chunk.YLENGTH)
            return false;

        int startZ = (int) Math.floor(character.z - characterHitBox.radiusZ);
        int endZ = (int) Math.floor(character.z + characterHitBox.radiusZ);

        int wallX = (int) Math.floor(character.x + characterHitBox.radiusX);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                short adjacentBlockID = world.getBlock(wallX - 1, blockY, blockZ);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(wallX, blockY, blockZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid()
                        && getDominantAxis(character, wallX, blockY, blockZ) == 0
                        && intersectsLeftPlane(character, wallX, blockY, blockZ))
                {
                    character.x = wallX - characterHitBox.radiusX;
                    character.velocityX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleRightWallCollision(Character character, World world) {
        HitBox characterHitBox = character.hitBox;

        int startY = (int) Math.floor(character.y - characterHitBox.radiusY);
        int endY = (int) Math.floor(character.y + characterHitBox.radiusY);

        if (startY < 0 || endY >= Chunk.YLENGTH)
            return false;

        int startZ = (int) Math.floor(character.z - characterHitBox.radiusZ);
        int endZ = (int) Math.floor(character.z + characterHitBox.radiusZ);

        int wallX = (int) Math.floor(character.x - characterHitBox.radiusX);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                short adjacentBlockID = world.getBlock(wallX + 1, blockY, blockZ);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(wallX, blockY, blockZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid()
                        && getDominantAxis(character, wallX, blockY, blockZ) == 0
                        && intersectsRightPlane(character, wallX, blockY, blockZ))
                {
                    character.x = (wallX + 1) + characterHitBox.radiusX;
                    character.velocityX = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleFrontWallCollision(Character character, World world) {
        HitBox characterHitBox = character.hitBox;

        int startY = (int) Math.floor(character.y - characterHitBox.radiusY);
        int endY = (int) Math.floor(character.y + characterHitBox.radiusY);

        if (startY < 0 || endY >= Chunk.YLENGTH)
            return false;

        int startX = (int) Math.floor(character.x - characterHitBox.radiusX);
        int endX = (int) Math.floor(character.x + characterHitBox.radiusX);

        int wallZ = (int) Math.floor(character.z - characterHitBox.radiusZ);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short adjacentBlockID = world.getBlock(blockX, blockY, wallZ + 1);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(blockX, blockY, wallZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid()
                        && getDominantAxis(character, blockX, blockY, wallZ) == 2
                        && intersectsFrontPlane(character, blockX, blockY, wallZ))
                {
                    character.z = (wallZ + 1) + characterHitBox.radiusZ;
                    character.velocityZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleBackWallCollision(Character character, World world) {
        HitBox characterHitBox = character.hitBox;

        int startY = (int) Math.floor(character.y - characterHitBox.radiusY);
        int endY = (int) Math.floor(character.y + characterHitBox.radiusY);

        if (startY < 0 || endY >= Chunk.YLENGTH)
            return false;

        int startX = (int) Math.floor(character.x - characterHitBox.radiusX);
        int endX = (int) Math.floor(character.x + characterHitBox.radiusX);

        int wallZ = (int) Math.floor(character.z + characterHitBox.radiusZ);

        for (int blockY = startY; blockY <= endY; ++blockY) {
            for (int blockX = startX; blockX <= endX; ++blockX) {
                short adjacentBlockID = world.getBlock(blockX, blockY, wallZ - 1);
                BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                if (adjacentBlockType.isSolid())
                    continue;

                short wallBlockID = world.getBlock(blockX, blockY, wallZ);
                BlockType wallBlockType = BlockType.byID(wallBlockID);

                if (wallBlockType.isSolid()
                        && getDominantAxis(character, blockX, blockY, wallZ) == 2
                        && intersectsBackPlane(character, blockX, blockY, wallZ))
                {
                    character.z = wallZ - characterHitBox.radiusZ;
                    character.velocityZ = 0.0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean intersectsFloorPlane(Character character, int blockX, int blockY, int blockZ) {
        HitBox hitBox = character.hitBox;

        return boxIntersectsYPlane(getBlockCollisionBoxTop(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(character.y), hitBox.getBottom(character.y),
                hitBox.getLeft(character.x), hitBox.getRight(character.x),
                hitBox.getFront(character.z), hitBox.getBack(character.z));
    }

    private boolean intersectsCeilingPlane(Character character, int blockX, int blockY,
            int blockZ)
    {
        HitBox hitBox = character.hitBox;

        return boxIntersectsYPlane(getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(character.y), hitBox.getBottom(character.y),
                hitBox.getLeft(character.x), hitBox.getRight(character.x),
                hitBox.getFront(character.z), hitBox.getBack(character.z));
    }

    private boolean intersectsLeftPlane(Character character, int blockX, int blockY, int blockZ) {
        HitBox hitBox = character.hitBox;

        return boxIntersectsXPlane(getBlockCollisionBoxLeft(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(character.y), hitBox.getBottom(character.y),
                hitBox.getLeft(character.x), hitBox.getRight(character.x),
                hitBox.getFront(character.z), hitBox.getBack(character.z));
    }

    private boolean intersectsRightPlane(Character character, int blockX, int blockY, int blockZ) {
        HitBox hitBox = character.hitBox;

        return boxIntersectsXPlane(getBlockCollisionBoxRight(blockX),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                hitBox.getTop(character.y), hitBox.getBottom(character.y),
                hitBox.getLeft(character.x), hitBox.getRight(character.x),
                hitBox.getFront(character.z), hitBox.getBack(character.z));
    }

    private boolean intersectsFrontPlane(Character character, int blockX, int blockY, int blockZ) {
        HitBox hitBox = character.hitBox;

        return boxIntersectsZPlane(getBlockCollisionBoxFront(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                hitBox.getTop(character.y), hitBox.getBottom(character.y),
                hitBox.getLeft(character.x), hitBox.getRight(character.x),
                hitBox.getFront(character.z), hitBox.getBack(character.z));
    }

    private boolean intersectsBackPlane(Character character, int blockX, int blockY, int blockZ) {
        HitBox hitBox = character.hitBox;

        return boxIntersectsZPlane(getBlockCollisionBoxBack(blockZ),
                getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                hitBox.getTop(character.y), hitBox.getBottom(character.y),
                hitBox.getLeft(character.x), hitBox.getRight(character.x),
                hitBox.getFront(character.z), hitBox.getBack(character.z));
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

    private int getDominantAxis(Character character, int blockX, int blockY, int blockZ) {
        double distanceY = Math.abs(character.y - getBlockCenterY(blockY));
        double distanceX = Math.abs(character.x - getBlockCenterX(blockX));
        double distanceZ = Math.abs(character.z - getBlockCenterZ(blockZ));
        double maxDistance = Math.max(Math.max(distanceY, distanceX), distanceZ);

        if (maxDistance == distanceX)
            return 0;
        if (maxDistance == distanceY)
            return 1;

        return 2;
    }
}