package net.survival.client;

import net.survival.actor.ActorSpace;
import net.survival.block.BlockSpace;
import net.survival.block.ColumnPos;
import net.survival.blocktype.Block;
import net.survival.blocktype.BlockType;
import net.survival.client.particle.ClientParticleSpace;
import net.survival.interaction.InteractionContext;
import net.survival.interaction.Message;
import net.survival.interaction.MessageQueue;
import net.survival.util.MathEx;

class LocalInteractionContext implements InteractionContext
{
    private final ActorSpace actorSpace;
    private final BlockSpace blockSpace;
    private final ClientParticleSpace particleSpace;
    private final MessageQueue messageQueue;

    private double elapsedTime;

    public LocalInteractionContext(
            ActorSpace actorSpace,
            BlockSpace blockSpace,
            ClientParticleSpace particleSpace,
            MessageQueue messageQueue)
    {
        this.actorSpace = actorSpace;
        this.blockSpace = blockSpace;
        this.particleSpace = particleSpace;
        this.messageQueue = messageQueue;
    }

    @Override
    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double to) {
        elapsedTime = to;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return BlockType.byFullID(blockSpace.getBlockFullID(x, y, z));
    }

    @Override
    public Block raycastBlock(double x, double y, double z, double dx, double dy, double dz) {
        var targetX = x + dx;
        var targetY = y + dy;
        var targetZ = z + dz;

        for (var t = 0.0; t <= 1.0; t += 0.05) {
            var bx = (int) Math.floor(MathEx.lerp(x, targetX, t));
            var by = (int) Math.floor(MathEx.lerp(y, targetY, t));
            var bz = (int) Math.floor(MathEx.lerp(z, targetZ, t));
            var block = getBlock(bx, by, bz);

            if (block.isSolid()) {
                return block;
            }
        }

        return null;
    }

    @Override
    public boolean isInStagedColumn(int x, int y, int z) {
        return blockSpace.containsColumn(ColumnPos.toColumnX(x), ColumnPos.toColumnZ(z));
    }

    @Override
    public boolean isInStagedColumn(double x, double y, double z) {
        return isInStagedColumn((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    @Override
    public void postMessage(Message message) {
        messageQueue.enqueueMessage(message);
    }
}