package net.survival.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum GLShadeModel
{
    FLAT,
    SMOOTH;

    public int toGLConstant() {
        switch (this) {
        case FLAT: return GL_FLAT;
        case SMOOTH: return GL_SMOOTH;
        }

        throw new IllegalStateException();
    }
}