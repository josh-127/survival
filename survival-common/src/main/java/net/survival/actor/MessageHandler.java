package net.survival.actor;

public interface MessageHandler<M>
{
    boolean handleMessage(M message);
}