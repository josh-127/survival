package dev.josh127.libwin32;

import java.nio.ByteBuffer;

import dev.josh127.libwin32.Dxgi.SampleDesc;
import dev.josh127.libwin32.Dxgi.SwapChain;
import dev.josh127.libwin32.Dxgi.SwapChainDesc;

public final class D3D11
{
    private D3D11() {}

    static native int nativeInit();

    public static final int DRIVER_TYPE_UNKNOWN = 0;
    public static final int DRIVER_TYPE_HARDWARE = 1;
    public static final int DRIVER_TYPE_REFERENCE = 2;
    public static final int DRIVER_TYPE_NULL = 3;
    public static final int DRIVER_TYPE_SOFTWARE = 4;
    public static final int DRIVER_TYPE_WARP = 5;

    public static final int PRIMITIVE_TOPOLOGY_UNDEFINED = 0;
    public static final int PRIMITIVE_TOPOLOGY_POINTLIST = 1;
    public static final int PRIMITIVE_TOPOLOGY_LINELIST = 2;
    public static final int PRIMITIVE_TOPOLOGY_LINESTRIP = 3;
    public static final int PRIMITIVE_TOPOLOGY_TRIANGLELIST = 4;
    public static final int PRIMITIVE_TOPOLOGY_TRIANGLESTRIP = 5;
    public static final int PRIMITIVE_TOPOLOGY_LINELIST_ADJ = 10;
    public static final int PRIMITIVE_TOPOLOGY_LINESTRIP_ADJ = 11;
    public static final int PRIMITIVE_TOPOLOGY_TRIANGLELIST_ADJ = 12;
    public static final int PRIMITIVE_TOPOLOGY_TRIANGLESTRIP_ADJ = 13;
    public static final int PRIMITIVE_TOPOLOGY_1_CONTROL_POINT_PATCHLIST = 33;
    public static final int PRIMITIVE_TOPOLOGY_2_CONTROL_POINT_PATCHLIST = 34;
    public static final int PRIMITIVE_TOPOLOGY_3_CONTROL_POINT_PATCHLIST = 35;
    public static final int PRIMITIVE_TOPOLOGY_4_CONTROL_POINT_PATCHLIST = 36;
    public static final int PRIMITIVE_TOPOLOGY_5_CONTROL_POINT_PATCHLIST = 37;
    public static final int PRIMITIVE_TOPOLOGY_6_CONTROL_POINT_PATCHLIST = 38;
    public static final int PRIMITIVE_TOPOLOGY_7_CONTROL_POINT_PATCHLIST = 39;
    public static final int PRIMITIVE_TOPOLOGY_8_CONTROL_POINT_PATCHLIST = 40;
    public static final int PRIMITIVE_TOPOLOGY_9_CONTROL_POINT_PATCHLIST = 41;
    public static final int PRIMITIVE_TOPOLOGY_10_CONTROL_POINT_PATCHLIST = 42;
    public static final int PRIMITIVE_TOPOLOGY_11_CONTROL_POINT_PATCHLIST = 43;
    public static final int PRIMITIVE_TOPOLOGY_12_CONTROL_POINT_PATCHLIST = 44;
    public static final int PRIMITIVE_TOPOLOGY_13_CONTROL_POINT_PATCHLIST = 45;
    public static final int PRIMITIVE_TOPOLOGY_14_CONTROL_POINT_PATCHLIST = 46;
    public static final int PRIMITIVE_TOPOLOGY_15_CONTROL_POINT_PATCHLIST = 47;
    public static final int PRIMITIVE_TOPOLOGY_16_CONTROL_POINT_PATCHLIST = 48;
    public static final int PRIMITIVE_TOPOLOGY_17_CONTROL_POINT_PATCHLIST = 49;
    public static final int PRIMITIVE_TOPOLOGY_18_CONTROL_POINT_PATCHLIST = 50;
    public static final int PRIMITIVE_TOPOLOGY_19_CONTROL_POINT_PATCHLIST = 51;
    public static final int PRIMITIVE_TOPOLOGY_20_CONTROL_POINT_PATCHLIST = 52;
    public static final int PRIMITIVE_TOPOLOGY_21_CONTROL_POINT_PATCHLIST = 53;
    public static final int PRIMITIVE_TOPOLOGY_22_CONTROL_POINT_PATCHLIST = 54;
    public static final int PRIMITIVE_TOPOLOGY_23_CONTROL_POINT_PATCHLIST = 55;
    public static final int PRIMITIVE_TOPOLOGY_24_CONTROL_POINT_PATCHLIST = 56;
    public static final int PRIMITIVE_TOPOLOGY_25_CONTROL_POINT_PATCHLIST = 57;
    public static final int PRIMITIVE_TOPOLOGY_26_CONTROL_POINT_PATCHLIST = 58;
    public static final int PRIMITIVE_TOPOLOGY_27_CONTROL_POINT_PATCHLIST = 59;
    public static final int PRIMITIVE_TOPOLOGY_28_CONTROL_POINT_PATCHLIST = 60;
    public static final int PRIMITIVE_TOPOLOGY_29_CONTROL_POINT_PATCHLIST = 61;
    public static final int PRIMITIVE_TOPOLOGY_30_CONTROL_POINT_PATCHLIST = 62;
    public static final int PRIMITIVE_TOPOLOGY_31_CONTROL_POINT_PATCHLIST = 63;
    public static final int PRIMITIVE_TOPOLOGY_32_CONTROL_POINT_PATCHLIST = 64;

