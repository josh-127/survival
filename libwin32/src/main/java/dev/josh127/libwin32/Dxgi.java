package dev.josh127.libwin32;

import dev.josh127.libwin32.D3D11.Texture2D;

public final class Dxgi
{
    private Dxgi() {}

    static native int nativeInit();

    public static final int FORMAT_UNKNOWN = 0;
    public static final int FORMAT_R32G32B32A32_TYPELESS = 1;
    public static final int FORMAT_R32G32B32A32_FLOAT = 2;
    public static final int FORMAT_R32G32B32A32_UINT = 3;
    public static final int FORMAT_R32G32B32A32_SINT = 4;
    public static final int FORMAT_R32G32B32_TYPELESS = 5;
    public static final int FORMAT_R32G32B32_FLOAT = 6;
    public static final int FORMAT_R32G32B32_UINT = 7;
    public static final int FORMAT_R32G32B32_SINT = 8;
    public static final int FORMAT_R16G16B16A16_TYPELESS = 9;
    public static final int FORMAT_R16G16B16A16_FLOAT = 10;
    public static final int FORMAT_R16G16B16A16_UNORM = 11;
    public static final int FORMAT_R16G16B16A16_UINT = 12;
    public static final int FORMAT_R16G16B16A16_SNORM = 13;
    public static final int FORMAT_R16G16B16A16_SINT = 14;
    public static final int FORMAT_R32G32_TYPELESS = 15;
    public static final int FORMAT_R32G32_FLOAT = 16;
    public static final int FORMAT_R32G32_UINT = 17;
    public static final int FORMAT_R32G32_SINT = 18;
    public static final int FORMAT_R32G8X24_TYPELESS = 19;
    public static final int FORMAT_D32_FLOAT_S8X24_UINT = 20;
    public static final int FORMAT_R32_FLOAT_X8X24_TYPELESS = 21;
    public static final int FORMAT_X32_TYPELESS_G8X24_UINT = 22;
    public static final int FORMAT_R10G10B10A2_TYPELESS = 23;
    public static final int FORMAT_R10G10B10A2_UNORM = 24;
    public static final int FORMAT_R10G10B10A2_UINT = 25;
    public static final int FORMAT_R11G11B10_FLOAT = 26;
    public static final int FORMAT_R8G8B8A8_TYPELESS = 27;
    public static final int FORMAT_R8G8B8A8_UNORM = 28;
    public static final int FORMAT_R8G8B8A8_UNORM_SRGB = 29;
    public static final int FORMAT_R8G8B8A8_UINT = 30;
    public static final int FORMAT_R8G8B8A8_SNORM = 31;
    public static final int FORMAT_R8G8B8A8_SINT = 32;
    public static final int FORMAT_R16G16_TYPELESS = 33;
    public static final int FORMAT_R16G16_FLOAT = 34;
    public static final int FORMAT_R16G16_UNORM = 35;
    public static final int FORMAT_R16G16_UINT = 36;
    public static final int FORMAT_R16G16_SNORM = 37;
    public static final int FORMAT_R16G16_SINT = 38;
    public static final int FORMAT_R32_TYPELESS = 39;
    public static final int FORMAT_D32_FLOAT = 40;
    public static final int FORMAT_R32_FLOAT = 41;
    public static final int FORMAT_R32_UINT = 42;
    public static final int FORMAT_R32_SINT = 43;
    public static final int FORMAT_R24G8_TYPELESS = 44;
    public static final int FORMAT_D24_UNORM_S8_UINT = 45;
    public static final int FORMAT_R24_UNORM_X8_TYPELESS = 46;
    public static final int FORMAT_X24_TYPELESS_G8_UINT = 47;
    public static final int FORMAT_R8G8_TYPELESS = 48;
    public static final int FORMAT_R8G8_UNORM = 49;
    public static final int FORMAT_R8G8_UINT = 50;
    public static final int FORMAT_R8G8_SNORM = 51;
    public static final int FORMAT_R8G8_SINT = 52;
    public static final int FORMAT_R16_TYPELESS = 53;
    public static final int FORMAT_R16_FLOAT = 54;
    public static final int FORMAT_D16_UNORM = 55;
    public static final int FORMAT_R16_UNORM = 56;
    public static final int FORMAT_R16_UINT = 57;
    public static final int FORMAT_R16_SNORM = 58;
    public static final int FORMAT_R16_SINT = 59;
    public static final int FORMAT_R8_TYPELESS = 60;
    public static final int FORMAT_R8_UNORM = 61;
    public static final int FORMAT_R8_UINT = 62;
    public static final int FORMAT_R8_SNORM = 63;
    public static final int FORMAT_R8_SINT = 64;
    public static final int FORMAT_A8_UNORM = 65;
    public static final int FORMAT_R1_UNORM = 66;
    public static final int FORMAT_R9G9B9E5_SHAREDEXP = 67;
    public static final int FORMAT_R8G8_B8G8_UNORM = 68;
    public static final int FORMAT_G8R8_G8B8_UNORM = 69;
    public static final int FORMAT_BC1_TYPELESS = 70;
    public static final int FORMAT_BC1_UNORM = 71;
    public static final int FORMAT_BC1_UNORM_SRGB = 72;
    public static final int FORMAT_BC2_TYPELESS = 73;
    public static final int FORMAT_BC2_UNORM = 74;
    public static final int FORMAT_BC2_UNORM_SRGB = 75;
    public static final int FORMAT_BC3_TYPELESS = 76;
    public static final int FORMAT_BC3_UNORM = 77;
    public static final int FORMAT_BC3_UNORM_SRGB = 78;
    public static final int FORMAT_BC4_TYPELESS = 79;
    public static final int FORMAT_BC4_UNORM = 80;
    public static final int FORMAT_BC4_SNORM = 81;
    public static final int FORMAT_BC5_TYPELESS = 82;
    public static final int FORMAT_BC5_UNORM = 83;
    public static final int FORMAT_BC5_SNORM = 84;
    public static final int FORMAT_B5G6R5_UNORM = 85;
    public static final int FORMAT_B5G5R5A1_UNORM = 86;
    public static final int FORMAT_B8G8R8A8_UNORM = 87;
    public static final int FORMAT_B8G8R8X8_UNORM = 88;
    public static final int FORMAT_R10G10B10_XR_BIAS_A2_UNORM = 89;
    public static final int FORMAT_B8G8R8A8_TYPELESS = 90;
    public static final int FORMAT_B8G8R8A8_UNORM_SRGB = 91;
    public static final int FORMAT_B8G8R8X8_TYPELESS = 92;
    public static final int FORMAT_B8G8R8X8_UNORM_SRGB = 93;
    public static final int FORMAT_BC6H_TYPELESS = 94;
    public static final int FORMAT_BC6H_UF16 = 95;
    public static final int FORMAT_BC6H_SF16 = 96;
    public static final int FORMAT_BC7_TYPELESS = 97;
    public static final int FORMAT_BC7_UNORM = 98;
    public static final int FORMAT_BC7_UNORM_SRGB = 99;
    public static final int FORMAT_AYUV = 100;
    public static final int FORMAT_Y410 = 101;
    public static final int FORMAT_Y416 = 102;
    public static final int FORMAT_NV12 = 103;
    public static final int FORMAT_P010 = 104;
    public static final int FORMAT_P016 = 105;
    public static final int FORMAT_420_OPAQUE = 106;
    public static final int FORMAT_FORMAT_YUY2 = 107;
    public static final int FORMAT_Y210 = 108;
    public static final int FORMAT_Y216 = 109;
    public static final int FORMAT_NV11 = 110;
    public static final int FORMAT_AI44 = 111;
    public static final int FORMAT_IA44 = 112;
    public static final int FORMAT_P8 = 113;
    public static final int FORMAT_A8P8 = 114;
    public static final int FORMAT_B4G4R4A4_UNORM = 115;
    public static final int FORMAT_P208 = 130;
    public static final int FORMAT_V208 = 131;
    public static final int FORMAT_V408 = 132;
    public static final int FORMAT_FORCE_UINT = 0xFFFFFFFF;

