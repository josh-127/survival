package net.survival.graphics.d3d11;

/**
 * RenderState contains immutable state that can be applied to a graphics
 * device.
 */
public class RenderState
{
    private final ColorTarget[] colorTargets;
    private final DepthStencilTarget depthStencilTarget;
    private final DepthStencilState depthStencilState;
    private final byte stencilCompareValue;
    private final BlendState blendState;
    private final VertexShader vertexShader;
    private final FragmentShader fragmentShader;
    private final Texture[] vertexTextures;
    private final Texture[] fragmentTextures;
    private final SamplerState[] vertexSamplerStates;
    private final SamplerState[] fragmentSamplerStates;
    private final Uniform[] vertexUniforms;
    private final Uniform[] fragmentUniforms;
    private final VertexBuffer[] vertexBuffers;
    private final IndexBuffer indexBuffer;
    private final PrimitiveTopology primitiveTopology;

    private RenderState(
            ColorTarget[] colorTargets,
            DepthStencilTarget depthStencilTarget,
            DepthStencilState depthStencilState,
            byte stencilCompareValue,
            BlendState blendState,
            VertexShader vertexShader,
            FragmentShader fragmentShader,
            Texture[] vertexTextures,
            Texture[] fragmentTextures,
            SamplerState[] vertexSamplerStates,
            SamplerState[] fragmentSamplerStates,
            Uniform[] vertexUniforms,
            Uniform[] fragmentUniforms,
            VertexBuffer[] vertexBuffers,
            IndexBuffer indexBuffer,
            PrimitiveTopology primitiveTopology)
    {
        this.colorTargets = colorTargets;
        this.depthStencilTarget = depthStencilTarget;
        this.depthStencilState = depthStencilState;
        this.stencilCompareValue = stencilCompareValue;
        this.blendState = blendState;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        this.vertexTextures = vertexTextures;
        this.fragmentTextures = fragmentTextures;
        this.vertexSamplerStates = vertexSamplerStates;
        this.fragmentSamplerStates = fragmentSamplerStates;
        this.vertexUniforms = vertexUniforms;
        this.fragmentUniforms = fragmentUniforms;
        this.vertexBuffers = vertexBuffers;
        this.indexBuffer = indexBuffer;
        this.primitiveTopology = primitiveTopology;
    }

    /**
     * Creates a RenderState builder.
     * 
     * @param info    the GraphicsDeviceInfo used to create the builder
     * @param factory the GraphicsResourceFactory used to create the builder
     * @return a RenderState builder
     */
    public static Builder createBuilder(GraphicsDeviceInfo info, GraphicsResourceFactory factory) {
        return new Builder(info, factory);
    }

    /**
     * Applies the RenderState to a graphics device.
     * 
     * @param context the graphics device context
     */
    public void setRenderStateOf(RenderContext context) {
        if (colorTargets == null) {
            context.setColorTargetToBackBuffer(0);
        }
        else {
            for (int i = 0; i < colorTargets.length; i++)
                context.setColorTarget(i, colorTargets[i]);
        }

        context.setDepthStencilTarget(depthStencilTarget);
        context.setDepthStencilState(depthStencilState);
        context.setStencilCompareValue(stencilCompareValue);

        if (blendState == null) {
            context.setBlendEnabled(false);
        }
        else {
            context.setBlendEnabled(true);
            context.setBlendState(blendState);
        }

        context.setVertexShader(vertexShader);
        context.setFragmentShader(fragmentShader);

        for (int i = 0; i < vertexTextures.length; i++)
            context.setTexture(ShaderType.VERTEX, i, vertexTextures[i]);

        for (int i = 0; i < fragmentTextures.length; i++)
            context.setTexture(ShaderType.FRAGMENT, i, fragmentTextures[i]);

        for (int i = 0; i < vertexSamplerStates.length; i++)
            context.setSamplerState(ShaderType.VERTEX, i, vertexSamplerStates[i]);

        for (int i = 0; i < fragmentSamplerStates.length; i++)
            context.setSamplerState(ShaderType.FRAGMENT, i, fragmentSamplerStates[i]);

        for (int i = 0; i < vertexUniforms.length; i++)
            context.setUniform(ShaderType.VERTEX, i, vertexUniforms[i]);

        for (int i = 0; i < fragmentUniforms.length; i++)
            context.setUniform(ShaderType.FRAGMENT, i, fragmentUniforms[i]);

        for (int i = 0; i < vertexBuffers.length; i++)
            context.setVertexBuffer(i, vertexBuffers[i]);

        context.setIndexBuffer(indexBuffer);
        context.setPrimitiveTopology(primitiveTopology);
    }

    /**
     * A RenderState.Builder is used to create RenderState objects.
     */
    public static class Builder
    {
        private ColorTarget[] colorTargets;
        private DepthStencilTarget depthStencilTarget;
        private DepthStencilState depthStencilState;
        private byte stencilCompareValue;
        private BlendState blendState;
        private VertexShader vertexShader;
        private FragmentShader fragmentShader;
        private Texture[] vertexTextures;
        private Texture[] fragmentTextures;
        private SamplerState[] vertexSamplerStates;
        private SamplerState[] fragmentSamplerStates;
        private Uniform[] vertexUniforms;
        private Uniform[] fragmentUniforms;
        private VertexBuffer[] vertexBuffers;
        private IndexBuffer indexBuffer;
        private PrimitiveTopology primitiveTopology;

