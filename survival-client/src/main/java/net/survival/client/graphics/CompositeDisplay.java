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

    private int visibilityFlags = VisibilityFlags.DEFAULT;

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
    public float getCameraX() {
        return camera.x;
    }

    @Override
    public float getCameraY() {
        return camera.y;
    }

    @Override
    public float getCameraZ() {
        return camera.z;
    }

    @Override
    public void moveCamera(float x, float y, float z) {
        camera.x = x;
        camera.y = y;
        camera.z = z;
    }

    @Override
    public float getCameraYaw() {
        return camera.yaw;
    }

    @Override
    public float getCameraPitch() {
        return camera.pitch;
    }

    @Override
    public void orientCamera(float yaw, float pitch) {
        camera.yaw = yaw;
        camera.pitch = pitch;
    }

    @Override
    public float getCameraFov() {
        return camera.fov;
    }

    @Override
    public void setCameraFov(float to) {
        camera.fov = to;
    }

    @Override
    public float getCameraWidth() {
        return camera.width;
    }

    @Override
    public float getCameraHeight() {
        return camera.height;
    }

    @Override
    public void resizeCamera(float width, float height) {
        camera.width = width;
        camera.height = height;
    }

    @Override
    public float getCameraNearClipPlane() {
        return camera.nearClipPlane;
    }

    @Override
    public float getCameraFarClipPlane() {
        return camera.farClipPlane;
    }

    @Override
    public void setCameraClipPlanes(float near, float far) {
        camera.nearClipPlane = near;
        camera.farClipPlane = far;
    }

    @Override
    public int getVisibilityFlags() {
        return visibilityFlags;
    }

    @Override
    public void setVisibilityFlags(int to) {
        visibilityFlags = to;
    }

    @Override
    public void redrawChunk(long hashedPos) {
        worldDisplay.redrawChunk(hashedPos);
    }

    @Override
    public float getSkyboxBottomR() {
        return skyboxDisplay.getBottomR();
    }

    @Override
    public float getSkyboxBottomG() {
        return skyboxDisplay.getBottomG();
    }

    @Override
    public float getSkyboxBottomB() {
        return skyboxDisplay.getBottomB();
    }

    @Override
    public float getSkyboxTopR() {
        return skyboxDisplay.getTopR();
    }

    @Override
    public float getSkyboxTopG() {
        return skyboxDisplay.getTopG();
    }

    @Override
    public float getSkyboxTopB() {
        return skyboxDisplay.getTopB();
    }

    @Override
    public void setSkyboxColor(float br, float bg, float bb, float tr, float tg, float tb) {
        skyboxDisplay.setColor(br, bg, bb, tr, tg, tb);
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
        if (isVisible(VisibilityFlags.SKYBOX))
            skyboxDisplay.display(cameraViewMatrix, cameraProjectionMatrix);

        // Display world and clouds.
        GLState.pushFogEnabled(true);
        GLState.pushExpFog(0.00390625f,
                0.5f * (skyboxDisplay.getBottomR() + skyboxDisplay.getTopR()),
                0.5f * (skyboxDisplay.getBottomG() + skyboxDisplay.getTopG()),
                0.5f * (skyboxDisplay.getBottomB() + skyboxDisplay.getTopB()),
                1.0f);
        {
            if (isVisible(VisibilityFlags.BLOCKS))
                worldDisplay.display();

            if (isVisible(VisibilityFlags.CLOUDS))
                cloudDisplay.display(cameraViewMatrix, cameraProjectionMatrix, camera.x, camera.z);
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
        if (isVisible(VisibilityFlags.HUD)) {
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
}