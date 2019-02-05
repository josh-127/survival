package net.survival.particle.message;

import net.survival.interaction.InteractionContext;
import net.survival.interaction.Message;
import net.survival.interaction.MessageVisitor;

public abstract class ParticleMessage extends Message
{
    @Override
    public void accept(MessageVisitor visitor, InteractionContext ic) {
        visitor.visit(ic, this);
    }

    public abstract void accept(ParticleMessageVisitor visitor);
}