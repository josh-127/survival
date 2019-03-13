package net.survival.client.graphics2.internal.d3d11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import dev.josh127.libwin32.D3D11;
import dev.josh127.libwin32.Dxgi;
import dev.josh127.libwin32.Dxgi.ModeDesc;
import dev.josh127.libwin32.Dxgi.Rational;
import dev.josh127.libwin32.Dxgi.SampleDesc;
import dev.josh127.libwin32.NativePtr;
import net.survival.client.graphics2.internal.BlendState;
import net.survival.client.graphics2.internal.ColorTarget;
import net.survival.client.graphics2.internal.DepthStencilState;
import net.survival.client.graphics2.internal.DepthStencilTarget;
import net.survival.client.graphics2.internal.Filter;
import net.survival.client.graphics2.internal.FragmentShader;
import net.survival.client.graphics2.internal.GraphicsDeviceInfo;
import net.survival.client.graphics2.internal.GraphicsResourceFactory;
import net.survival.client.graphics2.internal.IndexBuffer;
import net.survival.client.graphics2.internal.InputLayoutElement;
import net.survival.client.graphics2.internal.PrimitiveTopology;
import net.survival.client.graphics2.internal.RenderContext;
import net.survival.client.graphics2.internal.RenderTargetFormat;
import net.survival.client.graphics2.internal.Rgba8Texture;
import net.survival.client.graphics2.internal.SamplerState;
import net.survival.client.graphics2.internal.ShaderType;
import net.survival.client.graphics2.internal.Texture;
import net.survival.client.graphics2.internal.TextureAddressingMethod;
import net.survival.client.graphics2.internal.Uniform;
import net.survival.client.graphics2.internal.VertexBuffer;
import net.survival.client.graphics2.internal.VertexShader;
import net.survival.client.util.Rect;
import net.survival.client.util.Vec2;
import net.survival.client.util.Vec4;

/**
 * A D3D11Renderer wraps D3D11 using engine.graphics.
 */
public class D3D11Renderer implements GraphicsDeviceInfo, GraphicsResourceFactory, RenderContext
{
    private final int width;
    private final int height;
    private final D3D11.Device device;
    private final D3D11.DeviceContext context;
    private final Dxgi.SwapChain swapChain;
    private final D3D11.RenderTargetView backBuffer;
    private final D3D11.RenderTargetView[] colorTargets;
    private D3D11.DepthStencilView depthStencilTarget;
    private D3D11.DepthStencilState depthStencilState;
    private final D3D11.DepthStencilState defaultDepthStencilState;
    private byte stencilCompareValue;
    private D3D11.BlendState blendState;

    /**
     * Constructs a D3D11Renderer.
     * 
     * @param window    the window the renderer outputs to
     * @param width     the width of the screen
     * @param height    the height of the screen
     * @param framerate the framerate in frames per second
     */
    public D3D11Renderer(NativePtr window, int width, int height, int framerate) {
        this.width = width;
        this.height = height;

        D3D11.DeviceDeviceContextSwapChainTuple tuple = D3D11.createDeviceAndSwapChain(
                D3D11.DRIVER_TYPE_HARDWARE,
                0,
                Dxgi.SwapChainDesc.create(
                        ModeDesc.create(
                                width,
                                height,
                                Rational.create(framerate, 1),
                                Dxgi.FORMAT_R8G8B8A8_UNORM,
                                0,
                                0),
                        SampleDesc.create(1, 0),
                        Dxgi.USAGE_RENDER_TARGET_OUTPUT,
                        1,
                        window,
                        true,
                        Dxgi.SWAP_EFFECT_DISCARD,
                        0));

        device = tuple.getDevice();
        context = tuple.getDeviceContext();
        swapChain = tuple.getSwapChain();

        D3D11.Texture2D backBufferTexture = swapChain.getBuffer(0);
        backBuffer = device.createRenderTargetView(backBufferTexture, null);
        backBufferTexture.release();

        colorTargets = new D3D11.RenderTargetView[getMaxColorTargets()];
        colorTargets[0] = backBuffer;

        context.omSetRenderTargets(colorTargets, null);
        context.rsSetViewports(new D3D11.Viewport[] {
                D3D11.Viewport.create(0.0f, 0.0f, width, height, 0.0f, 1.0f)
        });
        context.rsSetState(device.createRasterizerState(
                D3D11.RasterizerDesc.create(
                        D3D11.FILL_SOLID,
                        D3D11.CULL_BACK,
                        true,
                        0,
                        0.0f,
                        0.0f,
                        true,
                        false,
                        false,
                        false)));

        defaultDepthStencilState = device.createDepthStencilState(
                D3D11.DepthStencilDesc.create(
                        true,
                        D3D11.DEPTH_WRITE_MASK_ALL,
                        D3D11.COMPARISON_LESS,
                        true,
                        (byte) 0xFF,
                        (byte) 0xFF,
                        D3D11.DepthStencilOpDesc.create(
                                D3D11.STENCIL_OP_KEEP,
                                D3D11.STENCIL_OP_DECR,
                                D3D11.STENCIL_OP_KEEP,
                                D3D11.COMPARISON_ALWAYS),
                        D3D11.DepthStencilOpDesc.create(
                                D3D11.STENCIL_OP_KEEP,
                                D3D11.STENCIL_OP_INCR,
                                D3D11.STENCIL_OP_KEEP,
                        D3D11.COMPARISON_ALWAYS)));

        depthStencilState = defaultDepthStencilState;
        context.omSetDepthStencilState(depthStencilState, 0);
    }

