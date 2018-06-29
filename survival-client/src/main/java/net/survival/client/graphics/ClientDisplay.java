package net.survival.client.graphics;

import org.joml.Matrix4f;

import net.survival.client.graphics.opengl.GLFogState;
import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLRenderContext;
import net.survival.world.World;

public class ClientDisplay implements GraphicsResource
{
    private final Camera camera;
    
    private final WorldDisplay worldDisplay;
    private final SkyboxDisplay skyboxDisplay;
    private final FontRenderer fontRenderer;
    
    private int windowWidth;
    private int windowHeight;
    
    public ClientDisplay(World world, int windowWidth, int windowHeight) {
        camera = new Camera();
        
        worldDisplay = new WorldDisplay(world, camera, 512.0f);
        skyboxDisplay = new SkyboxDisplay();
        
        fontRenderer = new FontRenderer();
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }
    
    @Override
    public void close() {
        worldDisplay.close();
        fontRenderer.close();
    }
    
    public void display(double frameRate) {
        // Clears color and depth buffers
        GLRenderContext.clearColorBuffer(0.0f, 0.0f, 0.0f, 0.0f);
        GLRenderContext.clearDepthBuffer(1.0f);
        
        // Skybox display
        skyboxDisplay.draw(camera.getViewMatrix(), camera.getProjectionMatrix());

        // World display
        try (@SuppressWarnings("resource")
        GLFogState fogState = new GLFogState().useExpFog(0.00390625f, SkyboxDisplay.MIDDLE_R, SkyboxDisplay.MIDDLE_G,
                SkyboxDisplay.MIDDLE_B, 1.0f))
        {
            worldDisplay.display();
        }
        
        // Axis display
        GLMatrixStack.setProjectionMatrix(camera.getProjectionMatrix());
        GLMatrixStack.push();
        GLMatrixStack.load(camera.getViewMatrix());
        
        GLImmediateDrawCall.beginLines(null)
            .coloredVertex(-256.0f, 0.0f,    0.0f,    1.0f, 0.0f, 0.0f)
            .coloredVertex(256.0f,  0.0f,    0.0f,    1.0f, 0.0f, 0.0f)
            .coloredVertex(0.0f,    -256.0f, 0.0f,    0.0f, 1.0f, 0.0f)
            .coloredVertex(0.0f,    256.0f , 0.0f,    0.0f, 1.0f, 0.0f)
            .coloredVertex(0.0f,    0.0f,    -256.0f, 0.0f, 0.0f, 1.0f)
            .coloredVertex(0.0f,    0.0f,    256.0f,  0.0f, 0.0f, 1.0f)
            .end();
        
        GLMatrixStack.pop();
        
        // HUD display
        GLMatrixStack.setProjectionMatrix(
                new Matrix4f().ortho2D(-getWindowAspectRatio(), getWindowAspectRatio(), -1.0f, 1.0f));
        
        GLMatrixStack.push();
        GLMatrixStack.loadIdentity();
        GLMatrixStack.translate(-getWindowAspectRatio() * 0.9375f, 0.875f, 0.0f);
        GLMatrixStack.scale(0.05f, 0.05f, 0.05f);
        fontRenderer.drawText(String.format("%5.2f FPS", frameRate), 0.0f, 0.0f, 0.0f);
        GLMatrixStack.pop();
        
        /*
        GLMatrixStack.push();
        GLMatrixStack.translate(-getWindowAspectRatio() * 0.9375f, 0.75f, 0.0f);
        GLMatrixStack.scale(0.05f, 0.05f, 0.05f);
        fontRenderer.drawText(String.format("X: %d", (int) userController.camera.position.x), 0.0f, 0.0f, 0.0f);
        GLMatrixStack.pop();
        
        GLMatrixStack.push();
        GLMatrixStack.translate(-getWindowAspectRatio() * 0.9375f, 0.6875f, 0.0f);
        GLMatrixStack.scale(0.05f, 0.05f, 0.05f);
        fontRenderer.drawText(String.format("Y: %d", (int) userController.camera.position.y), 0.0f, 0.0f, 0.0f);
        GLMatrixStack.pop();
        
        GLMatrixStack.push();
        GLMatrixStack.translate(-getWindowAspectRatio() * 0.9375f, 0.625f, 0.0f);
        GLMatrixStack.scale(0.05f, 0.05f, 0.05f);
        fontRenderer.drawText(String.format("Z: %d", (int) userController.camera.position.z), 0.0f, 0.0f, 0.0f);
        GLMatrixStack.pop();
        */
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public int getWindowWidth() {
        return windowWidth;
    }
    
    public int getWindowHeight() {
        return windowHeight;
    }
    
    public float getWindowAspectRatio() {
        return (float) windowWidth / windowHeight;
    }
}