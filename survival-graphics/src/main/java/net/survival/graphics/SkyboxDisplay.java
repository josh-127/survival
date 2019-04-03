package net.survival.graphics;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.survival.graphics.opengl.GLImmediateDrawCall;
import net.survival.graphics.opengl.GLMatrixStack;
import net.survival.graphics.opengl.GLState;

class SkyboxDisplay
{
    public static final float DEFAULT_BOTTOM_R = 0.8f;
    public static final float DEFAULT_BOTTOM_G = 1.0f;
    public static final float DEFAULT_BOTTOM_B = 1.0f;
    public static final float DEFAULT_TOP_R = 0.25f;
    public static final float DEFAULT_TOP_G = 0.5f;
    public static final float DEFAULT_TOP_B = 1.0f;

    private float bottomR = DEFAULT_BOTTOM_R;
    private float bottomG = DEFAULT_BOTTOM_G;
    private float bottomB = DEFAULT_BOTTOM_B;
    private float topR = DEFAULT_TOP_R;
    private float topG = DEFAULT_TOP_G;
    private float topB = DEFAULT_TOP_B;

    private Matrix3f tempMatrix = new Matrix3f();
    private Matrix4f viewWithoutTranslation = new Matrix4f();

    public float getBottomR() {
        return bottomR;
    }

    public float getBottomG() {
        return bottomG;
    }

    public float getBottomB() {
        return bottomB;
    }

    public float getTopR() {
        return topR;
    }

    public float getTopG() {
        return topG;
    }

    public float getTopB() {
        return topB;
    }

    public void setColor(float br, float bg, float bb, float tr, float tg, float tb) {
        bottomR = br;
        bottomG = bg;
        bottomB = bb;
        topR = tr;
        topG = tg;
        topB = tb;
    }

    public void display(Matrix4f view, Matrix4f projection) {
        view.get3x3(tempMatrix);
        viewWithoutTranslation.identity();
        viewWithoutTranslation.set(tempMatrix);

        GLMatrixStack.setProjectionMatrix(projection);
        GLMatrixStack.load(viewWithoutTranslation);

        GLState.pushDepthWriteMask(false);
        {
            GLImmediateDrawCall.beginTriangles(null)
                    // Top
                    .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)

                    // Bottom
                    .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)

                    // Front
                    .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)

                    // Back
                    .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)

                    // Left
                    .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)

                    // Right
                    .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .end();
        }
        GLState.popDepthWriteMask();
    }
}