    public static final class Rational extends PodStruct
    {
        private Rational(long address) {
            super(address);
        }

        public static native Rational create(int numerator, int denominator);
    }

    public static final class ModeDesc extends PodStruct
    {
        private ModeDesc(long address) {
            super(address);
        }

        public static native ModeDesc create(
                int width,
                int height,
                Rational refreshRate,
                int format,
                int scalineOrdering,
                int scaling);
    }

    public static final class SampleDesc extends PodStruct
    {
        private SampleDesc(long address) {
            super(address);
        }

        public static native SampleDesc create(int count, int quality);
    }

    public static final int CPU_ACCESS_NONE = 0;
    public static final int CPU_ACCESS_DYNAMIC = 1;
    public static final int CPU_ACCESS_READ_WRITE = 2;
    public static final int CPU_ACCESS_SCRATCH = 3;
    public static final int CPU_ACCESS_FIELD = 15;

    public static final int USAGE_SHADER_INPUT = 0x00000010;
    public static final int USAGE_RENDER_TARGET_OUTPUT = 0x00000020;
    public static final int USAGE_BACK_BUFFER = 0x00000040;
    public static final int USAGE_SHARED = 0x00000080;
    public static final int USAGE_READ_ONLY = 0x00000100;
    public static final int USAGE_DISCARD_ON_PRESENT = 0x00000200;
    public static final int USAGE_UNORDERED_ACCESS = 0x00000400;

