package net.survival.block.io

import net.survival.block.Column
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.atomic.AtomicBoolean

private const val FOOTER_LENGTH = 2 * VirtualAllocationUnit.STRUCTURE_SIZE

class ColumnServer(
    private val file: File,
    private val columnPipe: ColumnServerConnection,
    private val columnGenerator: (Long) -> Column
): Runnable {
    private val allocator = VirtualMemoryAllocator()
    private val directory = ColumnDirectory()
    private val columnCodec: ColumnCodec = ColumnCodec()
    private val running = AtomicBoolean(true)
    private var fileChannel: FileChannel? = null

    override fun run() {
        try {
            val fileExists = file.exists()
            fileChannel = RandomAccessFile(file, "rw").channel

            if (fileExists) {
                loadMetadata()
            }

            while (running.get()) {
                when (val request = columnPipe.waitForRequest()) {
                    ColumnRequest.Close -> running.set(false)
                    is ColumnRequest.Get -> {
                        val columnPos = request.columnPos
                        try {
                            val column = loadColumn(columnPos)
                            columnPipe.respond(ColumnResponse(columnPos, column))
                        }
                        catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                    is ColumnRequest.Post -> {
                        try {
                            saveColumn(request.columnPos, request.column)
                        }
                        catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                }
            }

            saveMetadata()
            fileChannel!!.close()
        }
        catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun loadColumn(columnPos: Long): Column {
        val existingVau = directory[columnPos] ?: return columnGenerator(columnPos)
        fileChannel!!.position(existingVau.address)
        return columnCodec.decompressColumn(fileChannel!!, existingVau.length.toInt())
    }

    private fun saveColumn(columnPos: Long, column: Column) {
        val existingVau = directory[columnPos]
        if (existingVau != null) {
            allocator.freeMemory(existingVau.address)
        }

        val compressedData = columnCodec.compressColumn(column)
        val columnVau = allocator.allocateMemoryAndReturnVau(compressedData.limit().toLong())
            ?: throw RuntimeException("Cannot allocate anymore columns.")

        directory.put(columnPos, columnVau)

        fileChannel!!.position(columnVau.address)
        while (compressedData.hasRemaining()) {
            fileChannel!!.write(compressedData)
        }
        compressedData.flip()
    }

    private fun loadMetadata() {
        val buffer = ByteBuffer.allocate(FOOTER_LENGTH)
        fileChannel!!.position(file.length() - FOOTER_LENGTH)
        while (buffer.hasRemaining()) {
            fileChannel!!.read(buffer)
        }
        buffer.flip()

        val allocatorVau = VirtualAllocationUnit()
        val directoryVau = VirtualAllocationUnit()
        allocatorVau.readFrom(buffer)
        directoryVau.readFrom(buffer)

        val metadataSectionSize = (allocatorVau.length + directoryVau.length).toInt()
        val metadataBuffer = ByteBuffer.allocateDirect(metadataSectionSize)

        fileChannel!!.position(allocatorVau.address)
        while (metadataBuffer.hasRemaining()) {
            fileChannel!!.read(metadataBuffer)
        }
        metadataBuffer.flip()

        allocator.readFrom(metadataBuffer)
        directory.readFrom(metadataBuffer)
    }

    private fun saveMetadata() {
        fileChannel!!.position(allocator.size())

        val metadataBuffer = ByteBuffer.allocateDirect(
            allocator.serializedSize + directory.serializedSize + FOOTER_LENGTH
        )
        val allocatorVau = VirtualAllocationUnit(
            fileChannel!!.position(),
            allocator.serializedSize.toLong(),
            true
        )
        allocator.writeTo(metadataBuffer)

        val directoryVau = VirtualAllocationUnit(
            fileChannel!!.position(),
            directory.serializedSize.toLong(),
            true
        )
        directory.writeTo(metadataBuffer)

        allocatorVau.writeTo(metadataBuffer)
        directoryVau.writeTo(metadataBuffer)

        metadataBuffer.flip()
        while (metadataBuffer.hasRemaining()) {
            fileChannel!!.write(metadataBuffer)
        }
    }
}