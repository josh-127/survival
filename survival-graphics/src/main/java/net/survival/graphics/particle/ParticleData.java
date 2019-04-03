package net.survival.graphics.particle;

public class ParticleData
{
    public final double[] xs;
    public final double[] ys;
    public final double[] zs;
    public final double[] velocityXs;
    public final double[] velocityYs;
    public final double[] velocityZs;
    public final int maxParticles;
    private int nextIndex;

    public ParticleData(int maxParticles) {
        xs = new double[maxParticles];
        ys = new double[maxParticles];
        zs = new double[maxParticles];
        velocityXs = new double[maxParticles];
        velocityYs = new double[maxParticles];
        velocityZs = new double[maxParticles];
        this.maxParticles = maxParticles;
    }

    public void addParticle(double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        xs[nextIndex] = x;
        ys[nextIndex] = y;
        zs[nextIndex] = z;
        velocityXs[nextIndex] = velocityX;
        velocityYs[nextIndex] = velocityY;
        velocityZs[nextIndex] = velocityZ;
        nextIndex = (nextIndex + 1) % maxParticles;
    }
}