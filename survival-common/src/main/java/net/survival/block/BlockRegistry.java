package net.survival.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockRegistry
{
    private final HashMap<String, ArrayList<Object>> table = new HashMap<>();
    private final HashMap<String, Supplier<Object>> schema = new HashMap<>();
    private int count;

    public Iterable<BlockState> query(Predicate<BlockState> filter) {
        return () -> new BlockTableIterator(filter); 
    }

    public void update(Predicate<BlockState> condition, BlockPropertyUpdate... mutations) {
        for (int i = 0; i < count; ++i) {
            BlockState entry = blockStateFromRow(i);

            if (condition.test(entry)) {
                for (Function<BlockState, BlockProperty> mutation : mutations) {
                    BlockProperty newProperty = mutation.apply(entry);
                    table.get(newProperty.name).set(i, newProperty.value);
                }
            }
        }
    }

    public void insert(BlockProperty... properties) {
        for (BlockProperty property : properties) {
            if (!schema.containsKey(property.name))
                throw new RuntimeException("Property doesn't exist in block schema.");
        }

        for (Map.Entry<String, Supplier<Object>> property : schema.entrySet()) {
            String name = property.getKey();
            Object value = property.getValue().get();
            table.get(name).add(value);
        }

        int lastIndex = table.size() - 1;
        for (BlockProperty property : properties) {
            table.get(property.name).set(lastIndex, property.value);
        }

        ++count;
    }

    public void delete(Predicate<BlockState> condition) {
        for (int i = count; i >= 0; --i) {
            BlockState entry = blockStateFromRow(i);
            if (condition.test(entry)) {
                deleteRow(i);
                --count;
            }
        }
    }

    public void insertProperty(String name, Supplier<Object> defaultValue) {
        schema.put(name, defaultValue);

        ArrayList<Object> column = new ArrayList<>(count);
        for (int i = 0; i < count; ++i)
            column.add(defaultValue.get());

        table.put(name, column);
    }

    public void deleteProperty(String name) {
        schema.remove(name);
        table.remove(name);
    }

    private BlockState blockStateFromRow(int row) {
        TreeMap<String, Object> properties = new TreeMap<>();
        properties.put(KnownBlockProperty.ID.name(), row);

        for (Map.Entry<String, ArrayList<Object>> entry : table.entrySet()) {
            String propertyName = entry.getKey();
            ArrayList<Object> column = entry.getValue();
            Object value = column.get(row);
            properties.put(propertyName, value);
        }

        return new BlockState(properties);
    }

    private void deleteRow(int row) {
        for (Map.Entry<String, ArrayList<Object>> entry : table.entrySet()) {
            ArrayList<Object> column = entry.getValue();
            column.remove(row);
        }
    }

    private class BlockTableIterator implements Iterator<BlockState>
    {
        private int index;
        private final Predicate<BlockState> filter;

        public BlockTableIterator(Predicate<BlockState> filter) {
            this.index = 0;
            this.filter = filter;

            while (index < count && !filter.test(blockStateFromRow(index)))
                ++index;
        }

        @Override
        public boolean hasNext() {
            return index < count;
        }

        @Override
        public BlockState next() {
            BlockState result = blockStateFromRow(index);

            while (index < count && !filter.test(blockStateFromRow(index)))
                ++index;

            return result;
        }
    }
}