    public static final int INPUT_PER_VERTEX_DATA = 0;
    public static final int INPUT_PER_INSTANCE_DATA = 1;

    public static final int FILL_WIREFRAME = 2;
    public static final int FILL_SOLID = 3;

    public static final int CULL_NONE = 1;
    public static final int CULL_FRONT = 2;
    public static final int CULL_BACK = 3;

    public static final int USAGE_DEFAULT = 0;
    public static final int USAGE_IMMUTABLE = 1;
    public static final int USAGE_DYNAMIC = 2;
    public static final int USAGE_STAGING = 3;

    public static final int BIND_VERTEX_BUFFER = 0x1;
    public static final int BIND_INDEX_BUFFER = 0x2;
    public static final int BIND_CONSTANT_BUFFER = 0x4;
    public static final int BIND_SHADER_RESOURCE = 0x8;
    public static final int BIND_STREAM_OUTPUT = 0x10;
    public static final int BIND_RENDER_TARGET = 0x20;
    public static final int BIND_DEPTH_STENCIL = 0x40;
    public static final int BIND_UNORDERED_ACCESS = 0x80;
    public static final int BIND_DECODER = 0x200;
    public static final int BIND_VIDEO_ENCODER = 0x400;

    public static final int CPU_ACCESS_WRITE = 0x10000;
    public static final int CPU_ACCESS_READ = 0x20000;

    public static final int MAP_READ = 1;
    public static final int MAP_WRITE = 2;
    public static final int MAP_READ_WRITE = 3;
    public static final int MAP_WRITE_DISCARD = 4;
    public static final int MAP_WRITE_NO_OVERWRITE = 5;
    public static final int MAP_FLAG_DO_NOT_WAIT = 0x100000;

    public static final int CLEAR_DEPTH = 0x1;
    public static final int CLEAR_STENCIL = 0x2;

    public static final int COMPARISON_NEVER = 1;
    public static final int COMPARISON_LESS = 2;
    public static final int COMPARISON_EQUAL = 3;
    public static final int COMPARISON_LESS_EQUAL = 4;
    public static final int COMPARISON_GREATER = 5;
    public static final int COMPARISON_NOT_EQUAL = 6;
    public static final int COMPARISON_GREATER_EQUAL = 7;
    public static final int COMPARISON_ALWAYS = 8;

    public static final int DEPTH_WRITE_MASK_ZERO = 0;
    public static final int DEPTH_WRITE_MASK_ALL = 1;

    public static final int STENCIL_OP_KEEP = 1;
    public static final int STENCIL_OP_ZERO = 2;
    public static final int STENCIL_OP_REPLACE = 3;
    public static final int STENCIL_OP_INCR_SAT = 4;
    public static final int STENCIL_OP_DECR_SAT = 5;
    public static final int STENCIL_OP_INVERT = 6;
    public static final int STENCIL_OP_INCR = 7;
    public static final int STENCIL_OP_DECR = 8;

