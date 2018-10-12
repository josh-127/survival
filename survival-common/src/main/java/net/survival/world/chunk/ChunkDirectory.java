package net.survival.world.chunk;

import java.nio.ByteBuffer;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

class ChunkDirectory
{
    // The ordering of on-disk chunkDirectory entries depends on Long2LongMap's implementation.
    // However, any change in the implementation will still be backwards compatible with
    // ChunkDatabase's file format.
    private final Long2ObjectOpenHashMap<VirtualAllocationUnit> entries;

    public ChunkDirectory() {
        entries = new Long2ObjectOpenHashMap<>();
    }

    public VirtualAllocationUnit get(long hashedPos) {
        return entries.get(hashedPos);
    }

    public void put(long hashedPos, VirtualAllocationUnit dataVau) {
        entries.put(hashedPos, dataVau);
    }

    public void remove(long hashedPos) {
        entries.remove(hashedPos);
    }

    public int getSerializedSize() {
        return 4 + (8 + VirtualAllocationUnit.STRUCTURE_SIZE) * entries.size();
    }

    public void writeTo(ByteBuffer buffer) {
        buffer.putInt(entries.size());

        ObjectIterator<Long2ObjectMap.Entry<VirtualAllocationUnit>> iterator =
                entries.long2ObjectEntrySet().fastIterator();

        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<VirtualAllocationUnit> entry = iterator.next();
            long hashedPos = entry.getLongKey();
            VirtualAllocationUnit dataVau = entry.getValue();

            buffer.putLong(hashedPos);
            dataVau.writeTo(buffer);
        }
    }

    public void readFrom(ByteBuffer buffer) {
        entries.clear();

        int size = buffer.getInt();
        for (int i = 0; i < size; ++i) {
            long hashedPos = buffer.getLong();
            VirtualAllocationUnit dataVau = new VirtualAllocationUnit();
            dataVau.readFrom(buffer);

            entries.put(hashedPos, dataVau);
        }
    }
}