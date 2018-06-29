package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

import net.survival.client.graphics.GraphicsResource;

public class GLDisplayList implements GraphicsResource
{
    final int id;

    private GLDisplayList(int id) {
        this.id = id;
    }

    @Override
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

        public Builder pushVertex(float x, float y, float z, float xt, float yt, float xc, float yc, float zc) {
            glColor3f(xc, yc, zc);
            glTexCoord2f(xt, yt);
            glVertex3f(x, y, z);
            return this;
        }
        
        public Builder pushQuadAlongX(float y1, float z1, float y2, float z2, float x, boolean useNegativeXDirection,
                float u1, float v1, float u2, float v2)
        {
            boolean shouldFlipY = y1 > y2;
            boolean shouldFlipZ = z2 > z1;
            
            if (useNegativeXDirection)
                shouldFlipZ = !shouldFlipZ;
            
            if (shouldFlipY) {
                float temp = y1; y1 = y2; y2 = temp;
                temp = v1; v1 = v2; v2 = temp;
            }
            if (shouldFlipZ) {
                float temp = z1; z1 = z2; z2 = temp;
                if (useNegativeXDirection) {
                    temp = u1;
                    u1 = u2;
                    u2 = temp;
                }
            }
            
            pushVertex(x, y1, z1, u1, v2, 1.0f, 1.0f, 1.0f);
            pushVertex(x, y1, z2, u2, v2, 1.0f, 1.0f, 1.0f);
            pushVertex(x, y2, z2, u2, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x, y2, z2, u2, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x, y2, z1, u1, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x, y1, z1, u1, v2, 1.0f, 1.0f, 1.0f);
            
            return this;
        }
        
        public Builder pushQuadAlongY(float x1, float z1, float x2, float z2, float y, boolean useNegativeYDirection,
                float u1, float v1, float u2, float v2)
        {
            boolean shouldFlipX = x1 > x2;
            boolean shouldFlipZ = z2 > z1;
            
            if (useNegativeYDirection)
                shouldFlipX = !shouldFlipX;
            
            if (shouldFlipX) {
                float temp = x1; x1 = x2; x2 = temp;
                temp = u1; u1 = u2; u2 = temp;
            }
            if (shouldFlipZ) {
                float temp = z1; z1 = z2; z2 = temp;
                if (useNegativeYDirection) {
                    temp = v1;
                    v1 = v2;
                    v2 = temp;
                }
            }
            
            pushVertex(x1, y, z1, u1, v2, 1.0f, 1.0f, 1.0f);
            pushVertex(x2, y, z1, u2, v2, 1.0f, 1.0f, 1.0f);
            pushVertex(x2, y, z2, u2, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x2, y, z2, u2, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x1, y, z2, u1, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x1, y, z1, u1, v2, 1.0f, 1.0f, 1.0f);
            
            return this;
        }
        
        public Builder pushQuadAlongZ(float x1, float y1, float x2, float y2, float z, boolean useNegativeZDirection,
                float u1, float v1, float u2, float v2)
        {
            boolean shouldFlipX = x1 > x2;
            boolean shouldFlipY = y1 > y2;
            
            if (useNegativeZDirection)
                shouldFlipX = !shouldFlipX;
            
            if (shouldFlipX) {
                float temp = x1; x1 = x2; x2 = temp;
                temp = u1; u1 = u2; u2 = temp;
            }
            if (shouldFlipY) {
                float temp = y1; y1 = y2; y2 = temp;
                temp = v1; v1 = v2; v2 = temp;
            }
            
            pushVertex(x1, y1, z, u1, v2, 1.0f, 1.0f, 1.0f);
            pushVertex(x2, y1, z, u2, v2, 1.0f, 1.0f, 1.0f);
            pushVertex(x2, y2, z, u2, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x2, y2, z, u2, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x1, y2, z, u1, v1, 1.0f, 1.0f, 1.0f);
            pushVertex(x1, y1, z, u1, v2, 1.0f, 1.0f, 1.0f);
            
            return this;
        }

        public GLDisplayList build() {
            glEnd();
            glEndList();
            return new GLDisplayList(displayList);
        }
    }
}