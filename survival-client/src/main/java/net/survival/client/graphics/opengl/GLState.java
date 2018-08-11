package net.survival.client.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

import java.util.Stack;

import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanStack;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatStack;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongStack;

public class GLState
{
    // ================================================================
    // GL_ALPHA_TEST
    // ================================================================
    private static final BooleanStack alphaTestEnabledStack = new BooleanArrayList();

    public static void pushAlphaTestEnabled(boolean enabled) {
        setAlphaTestEnabled(enabled);
        alphaTestEnabledStack.push(enabled);
    }

    public static void popAlphaTestEnabled() {
        alphaTestEnabledStack.popBoolean();
        setAlphaTestEnabled(alphaTestEnabledStack.topBoolean());
    }

    private static void setAlphaTestEnabled(boolean enabled) {
        if (enabled)
            glEnable(GL_ALPHA_TEST);
        else
            glDisable(GL_ALPHA_TEST);
    }

    // ================================================================
    // GL_BLEND
    // ================================================================
    private static final BooleanStack blendEnabledStack = new BooleanArrayList();

    public static void pushBlendEnabled(boolean enabled) {
        setBlendEnabled(enabled);
        blendEnabledStack.push(enabled);
    }

    public static void popBlendEnabled() {
        blendEnabledStack.popBoolean();
        setBlendEnabled(blendEnabledStack.topBoolean());
    }

    private static void setBlendEnabled(boolean enabled) {
        if (enabled)
            glEnable(GL_BLEND);
        else
            glDisable(GL_BLEND);
    }

    // ================================================================
    // GL_CULL_FACE
    // ================================================================
    private static final BooleanStack cullFaceEnabledStack = new BooleanArrayList();

    public static void pushCullFaceEnabled(boolean enabled) {
        setCullFaceEnabled(enabled);
        cullFaceEnabledStack.push(enabled);
    }

    public static void popCullFaceEnabled() {
        cullFaceEnabledStack.popBoolean();
        setCullFaceEnabled(cullFaceEnabledStack.topBoolean());
    }

    private static void setCullFaceEnabled(boolean enabled) {
        if (enabled)
            glEnable(GL_CULL_FACE);
        else
            glDisable(GL_CULL_FACE);
    }

    // ================================================================
    // GL_DEPTH_TEST
    // ================================================================
    private static final BooleanStack depthTestEnabledStack = new BooleanArrayList();

    public static void pushDepthTestEnabled(boolean enabled) {
        setDepthTestEnabled(enabled);
        depthTestEnabledStack.push(enabled);
    }

    public static void popDepthTestEnabled() {
        depthTestEnabledStack.popBoolean();
        setDepthTestEnabled(depthTestEnabledStack.topBoolean());
    }

    private static void setDepthTestEnabled(boolean enabled) {
        if (enabled)
            glEnable(GL_DEPTH_TEST);
        else
            glDisable(GL_DEPTH_TEST);
    }

    // ================================================================
    // GL_FOG
    // ================================================================
    private static BooleanStack fogEnabledStack = new BooleanArrayList();

    public static void pushFogEnabled(boolean enabled) {
        setFogEnabled(enabled);
        fogEnabledStack.push(enabled);
    }

    public static void popFogEnabled() {
        fogEnabledStack.popBoolean();
        setFogEnabled(fogEnabledStack.topBoolean());
    }

    private static void setFogEnabled(boolean enabled) {
        if (enabled)
            glEnable(GL_FOG);
        else
            glDisable(GL_FOG);
    }

    // ================================================================
    // GL_SCISSOR_TEST
    // ================================================================
    private static final BooleanStack scissorTestEnabledStack = new BooleanArrayList();

    public static void pushScissorTestEnabled(boolean enabled) {
        setScissorTestEnabled(enabled);
        scissorTestEnabledStack.push(enabled);
    }

    public static void popScissorTestEnabled() {
        scissorTestEnabledStack.popBoolean();
        setScissorTestEnabled(scissorTestEnabledStack.topBoolean());
    }

    private static void setScissorTestEnabled(boolean enabled) {
        if (enabled)
            glEnable(GL_SCISSOR_TEST);
        else
            glDisable(GL_SCISSOR_TEST);
    }

    // ================================================================
    // glAlphaFunc
    // ================================================================
    private static final Stack<GLComparisonFunc> alphaFunctionComparisonStack = new Stack<>();
    private static final FloatStack alphaFunctionReferenceValueStack = new FloatArrayList();

    public static void pushAlphaFunction(GLComparisonFunc comparisonFunction, float referenceValue) {
        setAlphaFunction(comparisonFunction, referenceValue);
        alphaFunctionComparisonStack.push(comparisonFunction);
        alphaFunctionReferenceValueStack.push(referenceValue);
    }

    public static void popAlphaFunction() {
        alphaFunctionComparisonStack.pop();
        alphaFunctionReferenceValueStack.popFloat();
        setAlphaFunction(alphaFunctionComparisonStack.peek(),
                alphaFunctionReferenceValueStack.topFloat());
    }

    private static void setAlphaFunction(GLComparisonFunc comparisonFunction, float referenceValue) {
        glAlphaFunc(comparisonFunction.toGLConstant(), referenceValue);
    }

    // ================================================================
    // glBlendFunc
    // ================================================================
    private static final Stack<GLBlendFactor> blendFunctionSrcFactor = new Stack<>();
    private static final Stack<GLBlendFactor> blendFunctionDstFactor = new Stack<>();

