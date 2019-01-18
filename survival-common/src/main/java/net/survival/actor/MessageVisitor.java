package net.survival.actor;

import net.survival.actor.interaction.InteractionContext;

public interface MessageVisitor
{
    default void visit(InteractionContext ic, StepMessage message) {}

    default void visit(InteractionContext ic, DrawMessage message) {}
}