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
    public void hashPos_givenASetOfGeneralCasePositions_returnsCorrectResult(int cx, int cy, int cz, long expected) {
        assertEquals(expected, ChunkPos.hashPos(cx, cy, cz));
    }

    @Test
    @Parameters(method = "getSetOfGeneralCasePositions")
    public void chunkXYZFromHashedPos_givenASetOfGeneralCasePositions_returnsCorrectResult(
            int expectedX, int expectedY, int expectedZ, long hashedPos)
    {
        assertEquals(expectedX, ChunkPos.chunkXFromHashedPos(hashedPos));
        assertEquals(expectedY, ChunkPos.chunkYFromHashedPos(hashedPos));
        assertEquals(expectedZ, ChunkPos.chunkZFromHashedPos(hashedPos));
    }
    @SuppressWarnings("unused")
    private Object getSetOfGeneralCasePositions() {
        return new Object[] {
                new Object[] {  96,   7,  31, 0x100060000L | 0x807L | 0x20003E00000000L },
                new Object[] {  60, -31,  69, 0x10003C000L | 0x7E1L | 0x20008A00000000L },
                new Object[] { -88, -89,  33, 0xFFFA8000L  | 0x7A7L | 0x20004200000000L },
                new Object[] {  85, -39, -71, 0x100055000L | 0x7D9L | 0x1FFF7200000000L },
                new Object[] { -25, -67, -76, 0xFFFE7000L  | 0x7BDL | 0x1FFF6800000000L },
                new Object[] { -6,   44,  63, 0xFFFFA000L  | 0x82CL | 0x20007E00000000L },
                new Object[] { -58,  63, -28, 0xFFFC6000L  | 0x83FL | 0x1FFFC800000000L },
                new Object[] {  62,  48, -56, 0x10003E000L | 0x830L | 0x1FFF9000000000L },
        };
    }
    
    @Test
    public void hashPos_givenAPositionAtTheOrigin_returnsCorrectResult() {
        assertEquals(0x20000100000800L, ChunkPos.hashPos(0, 0, 0));
    }
    @Test
    public void chunkXYZFromHashedPos_givenAPositionAtTheOrigin_returnsCorrectResult() {
        assertEquals(0, ChunkPos.chunkXFromHashedPos(0x20000100000800L));
        assertEquals(0, ChunkPos.chunkYFromHashedPos(0x20000100000800L));
        assertEquals(0, ChunkPos.chunkZFromHashedPos(0x20000100000800L));
    }
    
    @Test
    public void hashPos_givenAMaximumPositiveXPosition_returnsCorrectResult() {
        // cx: (2^21) / 2 - 1
        assertEquals(0x200001FFFFF800L, ChunkPos.hashPos(1048575, 0, 0));
    }
    @Test
    public void chunkXYZFromHashedPos_givenAMaximumPositiveXPosition_returnsCorrectResult() {
        assertEquals(1048575, ChunkPos.chunkXFromHashedPos(0x200001FFFFF800L));
        assertEquals(0,       ChunkPos.chunkYFromHashedPos(0x200001FFFFF800L));
        assertEquals(0,       ChunkPos.chunkZFromHashedPos(0x200001FFFFF800L));
    }
    
    @Test
    public void hashPos_givenAMaximumNegativeXPosition_returnsCorrectResult() {
        // cx: -(2^21) / 2
        assertEquals(0x20000000000800L, ChunkPos.hashPos(-1048576, 0, 0));
    }
    @Test
    public void chunkXYZFromHashedPos_givenAMaximumNegativeXPosition_returnsCorrectResult() {
        assertEquals(-1048576, ChunkPos.chunkXFromHashedPos(0x20000000000800L));
        assertEquals(0,        ChunkPos.chunkYFromHashedPos(0x20000000000800L));
        assertEquals(0,        ChunkPos.chunkZFromHashedPos(0x20000000000800L));
    }
    
    @Test
    public void hashPos_givenAMaximumPositiveZPosition_returnsCorrectResult() {
        // cz: (2^21) / 2 - 1
        assertEquals(0x3FFFFF00000800L, ChunkPos.hashPos(0, 0, 1048575));
    }
    @Test
    public void chunkXYZFromHashedPos_givenAMaximumPositiveZPosition_returnsCorrectResult() {
        assertEquals(0,       ChunkPos.chunkXFromHashedPos(0x3FFFFF00000800L));
        assertEquals(0,       ChunkPos.chunkYFromHashedPos(0x3FFFFF00000800L));
        assertEquals(1048575, ChunkPos.chunkZFromHashedPos(0x3FFFFF00000800L));
    }
    
    @Test
    public void hashPos_givenAMaximumNegativeZPosition_returnsCorrectResult() {
        // cz: -(2^21) / 2
        assertEquals(0x100000800L, ChunkPos.hashPos(0, 0, -1048576));
    }
    @Test
    public void chunkXYZFromHashedPos_givenAMaximumNegativeZPosition_returnsCorrectResult() {
        assertEquals(0,        ChunkPos.chunkXFromHashedPos(0x100000800L));
        assertEquals(0,        ChunkPos.chunkYFromHashedPos(0x100000800L));
        assertEquals(-1048576, ChunkPos.chunkZFromHashedPos(0x100000800L));
    }
    
    @Test
    public void hashPos_givenAMaximumPositiveYPosition_returnsCorrectResult() {
        // cz: (2^21) / 2 - 1
        assertEquals(0x20000100000FFFL, ChunkPos.hashPos(0, 2047, 0));
    }
    @Test
    public void chunkXYZFromHashedPos_givenAMaximumPositiveYPosition_returnsCorrectResult() {
        assertEquals(0,    ChunkPos.chunkXFromHashedPos(0x20000100000FFFL));
        assertEquals(2047, ChunkPos.chunkYFromHashedPos(0x20000100000FFFL));
        assertEquals(0,    ChunkPos.chunkZFromHashedPos(0x20000100000FFFL));
    }
    
    @Test
    public void hashPos_givenAMaximumNegativeYPosition_returnsCorrectResult() {
        // cz: -(2^21) / 2
        assertEquals(0x20000100000000L, ChunkPos.hashPos(0, -2048, 0));
    }
    @Test
    public void chunkXYZFromHashedPos_givenAMaximumNegativeYPosition_returnsCorrectResult() {
        assertEquals(0,     ChunkPos.chunkXFromHashedPos(0x20000100000000L));
        assertEquals(-2048, ChunkPos.chunkYFromHashedPos(0x20000100000000L));
        assertEquals(0,     ChunkPos.chunkZFromHashedPos(0x20000100000000L));
    }
}