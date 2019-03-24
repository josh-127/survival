package net.survival.block;

public class Column
{
    public static final int MAX_HEIGHT = 64;
    public static final int XLENGTH = Chunk.XLENGTH;
    public static final int YLENGTH = Chunk.YLENGTH * MAX_HEIGHT;
    public static final int ZLENGTH = Chunk.ZLENGTH;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    private final Chunk[] chunks;

    public Column() {
        chunks = new Chunk[MAX_HEIGHT];
    }

    private Column(Chunk[] chunks) {
        this.chunks = chunks;
    }

    public Column makeCopy() {
        var copyOfChunks = new Chunk[MAX_HEIGHT];
        for (var i = 0; i < MAX_HEIGHT; ++i) {
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

    public void setChunk(int index, Chunk to) {
        chunks[index] = to;
    }

    public int getHeight() {
        var topIndex = MAX_HEIGHT - 1;
        while (topIndex >= 0 && chunks[topIndex] == null)
            --topIndex;

        return topIndex + 1;
    }

    public int getBlockFullId(int x, int y, int z) {
        var index = y / Chunk.YLENGTH;

        if (chunks[index] == null)
            return 0;

        return chunks[index].getBlockFullId(x, y % Chunk.YLENGTH, z);
    }

    public void setBlockFullId(int x, int y, int z, int to) {
        var index = y / Chunk.YLENGTH;

        if (chunks[index] == null) {
            chunks[index] = new Chunk();
        }

        chunks[index].setBlockFullId(x, y % Chunk.YLENGTH, z, to);
    }

    public boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && ly < YLENGTH && lz < ZLENGTH;
    }
}