package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum GLDepthFunction {
    NEVER, ALWAYS, EQUAL, NOT_EQUAL, LESS, GREATER, LEQUAL, GEQUAL;
    
    int toGLConstant() {
        switch (this) {
        case NEVER:     return GL_NEVER;
        case ALWAYS:    return GL_ALWAYS;
        case EQUAL:     return GL_EQUAL;
        case NOT_EQUAL: return GL_NOTEQUAL;
        case LESS:      return GL_LESS;
        case GREATER:   return GL_GREATER;
        case LEQUAL:    return GL_LEQUAL;
        case GEQUAL:    return GL_GEQUAL;
        }

        throw new RuntimeException();
    }
}