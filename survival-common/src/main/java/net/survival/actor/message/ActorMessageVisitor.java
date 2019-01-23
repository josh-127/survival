package net.survival.actor.message;

import net.survival.interaction.InteractionContext;

public interface ActorMessageVisitor
{
    default void visit(InteractionContext ic, MoveMessage message) {}
    default void visit(InteractionContext ic, JumpMessage message) {}

    default void visit(InteractionContext ic, HurtMessage message) {}

    default void visit(InteractionContext ic, StepMessage message) {}

    default void visit(InteractionContext ic, DrawMessage message) {}
}