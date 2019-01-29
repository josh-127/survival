package net.survival.client.graphics;

import org.joml.Matrix4f;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.particle.ClientParticleSpace;
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
        var camUpX = 0.0f;
        var camUpY = 1.0f;
        var camUpZ = 0.0f;
        var camForwardX = camera.getDirectionX();
        var camForwardY = camera.getDirectionY();
        var camForwardZ = camera.getDirectionZ();
        var camRightX = MathEx.crossX(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);
        var camRightY = MathEx.crossY(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);
        var camRightZ = MathEx.crossZ(camForwardX, camForwardY, camForwardZ, camUpX, camUpY, camUpZ);

        var length = MathEx.length(camRightX, camRightY, camRightZ);
        camRightX /= length;
        camRightY /= length;
        camRightZ /= length;

        camUpX *= 0.25f;
        camUpY *= 0.25f;
        camUpZ *= 0.25f;
        camRightX *= 0.25f;
        camRightY *= 0.25f;
        camRightZ *= 0.25f;

        var drawCall = GLImmediateDrawCall.beginTriangles(null);
        drawCall.color(1.0f, 1.0f, 1.0f);

        var data = clientParticleSpace.getData();
        var maxParticles = data.maxParticles;

        for (var i = 0; i < maxParticles; ++i) {
            var x = (float) data.xs[i];
            var y = (float) data.ys[i];
            var z = (float) data.zs[i];
            displayBillboard(drawCall, x, y, z, camRightX, camRightY, camRightZ, camUpX, camUpY, camUpZ);
        }

        drawCall.end();
    }

    private void displayBillboard(
            GLImmediateDrawCall drawCall,
            float x, float y, float z,
            float camRightX, float camRightY, float camRightZ,
            float camUpX, float camUpY, float camUpZ)
    {
        var blX = x - camRightX - camUpX;
        var blY = y - camRightY - camUpY;
        var blZ = z - camRightZ - camUpZ;
        var brX = x + camRightX - camUpX;
        var brY = y + camRightY - camUpY;
        var brZ = z + camRightZ - camUpZ;
        var tlX = x - camRightX + camUpX;
        var tlY = y - camRightY + camUpY;
        var tlZ = z - camRightZ + camUpZ;
        var trX = x + camRightX + camUpX;
        var trY = y + camRightY + camUpY;
        var trZ = z + camRightZ + camUpZ;

        drawCall.vertex(blX, blY, blZ);
        drawCall.vertex(brX, brY, brZ);
        drawCall.vertex(trX, trY, trZ);
        drawCall.vertex(trX, trY, trZ);
        drawCall.vertex(tlX, tlY, tlZ);
        drawCall.vertex(blX, blY, blZ);
    }
}