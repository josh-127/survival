package net.survival.block;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.survival.util.XIntegerArray;

public class Chunk
{
    public static final int XLENGTH = 16;
    public static final int YLENGTH = 16;
    public static final int ZLENGTH = 16;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    private XIntegerArray rawData;
    private IntArrayList rawIdToFullIdMap;
    private Int2IntArrayMap fullIdToRawIdMap;

    public Chunk() {
        rawData = new XIntegerArray(VOLUME, 1);
        rawIdToFullIdMap = new IntArrayList(4);
        fullIdToRawIdMap = new Int2IntArrayMap(4);
        rawIdToFullIdMap.add(0);
        fullIdToRawIdMap.put(0, 0);
    }

    public Chunk(XIntegerArray rawData, int[] blockPalette) {
        this.rawData = rawData;
        rawIdToFullIdMap = new IntArrayList(blockPalette.length);
        fullIdToRawIdMap = new Int2IntArrayMap(blockPalette.length);

        for (var i = 0; i < blockPalette.length; ++i)
            rawIdToFullIdMap.add(blockPalette[i]);

        for (var i = 0; i < blockPalette.length; ++i)
            fullIdToRawIdMap.put(blockPalette[i], i);
    }

    private Chunk(
            XIntegerArray rawData,
            IntArrayList rawIdToFullIdMap,
            Int2IntArrayMap fullIdToRawIdMap)
    {
        this.rawData = rawData;
        this.rawIdToFullIdMap = rawIdToFullIdMap;
        this.fullIdToRawIdMap = fullIdToRawIdMap;
    }

    public Chunk makeCopy() {
        var copyOfRawData = rawData.makeCopy();
        var copyOfRawIdToFullIdMap = new IntArrayList(rawIdToFullIdMap);
        var copyOfFullIdToRawIdMap = new Int2IntArrayMap(fullIdToRawIdMap);
        return new Chunk(copyOfRawData, copyOfRawIdToFullIdMap, copyOfFullIdToRawIdMap);
    }

    public XIntegerArray getRawData() {
        return rawData;
    }

    public int[] getBlockPalette() {
        return rawIdToFullIdMap.toIntArray();
    }

    public int getBlockFullId(int index) {
        return rawIdToFullIdMap.getInt((int) rawData.get(index));
    }

    public int getBlockFullId(int x, int y, int z) {
        return getBlockFullId(localPositionToIndex(x, y, z));
    }

    public void setBlockFullId(int index, int to) {
        if (fullIdToRawIdMap.containsKey(to)) {
            rawData.set(index, fullIdToRawIdMap.get(to));
        }
        else {
            var newRawId = fullIdToRawIdMap.size();
            fullIdToRawIdMap.put(to, newRawId);
            rawIdToFullIdMap.add(to);

            if (!rawData.isValidValue(newRawId)) {
                rawData = rawData.getResized(rawData.bitsPerElement + 1);

                // TODO: Perform garbage collection on the block palette.
            }

            rawData.set(index, newRawId);
        }
    }

    public void setBlockFullId(int x, int y, int z, int to) {
        var index = localPositionToIndex(x, y, z);
        setBlockFullId(index, to);
    }

    public int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }

    public boolean isInBounds(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < XLENGTH && y < YLENGTH && z < ZLENGTH;
    }
}