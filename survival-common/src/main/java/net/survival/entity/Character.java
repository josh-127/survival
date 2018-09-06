package net.survival.entity;

import net.survival.util.HitBox;

public abstract class Character extends Entity
{
    public static final HitBox DEFAULT_HITBOX = new HitBox(0.4375, 0.4375, 0.4375);

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

    public HitBox hitBox;

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
        hitBox = DEFAULT_HITBOX;
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

    @Override
    public void clearControlState() {
        moveDirectionXControl = 0.0;
        moveDirectionZControl = 0.0;
        jumpControl = false;
    }
}