package net.survival.world.chunk;

import java.util.ArrayList;

import net.survival.entity.Npc;
import net.survival.entity.Player;
import net.survival.world.BlockStorage;

public class Chunk implements BlockStorage
{
    public static final int XLENGTH = 16;
    public static final int YLENGTH = 128;
    public static final int ZLENGTH = 16;
    public static final int BASE_AREA = XLENGTH * ZLENGTH;
    public static final int VOLUME = BASE_AREA * YLENGTH;

    public static final int BLOCKS_MODIFIED = 1;
    public static final int NPCS_MODIFIED = 2;
    public static final int PLAYERS_MODIFIED = 4;

    public final short[] blockIDs;
    private final ArrayList<Npc> npcs;
    private final ArrayList<Player> players;

    private int modified;
    private boolean decorated;

    public Chunk() {
        blockIDs = new short[VOLUME];
        npcs = new ArrayList<>();
        players = new ArrayList<>();
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
    
    public Iterable<Npc> iterateNpcs() {
        modified |= NPCS_MODIFIED;
        return npcs;
    }
    
    public Iterable<Player> iteratePlayers() {
        modified |= PLAYERS_MODIFIED;
        return players;
    }
    
    public void addNpc(Npc npc) {
        npcs.add(npc);
        modified |= NPCS_MODIFIED;
    }
    
    public void addPlayer(Player player) {
        players.add(player);
        modified |= PLAYERS_MODIFIED;
    }

    public int getModificationFlags() {
        return modified;
    }

    public boolean isBlocksModified() {
        return (modified & BLOCKS_MODIFIED) != 0;
    }

    public boolean isNPCsModified() {
        return (modified & NPCS_MODIFIED) != 0;
    }
    
    public boolean isPlayersModified() {
        return (modified & PLAYERS_MODIFIED) != 0;
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