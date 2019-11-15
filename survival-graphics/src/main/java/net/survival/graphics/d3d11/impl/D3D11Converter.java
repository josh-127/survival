package net.survival.graphics.d3d11.impl;

import java.util.ArrayList;

import dev.josh127.libwin32.D3D11;
import dev.josh127.libwin32.Dxgi;
import net.survival.graphics.d3d11.BlendFactor;
import net.survival.graphics.d3d11.BlendOperation;
import net.survival.graphics.d3d11.DepthStencilComparison;
import net.survival.graphics.d3d11.DepthStencilFunction;
import net.survival.graphics.d3d11.Filter;
import net.survival.graphics.d3d11.InputLayoutElement;
import net.survival.graphics.d3d11.PrimitiveTopology;
import net.survival.graphics.d3d11.RenderTargetFormat;
import net.survival.graphics.d3d11.ShaderPrimitiveType;
import net.survival.graphics.d3d11.StencilOperation;
import net.survival.graphics.d3d11.TextureAddressingMethod;

/**
 * Provides utility methods for converting engine.graphics structures to D3D11
 * structures.
 */
final class D3D11Converter
{
    private D3D11Converter() {
    }

    /**
     * Converts engine.graphics.DepthStencilComparison to D3D11_COMPARISON.
     * 
     * @param comparison the depth stencil comparison
     * @return the converted value
     */
    public static int toComparison(DepthStencilComparison comparison) {
        switch (comparison) {
        case ALWAYS:
            return D3D11.COMPARISON_ALWAYS;
        case NEVER:
            return D3D11.COMPARISON_NEVER;
        case EQ:
            return D3D11.COMPARISON_EQUAL;
        case NEQ:
            return D3D11.COMPARISON_NOT_EQUAL;
        case LT:
            return D3D11.COMPARISON_LESS;
        case LTE:
            return D3D11.COMPARISON_LESS_EQUAL;
        case GT:
            return D3D11.COMPARISON_GREATER;
        case GTE:
            return D3D11.COMPARISON_GREATER_EQUAL;
        default:
            throw new UnsupportedOperationException("comparison is unknown");
        }
    }

    /**
     * Converts engine.graphics.DepthStencilFunction to D3D11_DEPTH_STENCILOP_DESC.
     * 
     * @param function the depth stencil function
     * @return the converted structure
     */
    public static D3D11.DepthStencilOpDesc toDepthStencilOpDesc(DepthStencilFunction function) {
        return D3D11.DepthStencilOpDesc.create(
                toStencilOp(function.failOperation), toStencilOp(function.depthFailOperation),
                toStencilOp(function.passOperation), toComparison(function.comparison));
    }

    /**
     * Converts engine.graphics.StencilOperation to D3D11_STENCIL_OP.
     * 
     * @param operation the stencil operation
     * @return the converted value
     */
    private static int toStencilOp(StencilOperation operation) {
        switch (operation) {
        case KEEP:
            return D3D11.STENCIL_OP_KEEP;
        case ZERO:
            return D3D11.STENCIL_OP_ZERO;
        case REPLACE:
            return D3D11.STENCIL_OP_REPLACE;
        case INVERT:
            return D3D11.STENCIL_OP_INVERT;
        case INCREMENT_WARP:
            return D3D11.STENCIL_OP_INCR;
        case DECREMENT_WARP:
            return D3D11.STENCIL_OP_DECR;
        case INCREMENT_CLAMP:
            return D3D11.STENCIL_OP_INCR_SAT;
        case DECREMENT_CLAMP:
            return D3D11.STENCIL_OP_DECR_SAT;
        default:
            throw new IllegalArgumentException("operation is unknown");
        }
    }

    /**
     * Converts engine.graphics.BlendFactor to D3D11_BLEND.
     * 
     * @param factor the blend factor
     * @return the converted value
     */
    public static int toBlend(BlendFactor factor) {
        switch (factor) {
        case ONE:
            return D3D11.BLEND_ONE;
        case ZERO:
            return D3D11.BLEND_ZERO;
        case SRC_COLOR:
            return D3D11.BLEND_SRC_COLOR;
        case SRC_ALPHA:
            return D3D11.BLEND_SRC_ALPHA;
        case DST_COLOR:
            return D3D11.BLEND_DEST_COLOR;
        case DST_ALPHA:
            return D3D11.BLEND_DEST_ALPHA;
        case ONE_MINUS_SRC_COLOR:
            return D3D11.BLEND_INV_SRC_COLOR;
        case ONE_MINUS_SRC_ALPHA:
            return D3D11.BLEND_INV_SRC_ALPHA;
        case ONE_MINUS_DST_COLOR:
            return D3D11.BLEND_INV_DEST_COLOR;
        case ONE_MINUS_DST_ALPHA:
            return D3D11.BLEND_INV_DEST_ALPHA;
        default:
            throw new IllegalArgumentException("factor is unknown");
        }
    }

