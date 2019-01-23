package net.survival.client.particle;

public class ParticleData
{
    public final double[] x;
    public final double[] y;
    public final double[] z;
    public final double[] velocityX;
    public final double[] velocityY;
    public final double[] velocityZ;

    public ParticleData(int maxParticles) {
        x = new double[maxParticles];
        y = new double[maxParticles];
        z = new double[maxParticles];
        velocityX = new double[maxParticles];
        velocityY = new double[maxParticles];
        velocityZ = new double[maxParticles];
    }
}