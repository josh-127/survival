package net.survival.world.chunk;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class VirtualBlockAllocatorTest
{
    @Test
    public void fuzzTest_correctBlockCount() {
        VirtualMemoryAllocator virtualMemoryAllocator = new VirtualMemoryAllocator();
        long[] memoryAddresses = new long[256];

        Random random = new Random(0L);
        for (int i = 0; i < memoryAddresses.length; ++i) {
            long length = random.nextInt(1024) + 1;
            memoryAddresses[i] = virtualMemoryAllocator.allocateMemory(length);
            assertNotEquals(AllocationUnitEncoding.INVALID_EAU, memoryAddresses[i]);
        }

        assertEquals(memoryAddresses.length, virtualMemoryAllocator.countAllocatedBlocks());

        shuffle(memoryAddresses, 1L);
        for (int i = 0; i < memoryAddresses.length; ++i)
            virtualMemoryAllocator.freeMemory(memoryAddresses[i]);

        assertEquals(0, virtualMemoryAllocator.countAllocatedBlocks());
        assertEquals(1, virtualMemoryAllocator.countFreeBlocks());
    }

    private void shuffle(long[] array, long seed) {
        Random random = new Random(seed);

        for (int i = 0; i < array.length - 1; ++i) {
            int swapIndex = random.nextInt(array.length - i) + i;

            long temp = array[i];
            array[i] = array[swapIndex];
            array[swapIndex] = temp;
        }
    }
}