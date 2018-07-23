package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

import java.util.Stack;

class GLOutputMerger
{
    private static final Stack<Boolean> depthTestEnabledStack = new Stack<>();
    private static final Stack<GLDepthFunction> depthFunctionStack = new Stack<>();
    private static final Stack<Boolean> depthWriteMaskStack = new Stack<>();

    private GLOutputMerger() {}

    public static void pushDepthTest(boolean enabled) {
        setDepthTest(enabled);
        depthTestEnabledStack.push(enabled);
    }

    public static void popDepthTest() {
        depthTestEnabledStack.pop();
        setDepthTest(depthTestEnabledStack.peek());
    }

    private static void setDepthTest(boolean enabled) {
        if (enabled)
            glEnable(GL_DEPTH_TEST);
        else
            glDisable(GL_DEPTH_TEST);
    }

    public static void pushDepthFunction(GLDepthFunction depthFunction) {
        setDepthFunction(depthFunction);
        depthFunctionStack.push(depthFunction);
    }

    public static void popDepthFunction() {
        depthFunctionStack.pop();
        setDepthFunction(depthFunctionStack.peek());
    }

    private static void setDepthFunction(GLDepthFunction to) {
        glDepthFunc(to.toGLConstant());
    }

    public static void pushDepthWriteMask(boolean enabled) {
        setDepthWriteMask(enabled);
        depthWriteMaskStack.push(enabled);
    }

    public static void popDepthWriteMask() {
        depthWriteMaskStack.pop();
        setDepthWriteMask(depthWriteMaskStack.peek());
    }

    private static void setDepthWriteMask(boolean to) {
        glDepthMask(to);
    }
}