        private Builder(GraphicsDeviceInfo info, GraphicsResourceFactory factory) {
            colorTargets = new ColorTarget[info.getMaxColorTargets()];
            depthStencilState = factory.getDefaultDepthStencilState();
            stencilCompareValue = (byte) 0;
            vertexTextures = new Texture[info.getMaxTextureSlots()];
            fragmentTextures = new Texture[info.getMaxTextureSlots()];
            vertexSamplerStates = new SamplerState[info.getMaxTextureSlots()];
            fragmentSamplerStates = new SamplerState[info.getMaxTextureSlots()];
            vertexUniforms = new Uniform[info.getMaxTextureSlots()];
            fragmentUniforms = new Uniform[info.getMaxTextureSlots()];
            vertexBuffers = new VertexBuffer[info.getMaxVertexBufferSlots()];
            primitiveTopology = PrimitiveTopology.TRIANGLE_LIST;
        }

        /**
         * Sets the color targets that directly correspond to the color target slots.
         * 
         * @param targets the color targets
         * @return the builder
         */
        public Builder withColorTargets(ColorTarget... targets) {
            colorTargets = targets;
            return this;
        }

        /**
         * Clears all color target slots and assigns the first color target slot to the
         * back buffer.
         * 
         * @return the builder
         */
        public Builder withBackBufferOnly() {
            colorTargets = null;
            return this;
        }

        /**
         * Sets the depth-stencil target.
         * 
         * @param target the depth-stencil target
         * @return the builder
         */
        public Builder withDepthStencilTarget(DepthStencilTarget target) {
            depthStencilTarget = target;
            return this;
        }

        /**
         * Sets the depth-stencil state.
         * 
         * @param state the depth-stencil state
         * @return the builder
         */
        public Builder withDepthStencilState(DepthStencilState state) {
            depthStencilState = state;
            return this;
        }

        /**
         * Sets the stencil compare value.
         * 
         * @param value the stencil compare value
         * @return the builder
         */
        public Builder withStencilCompareValue(byte value) {
            stencilCompareValue = value;
            return this;
        }

        /**
         * Sets the blend state.
         * 
         * @param state the blend state
         * @return the builder
         */
        public Builder withBlendState(BlendState state) {
            blendState = state;
            return this;
        }

        /**
         * Sets the vertex shader.
         * 
         * @param shader the vertex shader
         * @return the builder
         */
        public Builder withVertexShader(VertexShader shader) {
            vertexShader = shader;
            return this;
        }

        /**
         * Sets the fragment shader.
         * 
         * @param shader the fragment shader
         * @return the builder
         */
        public Builder withFragmentShader(FragmentShader shader) {
            fragmentShader = shader;
            return this;
        }

        /**
         * Sets the textures that directly correspond to the vertex texture slots.
         * 
         * @param textures the textures
         * @return the builder
         */
        public Builder withVertexTextures(Texture... textures) {
            vertexTextures = textures;
            return this;
        }

        /**
         * Sets the textures that directly correspond to the fragment texture slots.
         * 
         * @param textures the textures
         * @return the builder
         */
        public Builder withFragmentTextures(Texture... textures) {
            fragmentTextures = textures;
            return this;
        }

        /**
         * Sets the sampler states that directly correspond to the vertex sampler state
         * slots.
         * 
         * @param states the sampler states
         * @return the builder
         */
        public Builder withVertexSamplerStates(SamplerState... states) {
            vertexSamplerStates = states;
            return this;
        }

        /**
         * Sets the sampler states that directly correspond to the fragment sampler
         * state slots.
         * 
         * @param states the sampler states
         * @return the builder
         */
        public Builder withFragmentSamplerStates(SamplerState... states) {
            fragmentSamplerStates = states;
            return this;
        }

        /**
         * Sets the uniforms that directly correspond to the vertex uniform slots.
         * 
         * @param uniforms the uniforms
         * @return the builder
         */
        public Builder withVertexUniforms(Uniform... uniforms) {
            vertexUniforms = uniforms;
            return this;
        }

        /**
         * Sets the uniforms that directly correspond to the fragment uniform slots.
         * 
         * @param uniforms the uniforms
         * @return the builder
         */
        public Builder withFragmentUniforms(Uniform... uniforms) {
            fragmentUniforms = uniforms;
            return this;
        }

        /**
         * Sets the vertex buffers that directly correspond to the vertex buffer slots.
         * 
         * @param buffers the vertex buffers
         * @return the builder
         */
        public Builder withVertexBuffers(VertexBuffer... buffers) {
            vertexBuffers = buffers;
            return this;
        }

        /**
         * Sets the index buffer.
         * 
         * @param buffer the index buffer
         * @return the builder
         */
        public Builder withIndexBuffer(IndexBuffer buffer) {
            indexBuffer = buffer;
            return this;
        }

        /**
         * Sets the primitive topology.
         * 
         * @param topology the primitive topology
         * @return the builder
         */
        public Builder withPrimitiveTopology(PrimitiveTopology topology) {
            primitiveTopology = topology;
            return this;
        }

        /**
         * Builds the RenderState from the RenderState.Builder.
         * 
         * @return the built RenderState
         */
        public RenderState build() {
            return new RenderState(
                    colorTargets,
                    depthStencilTarget,
                    depthStencilState,
                    stencilCompareValue,
                    blendState,
                    vertexShader,
                    fragmentShader,
                    vertexTextures,
                    fragmentTextures,
                    vertexSamplerStates,
                    fragmentSamplerStates,
                    vertexUniforms,
                    fragmentUniforms,
                    vertexBuffers,
                    indexBuffer,
                    primitiveTopology);
        }
    }
}