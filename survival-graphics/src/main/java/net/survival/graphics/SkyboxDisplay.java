package net.survival.graphics;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.survival.graphics.opengl.GLImmediateDrawCall;
import net.survival.graphics.opengl.GLMatrixStack;
import net.survival.graphics.opengl.GLState;

class SkyboxDisplay {
    public static final float DEFAULT_BOTTOM_R = 0.8f;
    public static final float DEFAULT_BOTTOM_G = 1.0f;
    public static final float DEFAULT_BOTTOM_B = 1.0f;
    public static final float DEFAULT_MIDDLE_R = 0.8f;
    public static final float DEFAULT_MIDDLE_G = 1.0f;
    public static final float DEFAULT_MIDDLE_B = 1.0f;
    public static final float DEFAULT_TOP_R = 0.25f;
    public static final float DEFAULT_TOP_G = 0.5f;
    public static final float DEFAULT_TOP_B = 1.0f;

    private static final float MIDDLE_Y = -0.0625f;

    private float bottomR = DEFAULT_BOTTOM_R;
    private float bottomG = DEFAULT_BOTTOM_G;
    private float bottomB = DEFAULT_BOTTOM_B;
    private float middleR = DEFAULT_MIDDLE_R;
    private float middleG = DEFAULT_MIDDLE_G;
    private float middleB = DEFAULT_MIDDLE_B;
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

    public float getMiddleR() {
        return middleR;
    }

    public float getMiddleG() {
        return middleG;
    }

    public float getMiddleB() {
        return middleB;
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

    public void setColor(
            float br, float bg, float bb,
            float mr, float mg, float mb,
            float tr, float tg, float tb)
    {
        bottomR = br;
        bottomG = bg;
        bottomB = bb;
        middleR = mr;
        middleG = mg;
        middleB = mb;
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

                    // Front (Top Half)
                    .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)

                    // Front (Bottom Half)
                    .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)

                    // Back (Top Half)
                    .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)

                    // Back (Bottom Half)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)

                    // Left (Top Half)
                    .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)

                    // Left (Bottom Half)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)

                    // Right (Top Half)
                    .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, 1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, 1.0f, -1.0f, topR, topG, topB)
                    .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)

                    // Right (Bottom Half)
                    .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, -1.0f, 1.0f, bottomR, bottomG, bottomB)
                    .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, MIDDLE_Y, 1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, MIDDLE_Y, -1.0f, middleR, middleG, middleB)
                    .coloredVertex(1.0f, -1.0f, -1.0f, bottomR, bottomG, bottomB)
                    .end();
        }
        GLState.popDepthWriteMask();
    }
}