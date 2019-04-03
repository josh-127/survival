package net.survival.graphics;

public final class VisibilityFlags
{
    public static final int BLOCKS = 0x1;
    public static final int ENTITIES = 0x2;
    public static final int PARTICLES = 0x4;
    public static final int SKYBOX = 0x8;
    public static final int CLOUDS = 0x10;
    public static final int HUD = 0x20;
    public static final int DEBUG_GEOMETRY = 0x40;

    public static final int DEFAULT =
            BLOCKS |
            ENTITIES |
            PARTICLES |
            SKYBOX |
            CLOUDS |
            HUD |
            DEBUG_GEOMETRY;

    private VisibilityFlags() {}
}