    public static final int SWAP_EFFECT_DISCARD = 0;
    public static final int SWAP_EFFECT_SEQUENTIAL = 1;
    public static final int SWAP_EFFECT_FLIP_SEQUENTIAL = 3;
    public static final int SWAP_EFFECT_FLIP_DISCARD = 4;
    public static final int SWAP_CHAIN_FLAG_NONPREROTATED = 1;
    public static final int SWAP_CHAIN_FLAG_ALLOW_MODE_SWITCH = 2;
    public static final int SWAP_CHAIN_FLAG_GDI_COMPATIBLE = 4;
    public static final int SWAP_CHAIN_FLAG_RESTRICTED_CONTENT = 8;
    public static final int SWAP_CHAIN_FLAG_RESTRICT_SHARED_RESOURCE_DRIVER = 16;
    public static final int SWAP_CHAIN_FLAG_DISPLAY_ONLY = 32;
    public static final int SWAP_CHAIN_FLAG_FRAME_LATENCY_WAITABLE_OBJECT = 64;
    public static final int SWAP_CHAIN_FLAG_FOREGROUND_LAYER = 128;
    public static final int SWAP_CHAIN_FLAG_FULLSCREEN_VIDEO = 256;
    public static final int SWAP_CHAIN_FLAG_YUV_VIDEO = 512;
    public static final int SWAP_CHAIN_FLAG_HW_PROTECTED = 1024;

    public static final class SwapChainDesc extends PodStruct
    {
        private SwapChainDesc(long address) {
            super(address);
        }

        public static native SwapChainDesc create(
                ModeDesc bufferDesc,
                SampleDesc sampleDesc,
                int bufferUsage,
                int bufferCount,
                NativePtr outputWindow,
                boolean windowed,
                int swapEffect,
                int flags);
    }

    public static final int MAP_READ_DXGI = 1;
    public static final int MAP_WRITE_DXGI = 2;
    public static final int MAP_DISCARD_DXGI = 4;

    public static final class SwapChain extends ComObject
    {
        private SwapChain(long address) {
            super(address);
        }

        public native Texture2D getBuffer(int buffer);

        public native void present(int syncInterval, int flags);
    }
}