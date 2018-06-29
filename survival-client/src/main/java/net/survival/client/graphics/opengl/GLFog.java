package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

import java.util.Stack;

class GLFog
{
    private static final Stack<GLFogMode> fogModeStack = new Stack<>();
    private static final Stack<Float> densityArgStack = new Stack<>();
    private static final Stack<Float> startArgStack = new Stack<>();
    private static final Stack<Float> endArgStack = new Stack<>();
    private static final Stack<Float> redArgStack = new Stack<>();
    private static final Stack<Float> greenArgStack = new Stack<>();
    private static final Stack<Float> blueArgStack = new Stack<>();
    private static final Stack<Float> alphaArgStack = new Stack<>();
    
    public static void pushNoFog() {
        setFog(GLFogMode.NONE, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        fogModeStack.push(GLFogMode.NONE);
        densityArgStack.push(0.0f);
        startArgStack.push(0.0f);
        endArgStack.push(0.0f);
        redArgStack.push(0.0f);
        greenArgStack.push(0.0f);
        blueArgStack.push(0.0f);
        alphaArgStack.push(0.0f);
    }
    
    public static void pushLinearFog(float start, float end, float r, float g, float b, float a) {
        setFog(GLFogMode.LINEAR, 0.0f, start, end, r, g, b, a);
        fogModeStack.push(GLFogMode.LINEAR);
        densityArgStack.push(0.0f);
        startArgStack.push(start);
        endArgStack.push(end);
        redArgStack.push(r);
        greenArgStack.push(g);
        blueArgStack.push(b);
        alphaArgStack.push(a);
    }
    
    public static void pushExpFog(float density, float r, float g, float b, float a) {
        setFog(GLFogMode.EXP, density, 0.0f, 0.0f, r, g, b, a);
        fogModeStack.push(GLFogMode.EXP);
        densityArgStack.push(density);
        startArgStack.push(0.0f);
        endArgStack.push(0.0f);
        redArgStack.push(r);
        greenArgStack.push(g);
        blueArgStack.push(b);
        alphaArgStack.push(a);
    }
    
    public static void pushExp2Fog(float density, float r, float g, float b, float a) {
        setFog(GLFogMode.EXP2, density, 0.0f, 0.0f, r, g, b, a);
        fogModeStack.push(GLFogMode.EXP2);
        densityArgStack.push(density);
        startArgStack.push(0.0f);
        endArgStack.push(0.0f);
        redArgStack.push(r);
        greenArgStack.push(g);
        blueArgStack.push(b);
        alphaArgStack.push(a);
    }
    
    public static void popFog() {
        fogModeStack.pop();
        densityArgStack.pop();
        startArgStack.pop();
        endArgStack.pop();
        redArgStack.pop();
        greenArgStack.pop();
        blueArgStack.pop();
        alphaArgStack.pop();
        
        GLFogMode mode = fogModeStack.peek();
        float density = densityArgStack.peek();
        float start = startArgStack.peek();
        float end = endArgStack.peek();
        float r = redArgStack.peek();
        float g = greenArgStack.peek();
        float b = blueArgStack.peek();
        float a = alphaArgStack.peek();
        setFog(mode, density, start, end, r, g, b, a);
    }
    
    private static float[] glFogfv_color = new float[4];
    private static void setFog(GLFogMode mode, float density, float start, float end, float r, float g, float b, float a) {
        if (mode == GLFogMode.NONE) {
            glDisable(GL_FOG);
            return;
        }
        else {
            glEnable(GL_FOG);
        }
        
        glFogfv_color[0] = r;
        glFogfv_color[1] = g;
        glFogfv_color[2] = b;
        glFogfv_color[3] = a;
        
        glFogi(GL_FOG_MODE, mode.toGLConstant());
        glFogf(GL_FOG_DENSITY, density);
        glFogf(GL_FOG_START, start);
        glFogf(GL_FOG_END, end);
        glFogfv(GL_FOG_COLOR, glFogfv_color);
    }
}