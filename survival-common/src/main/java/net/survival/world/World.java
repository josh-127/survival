package net.survival.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.survival.actor.Actor;
import net.survival.block.Column;
import net.survival.block.ColumnPos;
import net.survival.block.Block;

public class World {
    private ArrayList<Actor> actors = new ArrayList<>();
    private Map<Long, Column> columns = new HashMap<>();

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public Map<Long, Column> getColumns() {
        return columns;
    }

    public Block getBlock(int x, int y, int z) {
        var cx = ColumnPos.toColumnX(x);
        var cz = ColumnPos.toColumnZ(z);

        var column = columns.get(ColumnPos.hashPos(cx, cz));
        if (column == null) {
            throw new RuntimeException("Cannot query a block in an unloaded column.");
        }

        var lx = ColumnPos.toLocalX(cx, x);
        var lz = ColumnPos.toLocalZ(cz, z);
        return column.getBlock(lx, y, lz);
    }

    public void setBlock(int x, int y, int z, Block to) {
        var cx = ColumnPos.toColumnX(x);
        var cz = ColumnPos.toColumnZ(z);

        var column = columns.get(ColumnPos.hashPos(cx, cz));
        if (column == null) {
            throw new RuntimeException("Cannot place/replace a block in an unloaded column.");
        }

        var lx = ColumnPos.toLocalX(ColumnPos.toColumnX(x), x);
        var lz = ColumnPos.toLocalZ(ColumnPos.toColumnZ(z), z);
        column.setBlock(lx, y, lz, to);
    }
}