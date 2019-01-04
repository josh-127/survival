package net.survival.block;

import java.util.ArrayList;
import java.util.stream.Stream;

public class BlockRegistry
{
    public static final BlockRegistry INSTANCE = new BlockRegistry(
            new BlockFeatureSet_V0_1());

    private final ArrayList<BlockState> blocks = new ArrayList<>();

    private BlockRegistry(BlockFeatureSet... featureSets) {
        for (BlockFeatureSet featureSet : featureSets)
            featureSet.registerBlocks(this);
    }

    public Stream<BlockState> stream() {
        return blocks.stream();
    }

    public BlockState getBlock(int id) {
        return blocks.get(id);
    }

    //
    // TODO: Implement this.
    //
    public Iterable<BlockState> guessBlock(String query) {
        throw new UnsupportedOperationException();
    }

    void registerBlock(BlockState blockState) {
        blocks.add(blockState);
    }
}