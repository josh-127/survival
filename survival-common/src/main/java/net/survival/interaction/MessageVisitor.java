package net.survival.interaction;

import net.survival.actor.message.ActorMessage;
import net.survival.block.message.BlockMessage;
import net.survival.particle.message.ParticleMessage;

public interface MessageVisitor
{
    void visit(ActorMessage message);
    void visit(BlockMessage message);
    void visit(ParticleMessage message);
}