    public static final int BLEND_ZERO = 1;
    public static final int BLEND_ONE = 2;
    public static final int BLEND_SRC_COLOR = 3;
    public static final int BLEND_INV_SRC_COLOR = 4;
    public static final int BLEND_SRC_ALPHA = 5;
    public static final int BLEND_INV_SRC_ALPHA = 6;
    public static final int BLEND_DEST_ALPHA = 7;
    public static final int BLEND_INV_DEST_ALPHA = 8;
    public static final int BLEND_DEST_COLOR = 9;
    public static final int BLEND_INV_DEST_COLOR = 10;
    public static final int BLEND_SRC_ALPHA_SAT = 11;
    public static final int BLEND_BLEND_FACTOR = 14;
    public static final int BLEND_INV_BLEND_FACTOR = 15;
    public static final int BLEND_SRC1_COLOR = 16;
    public static final int BLEND_INV_SRC1_COLOR = 17;
    public static final int BLEND_SRC1_ALPHA = 18;
    public static final int BLEND_INV_SRC1_ALPHA = 19;

    public static final int BLEND_OP_ADD = 1;
    public static final int BLEND_OP_SUBTRACT = 2;
    public static final int BLEND_OP_REV_SUBTRACT = 3;
    public static final int BLEND_OP_MIN = 4;
    public static final int BLEND_OP_MAX = 5;

    public static final int COLOR_WRITE_ENABLE_RED = 1;
    public static final int COLOR_WRITE_ENABLE_GREEN = 2;
    public static final int COLOR_WRITE_ENABLE_BLUE = 4;
    public static final int COLOR_WRITE_ENABLE_ALPHA = 8;
    public static final int COLOR_WRITE_ENABLE_ALL =
            COLOR_WRITE_ENABLE_RED |
            COLOR_WRITE_ENABLE_GREEN |
            COLOR_WRITE_ENABLE_BLUE |
            COLOR_WRITE_ENABLE_ALPHA;

    public static final int FILTER_MIN_MAG_MIP_POINT = 0;
    public static final int FILTER_MIN_MAG_POINT_MIP_LINEAR = 0x1;
    public static final int FILTER_MIN_POINT_MAG_LINEAR_MIP_POINT = 0x4;
    public static final int FILTER_MIN_POINT_MAG_MIP_LINEAR = 0x5;
    public static final int FILTER_MIN_LINEAR_MAG_MIP_POINT = 0x10;
    public static final int FILTER_MIN_LINEAR_MAG_POINT_MIP_LINEAR = 0x11;
    public static final int FILTER_MIN_MAG_LINEAR_MIP_POINT = 0x14;
    public static final int FILTER_MIN_MAG_MIP_LINEAR = 0x15;
    public static final int FILTER_ANISOTROPIC = 0x55;
    public static final int FILTER_COMPARISON_MIN_MAG_MIP_POINT = 0x80;
    public static final int FILTER_COMPARISON_MIN_MAG_POINT_MIP_LINEAR = 0x81;
    public static final int FILTER_COMPARISON_MIN_POINT_MAG_LINEAR_MIP_POINT = 0x84;
    public static final int FILTER_COMPARISON_MIN_POINT_MAG_MIP_LINEAR = 0x85;
    public static final int FILTER_COMPARISON_MIN_LINEAR_MAG_MIP_POINT = 0x90;
    public static final int FILTER_COMPARISON_MIN_LINEAR_MAG_POINT_MIP_LINEAR = 0x91;
    public static final int FILTER_COMPARISON_MIN_MAG_LINEAR_MIP_POINT = 0x94;
    public static final int FILTER_COMPARISON_MIN_MAG_MIP_LINEAR = 0x95;
    public static final int FILTER_COMPARISON_ANISOTROPIC = 0xD5;
    public static final int FILTER_MINIMUM_MIN_MAG_MIP_POINT = 0x100;
    public static final int FILTER_MINIMUM_MIN_MAG_POINT_MIP_LINEAR = 0x101;
    public static final int FILTER_MINIMUM_MIN_POINT_MAG_LINEAR_MIP_POINT = 0x104;
    public static final int FILTER_MINIMUM_MIN_POINT_MAG_MIP_LINEAR = 0x105;
    public static final int FILTER_MINIMUM_MIN_LINEAR_MAG_MIP_POINT = 0x110;
    public static final int FILTER_MINIMUM_MIN_LINEAR_MAG_POINT_MIP_LINEAR = 0x111;
    public static final int FILTER_MINIMUM_MIN_MAG_LINEAR_MIP_POINT = 0x114;
    public static final int FILTER_MINIMUM_MIN_MAG_MIP_LINEAR = 0x115;
    public static final int FILTER_MINIMUM_ANISOTROPIC = 0x155;
    public static final int FILTER_MAXIMUM_MIN_MAG_MIP_POINT = 0x180;
    public static final int FILTER_MAXIMUM_MIN_MAG_POINT_MIP_LINEAR = 0x181;
    public static final int FILTER_MAXIMUM_MIN_POINT_MAG_LINEAR_MIP_POINT = 0x184;
    public static final int FILTER_MAXIMUM_MIN_POINT_MAG_MIP_LINEAR = 0x185;
    public static final int FILTER_MAXIMUM_MIN_LINEAR_MAG_MIP_POINT = 0x190;
    public static final int FILTER_MAXIMUM_MIN_LINEAR_MAG_POINT_MIP_LINEAR = 0x191;
    public static final int FILTER_MAXIMUM_MIN_MAG_LINEAR_MIP_POINT = 0x194;
    public static final int FILTER_MAXIMUM_MIN_MAG_MIP_LINEAR = 0x195;
    public static final int FILTER_MAXIMUM_ANISOTROPIC = 0x1D5;

