package net.survival.client.graphics.opengl;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

public class GLMatrixStack
{
    private static final FloatBuffer matrixParam = BufferUtils.createFloatBuffer(16);
    
    public static void setProjectionMatrix(Matrix4f to) {
        if (to == null)
            to = new Matrix4f();
        to.get(matrixParam);
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(matrixParam);
        glMatrixMode(GL_MODELVIEW);
    }
    
    public static void push() {
        glPushMatrix();
    }
    
    public static void pop() {
        glPopMatrix();
    }
    
    public static void loadIdentity() {
        glLoadIdentity();
    }
    
    public static void load(Matrix4f matrix) {
        matrix.get(matrixParam);
        glLoadMatrixf(matrixParam);
    }
    
    public static void multiply(Matrix4f matrix) {
        matrix.get(matrixParam);
        glMultMatrixf(matrixParam);
    }
    
    public static void translate(float x, float y, float z) {
        glTranslatef(x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z) {
        glRotatef((float) Math.toDegrees(angle), x, y, z);
    }
    
    public static void scale(float x, float y, float z) {
        glScalef(x, y, z);
    }
}