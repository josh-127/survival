package net.survival.gen.terrain;

import net.survival.blocktype.BlockType;
import net.survival.gen.ColumnPrimer;

public class FlatTerrainGenerator implements TerrainGenerator
{
    private static final int ELEVATION = 1;
    private static final int STARTING_INDEX = ELEVATION * ColumnPrimer.BASE_AREA;
    private static final int ENDING_INDEX = STARTING_INDEX + ColumnPrimer.BASE_AREA;

    private int waterBlockId = BlockType.WATER.getFullId();

    @Override
    public void generate(TerrainContext context) {
        var primer = context.getColumnPrimer();

        for (var i = STARTING_INDEX; i < ENDING_INDEX; ++i) {
            primer.blockIds[i] = waterBlockId;
        }
    }
}