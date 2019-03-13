package net.survival.client.util;

/**
 * Represents a mutable unordered list.
 */
public interface Bag<E> extends ImmutableBag<E>
{
    /**
     * Returns an immutable version of the bag.
     * 
     * @return an immutable version of the bag
     */
    ImmutableBag<E> asImmutableBag();

    /**
     * Replaces an element located in a given index in the bag with a given element.
     * 
     * @param index   the location in the bag
     * @param element the element that will replace the current element in the bag
     */
    void set(int index, E element);

    /**
     * Adds an element to the end of the bag.
     * 
     * @param element the element to add
     */
    void add(E element);

    /**
     * Replaces an element located in a given index in the bag with the last element
     * in the bag.
     * 
     * @param index the location in the bag
     * @return the element that was replaced
     */
    E remove(int index);

    /**
     * If the given element in found in the bag, then the bag replaces that element
     * with the last element in the bag. If the given element is not found in the
     * bag, then the bag will do nothing.
     * 
     * @param element the element that will be found and replaced if it is found in
     *                the bag
     */
    void remove(E element);

    /**
     * Removes all elements in the bag.
     */
    void clear();
}