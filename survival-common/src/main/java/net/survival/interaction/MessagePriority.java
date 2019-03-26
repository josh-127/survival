package net.survival.interaction;

public enum MessagePriority
{
    RESERVED_PRE_STEP (1000),
    RESERVED_STEP     (1100),
    GAMEPLAY_PRE_STEP (2000),
    GAMEPLAY_STEP     (2100),
    GAMEPLAY_POST_STEP(2200),
    GAMEPLAY_DRAW     (2300),
    RESERVED_POST_STEP(3000),
    RESERVED_PRE_DRAW (3100),
    RESERVED_DRAW     (3200),
    RESERVED_DRAW_DRAW(3300);

    private int priorityValue;

    private MessagePriority(int priority) {
        this.priorityValue = priority;
    }

    public int getValue() {
        return priorityValue;
    }
}