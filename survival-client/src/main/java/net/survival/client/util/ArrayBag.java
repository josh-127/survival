package net.survival.client.util;

import java.util.Iterator;

/**
 * Represents a mutable unordered list that internally uses an array.
 *
 * The capacity of an ArrayBag represents the length of the internal array in
 * the ArrayBag.
 */
public class ArrayBag<E> implements Bag<E>
{
    private final boolean isImmutable;
    private Object[] elements;
    private int size;

    /**
     * Constructs an ArrayBag with a capacity of eight elements.
     */
    public ArrayBag() {
        isImmutable = false;
        elements = new Object[8];
    }

    /**
     * Constructs an ArrayBag with a given capacity.
     * 
     * @param capacity the capacity of the ArrayBag
     */
    public ArrayBag(int capacity) {
        assert capacity > 0;

        isImmutable = false;
        elements = new Object[capacity];
    }

    private ArrayBag(ArrayBag<E> bag) {
        isImmutable = true;
        elements = bag.elements;
        size = bag.size;
    }

    /**
     * Gets the capacity of the bag.
     * 
     * @return the capacity of the bag
     */
    public int getCapacity() {
        return elements.length;
    }

    /**
     * Sets the capacity of the bag
     * 
     * @param capacity the new capacity of the bag
     */
    public void setCapacity(int capacity) {
        Object[] newElements = new Object[capacity];

        if (capacity < elements.length) {
            for (int i = 0; i < capacity; i++)
                newElements[i] = elements[i];
        }
        else {
            for (int i = 0; i < elements.length; i++)
                newElements[i] = elements[i];
        }

        elements = newElements;
    }

    /**
     * Gets the internal array of the bag. The bag will not reference the returned
     * array of the bag if the capacity of the bag changed. The capacity of the bag
     * is always changed when calling these methods: setCapacity, clear. The
     * capacity of the bag may or may not change when calling add.
     * 
     * @return the internal array of the bag
     */
    @SuppressWarnings("unchecked")
    public E[] getDirectData() {
        return (E[]) elements;
    }

    /**
     * Returns an immutable version of the bag.
     * 
     * @return an immutable version of the bag
     */
    @Override
    public ImmutableBag<E> asImmutableBag() {
        return new ArrayBag<E>(this);
    }

    /**
     * Gets the number of elements in the bag.
     * 
     * @return the number of elements in the bag
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the number of elements in the bag is zero.
     * 
     * @return true if the number of elements in the bag is zero; otherwise false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Finds a given element in the bag, and returns the index of the element within
     * the bag.
     * 
     * @param element the element to find
     * @return if the element found, the index of the element in the the bag;
     *         otherwise -1
     */
    @Override
    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element))
                return i;
        }

        return -1;
    }

    /**
     * Finds a given element in the bag to check to determine if the bag contains
     * the given element.
     * 
     * @param element the element to find
     * @return true if the element if find; otherwise false
     */
    @Override
    public boolean contains(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element))
                return true;
        }

        return false;
    }

    /**
     * Gets an element in the bag from a given index.
     * 
     * @param index the index of the element
     * @return the element in the bag from the index
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        assert index >= 0 && index < size;

        return (E) elements[index];
    }

    /**
     * Returns an iterator over the bag.
     * 
     * @return an iterator
     */
    @Override
    public Iterator<E> iterator() {
        return bagIterator();
    }

    /**
     * Returns a bag iterator over the bag.
     * 
     * @return a bag iterator
     */
    @Override
    public BagIterator<E> bagIterator() {
        return new BagIterator<E>() {
            private int index = -1;

            /**
             * Returns true if the iteration has more elements.
             * 
             * @return true if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return index != size - 1;
            }

            /**
             * Returns the next element in the iteration.
             * 
             * @return the next element in the iteration
             */
            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                return (E) elements[++index];
            }

            /**
             * Removes the last element.
             */
            @Override
            public void remove() {
                if (isImmutable)
                    throw new IllegalStateException("cannot remove from an immutable bag");

                if (index == -1)
                    throw new IllegalStateException("current element does not exist");

                ArrayBag.this.remove(index--);
            }

            /**
             * Adds an element to the end of the bag.
             */
            @Override
            public void add(E element) {
                if (isImmutable)
                    throw new IllegalStateException("cannot add to an immutable bag");

                ArrayBag.this.add(element);
            }
        };
    }

    /**
     * Replaces an element located in a given index in the bag with a given element.
     * 
     * @param index   the location in the bag
     * @param element the element that will replace the current element in the bag
     */
    @Override
    public void set(int index, E element) {
        assert index >= 0 && index < size;

        elements[index] = element;
    }

    /**
     * Adds an element to the end of the bag.
     * 
     * @param element the element to add
     */
    @Override
    public void add(E element) {
        if (size == elements.length)
            setCapacity(2 * elements.length);

        elements[size++] = element;
    }

    /**
     * Replaces an element located in a given index in the bag with the last element
     * in the bag.
     * 
     * @param index the location in the bag
     * @return the element that was replaced
     */
    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        assert index >= 0 && index < size;

        E removed = (E) elements[index];
        elements[index] = elements[--size];
        return removed;
    }

    /**
     * If the given element in found in the bag, then the bag replaces that element
     * with the last element in the bag. If the given element is not found in the
     * bag, then the bag will do nothing.
     * 
     * @param element the element that will be found and replaced if it is found in
     *                the bag
     */
    @Override
    public void remove(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                elements[i] = elements[--size];
                return;
            }
        }
    }

    /**
     * Removes all elements in the bag.
     */
    @Override
    public void clear() {
        elements = new Object[elements.length];
        size = 0;
    }
}