    public static void pushBlendFunction(GLBlendFactor srcFactor, GLBlendFactor dstFactor) {
        setBlendFunction(srcFactor, dstFactor);
        blendFunctionSrcFactor.push(srcFactor);
        blendFunctionDstFactor.push(dstFactor);
    }

    public static void popBlendFunction() {
        blendFunctionSrcFactor.pop();
        blendFunctionDstFactor.pop();
    }

    private static void setBlendFunction(GLBlendFactor srcFactor, GLBlendFactor dstFactor) {
        glBlendFunc(srcFactor.toGLConstant(), dstFactor.toGLConstant());
    }

    // ================================================================
    // glCullFace
    // ================================================================
    private static final Stack<GLCullMode> cullModeStack = new Stack<>();

    public static void pushCullMode(GLCullMode cullMode) {
        setCullMode(cullMode);
        cullModeStack.push(cullMode);
    }

    public static void popCullMode() {
        cullModeStack.pop();
        setCullMode(cullModeStack.peek());
    }

    private static void setCullMode(GLCullMode to) {
        glCullFace(to.toGLConstant());
    }

    // ================================================================
    // glDepthFunc
    // ================================================================
    private static final Stack<GLComparisonFunc> depthFunctionStack = new Stack<>();

    public static void pushDepthFunction(GLComparisonFunc depthFunction) {
        setDepthFunction(depthFunction);
        depthFunctionStack.push(depthFunction);
    }

    public static void popDepthFunction() {
        depthFunctionStack.pop();
        setDepthFunction(depthFunctionStack.peek());
    }

    private static void setDepthFunction(GLComparisonFunc to) {
        glDepthFunc(to.toGLConstant());
    }

    // ================================================================
    // glDepthMask
    // ================================================================
    private static final Stack<Boolean> depthWriteMaskStack = new Stack<>();

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

    // ================================================================
    // glFog
    // ================================================================
    private static final Stack<GLFogMode> fogModeStack = new Stack<>();
    private static final Stack<Float> densityArgStack = new Stack<>();
    private static final Stack<Float> startArgStack = new Stack<>();
    private static final Stack<Float> endArgStack = new Stack<>();
    private static final Stack<Float> redArgStack = new Stack<>();
    private static final Stack<Float> greenArgStack = new Stack<>();
    private static final Stack<Float> blueArgStack = new Stack<>();
    private static final Stack<Float> alphaArgStack = new Stack<>();
    private static float[] glFogfv_color = new float[4];

    public static void pushLinearFog(float start, float end, float r, float g, float b, float a) {
        setFogParams(GLFogMode.LINEAR, 0.0f, start, end, r, g, b, a);
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
        setFogParams(GLFogMode.EXP, density, 0.0f, 0.0f, r, g, b, a);
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
        setFogParams(GLFogMode.EXP2, density, 0.0f, 0.0f, r, g, b, a);
        fogModeStack.push(GLFogMode.EXP2);
        densityArgStack.push(density);
        startArgStack.push(0.0f);
        endArgStack.push(0.0f);
        redArgStack.push(r);
        greenArgStack.push(g);
        blueArgStack.push(b);
        alphaArgStack.push(a);
    }

    public static void popFogParams() {
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
        setFogParams(mode, density, start, end, r, g, b, a);
    }

    private static void setFogParams(GLFogMode mode, float density, float start, float end, float r,
            float g, float b, float a)
    {
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

    // ================================================================
    // glFrontFace
    // ================================================================
    private static final Stack<GLFrontFace> frontFaceStack = new Stack<>();

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

    // ================================================================
    // glLineWidth
    // ================================================================
    private static final FloatStack lineWidthStack = new FloatArrayList();

    public static void pushLineWidth(float lineWidth) {
        setLineWidth(lineWidth);
        lineWidthStack.push(lineWidth);
    }

    public static void popLineWidth() {
        lineWidthStack.popFloat();
        setLineWidth(lineWidthStack.topFloat());
    }

    private static void setLineWidth(float to) {
        glLineWidth(to);
    }

    // ================================================================
    // glPointSize
    // ================================================================
    private static final FloatStack pointSizeStack = new FloatArrayList();

    public static void pushPointSize(float pointSize) {
        setPointSize(pointSize);
        pointSizeStack.push(pointSize);
    }

    public static void popPointSize() {
        pointSizeStack.popFloat();
        setPointSize(pointSizeStack.topFloat());
    }

    private static void setPointSize(float to) {
        glPointSize(to);
    }

    // ================================================================
    // glPolygonMode
    // ================================================================
    private static final Stack<GLFillMode> fillModeStack = new Stack<>();

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

    // ================================================================
    // glScissor
    // ================================================================
    private static final LongStack scissorPositionStack = new LongArrayList();
    private static final LongStack scissorSizeStack = new LongArrayList();

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

    // ================================================================
    // glShadeModel
    // ================================================================
    private static final Stack<GLShadeModel> shadeModelStack = new Stack<>();

    public static void pushShadeModel(GLShadeModel shadeModel) {
        setShadeModel(shadeModel);
        shadeModelStack.push(shadeModel);
    }

    public static void popShadeModel() {
        shadeModelStack.pop();
        setShadeModel(shadeModelStack.peek());
    }

    private static void setShadeModel(GLShadeModel to) {
        glShadeModel(to.toGLConstant());
    }

    // ================================================================
    // glViewport
    // ================================================================
    private static final LongStack viewportPositionStack = new LongArrayList();
    private static final LongStack viewportSizeStack = new LongArrayList();

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