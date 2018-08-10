package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public class GLRenderContext
{
    private GLRenderContext() {}

    public static void init() {
        GLStateStack.pushCullMode(GLCullMode.BACK);
        GLStateStack.pushFillMode(GLFillMode.FILL);
        GLStateStack.pushFrontFace(GLFrontFace.CCW);

        GLStateStack.pushNoFog();

        GLStateStack.pushDepthTest(true);
        GLStateStack.pushDepthFunction(GLDepthFunction.LEQUAL);
        GLStateStack.pushDepthWriteMask(true);

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