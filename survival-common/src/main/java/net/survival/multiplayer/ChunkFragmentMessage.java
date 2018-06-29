package net.survival.multiplayer;

import java.nio.ByteBuffer;

public class ChunkFragmentMessage extends Message
{
    @Override
    public void serialize(ByteBuffer buffer) {
    }

    @Override
    public boolean deserialize(ByteBuffer buffer) {
        return false;
    }
}