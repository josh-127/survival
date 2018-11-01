package net.survival.world.chunk;

import java.nio.ByteBuffer;

class VirtualAllocationUnit
{
    public static final long MAX_LENGTH = 65536;
    public static final int STRUCTURE_SIZE = 17;

    public long address;
    public long length;
    public boolean allocated;

    public VirtualAllocationUnit() {}

    public VirtualAllocationUnit(long address, long length, boolean allocated) {
        this.address = address;
        this.length = length;
        this.allocated = allocated;
    }

    public void readFrom(ByteBuffer buffer) {
        address = buffer.getLong();
        length = buffer.getLong();
        allocated = (buffer.get() == 1);
    }

    public void writeTo(ByteBuffer buffer) {
        buffer.putLong(address);
        buffer.putLong(length);
        buffer.put(allocated ? (byte) 1 : (byte) 0);
    }
}