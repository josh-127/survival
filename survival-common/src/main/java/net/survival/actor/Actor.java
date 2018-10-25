package net.survival.actor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import net.survival.util.HitBox;

public abstract class Actor
{
    long containingChunk;

    protected double x;
    protected double y;
    protected double z;

    protected double yaw;
    protected double pitch;
    protected double roll;

    protected double scaleX;
    protected double scaleY;
    protected double scaleZ;

    protected double velocityX;
    protected double velocityY;
    protected double velocityZ;

    protected HitBox hitBox = HitBox.DEFAULT;
    protected ActorModel model = ActorModel.HUMAN;

    protected int rigidBodyHandle;

    final Queue<Object> inbox = new LinkedList<>();
    final Queue<Object> loopbackInbox = new LinkedList<>();
    Object currentMessage;

    // TODO: Should be de-duplicated across same types.
    final HashMap<Class<?>, MessageHandler<?>> messageHandlers = new HashMap<>();

    protected <M> void whenReceiving(Class<M> messageType, MessageHandler<M> handler) {
        messageHandlers.put(messageType, handler);
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

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public double getScaleZ() {
        return scaleZ;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityZ() {
        return velocityZ;
    }

    public HitBox getHitBox() {
        return hitBox;
    }

    public ActorModel getModel() {
        return model;
    }
}