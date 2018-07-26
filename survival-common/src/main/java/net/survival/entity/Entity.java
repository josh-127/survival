package net.survival.entity;

import net.survival.entity.controller.EntityControllerType;

public class Entity
{
    public double x;
    public double y;
    public double z;
    public double velocityX;
    public double velocityY;
    public double velocityZ;

    public double yaw;
    public double pitch;
    public double roll;

    public double collisionBoxRadiusX;
    public double collisionBoxRadiusY;
    public double collisionBoxRadiusZ;

    public EntityControllerType controllerType;

    public EntityModel model;
    public boolean visible;

    public Entity() {
        collisionBoxRadiusX = 0.4375;
        collisionBoxRadiusY = 0.4375;
        collisionBoxRadiusZ = 0.4375;
        controllerType = EntityControllerType.DEFAULT;
        model = EntityModel.HUMAN;
        visible = true;
    }
}