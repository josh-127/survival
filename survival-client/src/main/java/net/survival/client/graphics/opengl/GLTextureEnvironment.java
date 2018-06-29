package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

class GLTextureEnvironment
{
    private static GLTexture lastUsedTexture;
    
    public static void useTexture(GLTexture texture) {
        if (texture != lastUsedTexture) {
            if (texture != null)
                glBindTexture(GL_TEXTURE_2D, texture.id);
            else
                glBindTexture(GL_TEXTURE_2D, 0);
            
            lastUsedTexture = texture;
        }
    }
}