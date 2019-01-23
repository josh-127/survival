package net.survival.client.particle;

import java.util.ArrayList;

import net.survival.particle.message.AddParticleEmitterMessage;
import net.survival.particle.message.BurstParticlesMessage;
import net.survival.particle.message.ParticleMessageVisitor;

public class ClientParticleSpace implements ParticleMessageVisitor
{
    private final ArrayList<ClientParticleEmitter> objects = new ArrayList<>();
    private final ParticleData data = new ParticleData(1024);

    public Iterable<ClientParticleEmitter> iterateParticleEmitters() {
        return objects;
    }

    @Override
    public void visit(AddParticleEmitterMessage message) {
        objects.add(new ClientParticleEmitter(message.getX(), message.getY(), message.getZ()));
    }

    @Override
    public void visit(BurstParticlesMessage message) {
        for (int i = 0; i < message.getQuantity(); ++i) {
            double velocityX = (Math.random() - 0.5) * 2.0 * message.getStrength();
            double velocityY = (Math.random() - 0.5) * 2.0 * message.getStrength();
            double velocityZ = (Math.random() - 0.5) * 2.0 * message.getStrength();
            data.addParticle(
                    message.getX(),
                    message.getY(),
                    message.getZ(),
                    velocityX,
                    velocityY,
                    velocityZ);
        }
    }

    public ParticleData getData() {
        return data;
    }

    public void step(double elapsedTime) {
        for (int i = 0; i < data.maxParticles; ++i)
            data.xs[i] += data.velocityXs[i] * elapsedTime;
        for (int i = 0; i < data.maxParticles; ++i)
            data.ys[i] += data.velocityYs[i] * elapsedTime;
        for (int i = 0; i < data.maxParticles; ++i)
            data.zs[i] += data.velocityZs[i] * elapsedTime;
    }
}