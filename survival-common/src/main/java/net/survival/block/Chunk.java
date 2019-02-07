package net.survival.block;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import net.survival.util.XIntegerArray;

public class Chunk
{
    public static final int XLENGTH = 16;
    public static final int YLENGTH = 16;
    public static final int ZLENGTH = 16;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    private XIntegerArray rawData;
    private Int2IntArrayMap rawIdToFullIdMap;
    private Int2IntArrayMap fullIdToRawIdMap;

    public Chunk() {
        rawData = new XIntegerArray(VOLUME, 1);
        rawIdToFullIdMap = new Int2IntArrayMap(4);
        fullIdToRawIdMap = new Int2IntArrayMap(4);
        rawIdToFullIdMap.put(0, 0);
        fullIdToRawIdMap.put(0, 0);
    }

    private Chunk(
            XIntegerArray rawData,
            Int2IntArrayMap rawIdToFullIdMap,
            Int2IntArrayMap fullIdToRawIdMap)
    {
        this.rawData = rawData;
        this.rawIdToFullIdMap = rawIdToFullIdMap;
        this.fullIdToRawIdMap = fullIdToRawIdMap;
    }

    public Chunk makeCopy() {
        var copyOfRawData = rawData.makeCopy();
        var copyOfRawIdToFullIdMap = new Int2IntArrayMap(rawIdToFullIdMap);
        var copyOfFullIdToRawIdMap = new Int2IntArrayMap(fullIdToRawIdMap);
        return new Chunk(copyOfRawData, copyOfRawIdToFullIdMap, copyOfFullIdToRawIdMap);
    }

    public int getBlockFullID(int x, int y, int z) {
        return rawIdToFullIdMap.get((int) rawData.get(localPositionToIndex(x, y, z)));
    }

    public void setBlockFullID(int x, int y, int z, int to) {
        var index = localPositionToIndex(x, y, z);

        if (fullIdToRawIdMap.containsKey(to)) {
            rawData.set(index, fullIdToRawIdMap.get(to));
        }
        else {
            var newRawID = fullIdToRawIdMap.size();
            fullIdToRawIdMap.put(to, newRawID);
            rawIdToFullIdMap.put(newRawID, to);

            if (!rawData.isValidValue(newRawID)) {
                rawData = rawData.getResized(rawData.bitsPerElement + 1);

                // TODO: Perform garbage collection on the block palette.
            }

            rawData.set(index, newRawID);
        }
    }

    public int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }

    public boolean isInBounds(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < XLENGTH && y < YLENGTH && z < ZLENGTH;
    }
}