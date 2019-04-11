package net.survival.gen.structure;

import net.survival.blocktype.BlockType;
import net.survival.interaction.InteractionContext;

public class SmallTreeStructure extends Structure
{
    private static final int MAX_RADIUS = 16;

    public SmallTreeStructure(int x, int y, int z) {
        super(x, y, z, MAX_RADIUS);
    }

    @Override
    public void visit(InteractionContext ic, GenerateStructureMessage message) {
        var rootBlock = ic.getBlock(x, y, z);
        if (!rootBlock.isType(BlockType.DIRT) && !rootBlock.isType(BlockType.GRASS_BLOCK)) {
            return;
        }

        for (var j = y; j < y + 16; ++j) {
            ic.placeBlock(x, j, z, BlockType.OAK_LOG.getFullId());
        }
    }
}