    /**
     * Swaps the front and back buffer and displays the front buffer.
     */
    public void swapBuffers() {
        swapChain.present(0, 0);
    }

    /**
     * Gets the width of the screen.
     * 
     * @return the width of the screen
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the screen.
     * 
     * @return the height of the screen
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Gets the size of the screen.
     * 
     * @return the size of the screen
     */
    @Override
    public Rect getSize() {
        return new Rect(0, 0, width, height);
    }

    /**
     * Gets the size of the screen.
     * 
     * @return the size of the screen
     */
    @Override
    public Vec2 getSizeF() {
        return new Vec2(width, height);
    }

    /**
     * Gets the aspect ratio of the screen.
     * 
     * @return the aspect ratio of the screen
     */
    @Override
    public float getAspectRatio() {
        return (float) width / height;
    }

    /**
     * Gets the maximum number of color targets the graphics device supports.
     * 
     * @return the maximum number of color targets the graphics device supports
     */
    @Override
    public int getMaxColorTargets() {
        return 4;
    }

    /**
     * Creates a color target.
     * 
     * @param width  the width of the color target in pixels
     * @param height the height of the color target in pixels
     * @param format the format of the color target
     * @return the created color target
     */
    @Override
    public ColorTarget createColorTarget(int width, int height, RenderTargetFormat format) {
        assert width > 0 && height > 0;

        return new D3D11ColorTarget(
                device,
                device.createTexture2D(
                        D3D11.Texture2DDesc.create(
                                width,
                                height,
                                1,
                                1,
                                D3D11Converter.toFormat(format),
                                Dxgi.SampleDesc.create(1, 0),
                                D3D11.USAGE_DEFAULT,
                                D3D11.BIND_RENDER_TARGET | D3D11.BIND_SHADER_RESOURCE,
                                0,
                                0),
                        null),
                width,
                height);
    }

    /**
     * Creates a depth-stencil target.
     * 
     * @param width  the width of the depth-stencil target in pixels
     * @param height the height of the depth-stencil target in pixels
     * @return the created depth-stencil target
     */
    @Override
    public DepthStencilTarget createDepthStencilTarget(int width, int height) {
        assert width > 0 && height > 0;

        return new D3D11DepthStencilTarget(
                device,
                device.createTexture2D(
                        D3D11.Texture2DDesc.create(
                                width,
                                height,
                                1,
                                1,
                                Dxgi.FORMAT_D24_UNORM_S8_UINT,
                                Dxgi.SampleDesc.create(1, 0),
                                D3D11.USAGE_DEFAULT,
                                D3D11.BIND_DEPTH_STENCIL,
                                0,
                                0),
                        null),
                width,
                height);
    }

    /**
     * Creates a DepthStencilState builder.
     * 
     * @return a DepthStencilState builder
     */
    @Override
    public DepthStencilState.Builder createDepthStencilStateBuilder() {
        return new D3D11DepthStencilState.Builder(device);
    }

    /**
     * Gets the default depth-stencil state.
     * 
     * @return the default depth-stencil state.
     */
    @Override
    public DepthStencilState getDefaultDepthStencilState() {
        return new D3D11DepthStencilState(defaultDepthStencilState);
    }

