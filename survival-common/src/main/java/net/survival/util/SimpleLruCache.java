package net.survival.util;

public class SimpleLruCache<K, V extends KeyedElement<K> & LastAccessedMonitor> extends LruCache<K, V> {
    public SimpleLruCache(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    protected K getKeyForElement(V element) {
        return element.getKey();
    }
}