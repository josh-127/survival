package net.survival.render.message;

import net.survival.interaction.InteractionContext;

public interface RenderMessageVisitor
{
    void visit(InteractionContext ic, DrawModelMessage message);
    void visit(InteractionContext ic, InvalidateColumnMessage message);
    void visit(InteractionContext ic, MoveCameraMessage message);
    void visit(InteractionContext ic, OrientCameraMessage message);
    void visit(InteractionContext ic, SetCameraParamsMessage message);
    void visit(InteractionContext ic, SetCloudParamsMessage message);
    void visit(InteractionContext ic, SetSkyColorMessage message);
    void visit(InteractionContext ic, UiRenderMessage message);
}