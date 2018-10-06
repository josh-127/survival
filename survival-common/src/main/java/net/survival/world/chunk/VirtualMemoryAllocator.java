package net.survival.world.chunk;

import java.nio.ByteBuffer;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongListIterator;

class VirtualMemoryAllocator
{
    private static final long MAX_LENGTH = 65536;

    // TODO: Insertion is slow. Look for a different data structure.
    //       Also, the data structure has to be hand-written for
    //       backwards compatibility.
    private final LongArrayList implicitFreeList;

    public VirtualMemoryAllocator() {
        implicitFreeList = new LongArrayList();
        implicitFreeList.add(AllocationUnitEncoding.encode(
                AllocationUnitEncoding.LOWER_BOUND,
                AllocationUnitEncoding.UPPER_BOUND - AllocationUnitEncoding.LOWER_BOUND,
                false));
    }

    private VirtualMemoryAllocator(LongArrayList allocationUnits) {
        this.implicitFreeList = allocationUnits;
    }

    public long allocateMemory(long length) {
        long encodedAllocationUnit = allocateMemoryAndReturnEAU(length);
        if (encodedAllocationUnit == AllocationUnitEncoding.INVALID_EAU)
            return -1L;

        return AllocationUnitEncoding.decodeAddress(encodedAllocationUnit);
    }

    public long allocateMemoryAndReturnEAU(long length) {
        if (length <= 0L)
            throw new IllegalArgumentException("Precondition is not met: length > 0L.");
        if (length >= Math.min(MAX_LENGTH, AllocationUnitEncoding.UPPER_BOUND))
            throw new IllegalArgumentException("Precondition is not met: length < min(MAX_LENGTH, UPPER_BOUND).");

        length = AllocationUnitEncoding.padLength(length);

        for (int i = 0; i < implicitFreeList.size(); ++i) {
            long encodedAllocationUnit = implicitFreeList.getLong(i);
            boolean allocatedI = AllocationUnitEncoding.decodeAllocatedFlag(encodedAllocationUnit);
            long addressI = AllocationUnitEncoding.decodeAddress(encodedAllocationUnit);
            long lengthI = AllocationUnitEncoding.decodeLength(encodedAllocationUnit);

            if (!allocatedI && length <= lengthI) {
                long newAllocationUnit = AllocationUnitEncoding.encode(addressI, length, true);

                addressI += length;
                lengthI -= length;

                implicitFreeList.set(i, AllocationUnitEncoding.encode(addressI, lengthI, allocatedI));
                implicitFreeList.add(i, newAllocationUnit);
                return newAllocationUnit;
            }
        }

        return AllocationUnitEncoding.INVALID_EAU;
    }

    public void freeMemory(long address) {
        for (int i = 0; i < implicitFreeList.size(); ++i) {
            long encodedAllocationUnit = implicitFreeList.getLong(i);
            boolean allocatedI = AllocationUnitEncoding.decodeAllocatedFlag(encodedAllocationUnit);
            long addressI = AllocationUnitEncoding.decodeAddress(encodedAllocationUnit);
            long lengthI = AllocationUnitEncoding.decodeLength(encodedAllocationUnit);

            if (allocatedI && addressI == address) {
                allocatedI = false;

                int nextIndex = i + 1;
                int previousIndex = i - 1;
                boolean shouldRemoveNext = false;
                boolean shouldRemovePrevious = false;

                if (nextIndex < implicitFreeList.size()) {
                    long nextEAU = implicitFreeList.getLong(nextIndex);
                    boolean allocatedI_next = AllocationUnitEncoding.decodeAllocatedFlag(nextEAU);
                    long lengthI_next = AllocationUnitEncoding.decodeLength(nextEAU);

                    if (!allocatedI_next) {
                        lengthI += lengthI_next;
                        shouldRemoveNext = true;
                    }
                }

                if (previousIndex >= 0) {
                    long previousEAU = implicitFreeList.getLong(previousIndex);
                    boolean allocatedI_previous = AllocationUnitEncoding.decodeAllocatedFlag(previousEAU);
                    long addressI_previous = AllocationUnitEncoding.decodeAddress(previousEAU);

                    if (!allocatedI_previous) {
                        addressI -= addressI_previous;
                        shouldRemovePrevious = true;
                    }
                }

                implicitFreeList.set(i, AllocationUnitEncoding.encode(addressI, lengthI, false));

                if (shouldRemoveNext)
                    implicitFreeList.removeLong(nextIndex);
                if (shouldRemovePrevious)
                    implicitFreeList.removeLong(previousIndex);

                return;
            }
        }

        throw new IllegalArgumentException("Cannot free non-existing block.");
    }

    public ByteBuffer serialize() {
        int serializedDataLength = (int) AllocationUnitEncoding.padLength(
                4 + implicitFreeList.size() * 8);
        ByteBuffer serializedData = ByteBuffer.allocate(serializedDataLength);

        serializedData.putInt(implicitFreeList.size());

        for (int i = 0; i < implicitFreeList.size(); ++i)
            serializedData.putLong(implicitFreeList.getLong(i));

        while (serializedData.position() < serializedData.capacity())
            serializedData.put((byte) 0);

        serializedData.flip();
        return serializedData;
    }

    public static VirtualMemoryAllocator deserialize(ByteBuffer deserializedData) {
        int size = deserializedData.getInt();
        LongArrayList allocationUnits = new LongArrayList(size);

        for (int i = 0; i < size; ++i)
            allocationUnits.add(deserializedData.getLong());

        return new VirtualMemoryAllocator(allocationUnits);
    }

    public int countAllocatedBlocks() {
        int count = 0;

        LongListIterator iterator = implicitFreeList.iterator();
        while (iterator.hasNext()) {
            long encodedAllocationUnit = iterator.nextLong();
            boolean allocated = AllocationUnitEncoding.decodeAllocatedFlag(encodedAllocationUnit);
            if (allocated)
                ++count;
        }

        return count;
    }

    public int countFreeBlocks() {
        return implicitFreeList.size() - countAllocatedBlocks();
    }

    public long size() {
        int index = implicitFreeList.size() - 1;

        long lastAllocatedUnit = AllocationUnitEncoding.INVALID_EAU;

        while (index >= 0 && !AllocationUnitEncoding.decodeAllocatedFlag(lastAllocatedUnit))
            lastAllocatedUnit = implicitFreeList.getLong(index--);

        long address = AllocationUnitEncoding.decodeAddress(lastAllocatedUnit);
        long length = AllocationUnitEncoding.decodeLength(lastAllocatedUnit);
        return address + length;
    }
}