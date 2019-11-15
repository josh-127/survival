package net.survival.util;

public class TwoLevelCache<K, V> implements Cache<K, V> {
    private final Cache<K, V> l1Cache;
    private final Cache<K, V> l2Cache;

    public TwoLevelCache(Cache<K, V> l1Cache, Cache<K, V> l2Cache) {
        this.l1Cache = l1Cache;
        this.l2Cache = l2Cache;
    }

    @Override
    public V get(K key) {
        var element = l1Cache.get(key);
        if (element == null) {
            element = l2Cache.get(key);
        }

        return element;
    }

    @Override
    public void add(V element) {
        l2Cache.add(element);
        l1Cache.add(element);
    }
}