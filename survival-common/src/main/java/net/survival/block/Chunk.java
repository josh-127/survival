package net.survival.block;

import java.util.ArrayList;

import it.unimi.dsi.fastutil.objects.Object2ShortArrayMap;
import net.survival.util.XIntegerArray;

public class Chunk {
    public static final int XLENGTH = 16;
    public static final int YLENGTH = 16;
    public static final int ZLENGTH = 16;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    private XIntegerArray rawData;
    private ArrayList<Block> rawIdToBlockMap;
    private Object2ShortArrayMap<Block> blockToRawIdMap;

    public Chunk() {
        rawData = new XIntegerArray(VOLUME, 1);
        rawIdToBlockMap = new ArrayList<>(4);
        blockToRawIdMap = new Object2ShortArrayMap<>(4);
        rawIdToBlockMap.add(StandardBlocks.AIR);
        blockToRawIdMap.put(StandardBlocks.AIR, (short) 0);
    }

    public Chunk(XIntegerArray rawData, Block[] blockPalette) {
        this.rawData = rawData;
        rawIdToBlockMap = new ArrayList<>(blockPalette.length);
        blockToRawIdMap = new Object2ShortArrayMap<>(blockPalette.length);

        for (var i = 0; i < blockPalette.length; ++i) {
            rawIdToBlockMap.add(blockPalette[i]);
        }

        for (var i = 0; i < blockPalette.length; ++i) {
            blockToRawIdMap.put(blockPalette[i], (short) i);
        }
    }

    private Chunk(
            XIntegerArray rawData,
            ArrayList<Block> rawIdToBlockMap,
            Object2ShortArrayMap<Block> blockToRawIdMap)
    {
        this.rawData = rawData;
        this.rawIdToBlockMap = rawIdToBlockMap;
        this.blockToRawIdMap = blockToRawIdMap;
    }

    public Chunk makeCopy() {
        var copyOfRawData = rawData.makeCopy();
        var copyOfRawIdToBlockMap = new ArrayList<Block>(rawIdToBlockMap);
        var copyOfBlockToRawIdMap = new Object2ShortArrayMap<Block>(blockToRawIdMap);
        return new Chunk(copyOfRawData, copyOfRawIdToBlockMap, copyOfBlockToRawIdMap);
    }

    public XIntegerArray getRawData() {
        return rawData;
    }

    public Block[] getBlockPalette() {
        var array = new Block[rawIdToBlockMap.size()];
        rawIdToBlockMap.toArray(array);

        return array;
    }

    public Block getBlock(int index) {
        return rawIdToBlockMap.get((int) rawData.get(index));
    }

    public Block getBlock(int x, int y, int z) {
        return getBlock(localPositionToIndex(x, y, z));
    }

    public void setBlock(int index, Block to) {
        if (to == null) {
            throw new IllegalArgumentException("to");
        }

        if (blockToRawIdMap.containsKey(to)) {
            rawData.set(index, blockToRawIdMap.getShort(to));
        }
        else {
            var newRawId = blockToRawIdMap.size();
            blockToRawIdMap.put(to, (short) newRawId);
            rawIdToBlockMap.add(to);

            if (!rawData.isValidValue(newRawId)) {
                rawData = rawData.getResized(rawData.bitsPerElement + 1);

                // TODO: Perform garbage collection on the block palette.
            }

            rawData.set(index, newRawId);
        }
    }

    public void setBlock(int x, int y, int z, Block to) {
        var index = localPositionToIndex(x, y, z);
        setBlock(index, to);
    }

    public int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }

    public boolean isInBounds(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < XLENGTH && y < YLENGTH && z < ZLENGTH;
    }
}