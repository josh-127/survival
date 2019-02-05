package net.survival.interaction;

public abstract class Message
{
    public abstract void accept(MessageVisitor visitor, InteractionContext ic);
}