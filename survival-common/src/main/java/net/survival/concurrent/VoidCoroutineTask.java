package net.survival.concurrent;

public interface VoidCoroutineTask extends CoroutineTask<Object>
{
    static final Object FINISHED_TOKEN = new Object();
}