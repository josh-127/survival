package net.survival.client.graphics;

import java.util.Random;

import org.joml.Matrix4f;

import net.survival.client.graphics.opengl.GLBlendFactor;
import net.survival.client.graphics.opengl.GLImmediateDrawCall;
import net.survival.client.graphics.opengl.GLMatrixStack;
import net.survival.client.graphics.opengl.GLState;

class CloudDisplay
{
    private static final int MAP_LENGTH_X = 16;
    private static final int MAP_LENGTH_Z = 16;
    private static final long MAP_SEED = 0L;
    private static final int PIXEL_SIZE = 16;
    private static final double DENSITY = 0.5;
    private static final float POSITION_Y = 144.5f;
    private static final float SPEED_X = 0.0f;
    private static final float SPEED_Z = 2.0f;
    private static final float ALPHA = 0.875f;

    private static final int TOTAL_LENGTH_X = MAP_LENGTH_X * PIXEL_SIZE;
    private static final int TOTAL_LENGTH_Z = MAP_LENGTH_Z * PIXEL_SIZE;

    private final float[] vertexPositions;
    private float x;
    private float z;

    public CloudDisplay() {
        Random random = new Random(MAP_SEED);

        int index = 0;
        float[] positions = new float[MAP_LENGTH_X * MAP_LENGTH_Z * 12];

        for (int z = 0; z < MAP_LENGTH_Z; ++z) {
            for (int x = 0; x < MAP_LENGTH_X; ++x) {
                if (random.nextDouble() >= DENSITY)
                    continue;

                float posX = x * PIXEL_SIZE;
                float posZ = z * PIXEL_SIZE;
                positions[index++] = posX;
                positions[index++] = posZ;
                positions[index++] = posX + PIXEL_SIZE;
                positions[index++] = posZ;
                positions[index++] = posX + PIXEL_SIZE;
                positions[index++] = posZ + PIXEL_SIZE;
                positions[index++] = posX + PIXEL_SIZE;
                positions[index++] = posZ + PIXEL_SIZE;
                positions[index++] = posX;
                positions[index++] = posZ + PIXEL_SIZE;
                positions[index++] = posX;
                positions[index++] = posZ;
            }
        }

        vertexPositions = new float[index];
        System.arraycopy(positions, 0, vertexPositions, 0, index);
    }

    public void tick(double elapsedTime) {
        x += SPEED_X * elapsedTime;
        z += SPEED_Z * elapsedTime;

        if (x >= TOTAL_LENGTH_X)
            x -= TOTAL_LENGTH_X;

        if (z >= TOTAL_LENGTH_Z)
            z -= TOTAL_LENGTH_Z;
    }

    public void display(Matrix4f viewMatrix, Matrix4f projectionMatrix, float offsetX, float offsetZ)
    {
        offsetX = ((int) Math.floor(offsetX) / TOTAL_LENGTH_X) * TOTAL_LENGTH_X;
        offsetZ = ((int) Math.floor(offsetZ) / TOTAL_LENGTH_Z) * TOTAL_LENGTH_Z;
        offsetX += x;
        offsetZ += z;

        for (int i = -4; i <= 2; ++i) {
            for (int j = -3; j <= 2; ++j) {
                displayPart(viewMatrix, projectionMatrix, offsetX + TOTAL_LENGTH_X * j,
                        offsetZ + TOTAL_LENGTH_Z * i);
            }
        }
    }

    private void displayPart(Matrix4f viewMatrix, Matrix4f projectionMatrix, float offsetX,
            float offsetZ)
    {
        GLMatrixStack.setProjectionMatrix(projectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.load(viewMatrix);

        GLState.pushBlendEnabled(true);
        GLState.pushBlendFunction(GLBlendFactor.SRC_ALPHA, GLBlendFactor.ONE_MINUS_SRC_ALPHA);

        GLImmediateDrawCall drawCall = GLImmediateDrawCall.beginTriangles(null);
        drawCall.color(1.0f, 1.0f, 1.0f, ALPHA);

        for (int i = 0; i < vertexPositions.length; i += 2) {
            drawCall.vertex(offsetX + vertexPositions[i], POSITION_Y, offsetZ + vertexPositions[i + 1]);
        }

        drawCall.end();

        GLState.popBlendFunction();
        GLState.popBlendEnabled();

        GLMatrixStack.pop();
    }
}