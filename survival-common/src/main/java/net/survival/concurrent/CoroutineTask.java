package net.survival.concurrent;

public interface CoroutineTask<T>
{
    T pollResult();

    boolean next();

    default CoroutineTask<T> start() {
        TaskScheduler.pushTask(this);
        return this;
    }
}