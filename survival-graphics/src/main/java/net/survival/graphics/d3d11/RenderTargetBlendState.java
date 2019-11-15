package net.survival.graphics.d3d11;

/**
 * Represents the blend state of a render target.
 * 
 * During the output merging stage of the rendering pipeline, the fragments
 * produced by the fragment shader are blended with the previous pixels in the
 * render target. A RenderTargetBlendState describes how fragments are blended
 * with previous pixels in a render target.
 * 
 * The equation used for blending colors is: srcFactor * (fragmentColor) +
 * dstFactor * (renderTargetPreviousPixelColor)
 * 
 * The equation used for blending alpha is: srcFactor * (fragmentAlpha) +
 * dstFactor * (renderTargetPreviousPixelAlpha)
 */
public class RenderTargetBlendState
{
    public final boolean blendEnabled;
    public final BlendOperation blendOperation;
    public final BlendOperation alphaOperation;
    public final BlendFactor srcFactor;
    public final BlendFactor dstFactor;
    public final BlendFactor srcAlphaFactor;
    public final BlendFactor dstAlphaFactor;

    private RenderTargetBlendState(
            boolean blendEnabled,
            BlendOperation blendOperation,
            BlendOperation alphaOperation,
            BlendFactor srcFactor,
            BlendFactor dstFactor,
            BlendFactor srcAlphaFactor,
            BlendFactor dstAlphaFactor)
    {
        this.blendEnabled = blendEnabled;
        this.blendOperation = blendOperation;
        this.alphaOperation = alphaOperation;
        this.srcFactor = srcFactor;
        this.dstFactor = dstFactor;
        this.srcAlphaFactor = srcAlphaFactor;
        this.dstAlphaFactor = dstAlphaFactor;
    }

    /**
     * Creates a render target builder.
     * 
     * @return a render target builder
     */
    public static Builder createBuilder() {
        return new Builder();
    }

    /**
     * A RenderTargetBlendState.Builder is used to create render target blend
     * states.
     */
    public static class Builder
    {
        private boolean blendEnabled;
        private BlendOperation blendOperation;
        private BlendOperation alphaOperation;
        private BlendFactor srcFactor;
        private BlendFactor dstFactor;
        private BlendFactor srcAlphaFactor;
        private BlendFactor dstAlphaFactor;

        protected Builder() {
            blendEnabled = false;
            blendOperation = BlendOperation.ADD;
            alphaOperation = BlendOperation.ADD;
            srcFactor = BlendFactor.ONE;
            dstFactor = BlendFactor.ZERO;
            srcAlphaFactor = BlendFactor.ONE;
            dstAlphaFactor = BlendFactor.ZERO;
        }

        /**
         * Sets blending of the RenderTargetBlendState enabled or disable
         * 
         * @param enabled true to enable blending
         * @return the builder
         */
        public Builder withBlendEnabled(boolean enabled) {
            blendEnabled = enabled;
            return this;
        }

        /**
         * Sets the blend operation for the color channel of the RenderTargetBlendState.
         * 
         * @param operation the blend operation
         * @return the builder
         */
        public Builder withBlendOperation(BlendOperation operation) {
            blendOperation = operation;
            return this;
        }

        /**
         * Sets the blend operation for the alpha channel of the RenderTargetBlendState.
         * 
         * @param operation the blend operation
         * @return the builder
         */
        public Builder withAlphaOperation(BlendOperation operation) {
            alphaOperation = operation;
            return this;
        }

        /**
         * Sets the source factor.
         * 
         * @param factor the source factor
         * @return the builder
         */
        public Builder withSrcFactor(BlendFactor factor) {
            srcFactor = factor;
            return this;
        }

        /**
         * Sets the destination factor.
         * 
         * @param factor the destination factor
         * @return the builder
         */
        public Builder withDstFactor(BlendFactor factor) {
            dstFactor = factor;
            return this;
        }

        /**
         * Sets the source alpha factor.
         * 
         * @param factor the source alpha factor
         * @return the builder
         */
        public Builder withSrcAlphaFactor(BlendFactor factor) {
            srcAlphaFactor = factor;
            return this;
        }

        /**
         * Sets the destination alpha factor.
         * 
         * @param factor the destination alpha factor
         * @return the builder
         */
        public Builder withDstAlphaFactor(BlendFactor factor) {
            dstAlphaFactor = factor;
            return this;
        }

        /**
         * Builds the RenderTargetBlendState from the RenderTargetBlendState.Builder.
         * 
         * @return the built RenderTargetBlendState
         */
        public RenderTargetBlendState build() {
            return new RenderTargetBlendState(
                    blendEnabled,
                    blendOperation,
                    alphaOperation,
                    srcFactor,
                    dstFactor,
                    srcAlphaFactor,
                    dstAlphaFactor);
        }
    }
}