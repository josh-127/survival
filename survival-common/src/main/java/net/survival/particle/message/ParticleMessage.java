package net.survival.particle.message;

public abstract class ParticleMessage
{
    public abstract void accept(ParticleMessageVisitor visitor);
}