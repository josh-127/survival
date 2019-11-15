package net.survival.graphics.d3d11;

/**
 * Represents a depth-stencil state.
 */
public abstract class DepthStencilState
{
    /**
     * Represents the builder for DepthStencilState.
     */
    public static abstract class Builder
    {
        protected boolean depthTestEnabled;
        protected boolean canWriteDepth;
        protected DepthStencilComparison depthComparison;
        protected boolean stencilTestEnabled;
        protected byte stencilReadMask;
        protected byte stencilWriteMask;
        protected DepthStencilFunction frontFace;
        protected DepthStencilFunction backFace;

        /**
         * Constructs a DepthStencilState builder.
         */
        public Builder() {
            depthTestEnabled = true;
            canWriteDepth = true;
            depthComparison = DepthStencilComparison.LT;
            stencilTestEnabled = true;
            stencilReadMask = (byte) 0xFF;
            stencilWriteMask = (byte) 0xFF;
            frontFace = new DepthStencilFunction(
                    StencilOperation.KEEP,
                    StencilOperation.ZERO,
                    StencilOperation.KEEP,
                    DepthStencilComparison.ALWAYS);
            backFace = new DepthStencilFunction(
                    StencilOperation.KEEP,
                    StencilOperation.ZERO,
                    StencilOperation.KEEP,
                    DepthStencilComparison.ALWAYS);
        }

        /**
         * Sets depth test enabled or disabled.
         * 
         * @param enabled true for enabled and false for disabled
         * @return the builder
         */
        public Builder withDepthTestEnabled(boolean enabled) {
            depthTestEnabled = enabled;
            return this;
        }

        /**
         * Sets the ability to write depth enabled or disabled.
         * 
         * @param canWriteDepth true for enabled and false for disabled
         * @return the builder
         */
        public Builder withCanWriteDepth(boolean canWriteDepth) {
            this.canWriteDepth = canWriteDepth;
            return this;
        }

        /**
         * Sets the depth comparison function used for depth testing.
         * 
         * @param comparison the depth comparison function
         * @return the builder
         */
        public Builder withDepthComparison(DepthStencilComparison comparison) {
            depthComparison = comparison;
            return this;
        }

        /**
         * Sets stencil test enabled or disabled.
         * 
         * @param enabled true for enabled and false for disabled
         * @return the builder
         */
        public Builder withStencilTestEnabled(boolean enabled) {
            stencilTestEnabled = enabled;
            return this;
        }

        /**
         * Sets the stencil read mask.
         * 
         * @param mask the stencil read mask
         * @return the builder
         */
        public Builder withStencilReadMask(byte mask) {
            stencilReadMask = mask;
            return this;
        }

        /**
         * Sets the stencil write mask.
         * 
         * @param mask the stencil write mask
         * @return the builder
         */
        public Builder withStencilWriteMask(byte mask) {
            stencilWriteMask = mask;
            return this;
        }

        /**
         * Sets the depth-stencil function for front faces.
         * 
         * @param function the depth-stencil function
         * @return the builder
         */
        public Builder withFrontFace(DepthStencilFunction function) {
            frontFace = function;
            return this;
        }

        /**
         * Sets the depth-stencil function for back faces.
         * 
         * @param function the depth-stencil function
         * @return the builder
         */
        public Builder withBackFace(DepthStencilFunction function) {
            backFace = function;
            return this;
        }

        /**
         * Builds the DepthStencilState.
         * 
         * @return the built DepthStencilState
         */
        public abstract DepthStencilState build();
    }
}