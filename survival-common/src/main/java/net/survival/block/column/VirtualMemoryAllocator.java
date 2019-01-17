package net.survival.block.column;

import java.nio.ByteBuffer;
import java.util.ArrayList;

class VirtualMemoryAllocator
{
    // TODO: Insertion is slow. Look for a different data structure.
    //       Also, the data structure has to be hand-written for
    //       backwards compatibility.
    private final ArrayList<VirtualAllocationUnit> implicitFreeList;

    public VirtualMemoryAllocator() {
        implicitFreeList = new ArrayList<>();
        implicitFreeList.add(new VirtualAllocationUnit(0L, Integer.MAX_VALUE, false));
    }

    public long allocateMemory(long length) {
        return allocateMemoryAndReturnVau(length).address;
    }

    public VirtualAllocationUnit allocateMemoryAndReturnVau(long length) {
        if (length <= 0L)
            throw new IllegalArgumentException("Precondition is not met: length > 0L.");
        if (length >= VirtualAllocationUnit.MAX_LENGTH)
            throw new IllegalArgumentException("Precondition is not met: length < MAX_LENGTH.");

        for (int i = 0; i < implicitFreeList.size(); ++i) {
            VirtualAllocationUnit vau = implicitFreeList.get(i);

            if (!vau.allocated) {
                if (length < vau.length) {
                    VirtualAllocationUnit newVau = new VirtualAllocationUnit(vau.address, length, true);

                    vau.address += length;
                    vau.length -= length;

                    implicitFreeList.add(i, newVau);
                    return newVau;
                }
                else if (length == vau.length) {
                    vau.allocated = true;
                    return vau;
                }
            }
        }

        return null;
    }

    public void freeMemory(long address) {
        for (int i = 0; i < implicitFreeList.size(); ++i) {
            VirtualAllocationUnit vau = implicitFreeList.get(i);

            if (vau.allocated && vau.address == address) {
                vau.allocated = false;

                int nextIndex = i + 1;
                int previousIndex = i - 1;

                if (nextIndex < implicitFreeList.size()) {
                    VirtualAllocationUnit nextVau = implicitFreeList.get(nextIndex);

                    if (!nextVau.allocated) {
                        vau.length += nextVau.length;
                        implicitFreeList.remove(nextIndex);
                    }
                }

                if (previousIndex >= 0) {
                    VirtualAllocationUnit previousVau = implicitFreeList.get(previousIndex);

                    if (!previousVau.allocated) {
                        vau.address -= previousVau.length;
                        implicitFreeList.remove(previousIndex);
                    }
                }

                return;
            }
        }

        throw new IllegalArgumentException("Cannot free non-existing block.");
    }

    public int getSerializedSize() {
        return 4 + implicitFreeList.size() * 17;
    }

    public void writeTo(ByteBuffer buffer) {
        buffer.putInt(implicitFreeList.size());

        for (int i = 0; i < implicitFreeList.size(); ++i) {
            VirtualAllocationUnit vau = implicitFreeList.get(i);
            vau.writeTo(buffer);
        }
    }

    public void readFrom(ByteBuffer buffer) {
        implicitFreeList.clear();

        int vauCount = buffer.getInt();
        for (int i = 0; i < vauCount; ++i) {
            VirtualAllocationUnit vau = new VirtualAllocationUnit();
            vau.readFrom(buffer);
            implicitFreeList.add(vau);
        }
    }

    public int countAllocatedBlocks() {
        return (int) implicitFreeList.stream().filter(arg0 -> arg0.allocated).count();
    }

    public int countFreeBlocks() {
        return implicitFreeList.size() - countAllocatedBlocks();
    }

    public long size() {
        int index = implicitFreeList.size() - 1;
        VirtualAllocationUnit lastVau = implicitFreeList.get(index);

        while (index >= 0 && !lastVau.allocated)
            lastVau = implicitFreeList.get(index--);

        return lastVau.address + lastVau.length;
    }
}