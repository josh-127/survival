package net.survival.util;

import java.util.Arrays;

public class XIntegerArray {
    public final long[] underlyingArray;
    public final int length;
    public final int bitsPerElement;
    private final long elementMask;

    public XIntegerArray(int length, int bitsPerElement) {
        if (bitsPerElement <= 0L || bitsPerElement > 64)
            throw new IllegalArgumentException("bitsPerElement");

        this.underlyingArray = new long[(int) Math.ceil((double) length * bitsPerElement / 64.0)];
        this.length = length;
        this.bitsPerElement = bitsPerElement;
        this.elementMask = (1L << bitsPerElement) - 1L;
    }

    private XIntegerArray(long[] underlyingArray, int length, int bitsPerElement, long elementMask) {
        this.underlyingArray = underlyingArray;
        this.length = length;
        this.bitsPerElement = bitsPerElement;
        this.elementMask = elementMask;
    }

    public static XIntegerArray moveUnderlyingArray(long[] underlyingArray, int length, int bitsPerElement) {
        return new XIntegerArray(underlyingArray, length, bitsPerElement, (1L << bitsPerElement) - 1L);
    }

    public static XIntegerArray fromUnderlyingArray(long[] underlyingArray, int length, int bitsPerElement) {
        var copyOfUnderlyingArray = Arrays.copyOf(underlyingArray, underlyingArray.length);
        return moveUnderlyingArray(copyOfUnderlyingArray, length, bitsPerElement);
    }

    public XIntegerArray makeCopy() {
        var copyOfArray = Arrays.copyOf(underlyingArray, underlyingArray.length);
        return new XIntegerArray(copyOfArray, length, bitsPerElement, elementMask);
    }

    public XIntegerArray getResized(int newBitsPerElement) {
        if (newBitsPerElement == bitsPerElement)
            return makeCopy();
        else if (newBitsPerElement < bitsPerElement)
            throw new IllegalArgumentException("newBitsPerElement");

        var newArray = new XIntegerArray(length, newBitsPerElement);
        for (var i = 0; i < length; ++i)
            newArray.set(i, get(i));

        return newArray;
    }

    public boolean isValidValue(long value) {
        return (value & ~elementMask) == 0;
    }

    public long get(int index) {
        assert index >= 0 && index < length;

        var bitIndex = index * bitsPerElement;
        var coarseIndex = bitIndex / 64;
        var fineIndex = bitIndex % 64;

        var value = underlyingArray[coarseIndex] >>> fineIndex;

        var nextCoarseIndex = (bitIndex + bitsPerElement - 1) / 64;
        if (coarseIndex != nextCoarseIndex) {
            value |= underlyingArray[nextCoarseIndex] << (64 - fineIndex);
        }

        value &= elementMask;
        return value;
    }

    public void set(int index, long value) {
        assert index >= 0 && index < length;
        assert (value & ~elementMask) == 0;

        var bitIndex = index * bitsPerElement;
        var coarseIndex = bitIndex / 64;
        var fineIndex = bitIndex % 64;

        var positionalMask = elementMask << fineIndex;
        underlyingArray[coarseIndex] &= ~positionalMask;
        underlyingArray[coarseIndex] |= (value << fineIndex);

        var nextCoarseIndex = (bitIndex + bitsPerElement - 1) / 64;
        if (coarseIndex != nextCoarseIndex) {
            var bitsBeforeCutoff = 64 - fineIndex;
            var remainingMask = (1L << (bitsPerElement - bitsBeforeCutoff)) - 1L;
            var remainingBytes = (value & (remainingMask << bitsBeforeCutoff)) >>> bitsBeforeCutoff;
            underlyingArray[nextCoarseIndex] &= ~remainingMask;
            underlyingArray[nextCoarseIndex] |= remainingBytes;
        }
    }
}