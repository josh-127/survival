package net.survival.client.graphics;

import org.joml.Matrix4f;

import net.survival.actor.Actor;
import net.survival.actor.ActorSpace;
import net.survival.client.graphics.model.ModelRenderer;
import net.survival.client.graphics.model.StaticModel;
import net.survival.client.graphics.opengl.GLMatrixStack;

class ActorDisplay
{
    private final ActorSpace actorSpace;
    private final Camera camera;

    private Matrix4f cameraViewMatrix = new Matrix4f();
    private Matrix4f cameraProjectionMatrix = new Matrix4f();

    public ActorDisplay(ActorSpace actorSpace, Camera camera) {
        this.actorSpace = actorSpace;
        this.camera = camera;
    }

    public void display() {
        cameraViewMatrix.identity();
        camera.getViewMatrix(cameraViewMatrix);
        cameraProjectionMatrix.identity();
        camera.getProjectionMatrix(cameraProjectionMatrix);

        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.loadIdentity();

        displayActors(cameraViewMatrix);

        GLMatrixStack.pop();
    }

    private void displayActors(Matrix4f viewMatrix) {
        GLMatrixStack.push();
        GLMatrixStack.load(viewMatrix);

        var actors = actorSpace.iterateActors();

        for (var actor : actors)
            displayActor(actor);

        GLMatrixStack.pop();
    }

    private void displayActor(Actor actor) {
        if (actor.getModel() != null) {
            GLMatrixStack.push();
            GLMatrixStack.translate((float) actor.getX(), (float) actor.getY(), (float) actor.getZ());
            GLMatrixStack.rotate((float) actor.getYaw(), 0.0f, 1.0f, 0.0f);
            GLMatrixStack.rotate((float) actor.getPitch(), 1.0f, 0.0f, 0.0f);
            GLMatrixStack.rotate((float) actor.getRoll(), 0.0f, 0.0f, 1.0f);
    
            var model = StaticModel.fromActor(actor);
            ModelRenderer.displayStaticModel(model);
    
            GLMatrixStack.pop();
        }
    }
}