package net.survival.client.graphics;

public interface RenderContext
{
    float getCameraX();
    float getCameraY();
    float getCameraZ();
    float getCameraYaw();
    float getCameraPitch();
    float getCameraFov();
    float getCameraWidth();
    float getCameraHeight();
    float getCameraNearClipPlane();
    float getCameraFarClipPlane();

    default float getCameraAspectRatio() {
        return getCameraWidth() / getCameraHeight();
    }

    int getVisibilityFlags();
    void setVisibilityFlags(int to);

    default boolean isVisible(int types) {
        return (getVisibilityFlags() & types) != 0;
    }

    default void toggleVisibilityFlags(int flags) {
        setVisibilityFlags(getVisibilityFlags() ^ flags);
    }

    float getSkyboxBottomR();
    float getSkyboxBottomG();
    float getSkyboxBottomB();
    float getSkyboxTopR();
    float getSkyboxTopG();
    float getSkyboxTopB();

    long getCloudSeed();
    float getCloudDensity();
    float getCloudElevation();
    float getCloudSpeedX();
    float getCloudSpeedZ();
    float getCloudAlpha();
}