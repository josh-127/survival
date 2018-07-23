package net.survival.runtime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GlobalExecutor
{
    private static ExecutorService instance = Executors.newCachedThreadPool();

    public static ExecutorService getInstance() {
        return instance;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            instance.shutdown();
        }));
    }

    private GlobalExecutor() {}
}