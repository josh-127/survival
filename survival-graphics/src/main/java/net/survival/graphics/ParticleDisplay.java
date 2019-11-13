package net.survival.graphics;

import org.joml.Matrix4f;

import net.survival.graphics.opengl.GLImmediateDrawCall;
import net.survival.graphics.opengl.GLMatrixStack;
import net.survival.graphics.particle.ClientParticleSpace;
import net.survival.util.MathEx;

class ParticleDisplay
{
    private final ClientParticleSpace clientParticleSpace;
    private final PerspectiveCamera camera;

    private final Matrix4f cameraViewMatrix = new Matrix4f();
    private final Matrix4f cameraProjectionMatrix = new Matrix4f();

    public ParticleDisplay(ClientParticleSpace clientParticleSpace, PerspectiveCamera camera) {
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

        var textureAtlas = Assets.getMipmappedTextureAtlas();
        var textures = textureAtlas.getTextureObject();

        var drawCall = GLImmediateDrawCall.beginTriangles(textures);
        drawCall.color(1.0f, 1.0f, 1.0f);

        for (var entry : clientParticleSpace.getParticleDomains().entrySet()) {
            var texture = entry.getKey();
            var particleDomain = entry.getValue();

            var data = particleDomain.getData();
            var maxParticles = data.maxParticles;

            var u1 = textureAtlas.getTexCoordU1(texture);
            var v1 = textureAtlas.getTexCoordV1(texture);
            var u2 = textureAtlas.getTexCoordU2(texture);
            var v2 = textureAtlas.getTexCoordV2(texture);

            final var PADDING = 0.25f;
            var uu1 = u1 + PADDING * (u2 - u1);
            var vv1 = v1 + PADDING * (v2 - v1);
            var uu2 = u1 + (1.0f - PADDING) * (u2 - u1);
            var vv2 = v1 + (1.0f - PADDING) * (v2 - v1);

            for (var i = 0; i < maxParticles; ++i) {
                var x = (float) data.xs[i];
                var y = (float) data.ys[i];
                var z = (float) data.zs[i];
                displayBillboard(
                        drawCall,
                        x, y, z,
                        uu1, vv1, uu2, vv2,
                        camRightX, camRightY, camRightZ,
                        camUpX, camUpY, camUpZ);
            }
        }

        drawCall.end();
    }

    private void displayBillboard(
            GLImmediateDrawCall drawCall,
            float x, float y, float z,
            float u1, float v1, float u2, float v2,
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

        drawCall.texCoord(u1, v1);
        drawCall.vertex(blX, blY, blZ);
        drawCall.texCoord(u2, v1);
        drawCall.vertex(brX, brY, brZ);
        drawCall.texCoord(u2, v2);
        drawCall.vertex(trX, trY, trZ);
        drawCall.texCoord(u2, v2);
        drawCall.vertex(trX, trY, trZ);
        drawCall.texCoord(u1, v2);
        drawCall.vertex(tlX, tlY, tlZ);
        drawCall.texCoord(u1, v1);
        drawCall.vertex(blX, blY, blZ);
    }
}