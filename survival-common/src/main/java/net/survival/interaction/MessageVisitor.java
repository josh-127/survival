package net.survival.interaction;

import net.survival.actor.message.ActorMessage;
import net.survival.block.message.BlockMessage;
import net.survival.particle.message.ParticleMessage;
import net.survival.render.message.RenderMessage;

public interface MessageVisitor
{
    void visit(InteractionContext ic, ActorMessage message);
    void visit(InteractionContext ic, BlockMessage message);
    void visit(InteractionContext ic, ParticleMessage message);
    void visit(InteractionContext ic, RenderMessage message);
}