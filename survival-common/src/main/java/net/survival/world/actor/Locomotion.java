package net.survival.world.actor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.survival.block.BlockType;
import net.survival.util.HitBox;
import net.survival.util.MathEx;
import net.survival.world.World;
import net.survival.world.chunk.Chunk;

public class Locomotion
{
    public static class Component
    {
        private double x;
        private double y;
        private double z;
        private double velocityX;
        private double velocityY;
        private double velocityZ;
        private final HitBox hitBox;

        public Component(double x, double y, double z, HitBox hitBox) {
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
    }

    public static class Service
    {
        private static final double GRAVITY = 32.0;
        private static final double TERMINAL_VELOCITY = 30.0;

        private final HashMap<Actor, Component> objects = new HashMap<>();
        private final ActorEventQueue.Producer actorEventQueue;
        private final World world;

        public Service(ActorEventQueue.Producer actorEventQueue, World world) {
            this.actorEventQueue = actorEventQueue;
            this.world = world;
        }

        public Component subscribe(Actor actor, double x, double y, double z, HitBox hitBox) {
            Component component = new Component(x, y, z, hitBox);
            objects.put(actor, component);

            return component;
        }

        public void tick(double elapsedTime) {
            collect();

            for (Actor actor : world.getActors()) {
                Component component = objects.get(actor);
                if (component == null)
                    continue;

                double directionX = actor.getMovementDirectionX();
                double directionZ = actor.getMovementDirectionZ();
                double sqrSpeed = MathEx.sqrMagnitude(directionX, directionZ);

                if (sqrSpeed > 0.0) {
                    double speed = Math.sqrt(sqrSpeed);
                    component.velocityX = directionX / speed;
                    component.velocityZ = directionZ / speed;
                }
                else {
                    component.velocityX = 0.0;
                    component.velocityZ = 0.0;
                }
            }

            applyGravity(world, elapsedTime);
            applyVelocities(world, elapsedTime);
            handleBlockCollisions(world);
        }

        private void collect() {
            Iterator<Map.Entry<Actor, Component>> iterator = objects.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Actor, Component> entry = iterator.next();
                Actor actor = entry.getKey();

                if (actor.isDead())
                    iterator.remove();
            }
        }

        // ================================================================
        // Physics Code ===================================================
        // ================================================================

        private void applyGravity(World world, double elapsedTime) {
            for (Actor actor : world.getActors()) {
                Component component = objects.get(actor);
                if (component == null)
                    continue;

                component.velocityY -= GRAVITY * elapsedTime;

                if (component.velocityY < -TERMINAL_VELOCITY)
                    component.velocityY = -TERMINAL_VELOCITY;
            }
        }

        private void applyVelocities(World world, double elapsedTime) {
            for (Actor actor : world.getActors()) {
                Component component = objects.get(actor);
                if (component == null)
                    continue;

                component.x += component.velocityX * elapsedTime;
                component.y += component.velocityY * elapsedTime;
                component.z += component.velocityZ * elapsedTime;
            }
        }

        private void handleBlockCollisions(World world) {
            for (Actor actor : world.getActors()) {
                Component component = objects.get(actor);
                if (component == null)
                    continue;

                handleBlockCollisions(world, component);
            }
        }

        private void handleBlockCollisions(World world, Component component) {
            if (!handleFloorCollision(component, world))
                handleCeilingCollision(component, world);

            if (!handleLeftWallCollision(component, world))
                handleRightWallCollision(component, world);

            if (!handleBackWallCollision(component, world))
                handleFrontWallCollision(component, world);
        }

        //
        // TODO: Remove code duplication.
        //