    public static final int TEXTURE_ADDRESS_WRAP = 1;
    public static final int TEXTURE_ADDRESS_MIRROR = 2;
    public static final int TEXTURE_ADDRESS_CLAMP = 3;
    public static final int TEXTURE_ADDRESS_BORDER = 4;
    public static final int TEXTURE_ADDRESS_MIRROR_ONCE = 5;

    public static final int CREATE_DEVICE_SINGLETHREADED = 0x1;
    public static final int CREATE_DEVICE_DEBUG = 0x2;
    public static final int CREATE_DEVICE_SWITCH_TO_REF = 0x4;
    public static final int CREATE_DEVICE_PREVENT_INTERNAL_THREADING_OPTIMIZATIONS = 0x8;
    public static final int CREATE_DEVICE_BGRA_SUPPORT = 0x20;
    public static final int CREATE_DEVICE_DEBUGGABLE = 0x40;
    public static final int CREATE_DEVICE_PREVENT_ALTERING_LAYER_SETTINGS_FROM_REGISTRY = 0x80;
    public static final int CREATE_DEVICE_DISABLE_GPU_TIMEOUT = 0x100;
    public static final int CREATE_DEVICE_VIDEO_SUPPORT = 0x800;

    /**
     * A vertex-buffer description for an input slot.
     */
    public static final class InputElementDesc extends PodStruct
    {
        private InputElementDesc(long address) {
            super(address);
        }

        public static native InputElementDesc create(
                String semanticName,
                int semanticIndex,
                int format,
                int inputSlot,
                int alignedByteOffset,
                int inputSlotClass,
                int instanceDataStepRate);
    }

    /**
     * Defines the dimensions of a viewport.
     */
    public static final class Viewport extends PodStruct
    {
        private Viewport(long address) {
            super(address);
        }

        public static native Viewport create(
                float topLeftX,
                float topLeftY,
                float width,
                float height,
                float minDepth,
                float maxDepth);
    }

    public static final class Box extends PodStruct
    {
        private Box(long address) {
            super(address);
        }

        public static native Box create(
                int left,
                int top,
                int front,
                int right,
                int bottom,
                int back);
    }

    /**
     * Stencil operations that can be performed based on the results of a stencil
     * test.
     */
    public static final class DepthStencilOpDesc extends PodStruct
    {
        private DepthStencilOpDesc(long address) {
            super(address);
        }

        public static native DepthStencilOpDesc create(
                int stencilFailOp,
                int stencilDepthFailOp,
                int stencilPassOp,
                int stencilFunc);
    }

