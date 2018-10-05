package net.survival.concurrent;

public class Promise<T> extends DeferredResult<T>
{
    private boolean set;

    public void setResult(T to) {
        if (set)
            throw new IllegalStateException("Promise is already set.");

        result = to;
        set = true;
    }
}