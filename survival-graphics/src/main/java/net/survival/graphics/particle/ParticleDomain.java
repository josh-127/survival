package net.survival.graphics.particle;

public class ParticleDomain
{
    private static final double GRAVITY = -8.0;

    private final ParticleData data = new ParticleData(32);

    public void burstParticles(double x, double y, double z, double strength, int quantity) {
        for (var i = 0; i < quantity; ++i) {
            var velocityX = (Math.random() - 0.5) * 2.0 * strength;
            var velocityY = (Math.random() - 0.5) * 2.0 * strength;
            var velocityZ = (Math.random() - 0.5) * 2.0 * strength;
            data.addParticle(x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    public ParticleData getData() {
        return data;
    }

    public void step(double elapsedTime) {
        for (var i = 0; i < data.maxParticles; ++i)
            data.velocityYs[i] += GRAVITY * elapsedTime;
        for (var i = 0; i < data.maxParticles; ++i)
            data.xs[i] += data.velocityXs[i] * elapsedTime;
        for (var i = 0; i < data.maxParticles; ++i)
            data.ys[i] += data.velocityYs[i] * elapsedTime;
        for (var i = 0; i < data.maxParticles; ++i)
            data.zs[i] += data.velocityZs[i] * elapsedTime;
    }
}