    /**
     * Describes depth-stencil state.
     */
    public static final class DepthStencilDesc extends PodStruct
    {
        private DepthStencilDesc(long address) {
            super(address);
        }

        public static native DepthStencilDesc create(
                boolean depthEnable,
                int depthWriteMask,
                int depthFunc,
                boolean stencilEnable,
                byte stencilReadMask,
                byte stencilWriteMask,
                DepthStencilOpDesc frontFace,
                DepthStencilOpDesc backFace);
    }

    /**
     * Describes the blend state for a render target.
     */
    public static final class RenderTargetBlendDesc extends PodStruct
    {
        private RenderTargetBlendDesc(long address) {
            super(address);
        }

        public static native RenderTargetBlendDesc create(
                boolean blendEnable,
                int srcBlend,
                int destBlend,
                int blendOp,
                int srcBlendAlpha,
                int destBlendAlpha,
                int blendOpAlpha,
                byte renderTargetWriteMask);
    }

    /**
     * Describes the blend state.
     */
    public static final class BlendDesc extends PodStruct
    {
        private BlendDesc(long address) {
            super(address);
        }

        public static native BlendDesc create(
                boolean alphaToCoverageEnable,
                boolean independentBlendEnable,
                // Length: 8
                RenderTargetBlendDesc[] renderTarget);
    }

    /**
     * Describes rasterizer state.
     */
    public static final class RasterizerDesc extends PodStruct
    {
        private RasterizerDesc(long address) {
            super(address);
        }

        public static native RasterizerDesc create(
                int fillMode,
                int cullMode,
                boolean frontCounterClockwise,
                int depthBias,
                float depthBiasClamp,
                float slopeScaledDepthBias,
                boolean depthClipEnable,
                boolean scissorEnable,
                boolean multisampleEnable,
                boolean antialiasedLineEnable);
    }

    /**
     * Specifies data for initializing a subresource.
     */
    public static final class SubresourceData extends PodStruct
    {
        private SubresourceData(long address) {
            super(address);
        }

        public static native SubresourceData create(
                ByteBuffer sysMem,
                int sysMemPitch,
                int sysMemSlicePitch);
    }

    /**
     * Provides access to subresource data.
     */
    public static final class MappedSubresource extends PodStruct
    {
        private MappedSubresource(long address) {
            super(address);
        }

        public static native MappedSubresource create(
                ByteBuffer data,
                int rowPitch,
                int depthPitch);

        public native ByteBuffer getData();
    }

    /**
     * Describes a buffer resource.
     */
    public static final class BufferDesc extends PodStruct
    {
        private BufferDesc(long address) {
            super(address);
        }

        public static native BufferDesc create(
                int byteWidth,
                int usage,
                int bindFlags,
                int cpuAccessFlags,
                int miscFlags,
                int structureByteStride);
    }

    /**
     * Describes a 2D texture.
     */
    public static final class Texture2DDesc extends PodStruct
    {
        private Texture2DDesc(long address) {
            super(address);
        }

        public static native Texture2DDesc create(
                int width,
                int height,
                int mipLevels,
                int arraySize,
                int format,
                SampleDesc sampleDesc,
                int usage,
                int bindFlags,
                int cpuAccessFlags,
                int miscFlags);
    }

    /**
     * Describes a shader-resource view.
     */
    public static final class ShaderResourceViewDesc extends PodStruct
    {
        private ShaderResourceViewDesc(long address) {
            super(address);
        }
    }

    /**
     * Describes a render-target view.
     */
    public static final class RenderTargetViewDesc extends PodStruct
    {
        private RenderTargetViewDesc(long address) {
            super(address);
        }
    }

    /**
     * Describes a depth-stencil view.
     */
    public static final class DepthStencilViewDesc extends PodStruct
    {
        private DepthStencilViewDesc(long address) {
            super(address);
        }
    }

    /**
     * Describes a sampler state.
     */
    public static final class SamplerDesc extends PodStruct
    {
        private SamplerDesc(long address) {
            super(address);
        }

        public static native SamplerDesc create(
                int filter,
                int addressU,
                int addressV,
                int addressW,
                float mipLodBias,
                int maxAnisotropy,
                int comparisonFunc,
                float[] borderColor,
                float minLOD,
                float maxLOD);
    }

