package net.survival.entity;

public abstract class Character extends Entity
{
    //
    // Physics
    //
    public double velocityX;
    public double velocityY;
    public double velocityZ;

    public double collisionBoxRadiusX;
    public double collisionBoxRadiusY;
    public double collisionBoxRadiusZ;

    //
    // Rendering
    //
    public double yaw;
    public double pitch;
    public double roll;

    public double scaleX;
    public double scaleY;
    public double scaleZ;

    public CharacterModel model;
    public boolean visible;

    public Character() {
        collisionBoxRadiusX = 0.4375;
        collisionBoxRadiusY = 0.4375;
        collisionBoxRadiusZ = 0.4375;
        model = CharacterModel.HUMAN;
        visible = true;
    }

    public double getCollisionBoxTop() {
        return y + collisionBoxRadiusY;
    }

    public double getCollisionBoxBottom() {
        return y - collisionBoxRadiusY;
    }

    public double getCollisionBoxLeft() {
        return x - collisionBoxRadiusX;
    }

    public double getCollisionBoxRight() {
        return x + collisionBoxRadiusX;
    }

    public double getCollisionBoxFront() {
        return z + collisionBoxRadiusZ;
    }

    public double getCollisionBoxBack() {
        return z - collisionBoxRadiusZ;
    }
}