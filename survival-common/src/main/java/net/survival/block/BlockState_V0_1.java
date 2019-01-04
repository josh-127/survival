package net.survival.block;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class BlockState implements Comparable<BlockState>
{
    private final TreeMap<String, Object> properties;

    BlockState(Map<String, Object> properties) {
        this.properties = new TreeMap<>(properties);
    }

    public Stream<String> getPropertyNames() {
        return properties.keySet().stream();
    }

    public Stream<Object> getPropertyValues() {
        return properties.values().stream();
    }

    public Stream<Map.Entry<String, Object>> getProperties() {
        return properties.entrySet().stream();
    }

    public Iterator<String> iteratePropertyNames() {
        return properties.keySet().iterator();
    }

    public Iterator<Object> iteratePropertyValues() {
        return properties.values().iterator();
    }

    public Iterator<Map.Entry<String, Object>> iterateProperties() {
        return properties.entrySet().iterator();
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public byte getPropertyAsByte(String name) {
        return (byte) getProperty(name);
    }

    public short getPropertyAsShort(String name) {
        return (short) getProperty(name);
    }

    public int getPropertyAsInt(String name) {
        return (int) getProperty(name);
    }

    public long getPropertyAsLong(String name) {
        return (long) getProperty(name);
    }

    public float getPropertyAsFloat(String name) {
        return (float) getProperty(name);
    }

    public double getPropertyAsDouble(String name) {
        return (double) getProperty(name);
    }

    public boolean getPropertyAsBoolean(String name) {
        return (boolean) getProperty(name);
    }

    public char getPropertyAsChar(String name) {
        return (char) getProperty(name);
    }

    public String getPropertyAsString(String name) {
        return (String) getProperty(name);
    }

    @Override
    public int compareTo(BlockState o) {
        int id = (int) properties.get("id");
        int otherID = (int) o.properties.get("id");

        if (id < otherID) return -1;
        if (id > otherID) return 1;
        return 0;
    }

    @Override
    public int hashCode() {
        Object idObj = properties.get("id");
        if (idObj == null)
            throw new IllegalStateException("BlockState missing ID.");

        Integer id = (Integer) idObj;
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(BlockState.class))
            return false;

        BlockState other = (BlockState) obj;

        int id = (int) properties.get("id");
        int otherID = (int) other.properties.get("id");

        return id == otherID;
    }

    @Override
    public String toString() {
        int id = (int) properties.get("id");

        StringBuilder builder = new StringBuilder();
        builder.append("BlockState { id= ");
        builder.append(id);

        for (Map.Entry<String, Object> property : properties.entrySet()) {
            builder.append(", ");
            builder.append(property.getKey());
            builder.append('=');
            builder.append(property.getValue());
        }

        builder.append(" }");
        return builder.toString();
    }
}