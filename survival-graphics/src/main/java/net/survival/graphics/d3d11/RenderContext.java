package net.survival.graphics.d3d11;

import net.survival.graphics.d3d11.util.Vec4;

/**
 * A RenderContext is a graphics device context that is used for rendering.
 */
public interface RenderContext
{
    /**
     * Assigns a given color target slot to a given color target.
     * 
     * @param slot   the color target slot to assign
     * @param target the color target
     */
    void setColorTarget(int slot, ColorTarget target);

    /**
     * Assigns a given color target slot to the back buffer.
     * 
     * @param slot the color target slot to assign
     */
    void setColorTargetToBackBuffer(int slot);

    /**
     * Sets the depth-stencil target.
     * 
     * @param target the depth-stencil target
     */
    void setDepthStencilTarget(DepthStencilTarget target);

    /**
     * Clears a color target from a given slot.
     * 
     * @param slot  the slot that contains the color target to clear
     * @param color the clear color
     */
    void clearColorTarget(int slot, Vec4 color);

    /**
     * Clears the depth in the current depth-stencil target.
     * 
     * @param depth the depth clear value
     */
    void clearDepthTarget(float depth);

    /**
     * Clears the stencils in the current depth-stencil target.
     * 
     * @param stencil the stencil clear value
     */
    void clearStencilTarget(byte stencil);

    /**
     * Sets the current depth-stencil state.
     * 
     * @param state the depth-stencil state
     */
    void setDepthStencilState(DepthStencilState state);

    /**
     * Sets the stencil compare value used for stencil testing.
     * 
     * @param value the stencil compare value
     */
    void setStencilCompareValue(byte value);

    /**
     * Sets blending enabled or disabled
     * 
     * @param enabled true to enable blending
     */
    void setBlendEnabled(boolean enabled);

    /**
     * Sets the current blend state.
     * 
     * @param state the blend state
     */
    void setBlendState(BlendState state);

    /**
     * Sets the current vertex shader.
     * 
     * @param shader the vertex shader
     */
    void setVertexShader(VertexShader shader);

    /**
     * Sets the current fragment shader.
     * 
     * @param shader the fragment shader
     */
    void setFragmentShader(FragmentShader shader);

    /**
     * Assigns a given texture slot to a given texture.
     * 
     * @param type    the slot type
     * @param slot    the slot of type to assign to
     * @param texture the texture
     */
    void setTexture(ShaderType type, int slot, Texture texture);

    /**
     * Assigns a given sampler state slot to a given sampler state.
     * 
     * @param type  the slot type
     * @param slot  the slot of type to assign to
     * @param state the sampler state
     */
    void setSamplerState(ShaderType type, int slot, SamplerState state);

    /**
     * Assigns a given uniform slot to a given uniform.
     * 
     * @param type    the slot type
     * @param slot    the slot of type to assign to
     * @param uniform the uniform
     */
    void setUniform(ShaderType type, int slot, Uniform uniform);

    /**
     * Assigns a given vertex buffer slot to a given vertex buffer.
     * 
     * @param slot   the slot to assign to
     * @param buffer the vertex buffer
     */
    void setVertexBuffer(int slot, VertexBuffer buffer);

    /**
     * Sets the current index buffer.
     * 
     * @param buffer the index buffer
     */
    void setIndexBuffer(IndexBuffer buffer);

    /**
     * Sets the current primitive topology.
     * 
     * @param topology the primitive topology
     */
    void setPrimitiveTopology(PrimitiveTopology topology);

    /**
     * Runs the vertex shader and fragment shader with the vertex input as the bound
     * vertex buffers. The vertex shader will read directly from the vertex buffer.
     * 
     * @param vertexCount the number of vertices used by the vertex shader
     */
    void draw(int vertexCount);

    /**
     * Runs the vertex shader and fragment shader with the vertex input as the bound
     * vertex buffers. The vertex shader will use the bounded index buffer to get
     * vertices from the bound vertex buffers.
     * 
     * @param indexCount the number of indices used by the vertex shader
     */
    void drawIndexed(int indexCount);
}