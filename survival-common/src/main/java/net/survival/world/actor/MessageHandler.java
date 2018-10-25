package net.survival.world.actor;

public interface MessageHandler<M>
{
    boolean handleMessage(M message);
}