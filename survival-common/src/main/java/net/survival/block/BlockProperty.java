package net.survival.block;

public class BlockProperty
{
    public final String name;
    public final Object value;

    private BlockProperty(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static BlockProperty byteValue(String name, byte value) {
        return new BlockProperty(name, value);
    }

    public static BlockProperty shortValue(String name, short value) {
        return new BlockProperty(name, value);
    }

    public static BlockProperty intValue(String name, int value) {
        return new BlockProperty(name, value);
    }

    public static BlockProperty longValue(String name, long value) {
        return new BlockProperty(name, value);
    }

    public static BlockProperty floatValue(String name, float value) {
        return new BlockProperty(name, value);
    }

    public static BlockProperty doubleValue(String name, double value) {
        return new BlockProperty(name, value);
    }

    public static BlockProperty booleanValue(String name, boolean value) {
        return new BlockProperty(name, value);
    }

    public static BlockProperty charValue(String name, char value) {
        return new BlockProperty(name, value);
    }

    public static BlockProperty stringValue(String name, String value) {
        return new BlockProperty(name, value);
    }
}