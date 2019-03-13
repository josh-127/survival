package net.survival.client.graphics2.internal;

import java.io.InputStream;

/**
 * GraphicsResourceFactory creates and deletes disposable graphics resources.
 */
public interface GraphicsResourceFactory
{
    /**
     * Creates a color target.
     * 
     * @param width  the width of the color target in pixels
     * @param height the height of the color target in pixels
     * @param format the format of the color target
     * @return the created color target
     */
    ColorTarget createColorTarget(int width, int height, RenderTargetFormat format);

    /**
     * Creates a depth-stencil target.
     * 
     * @param width  the width of the depth-stencil target in pixels
     * @param height the height of the depth-stencil target in pixels
     * @return the created depth-stencil target
     */
    DepthStencilTarget createDepthStencilTarget(int width, int height);

    /**
     * Creates a DepthStencilState builder.
     * 
     * @return a DepthStencilState builder
     */
    DepthStencilState.Builder createDepthStencilStateBuilder();

    /**
     * Gets the default depth-stencil state.
     * 
     * @return the default depth-stencil state.
     */
    DepthStencilState getDefaultDepthStencilState();

    /**
     * Creates a BlendState builder.
     * 
     * @return a BlendState builder
     */
    BlendState.Builder createBlendStateBuilder();

    /**
     * Deletes a color target.
     * 
     * @param target the color target to delete
     */
    void deleteRenderTarget(ColorTarget target);

    /**
     * Deletes a depth-stencil target.
     * 
     * @param target the depth-stencil target to delete
     */
    void deleteRenderTarget(DepthStencilTarget target);

    /**
     * Deletes a DepthStencilState.
     * 
     * @param state the DepthStencilState to delete
     */
    void deleteDepthStencilState(DepthStencilState state);

    /**
     * Deletes a BlendState.
     * 
     * @param state the BlendState to delete
     */
    void deleteBlendState(BlendState state);

    /**
     * Creates a vertex shader
     * 
     * @param code     the vertex shader program
     * @param elements the input layout elements of the vertex shader
     * @return the created vertex shader
     */
    VertexShader createVertexShader(InputStream code, InputLayoutElement... elements);

    /**
     * Creates a fragment shader.
     * 
     * @param code the fragment shader program
     * @return the created fragment shader
     */
    FragmentShader createFragmentShader(InputStream code);

    /**
     * Creates a uniform.
     * 
     * @param size the size of the uniform in bytes
     * @return the create uniform
     */
    Uniform createUniform(int size);

    /**
     * Creates a SamplerState.
     * 
     * @param filter the filter of the SamplerState
     * @param method the texture addressing method of the SamplerState
     * @return the created SamplerState
     */
    SamplerState createSamplerState(Filter filter, TextureAddressingMethod method);

    /**
     * Deletes a vertex shader.
     * 
     * @param shader the vertex shader to delete
     */
    void deleteShader(VertexShader shader);

    /**
     * Deletes a fragment shader.
     * 
     * @param shader the fragment shader to delete
     */
    void deleteShader(FragmentShader shader);

    /**
     * Deletes a uniform.
     * 
     * @param uniform the uniform to delete
     */
    void deleteUniform(Uniform uniform);

    /**
     * Deletes a SamplerState.
     * 
     * @param state the SamplerState to delete
     */
    void deleteSamplerState(SamplerState state);

    /**
     * Creates an RGBA8 texture.
     * 
     * @param width   the width of the texture in texels
     * @param height  the height of the texture in texels
     * @param genMips true to generate mipmaps; otherwise false
     * @return the created texture
     */
    Rgba8Texture createRgba8Texture(int width, int height, boolean genMips);

    /**
     * Deletes an RGBA8 texture.
     * 
     * @param texture the RGBA8 texture to delete
     */
    void deleteTexture(Rgba8Texture texture);

    /**
     * Creates a vertex buffer.
     * 
     * @param vertexCount the number of vertices in the vertex buffer
     * @param vertexSize  the size of each vertex in the vertex buffer
     * @return the created vertex buffer
     */
    VertexBuffer createVertexBuffer(int vertexCount, int vertexSize);

    /**
     * Creates an index buffer.
     * 
     * @param indexCount the number of indices in the index buffer
     * @return the created index buffer
     */
    IndexBuffer createIndexBuffer(int indexCount);

    /**
     * Deletes vertex buffer,
     * 
     * @param buffer the vertex buffer to delete
     */
    void deleteBuffer(VertexBuffer buffer);

    /**
     * Deletes an index buffer.
     * 
     * @param buffer the index buffer to delete
     */
    void deleteBuffer(IndexBuffer buffer);
}