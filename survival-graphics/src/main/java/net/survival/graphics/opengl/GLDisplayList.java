package net.survival.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public class GLDisplayList {
    final int id;

    private GLDisplayList(int id) {
        this.id = id;
    }

    public void close() {
        glDeleteLists(id, 1);
    }

    public static class Builder
    {
        private final int displayList;

        public Builder() {
            displayList = glGenLists(1);
            glNewList(displayList, GL_COMPILE);
            glBegin(GL_TRIANGLES);
        }

        public Builder setColor(float r, float g, float b) {
            glColor3f(r, g, b);
            return this;
        }

        public Builder setTexCoord(float u, float v) {
            glTexCoord2f(u, v);
            return this;
        }

        public Builder pushVertex(float x, float y, float z) {
            glVertex3f(x, y, z);
            return this;
        }

        public Builder pushVertex(
            float x, float y, float z,
            float xt, float yt
        ) {
            glTexCoord2f(xt, yt);
            glVertex3f(x, y, z);
            return this;
        }

        public Builder pushVertex(
            float x, float y, float z,
            float xt, float yt,
            float xc, float yc, float zc
        ) {
            glColor3f(xc, yc, zc);
            glTexCoord2f(xt, yt);
            glVertex3f(x, y, z);
            return this;
        }

        public GLDisplayList build() {
            glEnd();
            glEndList();
            return new GLDisplayList(displayList);
        }
    }
}