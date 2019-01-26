package net.survival.interaction;

import net.survival.blocktype.Block;

public class InteractionContext
{
    private final BlockInteractionAdapter blocks;
    private final ParticleInteractionAdapter particles;
    private final TickInteractionAdapter tick;

    public InteractionContext(
            BlockInteractionAdapter blocks,
            ParticleInteractionAdapter particles,
            TickInteractionAdapter tick)
    {
        this.blocks = blocks;
        this.particles = particles;
        this.tick = tick;
    }

    public double getElapsedTime() {
        return tick.getElapsedTime();
    }

    public Block getBlock(int x, int y, int z) {
        return blocks.getBlock(x, y, z);
    }

    public void breakBlock(int x, int y, int z) {
        blocks.breakBlock(x, y, z);
    }

    public void placeBlock(int x, int y, int z, int fullID) {
        blocks.placeBlock(x, y, z, fullID);
    }

    public void burstParticles(double x, double y, double z, double strength, int quantity) {
        particles.burstParticles(x, y, z, strength, quantity);
    }
}