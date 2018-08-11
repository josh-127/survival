package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum GLFrontFace {
    CW, CCW;

    int toGLConstant() {
        switch (this) {
        case CW:  return GL_CW;
        case CCW: return GL_CCW;
        }

        throw new IllegalStateException();
    }
}