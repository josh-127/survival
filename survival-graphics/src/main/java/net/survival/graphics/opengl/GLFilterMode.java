package net.survival.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum GLFilterMode {
    NEAREST,
    NEAREST_MIPMAP_NEAREST,
    NEAREST_MIPMAP_LINEAR,
    LINEAR,
    LINEAR_MIPMAP_NEAREST,
    LINEAR_MIPMAP_LINEAR;

    int toGLConstant() {
        switch (this) {
        case NEAREST:                return GL_NEAREST;
        case NEAREST_MIPMAP_NEAREST: return GL_NEAREST_MIPMAP_NEAREST;
        case NEAREST_MIPMAP_LINEAR:  return GL_NEAREST_MIPMAP_LINEAR;
        case LINEAR:                 return GL_LINEAR;
        case LINEAR_MIPMAP_NEAREST:  return GL_LINEAR_MIPMAP_NEAREST;
        case LINEAR_MIPMAP_LINEAR:   return GL_LINEAR_MIPMAP_LINEAR;
        }

        throw new IllegalStateException();
    }
}