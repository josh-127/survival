package net.survival.client.graphics;

import org.joml.Matrix4f;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.particle.ClientParticleEmitter;
import net.survival.client.particle.ClientParticleSpace;
import net.survival.client.particle.ParticleData;
import net.survival.util.MathEx;

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
        float camUpX = 0.0f;
        float camUpY = 1.0f;
        float camUpZ = 0.0f;
        float camForwardX = camera.getDirectionX();
        float camForwardY = camera.getDirectionY();
        float camForwardZ = camera.getDirectionZ();
        float camRightX = MathEx.crossX(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);
        float camRightY = MathEx.crossY(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);
        float camRightZ = MathEx.crossZ(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);

        float length = MathEx.length(camRightX, camRightY, camRightZ);
        camRightX /= length;
        camRightY /= length;
        camRightZ /= length;

        camUpX *= 0.25f;
        camUpY *= 0.25f;
        camUpZ *= 0.25f;
        camRightX *= 0.25f;
        camRightY *= 0.25f;
        camRightZ *= 0.25f;

        GLImmediateDrawCall drawCall = GLImmediateDrawCall.beginTriangles(null);
        drawCall.color(1.0f, 1.0f, 1.0f);

        ParticleData data = clientParticleSpace.getData();
        int maxParticles = data.maxParticles;

        for (int i = 0; i < maxParticles; ++i) {
            float x = (float) data.xs[i];
            float y = (float) data.ys[i];
            float z = (float) data.zs[i];
            displayBillboard(drawCall, x, y, z, camRightX, camRightY, camRightZ, camUpX, camUpY, camUpZ);
        }

        drawCall.end();
    }

    private void displayBillboard(GLImmediateDrawCall drawCall, float x, float y, float z, float size) {
        float camUpX = 0.0f;
        float camUpY = 1.0f;
        float camUpZ = 0.0f;
        float camForwardX = camera.getDirectionX();
        float camForwardY = camera.getDirectionY();
        float camForwardZ = camera.getDirectionZ();
        float camRightX = MathEx.crossX(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);
        float camRightY = MathEx.crossY(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);
        float camRightZ = MathEx.crossZ(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);

        float length = MathEx.length(camRightX, camRightY, camRightZ);
        camRightX /= length;
        camRightY /= length;
        camRightZ /= length;

        camUpX *= 0.5f;
        camUpY *= 0.5f;
        camUpZ *= 0.5f;
        camRightX *= 0.5f;
        camRightY *= 0.5f;
        camRightZ *= 0.5f;

        displayBillboard(drawCall, x, y, z, camRightX, camRightY, camRightZ, camUpX, camUpY, camUpZ);
    }

    private void displayBillboard(
            GLImmediateDrawCall drawCall,
            float x, float y, float z,
            float camRightX, float camRightY, float camRightZ,
            float camUpX, float camUpY, float camUpZ)
    {
        float blX = x - camRightX - camUpX;
        float blY = y - camRightY - camUpY;
        float blZ = z - camRightZ - camUpZ;
        float brX = x + camRightX - camUpX;
        float brY = y + camRightY - camUpY;
        float brZ = z + camRightZ - camUpZ;
        float tlX = x - camRightX + camUpX;
        float tlY = y - camRightY + camUpY;
        float tlZ = z - camRightZ + camUpZ;
        float trX = x + camRightX + camUpX;
        float trY = y + camRightY + camUpY;
        float trZ = z + camRightZ + camUpZ;

        drawCall.vertex(blX, blY, blZ);
        drawCall.vertex(brX, brY, brZ);
        drawCall.vertex(trX, trY, trZ);
        drawCall.vertex(trX, trY, trZ);
        drawCall.vertex(tlX, tlY, tlZ);
        drawCall.vertex(blX, blY, blZ);
    }
}