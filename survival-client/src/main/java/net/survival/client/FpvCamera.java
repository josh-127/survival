package net.survival.client;

class FpvCamera {
    public double yaw;
    public double pitch;
    
    public FpvCamera(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
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
}