package net.survival.client.util;

/**
 * Represents a read-only unordered list.
 */
public interface ImmutableBag<E> extends Iterable<E>
{
    /**
     * Gets the number of elements in the bag.
     * 
     * @return the number of elements in the bag
     */
    int size();

    /**
     * Checks if the number of elements in the bag is zero.
     * 
     * @return true if the number of elements in the bag is zero; otherwise false
     */
    boolean isEmpty();

    /**
     * Finds a given element in the bag, and returns the index of the element within
     * the bag.
     * 
     * @param element the element to find
     * @return if the element found, the index of the element in the the bag;
     *         otherwise -1
     */
    int indexOf(E element);

    /**
     * Finds a given element in the bag to check to determine if the bag contains
     * the given element.
     * 
     * @param element the element to find
     * @return true if the element if find; otherwise false
     */
    boolean contains(E element);

    /**
     * Gets an element in the bag from a given index.
     * 
     * @param index the index of the element
     * @return the element in the bag from the index
     */
    E get(int index);

    /**
     * Returns a bag iterator over the bag.
     * 
     * @return a bag iterator
     */
    BagIterator<E> bagIterator();
}