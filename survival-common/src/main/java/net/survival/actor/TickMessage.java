package net.survival.actor;

public class TickMessage extends Message
{
    public static final TickMessage DEFAULT = new TickMessage();

    private TickMessage() {
        super(ALL_ACTORS);
    }
}