        private boolean handleFloorCollision(Component component, World world) {
            if (component.velocityY >= 0.0)
                return false;

            HitBox characterHitBox = component.hitBox;

            int startX = (int) Math.floor(component.x - characterHitBox.radiusX);
            int endX = (int) Math.floor(component.x + characterHitBox.radiusX);
            int startZ = (int) Math.floor(component.z - characterHitBox.radiusZ);
            int endZ = (int) Math.floor(component.z + characterHitBox.radiusZ);

            int floorY = (int) Math.floor(component.y - characterHitBox.radiusY);

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
                            && getDominantAxis(component, blockX, floorY, blockZ) == 1
                            && intersectsFloorPlane(component, blockX, floorY, blockZ))
                    {
                        component.y = (floorY + 1) + characterHitBox.radiusY;
                        component.velocityY = 0.0;
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean handleCeilingCollision(Component component, World world) {
            if (component.velocityY <= 0.0)
                return false;

            HitBox characterHitBox = component.hitBox;

            int startX = (int) Math.floor(component.x - characterHitBox.radiusX);
            int endX = (int) Math.floor(component.x + characterHitBox.radiusX);
            int startZ = (int) Math.floor(component.z - characterHitBox.radiusZ);
            int endZ = (int) Math.floor(component.z + characterHitBox.radiusZ);

            int ceilingY = (int) Math.floor(component.y + characterHitBox.radiusY);

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
                            && getDominantAxis(component, blockX, ceilingY, blockZ) == 1
                            && intersectsCeilingPlane(component, blockX, ceilingY, blockZ))
                    {
                        component.y = ceilingY - characterHitBox.radiusY;
                        component.velocityY = 0.0;
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean handleLeftWallCollision(Component component, World world) {
            HitBox characterHitBox = component.hitBox;

            int startY = (int) Math.floor(component.y - characterHitBox.radiusY);
            int endY = (int) Math.floor(component.y + characterHitBox.radiusY);

            if (startY < 0 || endY >= Chunk.YLENGTH)
                return false;

            int startZ = (int) Math.floor(component.z - characterHitBox.radiusZ);
            int endZ = (int) Math.floor(component.z + characterHitBox.radiusZ);

            int wallX = (int) Math.floor(component.x + characterHitBox.radiusX);

            for (int blockY = startY; blockY <= endY; ++blockY) {
                for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                    short adjacentBlockID = world.getBlock(wallX - 1, blockY, blockZ);
                    BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                    if (adjacentBlockType.isSolid())
                        continue;

                    short wallBlockID = world.getBlock(wallX, blockY, blockZ);
                    BlockType wallBlockType = BlockType.byID(wallBlockID);

                    if (wallBlockType.isSolid()
                            && getDominantAxis(component, wallX, blockY, blockZ) == 0
                            && intersectsLeftPlane(component, wallX, blockY, blockZ))
                    {
                        component.x = wallX - characterHitBox.radiusX;
                        component.velocityX = 0.0;
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean handleRightWallCollision(Component component, World world) {
            HitBox characterHitBox = component.hitBox;

            int startY = (int) Math.floor(component.y - characterHitBox.radiusY);
            int endY = (int) Math.floor(component.y + characterHitBox.radiusY);

            if (startY < 0 || endY >= Chunk.YLENGTH)
                return false;

            int startZ = (int) Math.floor(component.z - characterHitBox.radiusZ);
            int endZ = (int) Math.floor(component.z + characterHitBox.radiusZ);

            int wallX = (int) Math.floor(component.x - characterHitBox.radiusX);

            for (int blockY = startY; blockY <= endY; ++blockY) {
                for (int blockZ = startZ; blockZ <= endZ; ++blockZ) {
                    short adjacentBlockID = world.getBlock(wallX + 1, blockY, blockZ);
                    BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                    if (adjacentBlockType.isSolid())
                        continue;

                    short wallBlockID = world.getBlock(wallX, blockY, blockZ);
                    BlockType wallBlockType = BlockType.byID(wallBlockID);

                    if (wallBlockType.isSolid()
                            && getDominantAxis(component, wallX, blockY, blockZ) == 0
                            && intersectsRightPlane(component, wallX, blockY, blockZ))
                    {
                        component.x = (wallX + 1) + characterHitBox.radiusX;
                        component.velocityX = 0.0;
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean handleFrontWallCollision(Component component, World world) {
            HitBox characterHitBox = component.hitBox;

            int startY = (int) Math.floor(component.y - characterHitBox.radiusY);
            int endY = (int) Math.floor(component.y + characterHitBox.radiusY);

            if (startY < 0 || endY >= Chunk.YLENGTH)
                return false;

            int startX = (int) Math.floor(component.x - characterHitBox.radiusX);
            int endX = (int) Math.floor(component.x + characterHitBox.radiusX);

            int wallZ = (int) Math.floor(component.z - characterHitBox.radiusZ);

            for (int blockY = startY; blockY <= endY; ++blockY) {
                for (int blockX = startX; blockX <= endX; ++blockX) {
                    short adjacentBlockID = world.getBlock(blockX, blockY, wallZ + 1);
                    BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                    if (adjacentBlockType.isSolid())
                        continue;

                    short wallBlockID = world.getBlock(blockX, blockY, wallZ);
                    BlockType wallBlockType = BlockType.byID(wallBlockID);

                    if (wallBlockType.isSolid()
                            && getDominantAxis(component, blockX, blockY, wallZ) == 2
                            && intersectsFrontPlane(component, blockX, blockY, wallZ))
                    {
                        component.z = (wallZ + 1) + characterHitBox.radiusZ;
                        component.velocityZ = 0.0;
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean handleBackWallCollision(Component component, World world) {
            HitBox characterHitBox = component.hitBox;

            int startY = (int) Math.floor(component.y - characterHitBox.radiusY);
            int endY = (int) Math.floor(component.y + characterHitBox.radiusY);

            if (startY < 0 || endY >= Chunk.YLENGTH)
                return false;

            int startX = (int) Math.floor(component.x - characterHitBox.radiusX);
            int endX = (int) Math.floor(component.x + characterHitBox.radiusX);

            int wallZ = (int) Math.floor(component.z + characterHitBox.radiusZ);

            for (int blockY = startY; blockY <= endY; ++blockY) {
                for (int blockX = startX; blockX <= endX; ++blockX) {
                    short adjacentBlockID = world.getBlock(blockX, blockY, wallZ - 1);
                    BlockType adjacentBlockType = BlockType.byID(adjacentBlockID);

                    if (adjacentBlockType.isSolid())
                        continue;

                    short wallBlockID = world.getBlock(blockX, blockY, wallZ);
                    BlockType wallBlockType = BlockType.byID(wallBlockID);

                    if (wallBlockType.isSolid()
                            && getDominantAxis(component, blockX, blockY, wallZ) == 2
                            && intersectsBackPlane(component, blockX, blockY, wallZ))
                    {
                        component.z = wallZ - characterHitBox.radiusZ;
                        component.velocityZ = 0.0;
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean intersectsFloorPlane(Component component, int blockX, int blockY, int blockZ) {
            HitBox hitBox = component.hitBox;

            return boxIntersectsYPlane(getBlockCollisionBoxTop(blockY),
                    getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                    getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                    hitBox.getTop(component.y), hitBox.getBottom(component.y),
                    hitBox.getLeft(component.x), hitBox.getRight(component.x),
                    hitBox.getFront(component.z), hitBox.getBack(component.z));
        }

        private boolean intersectsCeilingPlane(Component component, int blockX, int blockY, int blockZ)
        {
            HitBox hitBox = component.hitBox;

            return boxIntersectsYPlane(getBlockCollisionBoxBottom(blockY),
                    getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                    getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                    hitBox.getTop(component.y), hitBox.getBottom(component.y),
                    hitBox.getLeft(component.x), hitBox.getRight(component.x),
                    hitBox.getFront(component.z), hitBox.getBack(component.z));
        }

        private boolean intersectsLeftPlane(Component component, int blockX, int blockY, int blockZ) {
            HitBox hitBox = component.hitBox;

            return boxIntersectsXPlane(getBlockCollisionBoxLeft(blockX),
                    getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                    getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                    hitBox.getTop(component.y), hitBox.getBottom(component.y),
                    hitBox.getLeft(component.x), hitBox.getRight(component.x),
                    hitBox.getFront(component.z), hitBox.getBack(component.z));
        }

        private boolean intersectsRightPlane(Component component, int blockX, int blockY, int blockZ) {
            HitBox hitBox = component.hitBox;

            return boxIntersectsXPlane(getBlockCollisionBoxRight(blockX),
                    getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                    getBlockCollisionBoxFront(blockZ), getBlockCollisionBoxBack(blockZ),
                    hitBox.getTop(component.y), hitBox.getBottom(component.y),
                    hitBox.getLeft(component.x), hitBox.getRight(component.x),
                    hitBox.getFront(component.z), hitBox.getBack(component.z));
        }

        private boolean intersectsFrontPlane(Component component, int blockX, int blockY, int blockZ) {
            HitBox hitBox = component.hitBox;

            return boxIntersectsZPlane(getBlockCollisionBoxFront(blockZ),
                    getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                    getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                    hitBox.getTop(component.y), hitBox.getBottom(component.y),
                    hitBox.getLeft(component.x), hitBox.getRight(component.x),
                    hitBox.getFront(component.z), hitBox.getBack(component.z));
        }

        private boolean intersectsBackPlane(Component component, int blockX, int blockY, int blockZ) {
            HitBox hitBox = component.hitBox;

            return boxIntersectsZPlane(getBlockCollisionBoxBack(blockZ),
                    getBlockCollisionBoxTop(blockY), getBlockCollisionBoxBottom(blockY),
                    getBlockCollisionBoxLeft(blockX), getBlockCollisionBoxRight(blockX),
                    hitBox.getTop(component.y), hitBox.getBottom(component.y),
                    hitBox.getLeft(component.x), hitBox.getRight(component.x),
                    hitBox.getFront(component.z), hitBox.getBack(component.z));
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

        private int getDominantAxis(Component component, int blockX, int blockY, int blockZ) {
            double distanceY = Math.abs(component.y - getBlockCenterY(blockY));
            double distanceX = Math.abs(component.x - getBlockCenterX(blockX));
            double distanceZ = Math.abs(component.z - getBlockCenterZ(blockZ));
            double maxDistance = Math.max(Math.max(distanceY, distanceX), distanceZ);

            if (maxDistance == distanceX)
                return 0;
            if (maxDistance == distanceY)
                return 1;

            return 2;
        }
    }
}