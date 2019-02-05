package net.survival.interaction;

public final class MessagePriority
{
    public static final int RESERVED_PRE_STEP  = 0;
    public static final int RESERVED_STEP      = 1;

    public static final int GAMEPLAY_PRE_STEP  = 2;
    public static final int GAMEPLAY_STEP      = 3;
    public static final int GAMEPLAY_POST_STEP = 4;
    public static final int GAMEPLAY_DRAW      = 5;

    public static final int RESERVED_POST_STEP = 6;

    private MessagePriority() {}
}