package net.survival.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public class GLImmediateDrawCall
{
    static final GLImmediateDrawCall instance = new GLImmediateDrawCall();

    public GLImmediateDrawCall color(float r, float g, float b) {
        glColor3f(r, g, b);
        return this;
    }

    public GLImmediateDrawCall color(float r, float g, float b, float a) {
        glColor4f(r, g, b, a);
        return this;
    }

    public GLImmediateDrawCall texCoord(float u, float v) {
        glTexCoord2f(u, v);
        return this;
    }

    public GLImmediateDrawCall normal(float x, float y, float z) {
        glNormal3f(x, y, z);
        return this;
    }

    public GLImmediateDrawCall vertex(float x, float y, float z) {
        glVertex3f(x, y, z);
        return this;
    }

    public GLImmediateDrawCall coloredVertex(float x, float y, float z, float r, float g, float b) {
        glColor3f(r, g, b);
        glVertex3f(x, y, z);
        return this;
    }

    public GLImmediateDrawCall texturedVertex(float x, float y, float z, float u, float v) {
        glTexCoord2f(u, v);
        glVertex3f(x, y, z);
        return this;
    }

    public static GLImmediateDrawCall beginLines(GLTexture texture) {
        GLTextureEnvironment.useTexture(texture);
        glBegin(GL_LINES);
        return instance;
    }

    public static GLImmediateDrawCall beginTriangles(GLTexture texture) {
        GLTextureEnvironment.useTexture(texture);
        glBegin(GL_TRIANGLES);
        return instance;
    }

    public void end() {
        glEnd();
    }
}