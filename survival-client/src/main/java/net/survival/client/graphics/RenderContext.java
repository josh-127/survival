package net.survival.client.graphics;

import net.survival.world.chunk.ChunkPos;

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

    void redrawChunk(long hashedPos);
    default void redrawChunk(int cx, int cz) {
        redrawChunk(ChunkPos.hashPos(cx, cz));
    }

    float getSkyboxBottomR();
    float getSkyboxBottomG();
    float getSkyboxBottomB();
    float getSkyboxTopR();
    float getSkyboxTopG();
    float getSkyboxTopB();
    void setSkyboxColor(float br, float bg, float bb, float tr, float tg, float tb);
}