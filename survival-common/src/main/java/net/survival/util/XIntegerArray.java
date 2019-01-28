package net.survival.util;

public class XIntegerArray
{
    public final long[] underlyingArray;
    public final int length;
    public final int bitsPerElement;
    private final long elementMask;

    public XIntegerArray(int length, int bitsPerElement) {
        if (bitsPerElement <= 0L || bitsPerElement > 64)
            throw new IllegalArgumentException("bitsPerElement");

        this.underlyingArray = new long[length];
        this.length = length;
        this.bitsPerElement = bitsPerElement;
        this.elementMask = (1L << bitsPerElement) - 1L;
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
            underlyingArray[nextCoarseIndex] &= ~remainingMask;
            underlyingArray[nextCoarseIndex] |= value & (remainingMask << bitsBeforeCutoff);
        }
    }
}