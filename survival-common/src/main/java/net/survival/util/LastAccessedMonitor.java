package net.survival.util;

public interface LastAccessedMonitor {
    long getLastAccessed();

    void access();

    default long getCurrentTime() {
        return System.nanoTime();
    }
}