    /**
     * Holds a description for depth-stencil state that can be bound to the
     * output-merger stage.
     */
    public static final class DepthStencilState extends ComObject
    {
        private DepthStencilState(long address) {
            super(address);
        }
    }

    /**
     * Holds a description for blending state that can be bound to the output-merger
     * stage.
     */
    public static final class BlendState extends ComObject
    {
        private BlendState(long address) {
            super(address);
        }
    }

    /**
     * Holds a description for rasterizer state that can be bound to the rasterizer
     * stage.
     */
    public static final class RasterizerState extends ComObject
    {
        private RasterizerState(long address) {
            super(address);
        }
    }

    /**
     * Represents an abstract resource.
     */
    public static abstract class Resource extends ComObject
    {
        protected Resource(long address) {
            super(address);
        }
    }

    /**
     * Accesses a buffer resource, which is unstructured memory. Buffers typically
     * store vertex and index data.
     */
    public static final class Buffer extends Resource
    {
        private Buffer(long address) {
            super(address);
        }
    }

    /**
     * Manages texel data, which is structured memory.
     */
    public static final class Texture2D extends Resource
    {
        private Texture2D(long address) {
            super(address);
        }

        public native Texture2DDesc getDesc();
    }

    /**
     * Specifies parts of a resource the pipeline can access during rendering.
     */
    public static abstract class View extends ComObject
    {
        private View(long address) {
            super(address);
        }
    }

    /**
     * Specifies the subresources a shader can access during rendering. Examples of
     * shader resources include a constant buffer, a texture buffer, and a texture.
     */
    public static final class ShaderResourceView extends View
    {
        private ShaderResourceView(long address) {
            super(address);
        }
    }

    /**
     * Identifies the render-target subresources that can be accessed during
     * rendering.
     */
    public static final class RenderTargetView extends View
    {
        private RenderTargetView(long address) {
            super(address);
        }
    }

    /**
     * Accesses a texture resource during depth-stencil testing.
     */
    public static final class DepthStencilView extends View
    {
        private DepthStencilView(long address) {
            super(address);
        }
    }

    /**
     * Manages an executable program (a vertex shader) that controls the
     * vertex-shader stage.
     */
    public static final class VertexShader extends ComObject
    {
        private VertexShader(long address) {
            super(address);
        }
    }

    /**
     * Manages an executable program (a pixel shader) that controls the pixel-shader
     * stage.
     */
    public static final class PixelShader extends ComObject
    {
        private PixelShader(long address) {
            super(address);
        }
    }

    /**
     * Holds a definition of how to feed vertex data that is laid out in memory into
     * the input-assembler stage of the graphics pipeline.
     */
    public static final class InputLayout extends ComObject
    {
        private InputLayout(long address) {
            super(address);
        }
    }

    /**
     * Holds a description for sampler state that can be bound to any shader stage
     * of the pipeline for reference by texture sample operations.
     */
    public static final class SamplerState extends ComObject
    {
        private SamplerState(long address) {
            super(address);
        }
    }

    /**
     * Encapsulates an HLSL class.
     */
    public static final class ClassInstance extends ComObject
    {
        private ClassInstance(long address) {
            super(address);
        }
    }

    public static final class ClassLinkage extends ComObject
    {
        private ClassLinkage(long address) {
            super(address);
        }
    }

    /**
     * Represents a virtual adapter used to create resources.
     */
    public static final class Device extends ComObject
    {
        private Device(long address) {
            super(address);
        }

        public native BlendState createBlendState(
                BlendDesc blendStateDesc);

        public native Buffer createBuffer(
                BufferDesc desc,
                SubresourceData initialData);

        public native DepthStencilState createDepthStencilState(
                DepthStencilDesc depthStencilDesc);

        public native DepthStencilView createDepthStencilView(
                Resource resource,
                DepthStencilViewDesc desc);

        public native InputLayout createInputLayout(
                InputElementDesc[] inputElementDescs,
                ByteBuffer shaderBytecodeWithInputSignature);

        public native PixelShader createPixelShader(
                ByteBuffer shaderBytecode,
                ClassLinkage classLinkage);

        public native RasterizerState createRasterizerState(
                RasterizerDesc rasterizerDesc);

