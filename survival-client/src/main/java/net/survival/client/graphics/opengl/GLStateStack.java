package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

import java.util.Stack;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongStack;

class GLStateStack
{
    private static final Stack<GLFogMode> fogModeStack = new Stack<>();
    private static final Stack<Float> densityArgStack = new Stack<>();
    private static final Stack<Float> startArgStack = new Stack<>();
    private static final Stack<Float> endArgStack = new Stack<>();
    private static final Stack<Float> redArgStack = new Stack<>();
    private static final Stack<Float> greenArgStack = new Stack<>();
    private static final Stack<Float> blueArgStack = new Stack<>();
    private static final Stack<Float> alphaArgStack = new Stack<>();

    private static final Stack<Boolean> depthTestEnabledStack = new Stack<>();
    private static final Stack<GLDepthFunction> depthFunctionStack = new Stack<>();
    private static final Stack<Boolean> depthWriteMaskStack = new Stack<>();

    private static final Stack<GLCullMode> cullModeStack = new Stack<>();
    private static final Stack<GLFillMode> fillModeStack = new Stack<>();
    private static final Stack<GLFrontFace> frontFaceStack = new Stack<>();
    private static final LongStack scissorPositionStack = new LongArrayList();
    private static final LongStack scissorSizeStack = new LongArrayList();
    private static final LongStack viewportPositionStack = new LongArrayList();
    private static final LongStack viewportSizeStack = new LongArrayList();

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

    private static void setFog(GLFogMode mode, float density, float start, float end, float r,
            float g, float b, float a)
    {
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

    public static void pushCullMode(GLCullMode cullMode) {
        setCullMode(cullMode);
        cullModeStack.push(cullMode);
    }

    public static void popCullMode() {
        cullModeStack.pop();
        setCullMode(cullModeStack.peek());
    }

    private static void setCullMode(GLCullMode to) {
        if (to == GLCullMode.NONE) {
            glDisable(GL_CULL_FACE);
        }
        else {
            glEnable(GL_CULL_FACE);
            if (to == GLCullMode.FRONT)
                glCullFace(GL_FRONT);
            else if (to == GLCullMode.BACK)
                glCullFace(GL_BACK);
        }
    }

    public static void pushFillMode(GLFillMode fillMode) {
        setFillMode(fillMode);
        fillModeStack.push(fillMode);
    }

    public static void popFillMode() {
        fillModeStack.pop();
        setFillMode(fillModeStack.peek());
    }

    private static void setFillMode(GLFillMode to) {
        glPolygonMode(GL_FRONT_AND_BACK, to.toGLConstant());
    }

    public static void pushFrontFace(GLFrontFace frontFace) {
        setFrontFace(frontFace);
        frontFaceStack.push(frontFace);
    }

    public static void popFrontFace() {
        frontFaceStack.pop();
        setFrontFace(frontFaceStack.peek());
    }

    private static void setFrontFace(GLFrontFace to) {
        glFrontFace(to.toGLConstant());
    }

    public static void pushScissor(int x, int y, int width, int height) {
        setScissor(x, y, width, height);
        scissorPositionStack.push(hashIntPair(x, y));
        scissorSizeStack.push(hashIntPair(width, height));
    }

    public static void popScissor() {
        scissorPositionStack.popLong();
        scissorSizeStack.popLong();

        long position = scissorPositionStack.topLong();
        long size = scissorPositionStack.topLong();
        int x = firstValueFromIntPair(position);
        int y = secondValueFromIntPair(position);
        int width = firstValueFromIntPair(size);
        int height = secondValueFromIntPair(size);
        setScissor(x, y, width, height);
    }

    private static void setScissor(int x, int y, int width, int height) {
        glScissor(x, y, width, height);
    }

    public static void pushViewport(int x, int y, int width, int height) {
        setViewport(x, y, width, height);
        viewportPositionStack.push(hashIntPair(x, y));
        viewportSizeStack.push(hashIntPair(width, height));
    }

    public static void popViewport() {
        viewportPositionStack.popLong();
        viewportSizeStack.popLong();

        long position = viewportPositionStack.topLong();
        long size = viewportPositionStack.topLong();
        int x = firstValueFromIntPair(position);
        int y = secondValueFromIntPair(position);
        int width = firstValueFromIntPair(size);
        int height = secondValueFromIntPair(size);
        setViewport(x, y, width, height);
    }

    private static void setViewport(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
    }

    private static long hashIntPair(int first, int second) {
        return (second << 32L) | first;
    }

    private static int firstValueFromIntPair(long intPair) {
        return (int) (intPair & 0xFFFFFFFFL);
    }

    private static int secondValueFromIntPair(long intPair) {
        return (int) ((intPair & 0xFFFFFFFF00000000L) >>> 32L);
    }
}