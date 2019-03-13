package net.survival.client.graphics2.internal.d3d11;

import dev.josh127.libwin32.D3D11;
import net.survival.client.graphics2.internal.SamplerState;

class D3D11SamplerState extends SamplerState
{
    final D3D11.SamplerState state;

    D3D11SamplerState(D3D11.SamplerState state) {
        this.state = state;
    }
}