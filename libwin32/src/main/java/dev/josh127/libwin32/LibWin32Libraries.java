package dev.josh127.libwin32;

public final class LibWin32Libraries {
    public static final int CORE   = 0x1;
    public static final int USER32 = 0x2 | CORE;
    public static final int DXGI   = 0x4 | USER32 | CORE;
    public static final int D3D11  = 0x8 | DXGI | USER32 | CORE;

    private LibWin32Libraries() {}
}