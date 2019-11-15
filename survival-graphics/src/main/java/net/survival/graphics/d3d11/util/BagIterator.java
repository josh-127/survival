package net.survival.graphics.d3d11.util;

import java.util.Iterator;

/**
 * BagIterator is an Iterator that can add elements.
 */
public interface BagIterator<E> extends Iterator<E>
{
    /**
     * Adds an element to the data structure.
     * 
     * @param element the element to add
     */
    void add(E element);
}