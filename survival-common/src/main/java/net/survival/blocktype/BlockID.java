package net.survival.blocktype;

public class BlockID
{
    private BlockID() {}

    public static short typeIDFromFullID(int fullID) {
        return (short) ((fullID & 0xFFFF0000) >>> 16);
    }

    public static short stateFromFullID(int fullID) {
        return (short) (fullID & 0xFFFF);
    }
}