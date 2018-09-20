package net.survival.concurrent;

public interface VoidCoroutine extends Coroutine<Object>
{
    static final Object FINISHED_TOKEN = new Object();
}