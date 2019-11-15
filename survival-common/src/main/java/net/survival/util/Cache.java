package net.survival.util;

public interface Cache<K, V> {
    V get(K key);

    void add(V element);

    default boolean contains(K key) {
        return get(key) != null;
    }
}