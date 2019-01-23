package net.survival.client.particle;

import java.util.ArrayList;

import net.survival.particle.message.AddParticleEmitterMessage;
import net.survival.particle.message.BurstParticlesMessage;
import net.survival.particle.message.ParticleMessageVisitor;

public class ClientParticleSpace implements ParticleMessageVisitor
{
    private final ArrayList<ClientParticleEmitter> objects = new ArrayList<>();

    public Iterable<ClientParticleEmitter> iterateParticleEmitters() {
        return objects;
    }

    @Override
    public void visit(AddParticleEmitterMessage message) {
        objects.add(new ClientParticleEmitter(message.getX(), message.getY(), message.getZ()));
    }

    @Override
    public void visit(BurstParticlesMessage message) {
    }
}