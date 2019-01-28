package net.survival.block.column;

import net.survival.block.BlockStorage;

public class Column implements BlockStorage
{
    public static final int HEIGHT = 8;
    public static final int XLENGTH = Chunk.XLENGTH;
    public static final int YLENGTH = Chunk.YLENGTH * HEIGHT;
    public static final int ZLENGTH = Chunk.ZLENGTH;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public static final int BLOCKS_MODIFIED = 1;

    private final Chunk[] chunks;

    private int modified;
    private boolean decorated;

    public Column() {
        chunks = new Chunk[HEIGHT];
    }

    private Column(Chunk[] chunks) {
        this.chunks = chunks;
    }

    public Column makeCopy() {
        var copyOfChunks = new Chunk[HEIGHT];
        for (var i = 0; i < HEIGHT; ++i) {
            if (chunks[i] != null)
                copyOfChunks[i] = chunks[i].makeCopy();
        }

        return new Column(copyOfChunks);
    }

    public Chunk getChunk(int index) {
        return chunks[index];
    }

    public Chunk getChunkLazy(int index) {
        if (chunks[index] == null)
            chunks[index] = new Chunk();

        return chunks[index];
    }

    public int getHeight() {
        var topIndex = HEIGHT - 1;
        while (topIndex >= 0 && chunks[topIndex] == null)
            --topIndex;

        return topIndex + 1;
    }

    @Override
    public int getBlockFullID(int x, int y, int z) {
        var index = y / Chunk.YLENGTH;

        if (chunks[index] == null)
            return 0;

        return chunks[index].getBlockFullID(x, y % Chunk.YLENGTH, z);
    }

    @Override
    public void setBlockFullID(int x, int y, int z, int to) {
        var index = y / Chunk.YLENGTH;

        if (chunks[index] == null) {
            chunks[index] = new Chunk();
        }

        chunks[index].setBlockFullID(x, y % Chunk.YLENGTH, z, to);
        modified |= BLOCKS_MODIFIED;
    }

    public boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && ly < YLENGTH && lz < ZLENGTH;
    }

    public int getModificationFlags() {
        return modified;
    }

    public boolean isBlocksModified() {
        return (modified & BLOCKS_MODIFIED) != 0;
    }

    public void clearModificationFlags() {
        modified = 0;
    }

    public void setModificationFlags(int to) {
        modified = to;
    }

    public boolean isDecorated() {
        return decorated;
    }

    public void markDecorated() {
        decorated = true;
    }
}