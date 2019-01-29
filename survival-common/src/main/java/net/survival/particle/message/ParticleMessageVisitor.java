package net.survival.particle.message;

public interface ParticleMessageVisitor
{
    void visit(BurstParticlesMessage message);
}