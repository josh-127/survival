package net.survival.client.graphics;

import org.joml.Matrix4f;

public class Camera
{
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

    public Camera() {}

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void moveTo(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void orient(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
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

    public float setHeight() {
        return height;
    }

    public float getAspectRatio() {
        return width / height;
    }

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getNearClipPlane() {
        return nearClipPlane;
    }

    public float getFarClipPlane() {
        return farClipPlane;
    }

    public void setClipPlanes(float nearClipPlane, float farClipPlane) {
        this.nearClipPlane = nearClipPlane;
        this.farClipPlane = farClipPlane;
    }

    public void getViewMatrix(Matrix4f destination) {
        destination.lookAt(
                x, y, z,
                x + (float) (Math.sin(yaw) * Math.cos(pitch)),
                y + (float) Math.sin(pitch),
                z - (float) (Math.cos(yaw) * Math.cos(pitch)),
                0.0f, 1.0f, 0.0f);
    }

    public void getProjectionMatrix(Matrix4f destination) {
        destination.perspective(fov, getAspectRatio(), nearClipPlane, farClipPlane);
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
}