    /**
     * Creates a BlendState builder.
     * 
     * @return a BlendState builder
     */
    @Override
    public BlendState.Builder createBlendStateBuilder() {
        return new D3D11BlendState.Builder(this, device);
    }

    /**
     * Deletes a color target.
     * 
     * @param target the color target to delete
     */
    @Override
    public void deleteRenderTarget(ColorTarget target) {
        ((D3D11ColorTarget) target).texture.texture.release();
        ((D3D11ColorTarget) target).view.release();
    }

    /**
     * Deletes a depth-stencil target.
     * 
     * @param target the depth-stencil target to delete
     */
    @Override
    public void deleteRenderTarget(DepthStencilTarget target) {
        ((D3D11DepthStencilTarget) target).texture.release();
        ((D3D11DepthStencilTarget) target).view.release();
    }

    /**
     * Deletes a DepthStencilState.
     * 
     * @param state the DepthStencilState to delete
     */
    @Override
    public void deleteDepthStencilState(DepthStencilState state) {
        ((D3D11DepthStencilState) state).state.release();
    }

    /**
     * Deletes a BlendState.
     * 
     * @param state the BlendState to delete
     */
    @Override
    public void deleteBlendState(BlendState state) {
        ((D3D11BlendState) state).state.release();
    }

    /**
     * Assigns a given color target slot to a given color target.
     * 
     * @param slot   the color target slot to assign
     * @param target the color target
     */
    @Override
    public void setColorTarget(int slot, ColorTarget target) {
        assert slot >= 0 && slot < getMaxColorTargets();

        colorTargets[slot] = ((D3D11ColorTarget) target).view;
        context.omSetRenderTargets(colorTargets, depthStencilTarget);
    }

    /**
     * Assigns a given color target slot to the back buffer.
     * 
     * @param slot the color target slot to assign
     */
    @Override
    public void setColorTargetToBackBuffer(int slot) {
        assert slot >= 0 && slot < getMaxColorTargets();

        colorTargets[slot] = backBuffer;
        context.omSetRenderTargets(colorTargets, depthStencilTarget);
    }

    /**
     * Sets the depth-stencil target.
     * 
     * @param target the depth-stencil target
     */
    @Override
    public void setDepthStencilTarget(DepthStencilTarget target) {
        depthStencilTarget = ((D3D11DepthStencilTarget) target).view;
        context.omSetRenderTargets(colorTargets, depthStencilTarget);
    }

    /**
     * Clears a color target from a given slot.
     * 
     * @param slot  the slot that contains the color target to clear
     * @param color the clear color
     */
    @Override
    public void clearColorTarget(int slot, Vec4 color) {
        assert slot >= 0 && slot < getMaxColorTargets();

        context.clearRenderTargetView(colorTargets[slot], color.x, color.y, color.z, color.w);
    }

    /**
     * Clears the depth in the current depth-stencil target.
     * 
     * @param depth the depth clear value
     */
    @Override
    public void clearDepthTarget(float depth) {
        if (depthStencilTarget == null)
            throw new IllegalStateException("depthStencilTarget != null");

        context.clearDepthStencilView(depthStencilTarget, D3D11.CLEAR_DEPTH, depth, (byte) 0);
    }

    /**
     * Clears the stencils in the current depth-stencil target.
     * 
     * @param stencil the stencil clear value
     */
    @Override
    public void clearStencilTarget(byte stencil) {
        if (depthStencilTarget == null)
            throw new IllegalStateException("depthStencilTarget != null");

        context.clearDepthStencilView(depthStencilTarget, D3D11.CLEAR_STENCIL, 0.0f, stencil);
    }

    /**
     * Sets the current depth-stencil state.
     * 
     * @param state the depth-stencil state
     */
    @Override
    public void setDepthStencilState(DepthStencilState state) {
        depthStencilState = ((D3D11DepthStencilState) state).state;
        context.omSetDepthStencilState(depthStencilState, stencilCompareValue);
    }

    /**
     * Sets the stencil compare value used for stencil testing.
     * 
     * @param value the stencil compare value
     */
    @Override
    public void setStencilCompareValue(byte value) {
        stencilCompareValue = value;
        context.omSetDepthStencilState(depthStencilState, value);
    }

