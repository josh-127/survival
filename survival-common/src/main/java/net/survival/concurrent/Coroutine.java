package net.survival.concurrent;

public interface Coroutine<T>
{
    T pollResult();

    boolean next();

    default Coroutine<T> start() {
        CoroutineScheduler.pushCoroutine(this);
        return this;
    }
}