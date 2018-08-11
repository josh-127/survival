package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum GLFillMode {
    POINT, LINE, FILL;

    int toGLConstant() {
        switch (this) {
        case POINT: return GL_POINT;
        case LINE:  return GL_LINE;
        case FILL:  return GL_FILL;
        }

        throw new IllegalStateException();
    }
}