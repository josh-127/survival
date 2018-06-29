package net.survival.multiplayer;

import java.nio.ByteBuffer;

public class SetPropertyMessage extends Message
{
    @Override
    public void serialize(ByteBuffer buffer) {
    }

    @Override
    public boolean deserialize(ByteBuffer buffer) {
        return false;
    }
}