package net.survival.client;

import java.util.Queue;

import net.survival.actor.ActorSpace;
import net.survival.actor.message.ActorMessage;
import net.survival.block.BlockSpace;
import net.survival.block.message.BlockMessage;
import net.survival.blocktype.Block;
import net.survival.client.particle.ClientParticleSpace;
import net.survival.interaction.InteractionContext;
import net.survival.particle.message.ParticleMessage;

class LocalInteractionContext implements InteractionContext
{
    private final ActorSpace actorSpace;
    private final BlockSpace blockSpace;
    private final ClientParticleSpace particleSpace;
    private final Queue<ActorMessage> actorMessages;
    private final Queue<BlockMessage> blockMessages;
    private final Queue<ParticleMessage> particleMessages;

    private double elapsedTime;

    public LocalInteractionContext(
            ActorSpace actorSpace,
            BlockSpace blockSpace,
            ClientParticleSpace particleSpace,
            Queue<ActorMessage> actorMessages,
            Queue<BlockMessage> blockMessages,
            Queue<ParticleMessage> particleMessages)
    {
        this.actorSpace = actorSpace;
        this.blockSpace = blockSpace;
        this.particleSpace = particleSpace;
        this.actorMessages = actorMessages;
        this.blockMessages = blockMessages;
        this.particleMessages = particleMessages;
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
        return blockSpace.getBlockState(x, y, z);
    }

    @Override
    public void postMessage(ActorMessage message) {
        actorMessages.add(message);
    }

    @Override
    public void postMessage(BlockMessage message) {
        blockMessages.add(message);
    }

    @Override
    public void postMessage(ParticleMessage message) {
        particleMessages.add(message);
    }
}