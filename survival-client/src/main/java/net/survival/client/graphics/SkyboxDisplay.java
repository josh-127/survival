package net.survival.client.graphics;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLState;

class SkyboxDisplay
{
    public static final float BOTTOM_R = 0.8f;
    public static final float BOTTOM_G = 1.0f;
    public static final float BOTTOM_B = 1.0f;
    public static final float TOP_R = 0.25f;
    public static final float TOP_G = 0.5f;
    public static final float TOP_B = 1.0f;
    public static final float MIDDLE_R = 0.5f * (TOP_R + BOTTOM_R);
    public static final float MIDDLE_G = 0.5f * (TOP_G + BOTTOM_G);
    public static final float MIDDLE_B = 0.5f * (TOP_B + BOTTOM_B);

    private Matrix3f tempMatrix;
    private Matrix4f viewWithoutTranslation;

    public SkyboxDisplay() {
        tempMatrix = new Matrix3f();
        viewWithoutTranslation = new Matrix4f();
    }

    public void draw(Matrix4f view, Matrix4f projection) {
        view.get3x3(tempMatrix);
        viewWithoutTranslation.identity();
        viewWithoutTranslation.set(tempMatrix);

        GLMatrixStack.setProjectionMatrix(projection);
        GLMatrixStack.load(viewWithoutTranslation);

        try (@SuppressWarnings("resource")
        GLState glState = new GLState()
                .withDepthWriteMask(false))
        {
            GLImmediateDrawCall.beginTriangles(null)
                    // Top
                    .coloredVertex(-1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    // Bottom
                    .coloredVertex(-1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    // Front
                    .coloredVertex(1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    // Back
                    .coloredVertex(-1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    // Left
                    .coloredVertex(-1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(-1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(-1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(-1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(-1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    // Right
                    .coloredVertex(1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(1.0f, -1.0f, 1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .coloredVertex(1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, 1.0f, 1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, 1.0f, -1.0f, TOP_R, TOP_G, TOP_B)
                    .coloredVertex(1.0f, -1.0f, -1.0f, BOTTOM_R, BOTTOM_G, BOTTOM_B)
                    .end();
        }
    }
}