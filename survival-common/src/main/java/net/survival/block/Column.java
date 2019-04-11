package net.survival.block;

import java.util.Stack;

public class Column
{
    public static final int XLENGTH = Chunk.XLENGTH;
    public static final int ZLENGTH = Chunk.ZLENGTH;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;

    private final Stack<Chunk> chunks;

    public Column() {
        chunks = new Stack<>();
    }

    private Column(Stack<Chunk> chunks) {
        this.chunks = chunks;
    }

    public Column makeCopy() {
        var copyOfChunks = new Stack<Chunk>();
        for (var chunk : chunks) {
            copyOfChunks.push(chunk.makeCopy());
        }

        return new Column(copyOfChunks);
    }

    public Chunk getChunk(int index) {
        if (index >= chunks.size()) {
            return null;
        }

        return chunks.get(index);
    }

    public Chunk getChunkLazy(int index) {
        while (index >= chunks.size()) {
            chunks.push(new Chunk());
        }

        return chunks.get(index);
    }

    public Chunk getTopChunk() {
        return chunks.peek();
    }

    public void pushChunk(Chunk chunk) {
        chunks.push(chunk);
    }

    public int getHeight() {
        return chunks.size();
    }

    public int getBlockFullId(int x, int y, int z) {
        var index = y / Chunk.YLENGTH;

        if (index >= chunks.size()) {
            return 0;
        }

        return chunks.get(index).getBlockFullId(x, y % Chunk.YLENGTH, z);
    }

    public void setBlockFullId(int x, int y, int z, int to) {
        var index = y / Chunk.YLENGTH;

        while (index >= chunks.size()) {
            chunks.push(new Chunk());
        }

        chunks.get(index).setBlockFullId(x, y % Chunk.YLENGTH, z, to);
    }

    public boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && lz < ZLENGTH;
    }
}