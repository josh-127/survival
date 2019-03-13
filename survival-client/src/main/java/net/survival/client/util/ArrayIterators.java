package net.survival.client.util;

import java.util.Iterator;

/**
 * Contains methods for iterating over arrays.
 */
public final class ArrayIterators
{
    private ArrayIterators() {
    }

    /**
     * Iterates over a byte array.
     * 
     * @param array the byte array
     * @return an iterable object of the byte array
     */
    public static Iterable<Byte> iterate(final byte[] array) {
        return new Iterable<Byte>() {
            @Override
            public Iterator<Byte> iterator() {
                return new Iterator<Byte>() {
                    private int index;

                    @Override
                    public boolean hasNext() {
                        return index < array.length;
                    }

                    @Override
                    public Byte next() {
                        return array[index++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over an empty byte array.
     * 
     * @return an iterable object of an empty byte array
     */
    public static Iterable<Byte> iterateEmptyBytes() {
        return new Iterable<Byte>() {
            @Override
            public Iterator<Byte> iterator() {
                return new Iterator<Byte>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Byte next() {
                        return null;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over a short array.
     * 
     * @param array the short array
     * @return an iterable object of the short array
     */
    public static Iterable<Short> iterate(final short[] array) {
        return new Iterable<Short>() {
            @Override
            public Iterator<Short> iterator() {
                return new Iterator<Short>() {
                    private int index;

                    @Override
                    public boolean hasNext() {
                        return index < array.length;
                    }

                    @Override
                    public Short next() {
                        return array[index++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over an empty short array.
     * 
     * @return an iterable object of an empty short array
     */
    public static Iterable<Short> iterateEmptyShorts() {
        return new Iterable<Short>() {
            @Override
            public Iterator<Short> iterator() {
                return new Iterator<Short>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Short next() {
                        return null;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over an integer array.
     * 
     * @param array the integer array
     * @return an iterable object of the integer array
     */
    public static Iterable<Integer> iterate(final int[] array) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    private int index;

                    @Override
                    public boolean hasNext() {
                        return index < array.length;
                    }

                    @Override
                    public Integer next() {
                        return array[index++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over an empty integer array
     * 
     * @return an iterable of an empty integer array
     */
    public static Iterable<Integer> iterateEmptyInts() {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Integer next() {
                        return null;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over a float array.
     * 
     * @param array the float array
     * @return an iterable object of the float array
     */
    public static Iterable<Float> iterate(final float[] array) {
        return new Iterable<Float>() {
            @Override
            public Iterator<Float> iterator() {
                return new Iterator<Float>() {
                    private int index;

                    @Override
                    public boolean hasNext() {
                        return index < array.length;
                    }

                    @Override
                    public Float next() {
                        return array[index++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over an empty float array
     * 
     * @return an iterable object of an empty float array
     */
    public static Iterable<Float> iterateEmptyFloats() {
        return new Iterable<Float>() {
            @Override
            public Iterator<Float> iterator() {
                return new Iterator<Float>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Float next() {
                        return null;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over a generic array.
     * 
     * @param array the generic array
     * @return an iterable object of the generic array
     */
    public static <E> Iterable<E> iterate(final E[] array) {
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return new Iterator<E>() {
                    private int index;

                    @Override
                    public boolean hasNext() {
                        return index < array.length;
                    }

                    @Override
                    public E next() {
                        return array[index++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates over an empty generic array.
     * 
     * @return an iterable object of an empty generic array
     */
    public static <E> Iterable<E> iterateEmpty() {
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return new Iterator<E>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public E next() {
                        return null;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates an array of generic iterators
     * 
     * @param array the array of generic iterators
     * @return an iterable object of the array of generic iterators
     */
    public static <E> Iterable<Iterator<E>> iterate(final E[][] array) {
        return new Iterable<Iterator<E>>() {
            @Override
            public Iterator<Iterator<E>> iterator() {
                return new Iterator<Iterator<E>>() {
                    private int index;

                    @Override
                    public boolean hasNext() {
                        return index < array.length;
                    }

                    @Override
                    public Iterator<E> next() {
                        return iterate(array[index++]).iterator();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }

    /**
     * Iterates an empty array of generic iterators.
     * 
     * @return an iterable object of an empty array of generic iterators
     */
    public static <E> Iterable<Iterator<E>> iterateEmptyIterators() {
        return new Iterable<Iterator<E>>() {
            @Override
            public Iterator<Iterator<E>> iterator() {
                return new Iterator<Iterator<E>>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Iterator<E> next() {
                        return null;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("remove() not supported");
                    }
                };
            }
        };
    }
}