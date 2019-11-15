package net.survival.util;

import java.util.HashMap;

public abstract class LruCache<K, V> implements Cache<K, V> {
    private final HashMap<K, LinkedNode<V>> elements;
    private LinkedNode<V> head;
    private LinkedNode<V> tail;
    private int maxCapacity;
    private int size;

    public LruCache(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("maxCapacity");
        }

        elements = new HashMap<>(maxCapacity * 4 / 3);
        this.maxCapacity = maxCapacity;
    }

    protected abstract K getKeyForElement(V element);

    public V get(K key) {
        var node = elements.get(key);
        if (node == null) {
            return null;
        }

        var nextNode = node.next;
        var prevNode = node.prev;

        if (prevNode != null) {
            prevNode.next = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        }

        if (node != head) {
            node.next = head;
            node.prev = null;
            head.prev = node;
            head = node;
        }

        return node.element;
    }

    public void add(V element) {
        var key = getKeyForElement(element);

        if (head == null) {
            head = new LinkedNode<>();
            head.element = element;
            tail = head;
            return;
        }
        else {
            var newHead = new LinkedNode<V>();
            newHead.next = head;
            head.prev = newHead;
            head = newHead;
        }

        elements.put(key, head);
        ++size;

        if (size > maxCapacity) {
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;

                var tailKey = getKeyForElement(tail.element);
                elements.remove(tailKey);
            }
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    private static class LinkedNode<E> {
        private E element;
        private LinkedNode<E> next;
        private LinkedNode<E> prev;
    }
}