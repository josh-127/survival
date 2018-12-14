package net.survival.client.graphics;

public final class VisibilityFlags
{
    public static final int BLOCKS = 0x1;
    public static final int ENTITIES = 0x2;
    public static final int SKYBOX = 0x4;
    public static final int CLOUDS = 0x8;
    public static final int HUD = 0x10;
    public static final int DEBUG_GEOMETRY = 0x20;

    public static final int DEFAULT =
            BLOCKS |
            ENTITIES |
            SKYBOX |
            CLOUDS |
            HUD |
            DEBUG_GEOMETRY;

    private VisibilityFlags() {}
}