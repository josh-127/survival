package net.survival.particle.message;

public interface ParticleMessageVisitor
{
    void visit(AddParticleEmitterMessage message);
    void visit(BurstParticlesMessage message);
}