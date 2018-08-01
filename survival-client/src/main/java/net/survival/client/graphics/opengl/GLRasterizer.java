package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

import java.util.Stack;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongStack;

class GLRasterizer
{
    private static final Stack<GLCullMode> cullModeStack = new Stack<>();
    private static final Stack<GLFillMode> fillModeStack = new Stack<>();
    private static final Stack<GLFrontFace> frontFaceStack = new Stack<>();
    private static final LongStack scissorPositionStack = new LongArrayList();
    private static final LongStack scissorSizeStack = new LongArrayList();
    private static final LongStack viewportPositionStack = new LongArrayList();
    private static final LongStack viewportSizeStack = new LongArrayList();

    private GLRasterizer() {}

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