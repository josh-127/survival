package net.survival.block.io

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import java.nio.ByteBuffer

internal class ColumnDirectory {
    // The ordering of on-disk columnDirectory entries depends on Long2LongMap's implementation.
    // However, any change in the implementation will still be backwards compatible with
    // ColumnServer's file format.
    private val entries: Long2ObjectOpenHashMap<VirtualAllocationUnit> = Long2ObjectOpenHashMap()

    operator fun get(hashedPos: Long): VirtualAllocationUnit? = entries[hashedPos]

    fun put(hashedPos: Long, dataVau: VirtualAllocationUnit) {
        entries[hashedPos] = dataVau
    }

    val serializedSize: Int get() = 4 + (8 + VirtualAllocationUnit.STRUCTURE_SIZE) * entries.size

    fun writeTo(buffer: ByteBuffer) {
        buffer.putInt(entries.size)

        val iterator = entries.long2ObjectEntrySet().fastIterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val hashedPos = entry.longKey
            val dataVau = entry.value
            buffer.putLong(hashedPos)
            dataVau.writeTo(buffer)
        }
    }

    fun readFrom(buffer: ByteBuffer) {
        entries.clear()

        val size = buffer.int
        for (i in 0 until size) {
            val hashedPos = buffer.long
            val dataVau = VirtualAllocationUnit()
            dataVau.readFrom(buffer)
            entries[hashedPos] = dataVau
        }
    }
}