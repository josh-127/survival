package net.survival.client;

import java.util.Queue;

import net.survival.client.particle.ClientParticleSpace;
import net.survival.interaction.ParticleInteractionAdapter;
import net.survival.particle.message.BurstParticlesMessage;
import net.survival.particle.message.ParticleMessage;

public class LocalParticleInteractionAdapter implements ParticleInteractionAdapter
{
    private final ClientParticleSpace clientParticleSpace;
    private final Queue<ParticleMessage> messageQueue;

    public LocalParticleInteractionAdapter(ClientParticleSpace clientParticleSpace, Queue<ParticleMessage> messageQueue) {
        this.clientParticleSpace = clientParticleSpace;
        this.messageQueue = messageQueue;
    }

    @Override
    public void burstParticles(double x, double y, double z, double strength, int quantity) {
        messageQueue.add(new BurstParticlesMessage(x, y, z, strength, quantity));
    }
}