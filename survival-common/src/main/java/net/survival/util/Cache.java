package net.survival.util;

public interface Cache<K, V extends LastAccessedMonitor> {
    V get(K key);

    void add(V element);
}