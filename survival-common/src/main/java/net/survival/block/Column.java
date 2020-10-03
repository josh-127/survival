package net.survival.block;

import java.util.Stack;

public class Column {
    public static final int XLENGTH = Chunk.XLENGTH;
    public static final int ZLENGTH = Chunk.ZLENGTH;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;

    private final Stack<Chunk> chunks;
    private boolean newFlag = true;
    private boolean modified;

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

    public Block getBlock(int x, int y, int z) {
        var index = y / Chunk.YLENGTH;

        if (index >= chunks.size()) {
            return StandardBlocks.AIR;
        }

        return chunks.get(index).getBlock(x, y % Chunk.YLENGTH, z);
    }

    public void setBlock(int x, int y, int z, Block to) {
        var index = y / Chunk.YLENGTH;

        while (index >= chunks.size()) {
            chunks.push(new Chunk());
        }

        chunks.get(index).setBlock(x, y % Chunk.YLENGTH, z, to);
        modified = true;
    }

    public boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && lz < ZLENGTH;
    }

    public boolean isNew() {
        return newFlag;
    }

    public void clearNewFlag() {
        newFlag = false;
    }

    public boolean isModified() {
        return modified;
    }

    public void clearModifiedFlag() {
        modified = false;
    }
}