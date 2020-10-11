package net.survival.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;

import net.survival.graphics.Bitmap;

public class GLTexture {
    final int id;

    public GLTexture() {
        id = glGenTextures();
    }

    public void close() {
        glDeleteTextures(id);
    }

    public BindGuard beginBind() {
        return new BindGuard(this);
    }

    public static class BindGuard {
        private final int id;

        private BindGuard(GLTexture texture) {
            id = texture.id;
            glBindTexture(GL_TEXTURE_2D, id);
        }

        public void endBind() {
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        public BindGuard setData(Bitmap bitmap) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, bitmap.getPixelArray());

            return this;
        }

        public BindGuard setMinFilter(GLFilterMode to) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, to.toGLConstant());
            return this;
        }

        public BindGuard setMagFilter(GLFilterMode to) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, to.toGLConstant());
            return this;
        }

        public BindGuard setWrapS(GLWrapMode to) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, to.toGLConstant());
            return this;
        }

        public BindGuard setWrapT(GLWrapMode to) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, to.toGLConstant());
            return this;
        }

        public BindGuard setMipmapEnabled(boolean to) {
            int value = to ? GL_TRUE : GL_FALSE;
            glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, value);
            return this;
        }

        public BindGuard setMinLod(int to) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_LOD, to);
            return this;
        }

        public BindGuard setMaxLod(int to) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LOD, to);
            return this;
        }
    }
}