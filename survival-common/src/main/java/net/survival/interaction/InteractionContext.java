package net.survival.interaction;

import net.survival.blocktype.Block;
import net.survival.input.Key;

public class InteractionContext
{
    private final BlockInteractionAdapter blocks;
    private final ParticleInteractionAdapter particles;
    private final KeyboardInteractionAdapter keyboard;
    private final TickInteractionAdapter tick;

    public InteractionContext(
            BlockInteractionAdapter blocks,
            ParticleInteractionAdapter particles,
            KeyboardInteractionAdapter keyboard,
            TickInteractionAdapter tick)
    {
        this.blocks = blocks;
        this.particles = particles;
        this.keyboard = keyboard;
        this.tick = tick;
    }

    public double getElapsedTime() {
        return tick.getElapsedTime();
    }

    public boolean isKeyDown(Key key) {
        return keyboard.isKeyDown(key);
    }

    public boolean isKeyUp(Key key) {
        return keyboard.isKeyUp(key);
    }

    public boolean isKeyPressed(Key key) {
        return keyboard.isKeyPressed(key);
    }

    public boolean isKeyReleased(Key key) {
        return keyboard.isKeyReleased(key);
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