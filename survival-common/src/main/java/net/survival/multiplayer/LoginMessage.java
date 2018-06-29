package net.survival.multiplayer;

import java.nio.ByteBuffer;

public class LoginMessage extends Message
{
    @Override
    public void serialize(ByteBuffer buffer) {
    }

    @Override
    public boolean deserialize(ByteBuffer buffer) {
        return true;
    }
}