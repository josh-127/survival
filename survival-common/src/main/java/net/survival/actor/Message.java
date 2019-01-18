package net.survival.actor;

import net.survival.actor.interaction.InteractionContext;

public abstract class Message
{
    public abstract void accept(MessageVisitor visitor, InteractionContext ic);
}