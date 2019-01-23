package net.survival.client.particle;

import java.util.ArrayList;

import net.survival.particle.message.AddParticleEmitterMessage;
import net.survival.particle.message.ParticleMessageVisitor;

public class ClientParticleSpace implements ParticleMessageVisitor
{
    private final ArrayList<ClientParticleEmitter> objects = new ArrayList<>();

    @Override
    public void visit(AddParticleEmitterMessage message) {
        objects.add(new ClientParticleEmitter(message.getX(), message.getY(), message.getZ()));
    }
}