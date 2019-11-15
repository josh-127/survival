package net.survival.graphics.d3d11.impl;

import dev.josh127.libwin32.D3D11;
import net.survival.graphics.d3d11.BlendState;
import net.survival.graphics.d3d11.GraphicsDeviceInfo;

/**
 * Represents a blend state.
 */
class D3D11BlendState extends BlendState
{
    final D3D11.BlendState state;

    /**
     * Constructs a D3D11BlendState.
     * 
     * @param state the underlying blend state
     */
    D3D11BlendState(D3D11.BlendState state) {
        this.state = state;
    }

    /**
     * The builder for BlendState.
     */
    public static class Builder extends BlendState.Builder
    {
        private final D3D11.Device device;

        /**
         * Constructs a BlendState builder.
         * 
         * @param info the graphics device info
         */
        public Builder(GraphicsDeviceInfo info, D3D11.Device device) {
            super(info);
            this.device = device;
        }

        /**
         * Builds the BlendState.
         * 
         * @return the built BlendState
         */
        @Override
        public BlendState build() {
            D3D11.RenderTargetBlendDesc[] descs = new D3D11.RenderTargetBlendDesc[renderTargetBlendStateCount];

            for (int i = 0; i < descs.length; i++) {
                descs[i] = D3D11.RenderTargetBlendDesc.create(
                        renderTargetBlendStates[i].blendEnabled,
                        D3D11Converter.toBlend(renderTargetBlendStates[i].srcFactor),
                        D3D11Converter.toBlend(renderTargetBlendStates[i].dstFactor),
                        D3D11Converter.toBlendOp(renderTargetBlendStates[i].blendOperation),
                        D3D11Converter.toBlend(renderTargetBlendStates[i].srcAlphaFactor),
                        D3D11Converter.toBlend(renderTargetBlendStates[i].dstAlphaFactor),
                        D3D11Converter.toBlendOp(renderTargetBlendStates[i].alphaOperation),
                        (byte) D3D11.COLOR_WRITE_ENABLE_ALL);
            }

            return new D3D11BlendState(device.createBlendState(D3D11.BlendDesc.create(false, false, descs)));
        }
    }
}