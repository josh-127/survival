package net.survival.graphics;

import org.joml.Matrix4f;

class PerspectiveCamera implements Camera {
    private float x;
    private float y;
    private float z;
    private float yaw;
    private float pitch;

    private float fov;
    private float width;
    private float height;
    private float nearClipPlane;
    private float farClipPlane;

    @Override
    public void getViewMatrix(Matrix4f dest) {
        dest.lookAt(
                x, y, z,
                x + (float) (Math.sin(yaw) * Math.cos(pitch)),
                y + (float) Math.sin(pitch),
                z - (float) (Math.cos(yaw) * Math.cos(pitch)),
                0.0f, 1.0f, 0.0f);
    }

    @Override
    public void getProjectionMatrix(Matrix4f dest) {
        dest.perspective(
                fov,
                width / height,
                nearClipPlane,
                farClipPlane);
    }

    public float getX() {
        return x;
    }

    public void setX(float to) {
        x = to;
    }

    public float getY() {
        return y;
    }

    public void setY(float to) {
        y = to;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float to) {
        z = to;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float to) {
        yaw = to;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float to) {
        pitch = to;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float to) {
        fov = to;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float to) {
        width = to;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float to) {
        height = to;
    }

    public float getNearClipPlane() {
        return nearClipPlane;
    }

    public void setNearClipPlane(float to) {
        nearClipPlane = to;
    }

    public float getFarClipPlane() {
        return farClipPlane;
    }

    public void setFarClipPlane(float to) {
        farClipPlane = to;
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