        public native RenderTargetView createRenderTargetView(
                Resource resource,
                RenderTargetViewDesc desc);

        public native SamplerState createSamplerState(
                SamplerDesc samplerDesc);

        public native ShaderResourceView createShaderResourceView(
                Resource resource,
                ShaderResourceViewDesc desc);

        public native Texture2D createTexture2D(
                Texture2DDesc desc,
                SubresourceData initialData);

        public native VertexShader createVertexShader(
                ByteBuffer shaderBytecode,
                ClassLinkage classLinkage);

        public native DeviceContext getImmediateContext();
    }

    /**
     * Represents the device context which generates rendering commands.
     */
    public static final class DeviceContext extends ComObject
    {
        private DeviceContext(long address) {
            super(address);
        }

        /**
         * Clears the depth-stencil resource.
         * 
         * @param depthStencilView the depth-stencil to be cleared
         * @param clearFlags       identity the the type of data to clear
         * @param depth            clear the depth buffer with this value. This value
         *                         will be clamped between 0 and 1
         * @param stencil          clear the stencil buffer with this value
         */
        public native void clearDepthStencilView(
                DepthStencilView depthStencilView,
                int clearFlags,
                float depth,
                byte stencil);

        /**
         * Sets all the elements in a render target to one value.
         * 
         * @param renderTargetView the render target
         * @param r                the red component to fill the target with
         * @param g                the green component to fill the target with
         * @param b                the blue component to fill the target with
         * @param a                the alpha component to fill the target with
         */
        public native void clearRenderTargetView(
                RenderTargetView renderTargetView,
                float r,
                float g,
                float b,
                float a);

        /**
         * Draw non-indexed, non-instanced primitives.
         * 
         * @param vertexCount         number of vertices to draw
         * @param startVertexLocation index of the first vertex, which is usually an
         *                            offset in the vertex buffer
         */
        public native void draw(
                int vertexCount,
                int startVertexLocation);

        /**
         * Draw indexed, non-instanced primitives.
         * 
         * @param indexCount         number of indices to draw
         * @param startIndexLocation the location of the first index read by the GPU
         *                           from the index buffer
         * @param baseVertexLocation a value added to each index before reading a vertex
         *                           from the vertex buffer
         */
        public native void drawIndexed(
                int indexCount,
                int startIndexLocation,
                int baseVertexLocation);

        /**
         * Generates mipmaps for the given shader resource.
         * 
         * @param shaderResourceView the shader resource
         */
        public native void generateMips(
                ShaderResourceView shaderResourceView);

        /**
         * Bind an index buffer to the input-assembler stage.
         * 
         * @param indexBuffer the buffer that contains indices. The index buffer must be
         *                    created with the BIND_INDEX_BUFFER flag. This parameter is
         *                    optional
         * @param format      specifies the format of the data in the index buffer. The
         *                    only formats allowed for the index buffer data are 16-bit
         *                    (DXGI.FORMAT_R16_UINT) and 32-bit (DXGI.FORMAT_R32_UINT)
         *                    integers.
         * @param offset      offset (in bytes) from the start of the index buffer to
         *                    the first index to use
         */
        public native void iaSetIndexBuffer(
                Buffer indexBuffer,
                int format,
                int offset);

        /**
         * Bind an input-layout object to the input-assembler stage.
         * 
         * @param inputLayout describes the input buffers that will be read by the IA
         *                    stage. This parameter is optional.
         */
        public native void iaSetInputLayout(
                InputLayout inputLayout);

        /**
         * Bind the information about the primitive type and data order that describes
         * input data for the input assembler stage.
         * 
         * @param topology the type of primitive and ordering of the primitive data
         */
        public native void iaSetPrimitiveTopology(
                int topology);

