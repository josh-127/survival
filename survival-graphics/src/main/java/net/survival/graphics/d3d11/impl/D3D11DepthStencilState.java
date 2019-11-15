package net.survival.graphics.d3d11.impl;

import dev.josh127.libwin32.D3D11;
import net.survival.graphics.d3d11.DepthStencilState;

/**
 * Represents a depth-stencil state.
 */
class D3D11DepthStencilState extends DepthStencilState
{
    final D3D11.DepthStencilState state;

    /**
     * Constructs a D3D11DepthStencilState.
     * 
     * @param state the underlying depth-stencil state
     */
    public D3D11DepthStencilState(D3D11.DepthStencilState state) {
        this.state = state;
    }

    /**
     * Represents the builder for DepthStencilState.
     */
    public static class Builder extends DepthStencilState.Builder
    {
        private final D3D11.Device device;

        /**
         * Constructs a DepthStencilState builder.
         * 
         * @param device the underlying graphics device
         */
        public Builder(D3D11.Device device) {
            this.device = device;
        }

        /**
         * Builds the DepthStencilState.
         * 
         * @return the built DepthStencilState
         */
        @Override
        public DepthStencilState build() {
            return new D3D11DepthStencilState(device.createDepthStencilState(D3D11.DepthStencilDesc.create(
                    depthTestEnabled, canWriteDepth ? D3D11.DEPTH_WRITE_MASK_ALL : D3D11.DEPTH_WRITE_MASK_ZERO,
                    D3D11Converter.toComparison(depthComparison), stencilTestEnabled, stencilReadMask, stencilWriteMask,
                    D3D11Converter.toDepthStencilOpDesc(frontFace), D3D11Converter.toDepthStencilOpDesc(backFace))));
        }
    }
}