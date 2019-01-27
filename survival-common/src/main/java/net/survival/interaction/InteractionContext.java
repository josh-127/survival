package net.survival.interaction;

import net.survival.actor.message.ActorMessage;
import net.survival.block.message.BlockMessage;
import net.survival.block.message.BreakBlockMessage;
import net.survival.block.message.PlaceBlockMessage;
import net.survival.blocktype.Block;
import net.survival.particle.message.BurstParticlesMessage;
import net.survival.particle.message.ParticleMessage;

public interface InteractionContext
{
    double getElapsedTime();

    Block getBlock(int x, int y, int z);

    void postMessage(ActorMessage message);

    void postMessage(BlockMessage message);

    default void breakBlock(int x, int y, int z) {
        postMessage(new BreakBlockMessage(x, y, z));
    }

    default void placeBlock(int x, int y, int z, int fullID) {
        postMessage(new PlaceBlockMessage(x, y, z, fullID));
    }

    void postMessage(ParticleMessage message);

    default void burstParticles(double x, double y, double z, double strength, int quantity) {
        postMessage(new BurstParticlesMessage(x, y, z, strength, quantity));
    }
}