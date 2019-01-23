package net.survival.client.graphics;

import org.joml.Matrix4f;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.particle.ClientParticleEmitter;
import net.survival.client.particle.ClientParticleSpace;

class ParticleDisplay
{
    private final ClientParticleSpace clientParticleSpace;
    private final Camera camera;

    private final Matrix4f cameraViewMatrix = new Matrix4f();
    private final Matrix4f cameraProjectionMatrix = new Matrix4f();

    public ParticleDisplay(ClientParticleSpace clientParticleSpace, Camera camera) {
        this.clientParticleSpace = clientParticleSpace;
        this.camera = camera;
    }

    public void display() {
        cameraViewMatrix.identity();
        camera.getViewMatrix(cameraViewMatrix);
        cameraProjectionMatrix.identity();
        camera.getProjectionMatrix(cameraProjectionMatrix);

        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.load(cameraViewMatrix);

        displayParticles();

        GLMatrixStack.pop();
    }

    private void displayParticles() {
        GLImmediateDrawCall drawCall = GLImmediateDrawCall.beginTriangles(null);
        drawCall.color(1.0f, 1.0f, 1.0f);

        for (ClientParticleEmitter clientParticleEmitter : clientParticleSpace.iterateParticleEmitters()) {
            float x = (float) clientParticleEmitter.getX();
            float y = (float) clientParticleEmitter.getY();
            float z = (float) clientParticleEmitter.getZ();
            displayBillboard(drawCall, x, y, z, 1.0f);
        }

        drawCall.end();
    }

    private void displayBillboard(GLImmediateDrawCall drawCall, float x, float y, float z, float size) {
        float halfSize = size / 2.0f;
        drawCall.vertex(x - halfSize, y, z - halfSize);
        drawCall.vertex(x + halfSize, y, z - halfSize);
        drawCall.vertex(x + halfSize, y, z + halfSize);
        drawCall.vertex(x + halfSize, y, z + halfSize);
        drawCall.vertex(x - halfSize, y, z + halfSize);
        drawCall.vertex(x - halfSize, y, z - halfSize);
    }
}