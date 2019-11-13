package net.survival.graphics;

import java.util.ArrayList;

import org.joml.Matrix4f;

import net.survival.graphics.model.ModelRenderer;
import net.survival.graphics.model.StaticModel;
import net.survival.graphics.opengl.GLMatrixStack;

class ModelDisplay
{
    private final ArrayList<DrawModelCommand> modelsToDraw = new ArrayList<>();
    private final PerspectiveCamera camera;

    private Matrix4f cameraViewMatrix = new Matrix4f();
    private Matrix4f cameraProjectionMatrix = new Matrix4f();

    public ModelDisplay(PerspectiveCamera camera) {
        this.camera = camera;
    }

    public void drawModel(DrawModelCommand model) {
        modelsToDraw.add(model);
    }

    public void display() {
        cameraViewMatrix.identity();
        camera.getViewMatrix(cameraViewMatrix);
        cameraProjectionMatrix.identity();
        camera.getProjectionMatrix(cameraProjectionMatrix);

        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.loadIdentity();

        displayPawns(cameraViewMatrix);

        GLMatrixStack.pop();

        modelsToDraw.clear();
    }

    private void displayPawns(Matrix4f viewMatrix) {
        GLMatrixStack.push();
        GLMatrixStack.load(viewMatrix);

        for (var pawn : modelsToDraw)
            displayModel(pawn);

        GLMatrixStack.pop();
    }

    private void displayModel(DrawModelCommand model) {
        if (model.getModelType() != null) {
            GLMatrixStack.push();
            GLMatrixStack.translate(
                    (float) model.getX(),
                    (float) model.getY(),
                    (float) model.getZ());
            GLMatrixStack.rotate((float) model.getYaw(), 0.0f, 1.0f, 0.0f);
            GLMatrixStack.rotate((float) model.getPitch(), 1.0f, 0.0f, 0.0f);
            GLMatrixStack.rotate((float) model.getRoll(), 0.0f, 0.0f, 1.0f);
    
            var displayable = StaticModel.fromModelType(model.getModelType());
            ModelRenderer.displayStaticModel(displayable);
    
            GLMatrixStack.pop();
        }
    }
}