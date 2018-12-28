package net.survival.world.actor;

public abstract class Message
{
    public static final int ALL_ACTORS = -1;
    public static final int CLIENT_DISPLAY = -2;

    public final int recipient;

    public Message(int recipient) {
        this.recipient = recipient;
    }
}