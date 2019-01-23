package net.survival.particle.message;

public class BurstParticlesMessage extends ParticleMessage
{
    private final double x;
    private final double y;
    private final double z;
    private final double strength;
    private final int quantity;

    public BurstParticlesMessage(double x, double y, double z, double strength, int quantity) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.strength = strength;
        this.quantity = quantity;
    }

    @Override
    public void accept(ParticleMessageVisitor visitor) {
        visitor.visit(this);
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

    public double getStrength() {
        return strength;
    }

    public int getQuantity() {
        return quantity;
    }
}