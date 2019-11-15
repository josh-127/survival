package net.survival.util;

import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class LruCache<K, V extends LastAccessedMonitor> implements Cache<K, V> {
    private final LruComparator<V> comparator = new LruComparator<>();
    private final PriorityQueue<V> elements;
    private final int maxCapacity;

    public LruCache(int maxCapacity) {
        elements = new PriorityQueue<>(maxCapacity, comparator);
        this.maxCapacity = maxCapacity;
    }

    protected abstract K getKeyForElement(V element);

    public V get(K key) {
        for (var element : elements) {
            var elementKey = getKeyForElement(element);

            if (key.equals(elementKey)) {
                element.access();
                return element;
            }
        }

        return null;
    }

    public void add(V element) {
        element.access();

        if (elements.size() + 1 >= maxCapacity) {
            elements.remove();
        }

        elements.add(element);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    private static class LruComparator<E extends LastAccessedMonitor> implements Comparator<E> {
        @Override
        public int compare(E o1, E o2) {
            return (int) (o1.getLastAccessed() - o2.getLastAccessed());
        }
    }
}