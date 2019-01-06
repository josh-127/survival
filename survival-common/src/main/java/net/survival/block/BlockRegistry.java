package net.survival.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
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

    public Iterable<BlockState> iterateAllBlocks() {
        return blocks;
    }

    public BlockState getBlock(int id) {
        return blocks.get(id);
    }

    public Iterable<BlockState> guessBlock(String query) {
        Stream<BlockState> stream = blocks.stream();
        HashSet<String> keywords = new HashSet<>(Arrays.asList(query.split(" ")));

        for (String keyword : keywords) {
            stream = stream.filter(o -> {
                String[] otherKeywordsArray = o.getKeywords().split(" ");
                List<String> otherKeywordsList = Arrays.asList(otherKeywordsArray);

                return otherKeywordsList.contains(keyword);
            });
        }

        return stream.collect(Collectors.toList());
    }

    public BlockState guessFirstBlock(String query) {
        return guessBlock(query).iterator().next();
    }

    void registerBlock(BlockState blockState) {
        blocks.add(blockState);
    }

    int getNextID() {
        return blocks.size();
    }
}