package net.survival.actor.message;

import net.survival.actor.interaction.InteractionContext;

public interface MessageVisitor
{
    default void visit(InteractionContext ic, MoveMessage message) {}

    default void visit(InteractionContext ic, HurtMessage message) {}

    default void visit(InteractionContext ic, StepMessage message) {}

    default void visit(InteractionContext ic, DrawMessage message) {}
}