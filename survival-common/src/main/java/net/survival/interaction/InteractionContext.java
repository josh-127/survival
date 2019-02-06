package net.survival.interaction;

import net.survival.block.message.BreakBlockMessage;
import net.survival.block.message.PlaceBlockMessage;
import net.survival.blocktype.Block;
import net.survival.particle.message.BurstParticlesMessage;

public interface InteractionContext
{
    double getElapsedTime();

    Block getBlock(int x, int y, int z);

    Block raycastBlock(double x, double y, double z, double dx, double dy, double dz);

    boolean isInStagedColumn(int x, int y, int z);
    boolean isInStagedColumn(double x, double y, double z);

    void postMessage(Message message);

    default void breakBlock(int x, int y, int z) {
        postMessage(new BreakBlockMessage(x, y, z));
    }

    default void placeBlock(int x, int y, int z, int fullID) {
        postMessage(new PlaceBlockMessage(x, y, z, fullID));
    }

    default void burstParticles(double x, double y, double z, double strength, int quantity) {
        postMessage(new BurstParticlesMessage(x, y, z, strength, quantity));
    }
}