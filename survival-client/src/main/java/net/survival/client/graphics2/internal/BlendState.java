package net.survival.client.graphics2.internal;

/**
 * Represents a blend state.
 */
public abstract class BlendState
{
    /**
     * The builder for BlendState.
     */
    public static abstract class Builder
    {
        protected final RenderTargetBlendState[] renderTargetBlendStates;
        protected int renderTargetBlendStateCount;

        /**
         * Constructs a BlendState builder.
         * 
         * @param info the graphics device info
         */
        public Builder(GraphicsDeviceInfo info) {
            renderTargetBlendStates = new RenderTargetBlendState[info.getMaxColorTargets()];
        }

        /**
         * Adds a render target blend state
         * 
         * @param state the render target blend state
         * @return the builder
         */
        public Builder addRenderTargetBlendState(RenderTargetBlendState state) {
            renderTargetBlendStates[renderTargetBlendStateCount++] = state;
            return this;
        }

        /**
         * Builds the BlendState.
         * 
         * @return the built BlendState
         */
        public abstract BlendState build();
    }
}