    /**
     * Converts engine.graphics.BlendOperation to D3D11_BLEND_OP.
     * 
     * @param operation the blend operationi
     * @return the converted value
     */
    public static int toBlendOp(BlendOperation operation) {
        switch (operation) {
        case ADD:
            return D3D11.BLEND_OP_ADD;
        case SUBTRACT:
            return D3D11.BLEND_OP_SUBTRACT;
        case REVERSE_SUBTRACT:
            return D3D11.BLEND_OP_REV_SUBTRACT;
        default:
            throw new IllegalArgumentException("operation is unknown");
        }
    }

    /**
     * Converts engine.graphics.ShaderPrimitiveType to DXGI_FORMAT.
     * 
     * @param type the shader primitive type
     * @return the converted value
     */
    public static int toFormat(ShaderPrimitiveType type) {
        switch (type) {
        case INT8:
            return Dxgi.FORMAT_R8_SINT;
        case INT16:
            return Dxgi.FORMAT_R16_SINT;
        case INT32:
            return Dxgi.FORMAT_R32_SINT;
        case FLOAT:
            return Dxgi.FORMAT_R32_FLOAT;
        case VEC2:
            return Dxgi.FORMAT_R32G32_FLOAT;
        case VEC3:
            return Dxgi.FORMAT_R32G32B32_FLOAT;
        case VEC4:
            return Dxgi.FORMAT_R32G32B32A32_FLOAT;
        case MAT4:
            return Dxgi.FORMAT_R32G32B32A32_FLOAT;
        default:
            throw new IllegalArgumentException("type is unknown");
        }
    }

    /**
     * Converts engine.graphics.RenderTargetFormat to DXGI_FORMAT.
     * 
     * @param format the render target format
     * @return the converted value
     */
    public static int toFormat(RenderTargetFormat format) {
        switch (format) {
        case R8G8B8A8:
            return Dxgi.FORMAT_R8G8B8A8_UNORM;
        default:
            throw new UnsupportedOperationException("format is unknown");
        }
    }

    /**
     * Gets the number of indices in a given shader primitive type.
     * 
     * @param type the shader primitive type
     * @return the number of indices
     */
    private static int getShaderPrimitiveIndexCount(ShaderPrimitiveType type) {
        switch (type) {
        case INT8:
        case INT16:
        case INT32:
        case FLOAT:
        case VEC2:
        case VEC3:
        case VEC4:
            return 1;
        case MAT4:
            return 4;
        default:
            throw new IllegalArgumentException("type is unknown");
        }
    }

    /**
     * Converts engine.graphics.Filter to D3D11_FILTER.
     * 
     * @param filter the filter
     * @return the converted value
     */
    public static int toFilter(Filter filter) {
        switch (filter) {
        case POINT:
            return D3D11.FILTER_MIN_MAG_MIP_POINT;
        case LINEAR:
            return D3D11.FILTER_MIN_MAG_MIP_LINEAR;
        default:
            throw new IllegalArgumentException("filter is unknown");
        }
    }

    /**
     * Converts engine.graphics.TextureAddressingMethod to D3D11_TEXTURE_ADDRESS.
     * 
     * @param method the texture addressing method
     * @return the converted value
     */
    public static int toTextureAddressMode(TextureAddressingMethod method) {
        switch (method) {
        case WRAP:
            return D3D11.TEXTURE_ADDRESS_WRAP;
        case CLAMP:
            return D3D11.TEXTURE_ADDRESS_CLAMP;
        default:
            throw new IllegalArgumentException("method is unknown");
        }
    }

    /**
     * Converts engine.graphics.PrimitiveTopology to D3D11_PRIMITIVE_TOPOLOGY.
     * 
     * @param topology the primitive topology
     * @return the converted value
     */
    public static int toPrimitiveTopology(PrimitiveTopology topology) {
        switch (topology) {
        case POINT_LIST:
            return D3D11.PRIMITIVE_TOPOLOGY_POINTLIST;
        case LINE_LIST:
            return D3D11.PRIMITIVE_TOPOLOGY_LINELIST;
        case TRIANGLE_LIST:
            return D3D11.PRIMITIVE_TOPOLOGY_TRIANGLELIST;
        case TRIANGLE_STRIP:
            return D3D11.PRIMITIVE_TOPOLOGY_TRIANGLELIST;
        default:
            throw new IllegalArgumentException("topology is unknown");
        }
    }

    /**
     * Converts engine.graphics.InputLayoutElement[] to D3D11_INPUT_ELEMENT_DESC.
     * 
     * @param elements an array of input layout elements
     * @return the converted structure
     */
    public static D3D11.InputElementDesc[] toInputElementDescs(InputLayoutElement[] elements) {
        ArrayList<D3D11.InputElementDesc> descs = new ArrayList<>();

        for (InputLayoutElement element : elements) {
            for (int i = 0; i < getShaderPrimitiveIndexCount(element.type); i++) {
                descs.add(D3D11.InputElementDesc.create(
                        element.name,
                        element.index,
                        toFormat(element.type),
                        element.slot,
                        element.offset,
                        D3D11.INPUT_PER_VERTEX_DATA,
                        0));
            }
        }

        return descs.toArray(new D3D11.InputElementDesc[descs.size()]);
    }
}