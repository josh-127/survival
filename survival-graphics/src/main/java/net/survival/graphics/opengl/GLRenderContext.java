package net.survival.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public class GLRenderContext
{
    private GLRenderContext() {}

    public static void init() {
        GLState.pushAlphaTestEnabled(false);
        GLState.pushBlendEnabled(false);
        GLState.pushCullFaceEnabled(true);
        GLState.pushDepthTestEnabled(true);
        GLState.pushFogEnabled(false);
        GLState.pushScissorTestEnabled(false);
        GLState.pushAlphaFunction(GLComparisonFunc.ALWAYS, 0.0f);
        GLState.pushBlendFunction(GLBlendFactor.ONE, GLBlendFactor.ZERO);
        GLState.pushCullMode(GLCullMode.BACK);
        GLState.pushDepthFunction(GLComparisonFunc.LESS);
        GLState.pushDepthWriteMask(true);
        GLState.pushLinearFog(0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
        GLState.pushFrontFace(GLFrontFace.CCW);
        GLState.pushLineWidth(1.0f);
        GLState.pushPointSize(1.0f);
        GLState.pushFillMode(GLFillMode.FILL);
        GLState.pushShadeModel(GLShadeModel.SMOOTH);

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