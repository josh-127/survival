package net.survival.block.io

import java.nio.ByteBuffer

internal class VirtualAllocationUnit {
    companion object {
        const val MAX_LENGTH: Long = 16777216
        const val STRUCTURE_SIZE = 17
    }

    var address: Long = 0
    var length: Long = 0
    var allocated = false

    constructor()

    constructor(address: Long, length: Long, allocated: Boolean) {
        this.address = address
        this.length = length
        this.allocated = allocated
    }

    fun readFrom(buffer: ByteBuffer) {
        address = buffer.long
        length = buffer.long
        allocated = buffer.get().toInt() == 1
    }

    fun writeTo(buffer: ByteBuffer) {
        buffer.putLong(address)
        buffer.putLong(length)
        buffer.put(if (allocated) 1.toByte() else 0.toByte())
    }
}