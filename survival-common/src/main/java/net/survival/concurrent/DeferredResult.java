package net.survival.concurrent;

public class DeferredResult<T>
{
    protected T result;

    public T pollResult() {
        return result;
    }
}