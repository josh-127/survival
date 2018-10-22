package net.survival.concurrent;

public interface Fiber<T>
{
    T pollResult();

    boolean next();

    default Fiber<T> start() {
        FiberScheduler.pushFiber(this);
        return this;
    }
}