package net.survival.gen.structure;

import net.survival.interaction.InteractionContext;

public interface StructureMessageVisitor
{
    void visit(InteractionContext ic, GenerateStructureMessage message);
}