package dev.josh127.libwin32;

public final class LibWin32Startup
{
    private LibWin32Startup() {}

    public static void init() {
        int result = nativeInit();
        checkResult("libwin32.cpp", result);

        result = User32.nativeInit();
        checkResult("user32.cpp", result);

        result = Dxgi.nativeInit();
        checkResult("dxgi.cpp", result);

        result = D3D11.nativeInit();
        checkResult("d3d11.cpp", result);
    }

    private static native int nativeInit();

    private static void checkResult(String sourceFile, int result) {
        if (result != -1)
            System.err.printf("%s(%d)\n", sourceFile, result);
    }
}