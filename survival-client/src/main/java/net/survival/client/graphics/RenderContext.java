package net.survival.client.graphics;

import net.survival.block.ColumnPos;

public interface RenderContext
{
    float getCameraX();
    float getCameraY();
    float getCameraZ();
    void moveCamera(float x, float y, float z);

    float getCameraYaw();
    float getCameraPitch();
    void orientCamera(float yaw, float pitch);

    float getCameraFov();
    void setCameraFov(float to);

    float getCameraWidth();
    float getCameraHeight();
    void resizeCamera(float width, float height);
    default float getCameraAspectRatio() {
        return getCameraWidth() / getCameraHeight();
    }

    float getCameraNearClipPlane();
    float getCameraFarClipPlane();
    void setCameraClipPlanes(float near, float far);

    int getVisibilityFlags();
    void setVisibilityFlags(int to);

    default boolean isVisible(int types) {
        return (getVisibilityFlags() & types) != 0;
    }

    default void toggleVisibilityFlags(int flags) {
        setVisibilityFlags(getVisibilityFlags() ^ flags);
    }

    void redrawColumn(long hashedPos);
    default void redrawColumn(int cx, int cz) {
        redrawColumn(ColumnPos.hashPos(cx, cz));
    }

    float getSkyboxBottomR();
    float getSkyboxBottomG();
    float getSkyboxBottomB();
    float getSkyboxTopR();
    float getSkyboxTopG();
    float getSkyboxTopB();
    void setSkyboxColor(float br, float bg, float bb, float tr, float tg, float tb);

    long getCloudSeed();
    void setCloudSeed(long to);

    float getCloudDensity();
    void setCloudDensity(float to);

    float getCloudElevation();
    void setCloudElevation(float to);

    float getCloudSpeedX();
    float getCloudSpeedZ();
    void setCloudSpeed(float dx, float dz);

    float getCloudAlpha();
    void setCloudAlpha(float to);
}