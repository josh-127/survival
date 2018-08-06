package net.survival.entity;

public abstract class Character extends Entity
{
    //
    // Controls
    //
    private double moveDirectionXControl;
    private double moveDirectionZControl;
    private boolean jumpControl;

    //
    // Stats
    //
    public double moveSpeed;
    public double jumpSpeed;

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
        moveSpeed = 5.0;
        jumpSpeed = 10.0;
        collisionBoxRadiusX = 0.4375;
        collisionBoxRadiusY = 0.4375;
        collisionBoxRadiusZ = 0.4375;
        model = CharacterModel.HUMAN;
        visible = true;
    }

    public double getMoveDirectionXControlValue() {
        return moveDirectionXControl;
    }

    public double getMoveDirectionZControlValue() {
        return moveDirectionZControl;
    }

    public void setMoveDirectionControlValues(double dx, double dz) {
        moveDirectionXControl = dx;
        moveDirectionZControl = dz;
    }

    public boolean getJumpControlValue() {
        return jumpControl;
    }

    public void setJumpControlValue() {
        jumpControl = true;
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

    @Override
    public void clearControlState() {
        moveDirectionXControl = 0.0;
        moveDirectionZControl = 0.0;
        jumpControl = false;
    }
}