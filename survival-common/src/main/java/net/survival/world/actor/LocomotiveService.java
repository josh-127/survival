package net.survival.world.actor;

import java.util.Map;

import net.survival.entity.Character;
import net.survival.util.HitBox;
import net.survival.world.World;

public class LocomotiveService extends BasicService<LocomotiveService.Data>
{
    public static final double GRAVITY = 32.0;
    public static final double TERMINAL_VELOCITY = 30.0;

    private final World world;

    public LocomotiveService(World world, EventQueue.Producer eventQueue) {
        super(eventQueue);
        this.world = world;
    }

    @Override
    protected Data createInstance() {
        return new Data();
    }

    public void tick(double elapsedTime) {
        collect();

        for (Map.Entry<Actor, Data> entry : objects.entrySet())
            tickSingle(entry.getValue(), elapsedTime);
    }

    private void tickSingle(Data data, double elapsedTime) {
    }

    protected static class Data
    {
        public double x;
        public double y;
        public double z;
        public double velocityX;
        public double velocityY;
        public double velocityZ;
    }

    // ================================================================
    // Physics Code ===================================================
    // ================================================================

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