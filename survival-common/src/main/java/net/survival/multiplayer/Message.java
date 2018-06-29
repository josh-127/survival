package net.survival.multiplayer;

import java.nio.ByteBuffer;

public abstract class Message {
    public abstract void serialize(ByteBuffer buffer);
    public abstract boolean deserialize(ByteBuffer buffer);
}