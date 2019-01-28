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
    private static final long DEFAULT_MAP_SEED = 0L;
    private static final int DEFAULT_SCALE = 16;
    private static final float DEFAULT_DENSITY = 0.5f;
    private static final float DEFAULT_ELEVATION = 144.5f;
    private static final float DEFAULT_SPEED_X = 0.0f;
    private static final float DEFAULT_SPEED_Z = 2.0f;
    private static final float DEFAULT_ALPHA = 0.875f;

    private static final int TOTAL_LENGTH_X = MAP_LENGTH_X * DEFAULT_SCALE;
    private static final int TOTAL_LENGTH_Z = MAP_LENGTH_Z * DEFAULT_SCALE;

    private long mapSeed;
    private int scale;
    private float density;
    private float elevation;
    private float speedX;
    private float speedZ;
    private float alpha;
    private boolean shouldRebuild;

    private float[] vertexPositions;
    private float x;
    private float z;

    public CloudDisplay() {
        mapSeed = DEFAULT_MAP_SEED;
        scale = DEFAULT_SCALE;
        density = DEFAULT_DENSITY;
        elevation = DEFAULT_ELEVATION;
        speedX = DEFAULT_SPEED_X;
        speedZ = DEFAULT_SPEED_Z;
        alpha = DEFAULT_ALPHA;
        rebuildVertexArray();
    }

    public long getSeed() {
        return mapSeed;
    }

    public void setSeed(long to) {
        mapSeed = to;
        shouldRebuild = true;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float to) {
        density = to;
        shouldRebuild = true;
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float to) {
        elevation = to;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedZ() {
        return speedZ;
    }

    public void setSpeed(float dx, float dz) {
        speedX = dx;
        speedZ = dz;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float to) {
        alpha = to;
    }

    private void rebuildVertexArray() {
        var random = new Random(mapSeed);

        var index = 0;
        var positions = new float[scale * scale * 12];

        for (var z = 0; z < scale; ++z) {
            for (var x = 0; x < scale; ++x) {
                if (random.nextDouble() >= density)
                    continue;

                var posX = x * scale;
                var posZ = z * scale;
                positions[index++] = posX;
                positions[index++] = posZ;
                positions[index++] = posX + scale;
                positions[index++] = posZ;
                positions[index++] = posX + scale;
                positions[index++] = posZ + scale;
                positions[index++] = posX + scale;
                positions[index++] = posZ + scale;
                positions[index++] = posX;
                positions[index++] = posZ + scale;
                positions[index++] = posX;
                positions[index++] = posZ;
            }
        }

        vertexPositions = new float[index];
        System.arraycopy(positions, 0, vertexPositions, 0, index);
    }

    public void tick(double elapsedTime) {
        x += speedX * elapsedTime;
        z += speedZ * elapsedTime;

        // NOTE: Clouds will run away if speedX or speedZ is too large.
        if (speedX > 0.0f && x >= TOTAL_LENGTH_X)
            x -= TOTAL_LENGTH_X;
        else if (speedX < 0.0f && x < 0.0f)
            x += TOTAL_LENGTH_X;

        if (speedZ > 0.0f && z >= TOTAL_LENGTH_Z)
            z -= TOTAL_LENGTH_Z;
        else if (speedZ < 0.0f && z < 0.0f)
            z += TOTAL_LENGTH_Z;
    }

    public void display(Matrix4f viewMatrix, Matrix4f projectionMatrix, float offsetX, float offsetZ)
    {
        if (shouldRebuild) {
            rebuildVertexArray();
            shouldRebuild = false;
        }

        offsetX = ((int) Math.floor(offsetX) / TOTAL_LENGTH_X) * TOTAL_LENGTH_X;
        offsetZ = ((int) Math.floor(offsetZ) / TOTAL_LENGTH_Z) * TOTAL_LENGTH_Z;
        offsetX += x;
        offsetZ += z;

        for (var i = -4; i <= 2; ++i) {
            for (var j = -3; j <= 2; ++j) {
                displayPart(
                        viewMatrix,
                        projectionMatrix,
                        offsetX + TOTAL_LENGTH_X * j,
                        offsetZ + TOTAL_LENGTH_Z * i);
            }
        }
    }

    private void displayPart(
            Matrix4f viewMatrix,
            Matrix4f projectionMatrix,
            float offsetX,
            float offsetZ)
    {
        GLMatrixStack.setProjectionMatrix(projectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.load(viewMatrix);

        GLState.pushBlendEnabled(true);
        GLState.pushBlendFunction(GLBlendFactor.SRC_ALPHA, GLBlendFactor.ONE_MINUS_SRC_ALPHA);

        var drawCall = GLImmediateDrawCall.beginTriangles(null);
        drawCall.color(1.0f, 1.0f, 1.0f, alpha);

        for (var i = 0; i < vertexPositions.length; i += 2) {
            drawCall.vertex(offsetX + vertexPositions[i], elevation, offsetZ + vertexPositions[i + 1]);
        }

        drawCall.end();

        GLState.popBlendFunction();
        GLState.popBlendEnabled();

        GLMatrixStack.pop();
    }
}