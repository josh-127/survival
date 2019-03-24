package net.survival.blocktype;

public class BlockId
{
    private BlockId() {}

    public static short typeIdFromFullId(int fullId) {
        return (short) ((fullId & 0xFFFF0000) >>> 16);
    }

    public static short stateFromFullId(int fullId) {
        return (short) (fullId & 0xFFFF);
    }
}