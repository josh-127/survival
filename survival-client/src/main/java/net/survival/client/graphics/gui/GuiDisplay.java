package net.survival.client.graphics.gui;

import org.joml.Matrix4f;

import net.survival.client.graphics.GraphicsSettings;
import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLOutputMergerState;
import net.survival.client.gui.Control;
import net.survival.util.Rectangle;

public class GuiDisplay
{
    private final Control rootControl;
    
    public GuiDisplay(Control rootControl) {
        this.rootControl = rootControl;
    }
    
    public void display() {
        float aspectRatio = (float) GraphicsSettings.WINDOW_WIDTH / GraphicsSettings.WINDOW_HEIGHT;
        
        GLMatrixStack.setProjectionMatrix(null);
        GLMatrixStack.push();
        GLMatrixStack.load(new Matrix4f().ortho2D(0.0f, aspectRatio, 1.0f, 0.0f));
        
        try (@SuppressWarnings("resource")
        GLOutputMergerState outputMergerState = new GLOutputMergerState().withDepthTest(false)) {
            displayInternal(rootControl);
        }
        
        GLMatrixStack.pop();
    }
    
    private void displayInternal(Control control) {
        Rectangle clientRect = control.getClientRectangle();
        float left           = (float) clientRect.getLeft();
        float top            = (float) clientRect.getTop();
        float right          = (float) clientRect.getRight();
        float bottom         = (float) clientRect.getBottom();
        
        GLImmediateDrawCall drawCall = GLImmediateDrawCall.beginTriangles(null);
        drawCall.color(1.0f, 1.0f, 1.0f);
        drawCall.texCoord(0.0f, 1.0f);
        drawCall.vertex(left, bottom, 0.0f);
        drawCall.vertex(right, bottom, 0.0f);
        drawCall.vertex(right, top, 0.0f);
        drawCall.vertex(right, top, 0.0f);
        drawCall.vertex(left, top, 0.0f);
        drawCall.vertex(left, bottom, 0.0f);
        drawCall.end();
        
        GLMatrixStack.push();
        GLMatrixStack.scale(0.1f, 0.1f, 1.0f);
        FontRenderer.drawText(control.getFont(), control.getText());
        GLMatrixStack.pop();
        
        for (Control child : control.getChildren()) {
            displayInternal(child);
        }
    }
}