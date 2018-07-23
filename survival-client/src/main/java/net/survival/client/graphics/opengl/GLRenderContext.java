package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public class GLRenderContext
{
    private GLRenderContext() {}

    public static void init() {
        GLRasterizer.pushCullMode(GLCullMode.BACK);
        GLRasterizer.pushFillMode(GLFillMode.FILL);
        GLRasterizer.pushFrontFace(GLFrontFace.CCW);

        GLFog.pushNoFog();

        GLOutputMerger.pushDepthTest(true);
        GLOutputMerger.pushDepthFunction(GLDepthFunction.LEQUAL);
        GLOutputMerger.pushDepthWriteMask(true);

        glEnable(GL_TEXTURE_2D);
    }

    public static void clearColorBuffer(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public static void clearDepthBuffer(float z) {
        glClearDepth((float) z);
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public static void callDisplayList(GLDisplayList displayList, GLTexture texture) {
        GLTextureEnvironment.useTexture(texture);
        glCallList(displayList.id);
    }
}