package net.survival.particle.message;

public class AddParticleEmitterMessage extends ParticleMessage
{
    private final double x;
    private final double y;
    private final double z;

    public AddParticleEmitterMessage(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public void accept(ParticleMessageVisitor visitor) {
        visitor.visit(this);
    }
}