package net.survival.interaction;

public final class MessagePriority
{
    public static final int RESERVED_PRE_STEP  = 1000;
    public static final int RESERVED_STEP      = 1100;

    public static final int GAMEPLAY_PRE_STEP  = 2000;
    public static final int GAMEPLAY_STEP      = 2100;
    public static final int GAMEPLAY_POST_STEP = 2200;
    public static final int GAMEPLAY_DRAW      = 2300;

    public static final int RESERVED_POST_STEP = 3000;
    public static final int RESERVED_PRE_DRAW = 3100;
    public static final int RESERVED_DRAW = 3200;
    public static final int RESERVED_POST_DRAW = 3300;

    private MessagePriority() {}
}