        /**
         * Bind an array of vertex buffers to the input-assembler stage.
         * 
         * @param startSlot     the first input slot for binding. The first vertex is
         *                      explicitly bound to the start slot; this causes each
         *                      additional vertex buffer in the array to be implicitly
         *                      bound to each subsequent input slot.
         * @param vertexBuffers an array of vertex buffers. This parameter is optional
         * @param strides       an array of stride values; one stride value for each
         *                      buffer in the vertex-buffer array. Each stride is the
         *                      size (in bytes) of the elements that are to be used from
         *                      that vertex buffer. This parameter is optional
         * @param offsets       an array of offset values; one offset value for each
         *                      buffer int the vertex-buffer array. Each offset is the
         *                      number of bytes between the first element of a vertex
         *                      buffer and the first element that will be used. This
         *                      parameter is optional
         */
        public native void iaSetVertexBuffers(
                int startSlot,
                Buffer[] vertexBuffers,
                int[] strides,
                int[] offsets);

        /**
         * Set the blend state of the output-merger stage.
         * 
         * @param blendState  the blend state. Pass null for a default blend state
         * @param blendFactor array of blend factors, one for each RGBA component. Pass
         *                    null for a blend factor equal to { 1, 1, 1, 1 }
         * @param sampleMask  32-bit sample coverage. The default value if 0xFFFFFFFF
         */
        public native void omSetBlendState(
                BlendState blendState,
                float[] blendFactor,
                int sampleMask);

        public native void omSetDepthStencilState(
                DepthStencilState depthStencilState,
                int stencilRef);

        public native void omSetRenderTargets(
                RenderTargetView[] renderTargetViews,
                DepthStencilView depthStencilView);

        public native void psSetConstantBuffers(
                int startSlot,
                Buffer[] constantBuffers);

        public native void psSetSamplers(
                int startSlot,
                SamplerState[] samplers);

        public native void psSetShader(
                PixelShader pixelShader,
                ClassInstance[] classInstances);

        public native void psSetShaderResources(
                int startSlot,
                ShaderResourceView[] shaderResourceViews);

        public native void rsSetScissorRects(
                Rect[] rects);

        public native void rsSetState(
                RasterizerState rasterizerState);

        public native void rsSetViewports(
                Viewport[] viewports);

        public native void updateSubresource(
                Resource dstResource,
                int dstSubresource,
                Box dstBox,
                ByteBuffer srcData,
                int srcRowPitch,
                int srcDepthPitch);

        public native MappedSubresource map(
                Resource resource,
                int subresource,
                int mapType,
                int mapFlags);

        public native void unmap(
                Resource resource,
                int subresource);

        /**
         * Sets the constant buffers used by the vertex shader pipeline stage.
         * 
         * @param startSlot       index into the device's zero-based array to begin
         *                        setting constant buffers to
         * @param constantBuffers an array of constant buffers being given to the
         *                        device. This parameter is optional
         */
        public native void vsSetConstantBuffers(
                int startSlot,
                Buffer[] constantBuffers);

        /**
         * Sets an array of sampler states to the vertex shader pipeline stage.
         * 
         * @param startSlot index into the device's zero-based array to begin setting
         *                  samplers to
         * @param samplers  an array of sampler states. This parameter is optional
         */
        public native void vsSetSamplers(
                int startSlot,
                SamplerState[] samplers);

        /**
         * Set a vertex shader to the device.
         * 
         * @param vertexShader   a vertex shader. Passing in null disables the shader
         *                       for this pipeline stage
         * @param classInstances an array of class-instance interfaces. Each interface
         *                       used by a shader must have a corresponding class
         *                       instance or the shader will get disabled. Pass null if
         *                       the shader does not use any interfaces
         */
        public native void vsSetShader(
                VertexShader vertexShader,
                ClassInstance[] classInstances);

        /**
         * Bind an array of shader resources to the vertex-shader stage.
         * 
         * @param startSlot           index into the device's zero-based array to begin
         *                            setting shader resources to
         * @param shaderResourceViews array of shader resource views to set to the
         *                            device. This parameter is optional
         */
        public native void vsSetShaderResources(
                int startSlot,
                ShaderResourceView[] shaderResourceViews);
    }

    public static final class DeviceDeviceContextSwapChainTuple extends PodStruct
    {
        private DeviceDeviceContextSwapChainTuple(long address) {
            super(address);
        }

        public native Device getDevice();

        public native DeviceContext getDeviceContext();

        public native SwapChain getSwapChain();
    }

    public static native DeviceDeviceContextSwapChainTuple createDeviceAndSwapChain(
            int driverType,
            int flags,
            SwapChainDesc desc);
}