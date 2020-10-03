package net.survival.graphics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.joml.Matrix4f;

import net.survival.block.Column;
import net.survival.graphics.opengl.GLMatrixStack;
import net.survival.graphics.opengl.GLRenderContext;
import net.survival.graphics.opengl.GLState;
import net.survival.graphics.particle.ClientParticleSpace;
import net.survival.render.ModelType;

public class CompositeDisplay implements GraphicsResource {
    private static final String ASSET_ROOT_PATH = "./assets/";
    private static final String TEXTURE_PATH = ASSET_ROOT_PATH;
    private static final String FONT_PATH = "textures/fonts/default";
    private static final int PIXELS_PER_EM = 24;

    private final PerspectiveCamera camera = new PerspectiveCamera();

    private final BlockDisplay blockDisplay;
    private final ModelDisplay modelDisplay;
    private final ParticleDisplay particleDisplay;
    private final SkyboxDisplay skyboxDisplay = new SkyboxDisplay();
    private final CloudDisplay cloudDisplay = new CloudDisplay();
    private final TextRenderer textRenderer;

    private final Queue<DrawTextCommand> drawTextCommandQueue;

    private int viewportWidth;
    private int viewportHeight;

    private int visibilityFlags = VisibilityFlags.DEFAULT;

    private Matrix4f cameraViewMatrix = new Matrix4f();
    private Matrix4f cameraProjectionMatrix = new Matrix4f();
    private Matrix4f textProjectionMatrix = new Matrix4f();

    private List<InvalidateColumnDto> columnsToInvalidate = new ArrayList<InvalidateColumnDto>();

    public CompositeDisplay(
            ClientParticleSpace clientParticleSpace,
            int viewportWidth,
            int viewportHeight)
    {
        blockDisplay = new BlockDisplay(camera);
        modelDisplay = new ModelDisplay(camera);
        particleDisplay = new ParticleDisplay(clientParticleSpace, camera);
        textRenderer = new TextRenderer(FONT_PATH);

        drawTextCommandQueue = new LinkedList<>();

        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;

        var mipmappedTextureAtlas = new TextureAtlas(TEXTURE_PATH, true);
        var textureAtlas = new TextureAtlas(TEXTURE_PATH, false);
        Assets.setup(mipmappedTextureAtlas, textureAtlas);
    }

    @Override
    public void close() {
        blockDisplay.close();
        Assets.tearDown();
    }

    public float getCameraX() { return camera.getX(); }
    public float getCameraY() { return camera.getY(); }
    public float getCameraZ() { return camera.getZ(); }
    public float getCameraYaw() { return camera.getYaw(); }
    public float getCameraPitch() { return camera.getPitch(); }
    public float getCameraFov() { return camera.getFov(); }
    public float getCameraWidth() { return camera.getWidth(); }
    public float getCameraHeight() { return camera.getHeight(); }
    public float getCameraNearClipPlane() { return camera.getNearClipPlane(); }
    public float getCameraFarClipPlane() { return camera.getFarClipPlane(); }
    public int getVisibilityFlags() { return visibilityFlags; }
    public void setVisibilityFlags(int to) { visibilityFlags = to; }
    public float getSkyboxBottomR() { return skyboxDisplay.getBottomR(); }
    public float getSkyboxBottomG() { return skyboxDisplay.getBottomG(); }
    public float getSkyboxBottomB() { return skyboxDisplay.getBottomB(); }
    public float getSkyboxTopR() { return skyboxDisplay.getTopR(); }
    public float getSkyboxTopG() { return skyboxDisplay.getTopG(); }
    public float getSkyboxTopB() { return skyboxDisplay.getTopB(); }
    public long getCloudSeed() { return cloudDisplay.getSeed(); }
    public float getCloudDensity() { return cloudDisplay.getDensity(); }
    public float getCloudElevation() { return cloudDisplay.getElevation(); }
    public float getCloudSpeedX() { return cloudDisplay.getSpeedX(); }
    public float getCloudSpeedZ() { return cloudDisplay.getSpeedZ(); }
    public float getCloudAlpha() { return cloudDisplay.getAlpha(); }

    public int getViewportWidth() { return viewportWidth; }
    public int getViewportHeight() { return viewportHeight; }
    public float getViewportAspectRatio() { return (float) viewportWidth / viewportHeight; }

    public boolean isVisible(int flag) { return (visibilityFlags & flag) != 0; }
    public void toggleVisibilityFlags(int flags) { visibilityFlags ^= flags; }

    public void tick(double elapsedTime) {
        cloudDisplay.tick(elapsedTime);
    }

