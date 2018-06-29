package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

enum GLFogMode
{
    NONE, LINEAR, EXP, EXP2;
    
    int toGLConstant() {
        switch (this) {
        case NONE:   throw new RuntimeException();
        case LINEAR: return GL_LINEAR;
        case EXP:    return GL_EXP;
        case EXP2:   return GL_EXP2;
        }
        
        throw new RuntimeException();
    }
}