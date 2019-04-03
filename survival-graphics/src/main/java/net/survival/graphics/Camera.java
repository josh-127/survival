package net.survival.graphics;

import org.joml.Matrix4f;

class Camera
{
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float pitch;

    public float fov;
    public float width;
    public float height;
    public float nearClipPlane;
    public float farClipPlane;

    public void getViewMatrix(Matrix4f dest) {
        dest.lookAt(
                x, y, z,
                x + (float) (Math.sin(yaw) * Math.cos(pitch)),
                y + (float) Math.sin(pitch),
                z - (float) (Math.cos(yaw) * Math.cos(pitch)),
                0.0f, 1.0f, 0.0f);
    }

    public void getProjectionMatrix(Matrix4f dest) {
        dest.perspective(
                fov,
                width / height,
                nearClipPlane,
                farClipPlane);
    }

    public float getDirectionX() {
        return (float) (Math.sin(yaw) * Math.cos(pitch));
    }

    public float getDirectionY() {
        return (float) Math.sin(pitch);
    }

    public float getDirectionZ() {
        return (float) -(Math.cos(yaw) * Math.cos(pitch));
    }

    public int getDominantAxis() {
        var lx = Math.abs(getDirectionX());
        var ly = Math.abs(getDirectionY());
        var lz = Math.abs(getDirectionZ());
        var maxValue = Math.max(Math.max(lx, ly), lz);

        if (maxValue == lx) return 0;
        if (maxValue == ly) return 1;
        return 2;
    }
}