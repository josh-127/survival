package net.survival.client.graphics;

import org.joml.Matrix4f;

import net.survival.actor.ActorSpace;
import net.survival.block.BlockSpace;
import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.client.graphics.opengl.GLState;
import net.survival.client.particle.ClientParticleSpace;
import net.survival.client.ui.BasicUI;

public class CompositeDisplay implements RenderContext, GraphicsResource
{
    private final Camera camera = new Camera();

    private final BlockDisplay blockDisplay;
    private final ActorDisplay actorDisplay;
    private final ParticleDisplay particleDisplay;
    private final SkyboxDisplay skyboxDisplay = new SkyboxDisplay();
    private final CloudDisplay cloudDisplay = new CloudDisplay();
    private final UIDisplay uiDisplay;

    private int viewportWidth;
    private int viewportHeight;

    private int visibilityFlags = VisibilityFlags.DEFAULT;

    private Matrix4f cameraViewMatrix = new Matrix4f();
    private Matrix4f cameraProjectionMatrix = new Matrix4f();
    private Matrix4f hudProjectionMatrix = new Matrix4f();

    public CompositeDisplay(
            BlockSpace blockSpace,
            ActorSpace actorSpace,
            ClientParticleSpace clientParticleSpace,
            int viewportWidth,
            int viewportHeight,
            BasicUI.Client uiClientPipe)
    {
        blockDisplay = new BlockDisplay(blockSpace, camera, 512.0f);
        actorDisplay = new ActorDisplay(actorSpace, camera);
        particleDisplay = new ParticleDisplay(clientParticleSpace);
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;

        uiDisplay = new UIDisplay(uiClientPipe);
    }

    @Override
    public void close() {
        blockDisplay.close();
        uiDisplay.close();
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
    public void redrawColumn(long hashedPos) {
        blockDisplay.redrawColumn(hashedPos);
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

    @Override
    public long getCloudSeed() {
        return cloudDisplay.getSeed();
    }

    @Override
    public void setCloudSeed(long to) {
        cloudDisplay.setSeed(to);
    }

    @Override
    public float getCloudDensity() {
        return cloudDisplay.getDensity();
    }

    @Override
    public void setCloudDensity(float to) {
        cloudDisplay.setDensity(to);
    }

    @Override
    public float getCloudElevation() {
        return cloudDisplay.getElevation();
    }

    @Override
    public void setCloudElevation(float to) {
        cloudDisplay.setElevation(to);
    }

    @Override
    public float getCloudSpeedX() {
        return cloudDisplay.getSpeedX();
    }

    @Override
    public float getCloudSpeedZ() {
        return cloudDisplay.getSpeedZ();
    }

    @Override
    public void setCloudSpeed(float dx, float dz) {
        cloudDisplay.setSpeed(dx, dz);
    }

    @Override
    public float getCloudAlpha() {
        return cloudDisplay.getAlpha();
    }

    @Override
    public void setCloudAlpha(float to) {
        cloudDisplay.setAlpha(to);
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

        // Display blocks, entities, and clouds.
        GLState.pushFogEnabled(true);
        GLState.pushExpFog(0.00390625f,
                0.5f * (skyboxDisplay.getBottomR() + skyboxDisplay.getTopR()),
                0.5f * (skyboxDisplay.getBottomG() + skyboxDisplay.getTopG()),
                0.5f * (skyboxDisplay.getBottomB() + skyboxDisplay.getTopB()),
                1.0f);
        {
            if (isVisible(VisibilityFlags.BLOCKS))
                blockDisplay.display();

            if (isVisible(VisibilityFlags.ENTITIES))
                actorDisplay.display();

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
            hudProjectionMatrix.ortho2D(0.0f, viewportWidth, viewportHeight, 0.0f);
            GLMatrixStack.setProjectionMatrix(hudProjectionMatrix);

            GLMatrixStack.push();
            GLMatrixStack.loadIdentity();
            uiDisplay.display();
            GLMatrixStack.pop();
        }
    }
}