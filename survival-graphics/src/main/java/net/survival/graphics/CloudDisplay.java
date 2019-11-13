package net.survival.graphics;

import java.util.Random;

import org.joml.Matrix4f;

import net.survival.graphics.opengl.GLBlendFactor;
import net.survival.graphics.opengl.GLImmediateDrawCall;
import net.survival.graphics.opengl.GLMatrixStack;
import net.survival.graphics.opengl.GLState;

class CloudDisplay
{
    private static final int MAP_LENGTH_X = 48;
    private static final int MAP_LENGTH_Z = 48;
    private static final long DEFAULT_MAP_SEED = 0L;
    private static final int DEFAULT_SCALE = 48;
    private static final float DEFAULT_DENSITY = 0.5f;
    private static final float DEFAULT_ELEVATION = 383.5f;
    private static final float DEFAULT_SPEED_X = 0.0f;
    private static final float DEFAULT_SPEED_Z = 8.0f;
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
    private float posX;
    private float posZ;

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
        posX += speedX * elapsedTime;
        posZ += speedZ * elapsedTime;

        // NOTE: Clouds will run away if speedX or speedZ is too large.
        if (speedX > 0.0f && posX >= TOTAL_LENGTH_X)
            posX -= TOTAL_LENGTH_X;
        else if (speedX < 0.0f && posX < 0.0f)
            posX += TOTAL_LENGTH_X;

        if (speedZ > 0.0f && posZ >= TOTAL_LENGTH_Z)
            posZ -= TOTAL_LENGTH_Z;
        else if (speedZ < 0.0f && posZ < 0.0f)
            posZ += TOTAL_LENGTH_Z;
    }

    public void display(Matrix4f viewMatrix, Matrix4f projectionMatrix, float cameraX, float cameraZ)
    {
        if (shouldRebuild) {
            rebuildVertexArray();
            shouldRebuild = false;
        }

        var offsetX = (float) ((int) Math.floor(cameraX) / TOTAL_LENGTH_X) * TOTAL_LENGTH_X;
        var offsetZ = (float) ((int) Math.floor(cameraZ) / TOTAL_LENGTH_Z) * TOTAL_LENGTH_Z;
        offsetX += posX;
        offsetZ += posZ;

        for (var i = -2; i <= 0; ++i) {
            for (var j = -1; j <= 0; ++j) {
                displayPart(
                        viewMatrix,
                        projectionMatrix,
                        cameraX,
                        cameraZ,
                        offsetX + TOTAL_LENGTH_X * j,
                        offsetZ + TOTAL_LENGTH_Z * i);
            }
        }
    }

    private void displayPart(
            Matrix4f viewMatrix,
            Matrix4f projectionMatrix,
            float cameraX,
            float cameraZ,
            float offsetX,
            float offsetZ)
    {
        GLMatrixStack.setProjectionMatrix(projectionMatrix);
        GLMatrixStack.push();
        GLMatrixStack.load(viewMatrix);

        GLState.pushBlendEnabled(true);
        GLState.pushBlendFunction(GLBlendFactor.SRC_ALPHA, GLBlendFactor.ONE_MINUS_SRC_ALPHA);
        GLState.pushCullFaceEnabled(false);

        var drawCall = GLImmediateDrawCall.beginTriangles(null);

        for (var i = 0; i < vertexPositions.length; i += 2) {
            var vx = vertexPositions[i];
            var vz = vertexPositions[i + 1];
            var gx = offsetX + vx;
            var gz = offsetZ + vz;

            var distX = cameraX - gx;
            var distZ = cameraZ - gz;
            var dist = (float) Math.sqrt(distX * distX + distZ * distZ);
            var alpha = Math.max(0.0f, (float) Math.exp(-dist * 0.002f));

            drawCall.color(1.0f, 1.0f, 1.0f, alpha);
            drawCall.vertex(gx, elevation, gz);
        }

        drawCall.end();

        GLState.popCullFaceEnabled();
        GLState.popBlendFunction();
        GLState.popBlendEnabled();

        GLMatrixStack.pop();
    }
}