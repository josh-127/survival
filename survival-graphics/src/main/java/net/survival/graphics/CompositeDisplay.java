package net.survival.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.joml.Matrix4f;

import net.survival.graphics.opengl.GLImmediateDrawCall;
import net.survival.graphics.opengl.GLMatrixStack;
import net.survival.graphics.opengl.GLRenderContext;
import net.survival.graphics.opengl.GLState;
import net.survival.graphics.particle.ClientParticleSpace;
import net.survival.interaction.InteractionContext;
import net.survival.render.message.ColumnInvalidationPriority;
import net.survival.render.message.DrawModelMessage;
import net.survival.render.message.InvalidateColumnMessage;
import net.survival.render.message.MoveCameraMessage;
import net.survival.render.message.OrientCameraMessage;
import net.survival.render.message.RenderMessageVisitor;
import net.survival.render.message.SetCameraParamsMessage;
import net.survival.render.message.SetCloudParamsMessage;
import net.survival.render.message.SetSkyColorMessage;
import net.survival.render.message.UiRenderMessage;

public class CompositeDisplay implements RenderContext, GraphicsResource, RenderMessageVisitor
{
    private final Camera camera = new Camera();

    private final BlockDisplay blockDisplay;
    private final ActorDisplay actorDisplay;
    private final ParticleDisplay particleDisplay;
    private final SkyboxDisplay skyboxDisplay = new SkyboxDisplay();
    private final CloudDisplay cloudDisplay = new CloudDisplay();
    private final UiDisplay uiDisplay = new UiDisplay();

    private int viewportWidth;
    private int viewportHeight;

    private int visibilityFlags = VisibilityFlags.DEFAULT;

    private Matrix4f cameraViewMatrix = new Matrix4f();
    private Matrix4f cameraProjectionMatrix = new Matrix4f();
    private Matrix4f hudProjectionMatrix = new Matrix4f();

    private List<InvalidateColumnMessage> columnsToInvalidate = new ArrayList<InvalidateColumnMessage>();

    public CompositeDisplay(
            ClientParticleSpace clientParticleSpace,
            int viewportWidth,
            int viewportHeight)
    {
        blockDisplay = new BlockDisplay(camera);
        actorDisplay = new ActorDisplay(camera);
        particleDisplay = new ParticleDisplay(clientParticleSpace, camera);

        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    @Override
    public void close() {
        blockDisplay.close();
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
    public float getCameraYaw() {
        return camera.yaw;
    }

    @Override
    public float getCameraPitch() {
        return camera.pitch;
    }

    @Override
    public float getCameraFov() {
        return camera.fov;
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
    public float getCameraNearClipPlane() {
        return camera.nearClipPlane;
    }

    @Override
    public float getCameraFarClipPlane() {
        return camera.farClipPlane;
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
    public long getCloudSeed() {
        return cloudDisplay.getSeed();
    }

    @Override
    public float getCloudDensity() {
        return cloudDisplay.getDensity();
    }

    @Override
    public float getCloudElevation() {
        return cloudDisplay.getElevation();
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
    public float getCloudAlpha() {
        return cloudDisplay.getAlpha();
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
            for (int i = 0; i < 3 && !columnsToInvalidate.isEmpty(); ++i) {
                var lastIndex = columnsToInvalidate.size() - 1;
                var message = columnsToInvalidate.remove(lastIndex);
                var columnPos = message.getColumnPos();
                var column = message.getColumn();
                blockDisplay.redrawColumn(columnPos, column);
            }

            for (int i = columnsToInvalidate.size() - 1; i >= 0; --i) {
                var message = columnsToInvalidate.get(i);
                if (message.getInvalidationPriority().equals(ColumnInvalidationPriority.NOW)) {
                    var columnPos = message.getColumnPos();
                    var column = message.getColumn();
                    blockDisplay.redrawColumn(columnPos, column);
                }
            }

            if (isVisible(VisibilityFlags.BLOCKS))
                blockDisplay.display();

            if (isVisible(VisibilityFlags.ENTITIES))
                actorDisplay.display();

            if (isVisible(VisibilityFlags.PARTICLES))
                particleDisplay.display();

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

        // Display HUD and new UI system.
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

    @Override
    public void visit(InteractionContext ic, DrawModelMessage message) {
        actorDisplay.drawModel(message);
    }

    @Override
    public void visit(InteractionContext ic, InvalidateColumnMessage message) {
        var columnPos = message.getColumnPos();

        columnsToInvalidate = columnsToInvalidate.stream()
                .filter(e -> e.getColumnPos() != columnPos)
                .collect(Collectors.toList());
        columnsToInvalidate.add(message);
    }

    @Override
    public void visit(InteractionContext ic, MoveCameraMessage message) {
        camera.x = message.getX();
        camera.y = message.getY();
        camera.z = message.getZ();
    }

    @Override
    public void visit(InteractionContext ic, OrientCameraMessage message) {
        camera.yaw = message.getYaw();
        camera.pitch = message.getPitch();
        // TODO: Missing Camera::roll.
    }
    
    @Override
    public void visit(InteractionContext ic, SetCameraParamsMessage message) {
        camera.fov = message.getFov();
        camera.width = message.getWidth();
        camera.height = message.getHeight();
        camera.nearClipPlane = message.getNearClipPlane();
        camera.farClipPlane = message.getFarClipPlane();
    }

    @Override
    public void visit(InteractionContext ic, SetCloudParamsMessage message) {
        cloudDisplay.setSeed(message.getSeed());
        cloudDisplay.setDensity(message.getDensity());
        cloudDisplay.setElevation(message.getElevation());
        cloudDisplay.setSpeed(message.getSpeedX(), message.getSpeedZ());
        cloudDisplay.setAlpha(message.getAlpha());
    }

    @Override
    public void visit(InteractionContext ic, SetSkyColorMessage message) {
        var br = message.getBottomR();
        var bg = message.getBottomG();
        var bb = message.getBottomB();
        var tr = message.getTopR();
        var tg = message.getTopG();
        var tb = message.getTopB();
        skyboxDisplay.setColor(br, bg, bb, tr, tg, tb);
    }

    @Override
    public void visit(InteractionContext ic, UiRenderMessage message) {
        uiDisplay.drawControl(message);
    }
}