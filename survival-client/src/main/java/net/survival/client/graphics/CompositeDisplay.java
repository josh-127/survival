package net.survival.client.graphics;

import org.joml.Matrix4f;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.graphics.opengl.GLState;
import net.survival.world.World;

public class CompositeDisplay implements RenderContext, GraphicsResource
{
    private final Camera camera = new Camera();

    private final WorldDisplay worldDisplay;
    private final SkyboxDisplay skyboxDisplay = new SkyboxDisplay();
    private final CloudDisplay cloudDisplay = new CloudDisplay();
    private final FontRenderer fontRenderer = new FontRenderer();

    private int viewportWidth;
    private int viewportHeight;

    private Matrix4f cameraViewMatrix = new Matrix4f();
    private Matrix4f cameraProjectionMatrix = new Matrix4f();
    private Matrix4f hudProjectionMatrix = new Matrix4f();

    public CompositeDisplay(World world, int viewportWidth, int viewportHeight) {
        worldDisplay = new WorldDisplay(world, camera, 512.0f);
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    @Override
    public void close() {
        worldDisplay.close();
        fontRenderer.close();
    }

    @Override
    public void redrawChunk(long hashedPos) {
        worldDisplay.redrawChunk(hashedPos);
    }

    public Camera getCamera() {
        return camera;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    public float getViewportAspectRatio() {
        return (float) viewportWidth / viewportHeight;
    }

    public void tick(double elapsedTime) {
        cloudDisplay.tick(elapsedTime);
    }

    public void display(double frameRate) {
        cameraViewMatrix.identity();
        camera.getViewMatrix(cameraViewMatrix);
        cameraProjectionMatrix.identity();
        camera.getProjectionMatrix(cameraProjectionMatrix);

        // Clears color and depth buffers.
        GLRenderContext.clearColorBuffer(0.0f, 0.0f, 0.0f, 0.0f);
        GLRenderContext.clearDepthBuffer(1.0f);

        // Display skybox.
        skyboxDisplay.draw(cameraViewMatrix, cameraProjectionMatrix);

        // Display world and clouds.
        GLState.pushFogEnabled(true);
        GLState.pushExpFog(0.00390625f, SkyboxDisplay.MIDDLE_R,
                SkyboxDisplay.MIDDLE_G, SkyboxDisplay.MIDDLE_B, 1.0f);
        {
            worldDisplay.display();
            cloudDisplay.display(cameraViewMatrix, cameraProjectionMatrix, camera.getX(), camera.getZ());
        }
        GLState.popFogParams();
        GLState.popFogEnabled();

        // Display axis.
        GLMatrixStack.setProjectionMatrix(cameraProjectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.load(cameraViewMatrix);

        GLImmediateDrawCall.beginLines(null)
                .coloredVertex(-256.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)
                .coloredVertex(256.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f)
                .coloredVertex(0.0f, -256.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                .coloredVertex(0.0f, 256.0f, 0.0f, 0.0f, 1.0f, 0.0f)
                .coloredVertex(0.0f, 0.0f, -256.0f, 0.0f, 0.0f, 1.0f)
                .coloredVertex(0.0f, 0.0f, 256.0f, 0.0f, 0.0f, 1.0f)
                .end();

        GLMatrixStack.pop();

        // Display HUD.
        hudProjectionMatrix.identity();
        hudProjectionMatrix.ortho2D(-getViewportAspectRatio(), getViewportAspectRatio(), -1.0f, 1.0f);
        GLMatrixStack.setProjectionMatrix(hudProjectionMatrix);

        GLMatrixStack.push();
        GLMatrixStack.loadIdentity();
        GLMatrixStack.translate(-getViewportAspectRatio() * 0.9375f, 0.875f, 0.0f);
        GLMatrixStack.scale(0.05f, 0.05f, 0.05f);
        fontRenderer.drawText(String.valueOf(frameRate), 0.0f, 0.0f, 0.0f);
        GLMatrixStack.pop();
    }
}