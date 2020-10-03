package net.survival.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum GLWrapMode {
    REPEAT,
    CLAMP;

    int toGLConstant() {
        switch (this) {
        case REPEAT: return GL_REPEAT;
        case CLAMP:  return GL_CLAMP;
        }
        
        throw new IllegalStateException();
    }
}