package net.survival.world.chunk;

import java.util.ArrayList;

import net.survival.entity.Character;
import net.survival.world.BlockStorage;

public class Chunk implements BlockStorage
{
    public static final int XLENGTH = 16;
    public static final int YLENGTH = 128;
    public static final int ZLENGTH = 16;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public static final int BLOCKS_MODIFIED = 1;
    public static final int CHARACTERS_MODIFIED = 2;

    public final short[] blockIDs;
    private final ArrayList<Character> characters;

    private int modified;
    private boolean decorated;

    public Chunk() {
        blockIDs = new short[VOLUME];
        characters = new ArrayList<>();
    }

    @Override
    public short getBlock(int lx, int ly, int lz) {
        return blockIDs[localPositionToIndex(lx, ly, lz)];
    }

    @Override
    public void setBlock(int lx, int ly, int lz, short to) {
        blockIDs[localPositionToIndex(lx, ly, lz)] = to;
        modified |= BLOCKS_MODIFIED;
    }

    public int localPositionToIndex(int x, int y, int z) {
        return x + (z * XLENGTH) + (y * BASE_AREA);
    }

    public boolean isInBounds(int lx, int ly, int lz) {
        return lx >= 0 && ly >= 0 && lz >= 0 && lx < XLENGTH && ly < YLENGTH && lz < ZLENGTH;
    }

    public Iterable<Character> iterateCharacters() {
        modified |= CHARACTERS_MODIFIED;
        return characters;
    }

    public void addCharacter(Character character) {
        characters.add(character);
        modified |= CHARACTERS_MODIFIED;
    }

    public int getModificationFlags() {
        return modified;
    }

    public boolean isBlocksModified() {
        return (modified & BLOCKS_MODIFIED) != 0;
    }

    public boolean isCharactersModified() {
        return (modified & CHARACTERS_MODIFIED) != 0;
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