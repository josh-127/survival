package net.survival.world.chunk;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class ChunkPosTest
{
    @Test
    @Parameters(method = "getSetOfGeneralCasePositions")
    public void hashPos_givenASetOfGeneralCasePositions_returnsCorrectResult(int cx, int cz, long expected) {
        assertEquals(expected, ChunkPos.hashPos(cx, cz));
    }

    @Test
    @Parameters(method = "getSetOfGeneralCasePositions")
    public void chunkXYZFromHashedPos_givenASetOfGeneralCasePositions_returnsCorrectResult(
            int expectedX, int expectedZ, long hashedPos)
    {
        assertEquals(expectedX, ChunkPos.chunkXFromHashedPos(hashedPos));
        assertEquals(expectedZ, ChunkPos.chunkZFromHashedPos(hashedPos));
    }
    @SuppressWarnings("unused")
    private Object getSetOfGeneralCasePositions() {
        return new Object[] {
                new Object[] {  96,  31, 0x60L       | 0x1F00000000L       },
                new Object[] {  60,  69, 0x3CL       | 0x4500000000L       },
                new Object[] { -88,  33, 0xFFFFFFA8L | 0x2100000000L       },
                new Object[] {  85, -71, 0x55L       | 0xFFFFFFB900000000L },
                new Object[] { -25, -76, 0xFFFFFFE7L | 0xFFFFFFB400000000L },
                new Object[] { -6,   63, 0xFFFFFFFAL | 0x3F00000000L       },
                new Object[] { -58, -28, 0xFFFFFFC6L | 0xFFFFFFE400000000L },
                new Object[] {  62, -56, 0x3EL       | 0xFFFFFFC800000000L },
        };
    }
    
    @Test
    public void hashPos_givenAPositionAtTheOrigin_returnsCorrectResult() {
        assertEquals(0L, ChunkPos.hashPos(0, 0));
    }
    @Test
    public void chunkXZFromHashedPos_givenAPositionAtTheOrigin_returnsCorrectResult() {
        assertEquals(0, ChunkPos.chunkXFromHashedPos(0L));
        assertEquals(0, ChunkPos.chunkZFromHashedPos(0L));
    }
    
    @Test
    public void hashPos_givenAMaximumPositiveXPosition_returnsCorrectResult() {
        assertEquals(0x7FFFFFFFL, ChunkPos.hashPos(0x7FFFFFFF, 0));
    }
    @Test
    public void chunkXZFromHashedPos_givenAMaximumPositiveXPosition_returnsCorrectResult() {
        assertEquals(0x7FFFFFFF, ChunkPos.chunkXFromHashedPos(0x7FFFFFFFL));
        assertEquals(0,          ChunkPos.chunkZFromHashedPos(0x7FFFFFFFL));
    }
    
    @Test
    public void hashPos_givenAMaximumNegativeXPosition_returnsCorrectResult() {
        assertEquals(0x80000000L, ChunkPos.hashPos(0x80000000, 0));
    }
    @Test
    public void chunkXZFromHashedPos_givenAMaximumNegativeXPosition_returnsCorrectResult() {
        assertEquals(0x80000000, ChunkPos.chunkXFromHashedPos(0x80000000L));
        assertEquals(0,          ChunkPos.chunkZFromHashedPos(0x80000000L));
    }
    
    @Test
    public void hashPos_givenAMaximumPositiveZPosition_returnsCorrectResult() {
        assertEquals(0x7FFFFFFF00000000L, ChunkPos.hashPos(0, 0x7FFFFFFF));
    }
    @Test
    public void chunkXZFromHashedPos_givenAMaximumPositiveZPosition_returnsCorrectResult() {
        assertEquals(0,          ChunkPos.chunkXFromHashedPos(0x7FFFFFFF00000000L));
        assertEquals(0x7FFFFFFF, ChunkPos.chunkZFromHashedPos(0x7FFFFFFF00000000L));
    }
    
    @Test
    public void hashPos_givenAMaximumNegativeZPosition_returnsCorrectResult() {
        assertEquals(0x8000000000000000L, ChunkPos.hashPos(0, 0x80000000));
    }
    @Test
    public void chunkXZFromHashedPos_givenAMaximumNegativeZPosition_returnsCorrectResult() {
        assertEquals(0,          ChunkPos.chunkXFromHashedPos(0x8000000000000000L));
        assertEquals(0x80000000, ChunkPos.chunkZFromHashedPos(0x8000000000000000L));
    }
}