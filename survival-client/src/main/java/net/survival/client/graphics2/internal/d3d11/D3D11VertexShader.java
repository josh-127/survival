package net.survival.client.graphics2.internal.d3d11;

import dev.josh127.libwin32.D3D11;
import net.survival.client.graphics2.internal.VertexShader;

class D3D11VertexShader implements VertexShader
{
    final D3D11.VertexShader shader;
    final D3D11.InputLayout inputLayout;

    D3D11VertexShader(D3D11.VertexShader shader, D3D11.InputLayout layout) {
        this.shader = shader;
        inputLayout = layout;
    }
}