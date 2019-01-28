package net.survival.block.column;

import java.nio.ByteBuffer;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

class ColumnDirectory
{
    // The ordering of on-disk columnDirectory entries depends on Long2LongMap's implementation.
    // However, any change in the implementation will still be backwards compatible with
    // ColumnServer's file format.
    private final Long2ObjectOpenHashMap<VirtualAllocationUnit> entries;

    public ColumnDirectory() {
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

        var iterator = entries.long2ObjectEntrySet().fastIterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            var hashedPos = entry.getLongKey();
            var dataVau = entry.getValue();

            buffer.putLong(hashedPos);
            dataVau.writeTo(buffer);
        }
    }

    public void readFrom(ByteBuffer buffer) {
        entries.clear();

        var size = buffer.getInt();
        for (var i = 0; i < size; ++i) {
            var hashedPos = buffer.getLong();
            var dataVau = new VirtualAllocationUnit();
            dataVau.readFrom(buffer);

            entries.put(hashedPos, dataVau);
        }
    }
}