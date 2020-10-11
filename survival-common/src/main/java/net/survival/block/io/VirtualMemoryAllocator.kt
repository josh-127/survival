package net.survival.block.io

import java.nio.ByteBuffer
import java.util.*

internal class VirtualMemoryAllocator {
    // TODO: Insertion is slow. Look for a different data structure.
    //       Also, the data structure has to be hand-written for
    //       backwards compatibility.
    private val implicitFreeList: ArrayList<VirtualAllocationUnit> = ArrayList()

    init {
        implicitFreeList.add(VirtualAllocationUnit(0L, Int.MAX_VALUE.toLong(), false))
    }

    fun allocateMemory(length: Long): Long {
        return allocateMemoryAndReturnVau(length)!!.address
    }

    fun allocateMemoryAndReturnVau(length: Long): VirtualAllocationUnit? {
        require(length > 0L) { "Precondition is not met: length > 0L." }
        require(length < VirtualAllocationUnit.MAX_LENGTH) { "Precondition is not met: length < MAX_LENGTH." }

        for (i in implicitFreeList.indices) {
            val vau = implicitFreeList[i]
            if (!vau.allocated) {
                if (length < vau.length) {
                    val newVau = VirtualAllocationUnit(vau.address, length, true)
                    vau.address += length
                    vau.length -= length
                    implicitFreeList.add(i, newVau)
                    return newVau
                }
                else if (length == vau.length) {
                    vau.allocated = true
                    return vau
                }
            }
        }
        return null
    }

    fun freeMemory(address: Long) {
        for (i in implicitFreeList.indices) {
            val vau = implicitFreeList[i]
            if (vau.allocated && vau.address == address) {
                vau.allocated = false
                val nextIndex = i + 1
                val previousIndex = i - 1
                if (nextIndex < implicitFreeList.size) {
                    val nextVau = implicitFreeList[nextIndex]
                    if (!nextVau.allocated) {
                        vau.length += nextVau.length
                        implicitFreeList.removeAt(nextIndex)
                    }
                }
                if (previousIndex >= 0) {
                    val previousVau = implicitFreeList[previousIndex]
                    if (!previousVau.allocated) {
                        vau.address -= previousVau.length
                        implicitFreeList.removeAt(previousIndex)
                    }
                }
                return
            }
        }
        throw IllegalArgumentException("Cannot free non-existing block.")
    }

    val serializedSize: Int get() = 4 + implicitFreeList.size * 17

    fun writeTo(buffer: ByteBuffer) {
        buffer.putInt(implicitFreeList.size)
        for (i in implicitFreeList.indices) {
            val vau = implicitFreeList[i]
            vau.writeTo(buffer)
        }
    }

    fun readFrom(buffer: ByteBuffer) {
        implicitFreeList.clear()
        val vauCount = buffer.int
        for (i in 0 until vauCount) {
            val vau = VirtualAllocationUnit()
            vau.readFrom(buffer)
            implicitFreeList.add(vau)
        }
    }

    fun countAllocatedBlocks(): Int {
        return implicitFreeList.stream()
            .filter { arg0: VirtualAllocationUnit -> arg0.allocated }
            .count()
            .toInt()
    }

    fun countFreeBlocks(): Int {
        return implicitFreeList.size - countAllocatedBlocks()
    }

    fun size(): Long {
        var index = implicitFreeList.size - 1
        var lastVau = implicitFreeList[index]
        while (index >= 0 && !lastVau.allocated) {
            lastVau = implicitFreeList[index--]
        }
        return lastVau.address + lastVau.length
    }
}