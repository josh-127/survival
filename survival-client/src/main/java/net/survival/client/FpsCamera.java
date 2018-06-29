package net.survival.client;

import org.joml.Matrix4f;
import org.joml.Vector3d;

class FpsCamera
{
    public Vector3d position;
    public double yaw;
    public double pitch;
    
    public FpsCamera(Vector3d position, double yaw, double pitch) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public void moveXZ(double angle, double speed) {
        double dx = Math.sin(yaw + angle) * speed;
        double dz = -Math.cos(yaw + angle) * speed;
        position.x += dx;
        position.z += dz;
    }
    
    public void moveY(double delta) {
        position.y += delta;
    }
    
    public void rotate(double dYaw, double dPitch) {
        yaw -= dYaw;
        pitch += dPitch;
        
        if (yaw < 0.0)
            yaw += Math.PI * 2.0;
        else if (yaw >= Math.PI * 2.0)
            yaw -= Math.PI * 2.0;
        
        if (pitch < -Math.PI / 2.03125)
            pitch = -Math.PI / 2.03125;
        else if (pitch > Math.PI / 2.03125)
            pitch = Math.PI / 2.03125;
    }
    
    public Matrix4f viewMatrix()
    {
        return new Matrix4f()
                .lookAt(
                        (float) position.x, (float) position.y, (float) position.z,
                        (float) (position.x + (Math.sin(yaw) * Math.cos(pitch))),
                        (float) (position.y + Math.sin(pitch)),
                        (float) (position.z - (Math.cos(yaw) * Math.cos(pitch))),
                        0.0f, 1.0f, 0.0f
                        );
    }
}