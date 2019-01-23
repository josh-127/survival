package net.survival.block.message;

public class PlaceBlockMessage extends BlockMessage
{
    private final int x;
    private final int y;
    private final int z;
    private final int fullID;

    public PlaceBlockMessage(int x, int y, int z, int fullID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.fullID = fullID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getFullID() {
        return fullID;
    }

    @Override
    public void accept(BlockMessageVisitor visitor) {
        visitor.visit(this);
    }
}