    /**
     * Sets blending enabled or disabled
     * 
     * @param enabled true to enable blending
     */
    @Override
    public void setBlendEnabled(boolean enabled) {
        context.omSetBlendState(enabled ? blendState : null, null, 0xFFFFFFFF);
    }

    /**
     * Sets the current blend state.
     * 
     * @param state the blend state
     */
    @Override
    public void setBlendState(BlendState state) {
        context.omSetBlendState(((D3D11BlendState) state).state, null, 0xFFFFFFFF);
    }

    /**
     * Gets the maximum number of texture slots the graphics device supports.
     * 
     * @return the maximum number of texture slots the graphics device supports
     */
    @Override
    public int getMaxTextureSlots() {
        return 2;
    }

    /**
     * Creates a vertex shader
     * 
     * @param code     the vertex shader program
     * @param elements the input layout elements of the vertex shader
     * @return the created vertex shader
     */
    @Override
    public VertexShader createVertexShader(InputStream code, InputLayoutElement... elements) {
        try {
            byte[] bytes = new byte[256];
            int readByte = code.read();
            int size = 0;

            while (readByte != -1) {
                bytes[size++] = (byte) readByte;
                readByte = code.read();

                if (size == bytes.length) {
                    byte[] newBytes = new byte[2 * bytes.length];

                    for (int i = 0; i < bytes.length; i++)
                        newBytes[i] = bytes[i];

                    bytes = newBytes;
                }
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(size);
            buffer.put(bytes, 0, size);

            return new D3D11VertexShader(
                    device.createVertexShader(buffer, null),
                    device.createInputLayout(D3D11Converter.toInputElementDescs(elements), buffer));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a fragment shader.
     * 
     * @param code the fragment shader program
     * @return the created fragment shader
     */
    @Override
    public FragmentShader createFragmentShader(InputStream code) {
        try {
            byte[] bytes = new byte[256];
            int readByte = code.read();
            int size = 0;

            while (readByte != -1) {
                bytes[size++] = (byte) readByte;
                readByte = code.read();

                if (size == bytes.length) {
                    byte[] newBytes = new byte[2 * bytes.length];

                    for (int i = 0; i < bytes.length; i++)
                        newBytes[i] = bytes[i];

                    bytes = newBytes;
                }
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(size);
            buffer.put(bytes, 0, size);

            return new D3D11FragmentShader(device.createPixelShader(buffer, null));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a uniform.
     * 
     * @param size the size of the uniform in bytes
     * @return the create uniform
     */
    @Override
    public Uniform createUniform(int size) {
        assert size > 0;

        return new D3D11Uniform(
                context,
                device.createBuffer(
                        D3D11.BufferDesc.create(
                                size,
                                D3D11.USAGE_DEFAULT,
                                D3D11.BIND_CONSTANT_BUFFER,
                                0,
                                0,
                                0),
                        null),
                size);
    }

    /**
     * Creates a SamplerState.
     * 
     * @param filter the filter of the SamplerState
     * @param method the texture addressing method of the SamplerState
     * @return the created SamplerState
     */
    @Override
    public SamplerState createSamplerState(Filter filter, TextureAddressingMethod method) {
        return new D3D11SamplerState(
                device.createSamplerState(
                        D3D11.SamplerDesc.create(
                                D3D11Converter.toFilter(filter),
                                D3D11Converter.toTextureAddressMode(method),
                                D3D11Converter.toTextureAddressMode(method),
                                D3D11Converter.toTextureAddressMode(method),
                                0.0f,
                                0,
                                D3D11.COMPARISON_NEVER,
                                new float[] { 0.0f, 0.0f, 0.0f, 1.0f },
                                0.0f,
                                Float.MAX_VALUE)));
    }

    /**
     * Deletes a vertex shader.
     * 
     * @param shader the vertex shader to delete
     */
    @Override
    public void deleteShader(VertexShader shader) {
        ((D3D11VertexShader) shader).shader.release();
    }

    /**
     * Deletes a fragment shader.
     * 
     * @param shader the fragment shader to delete
     */
    @Override
    public void deleteShader(FragmentShader shader) {
        ((D3D11FragmentShader) shader).shader.release();
    }

    /**
     * Deletes a uniform.
     * 
     * @param uniform the uniform to delete
     */
    @Override
    public void deleteUniform(Uniform uniform) {
        ((D3D11Uniform) uniform).buffer.release();
    }

    /**
     * Deletes a SamplerState.
     * 
     * @param state the SamplerState to delete
     */
    @Override
    public void deleteSamplerState(SamplerState state) {
        ((D3D11SamplerState) state).state.release();
    }

    /**
     * Sets the current vertex shader.
     * 
     * @param shader the vertex shader
     */
    @Override
    public void setVertexShader(VertexShader shader) {
        D3D11VertexShader d3d11Shader = (D3D11VertexShader) shader;
        context.vsSetShader(shader != null ? d3d11Shader.shader : null, null);
        context.iaSetInputLayout(shader != null ? d3d11Shader.inputLayout : null);
    }

    /**
     * Sets the current fragment shader.
     * 
     * @param shader the fragment shader
     */
    @Override
    public void setFragmentShader(FragmentShader shader) {
        context.psSetShader(shader != null ? ((D3D11FragmentShader) shader).shader : null, null);
    }

    /**
     * Assigns a given texture slot to a given texture.
     * 
     * @param type    the slot type
     * @param slot    the slot of type to assign to
     * @param texture the texture
     */
    @Override
    public void setTexture(ShaderType type, int slot, Texture texture) {
        assert slot >= 0 && slot < getMaxTextureSlots();

        if (type == ShaderType.VERTEX) {
            context.vsSetShaderResources(
                    slot,
                    texture != null
                        ? new D3D11.ShaderResourceView[] {
                                ((D3D11Texture) texture).view
                        }
                        : null);
        }
        else {
            context.psSetShaderResources(
                    slot,
                    texture != null
                        ? new D3D11.ShaderResourceView[] {
                                ((D3D11Texture) texture).view
                        }
                        : null);
        }
    }

    /**
     * Assigns a given sampler state slot to a given sampler state.
     * 
     * @param type  the slot type
     * @param slot  the slot of type to assign to
     * @param state the sampler state
     */
    @Override
    public void setSamplerState(ShaderType type, int slot, SamplerState state) {
        assert slot >= 0 && slot < getMaxTextureSlots();

        if (type == ShaderType.VERTEX) {
            context.vsSetSamplers(
                    slot,
                    state != null
                        ? new D3D11.SamplerState[] {
                                ((D3D11SamplerState) state).state
                        }
                    : null);
        }
        else {
            context.psSetSamplers(
                    slot,
                    state != null
                        ? new D3D11.SamplerState[] {
                                ((D3D11SamplerState) state).state
                        }
                        : null);
        }
    }

    /**
     * Assigns a given uniform slot to a given uniform.
     * 
     * @param type    the slot type
     * @param slot    the slot of type to assign to
     * @param uniform the uniform
     */
    @Override
    public void setUniform(ShaderType type, int slot, Uniform uniform) {
        if (type == ShaderType.VERTEX) {
            context.vsSetConstantBuffers(
                    slot,
                    uniform != null
                        ? new D3D11.Buffer[] {
                                ((D3D11Uniform) uniform).buffer
                        }
                        : null);
        }
        else {
            context.psSetConstantBuffers(
                    slot,
                    uniform != null
                        ? new D3D11.Buffer[] {
                                ((D3D11Uniform) uniform).buffer
                        }
                        : null);
        }
    }

    /**
     * Creates an RGBA8 texture.
     * 
     * @param width   the width of the texture in texels
     * @param height  the height of the texture in texels
     * @param genMips true to generate mipmaps; otherwise false
     * @return the created texture
     */
    @Override
    public Rgba8Texture createRgba8Texture(int width, int height, boolean genMips) {
        assert width > 0 && height > 0;

        return new D3D11Rgba8Texture(
                device,
                device.createTexture2D(
                        D3D11.Texture2DDesc.create(
                                width,
                                height,
                                1,
                                1,
                                Dxgi.FORMAT_R8G8B8A8_UNORM,
                                Dxgi.SampleDesc.create(1, 0),
                                D3D11.USAGE_DEFAULT,
                                D3D11.BIND_SHADER_RESOURCE,
                                0,
                                0),
                        null),
                width,
                height,
                genMips);
    }

    /**
     * Deletes an RGBA8 texture.
     * 
     * @param texture the RGBA8 texture to delete
     */
    @Override
    public void deleteTexture(Rgba8Texture texture) {
        ((D3D11Rgba8Texture) texture).view.release();
        ((D3D11Rgba8Texture) texture).texture.release();
    }

    /**
     * Gets the maximum number of vertex buffer slots the graphics device supports.
     * 
     * @return the maximum number of vertex buffer slots the graphics device
     *         supports
     */
    @Override
    public int getMaxVertexBufferSlots() {
        return 1;
    }

    /**
     * Creates a vertex buffer.
     * 
     * @param vertexCount the number of vertices in the vertex buffer
     * @param vertexSize  the size of each vertex in the vertex buffer
     * @return the created vertex buffer
     */
    @Override
    public VertexBuffer createVertexBuffer(int vertexCount, int vertexSize) {
        assert vertexCount > 0 && vertexSize > 0;

        return new D3D11VertexBuffer(
                context,
                device.createBuffer(
                        D3D11.BufferDesc.create(
                                vertexCount * vertexSize,
                                D3D11.USAGE_DEFAULT,
                                D3D11.BIND_VERTEX_BUFFER,
                                0,
                                0,
                                vertexSize),
                        null),
                vertexCount, vertexSize);
    }

    /**
     * Creates an index buffer.
     * 
     * @param indexCount the number of indices in the index buffer
     * @return the created index buffer
     */
    @Override
    public IndexBuffer createIndexBuffer(int indexCount) {
        assert indexCount > 0;

        return new D3D11IndexBuffer(
                context,
                device.createBuffer(
                        D3D11.BufferDesc.create(
                                2 * indexCount,
                                D3D11.USAGE_DEFAULT,
                                D3D11.BIND_INDEX_BUFFER,
                                0,
                                0,
                                2),
                        null),
                indexCount);
    }

    /**
     * Deletes vertex buffer,
     * 
     * @param buffer the vertex buffer to delete
     */
    @Override
    public void deleteBuffer(VertexBuffer buffer) {
        ((D3D11VertexBuffer) buffer).buffer.release();
    }

    /**
     * Deletes an index buffer.
     * 
     * @param buffer the index buffer to delete
     */
    @Override
    public void deleteBuffer(IndexBuffer buffer) {
        ((D3D11IndexBuffer) buffer).buffer.release();
    }

    /**
     * Assigns a given vertex buffer slot to a given vertex buffer.
     * 
     * @param slot   the slot to assign to
     * @param buffer the vertex buffer
     */
    @Override
    public void setVertexBuffer(int slot, VertexBuffer buffer) {
        assert slot >= 0 && slot < getMaxVertexBufferSlots();

        if (buffer != null) {
            D3D11VertexBuffer d3d11Buffer = (D3D11VertexBuffer) buffer;
            context.iaSetVertexBuffers(
                    slot,
                    new D3D11.Buffer[] { d3d11Buffer.buffer },
                    new int[] { d3d11Buffer.vertexSize },
                    new int[] { 0 });
        }
        else {
            context.iaSetVertexBuffers(slot, null, null, null);
        }
    }

    /**
     * Sets the current index buffer.
     * 
     * @param buffer the index buffer
     */
    @Override
    public void setIndexBuffer(IndexBuffer buffer) {
        context.iaSetIndexBuffer(
                buffer != null
                    ? ((D3D11IndexBuffer) buffer).buffer
                    : null,
                Dxgi.FORMAT_R16_UINT,
                0);
    }

    /**
     * Sets the current primitive topology.
     * 
     * @param topology the primitive topology
     */
    @Override
    public void setPrimitiveTopology(PrimitiveTopology topology) {
        context.iaSetPrimitiveTopology(D3D11Converter.toPrimitiveTopology(topology));
    }

    /**
     * Runs the vertex shader and fragment shader with the vertex input as the bound
     * vertex buffers. The vertex shader will read directly from the vertex buffer.
     * 
     * @param vertexCount the number of vertices used by the vertex shader
     */
    @Override
    public void draw(int vertexCount) {
        assert vertexCount >= 0;

        context.draw(vertexCount, 0);
    }

    /**
     * Runs the vertex shader and fragment shader with the vertex input as the bound
     * vertex buffers. The vertex shader will use the bounded index buffer to get
     * vertices from the bound vertex buffers.
     * 
     * @param indexCount the number of indices used by the vertex shader
     */
    @Override
    public void drawIndexed(int indexCount) {
        assert indexCount >= 0;

        context.drawIndexed(indexCount, 0, 0);
    }
}