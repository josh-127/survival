package dev.josh127.libwin32;

public final class LibWin32Startup
{
    private LibWin32Startup() {}

    public static void init(LibWin32StartupOptions options) {
        var libraries = options.getLibraries();

        if ((libraries & LibWin32Libraries.CORE) != 0) {
            var result = nativeInit();
            checkResult("libwin32.cc", result);
        }

        if ((libraries & LibWin32Libraries.USER32) != 0) {
            var result = User32.nativeInit();
            checkResult("user32.cc", result);
        }

        if ((libraries & LibWin32Libraries.DXGI) != 0) {
            var result = Dxgi.nativeInit();
            checkResult("dxgi.cc", result);
        }

        if ((libraries & LibWin32Libraries.DXGI) != 0) {
            var result = D3D11.nativeInit();
            checkResult("d3d11.cc", result);
        }
    }

    private static native int nativeInit();

    private static void checkResult(String sourceFile, int result) {
        if (result != -1)
            System.err.printf("%s(%d)\n", sourceFile, result);
    }
}