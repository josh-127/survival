package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

public enum GLCullMode
{
    FRONT, BACK;

    public int toGLConstant() {
        switch (this) {
        case FRONT: return GL_FRONT;
        case BACK: return GL_BACK;
        }

        throw new IllegalStateException();
    }
}