package net.survival.concurrent;

public interface Fiber
{
    boolean next();

    default Fiber start() {
        FiberScheduler.pushFiber(this);
        return this;
    }
}