    public void display() {
        cameraViewMatrix.identity();
        camera.getViewMatrix(cameraViewMatrix);
        cameraProjectionMatrix.identity();
        camera.getProjectionMatrix(cameraProjectionMatrix);

        Assets.getMipmappedTextureAtlas().updateTextures();
        Assets.getTextureAtlas().updateTextures();

        // Clears color and depth buffers.
        GLRenderContext.clearColorBuffer(0.0f, 0.0f, 0.0f, 0.0f);
        GLRenderContext.clearDepthBuffer(1.0f);

        // Display skybox.
        if (isVisible(VisibilityFlags.SKYBOX))
            skyboxDisplay.display(cameraViewMatrix, cameraProjectionMatrix);

        // Display blocks, entities, and clouds.
        GLState.pushFogEnabled(true);
        GLState.pushLinearFog(16.0f, 128.0f,
                skyboxDisplay.getMiddleR(),
                skyboxDisplay.getMiddleG(),
                skyboxDisplay.getMiddleB(),
                1.0f);
        {
            for (int i = 0; i < 6 && !columnsToInvalidate.isEmpty(); ++i) {
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
                modelDisplay.display();

            if (isVisible(VisibilityFlags.PARTICLES))
                particleDisplay.display();

            if (isVisible(VisibilityFlags.CLOUDS)) {
                cloudDisplay.display(
                        cameraViewMatrix,
                        cameraProjectionMatrix,
                        camera.getX(),
                        camera.getZ());
            }
        }
        GLState.popFogParams();
        GLState.popFogEnabled();

        // Draw Text
        if (isVisible(VisibilityFlags.HUD)) {
            textProjectionMatrix.identity();
            textProjectionMatrix.ortho2D(
                    0.0f,
                    (float) viewportWidth / PIXELS_PER_EM,
                    (float) viewportHeight / PIXELS_PER_EM,
                    0.0f);
            GLMatrixStack.setProjectionMatrix(textProjectionMatrix);

            GLMatrixStack.push();
            GLMatrixStack.loadIdentity();

            while (!drawTextCommandQueue.isEmpty()) {
                var command = drawTextCommandQueue.remove();
                var text = command.getText();
                var x = command.getX();
                var y = command.getY();
                var z = command.getZ();
                textRenderer.drawText(text, TextStyle.DEFAULT, x, y, z);
            }

            GLMatrixStack.pop();
        }
    }

    public void drawModel(
            double x, double y, double z,
            double yaw, double pitch, double roll,
            double scaleX, double scaleY, double scaleZ,
            ModelType modelType)
    {
        modelDisplay.drawModel(new DrawModelCommand(
                x, y, z,
                yaw, pitch, roll,
                scaleX, scaleY, scaleZ,
                modelType));
    }

    public void invalidateColumn(
            long columnPos,
            Column column,
            ColumnInvalidationPriority invalidationPriority)
    {
        columnsToInvalidate = columnsToInvalidate.stream()
                .filter(e -> e.getColumnPos() != columnPos)
                .collect(Collectors.toList());

        columnsToInvalidate.add(
                new InvalidateColumnDto(
                        columnPos,
                        column,
                        invalidationPriority));
    }

    public void moveCamera(float x, float y, float z) {
        camera.setX(x);
        camera.setY(y);
        camera.setZ(z);
    }

    public void orientCamera(float yaw, float pitch) {
        camera.setYaw(yaw);
        camera.setPitch(pitch);
        // TODO: Missing Camera::roll.
    }

    public void setCameraParams(float fov, float width, float height, float nearClipPlane, float farClipPlane) {
        camera.setFov(fov);
        camera.setWidth(width);
        camera.setHeight(height);
        camera.setNearClipPlane(nearClipPlane);
        camera.setFarClipPlane(farClipPlane);
    }

    public void setCloudParams(
            long seed,
            float density,
            float elevation,
            float speedX,
            float speedZ,
            float alpha)
    {
        cloudDisplay.setSeed(seed);
        cloudDisplay.setDensity(density);
        cloudDisplay.setElevation(elevation);
        cloudDisplay.setSpeed(speedX, speedZ);
        cloudDisplay.setAlpha(alpha);
    }

    public void setSkyColor(
            float br, float bg, float bb,
            float mr, float mg, float mb,
            float tr, float tg, float tb)
    {
        skyboxDisplay.setColor(br, bg, bb, mr, mg, mb, tr, tg, tb);
    }

    public void drawLabel(String text, double fontSize, double left, double top) {
        drawTextCommandQueue.add(new DrawTextCommand(text, left, top, 0.0));
    }
}