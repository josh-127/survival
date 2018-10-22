package net.survival.concurrent;

public interface VoidFiber extends Fiber<Object>
{
    static final Object FINISHED_TOKEN = new Object();
}