package net.survival.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum GLBlendFactor {
    ZERO,
    ONE,
    SRC_COLOR,
    ONE_MINUS_SRC_COLOR,
    SRC_ALPHA,
    ONE_MINUS_SRC_ALPHA,
    DST_COLOR,
    ONE_MINUS_DST_COLOR,
    DST_ALPHA,
    ONE_MINUS_DST_ALPHA;

    public int toGLConstant() {
        switch (this) {
        case ZERO:                return GL_ZERO;
        case ONE:                 return GL_ONE;
        case SRC_COLOR:           return GL_SRC_COLOR;
        case ONE_MINUS_SRC_COLOR: return GL_ONE_MINUS_SRC_COLOR;
        case SRC_ALPHA:           return GL_SRC_ALPHA;
        case ONE_MINUS_SRC_ALPHA: return GL_ONE_MINUS_SRC_ALPHA;
        case DST_COLOR:           return GL_DST_COLOR;
        case ONE_MINUS_DST_COLOR: return GL_ONE_MINUS_DST_COLOR;
        case DST_ALPHA:           return GL_DST_ALPHA;
        case ONE_MINUS_DST_ALPHA: return GL_ONE_MINUS_DST_ALPHA;
        }

        throw new IllegalStateException();
    }
}