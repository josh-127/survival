package dev.josh127.libwin32.sampleapp;

import java.nio.file.Paths;

import dev.josh127.libwin32.D3D11;
import dev.josh127.libwin32.Dxgi;
import dev.josh127.libwin32.LibWin32Libraries;
import dev.josh127.libwin32.LibWin32Startup;
import dev.josh127.libwin32.LibWin32StartupOptions;
import dev.josh127.libwin32.User32;

public class App {
    private static final int CLIENT_WIDTH = 1280;
    private static final int CLIENT_HEIGHT = 720;

    public static void main(String[] args) {
        var cwd = System.getProperty("user.dir");
        var dllPath = "../libwin32-native/x64/Debug";
        var dllName = "LIBWIN32NATIVE.dll";
        var fullPath = Paths.get(cwd, dllPath, dllName).toString();
        System.load(fullPath);

        var options = new LibWin32StartupOptions();
        options.includeLibraries(LibWin32Libraries.CORE);
        options.includeLibraries(LibWin32Libraries.USER32);
        options.includeLibraries(LibWin32Libraries.DXGI);
        options.includeLibraries(LibWin32Libraries.D3D11);

        LibWin32Startup.init(options);

        var window = User32.createWindow(CLIENT_WIDTH, CLIENT_HEIGHT, "LibWin32 Sample Application");

        var refreshRate = Dxgi.Rational.create(60, 1);
        var bufferDesc = Dxgi.ModeDesc.create(
                CLIENT_WIDTH,
                CLIENT_HEIGHT,
                refreshRate,
                Dxgi.FORMAT_R8G8B8A8_UNORM,
                0,
                0);
        var sampleDesc = Dxgi.SampleDesc.create(1, 0);
        var swapChainDesc = Dxgi.SwapChainDesc.create(
                bufferDesc,
                sampleDesc,
                Dxgi.USAGE_RENDER_TARGET_OUTPUT,
                1,
                window,
                true,
                Dxgi.SWAP_EFFECT_DISCARD,
                0);

        var tuple = D3D11.createDeviceAndSwapChain(D3D11.DRIVER_TYPE_HARDWARE, 0, swapChainDesc);

        var device = tuple.getDevice();
        var deviceContext = tuple.getDeviceContext();
        var swapChain = tuple.getSwapChain();

        var backBufferTexture = swapChain.getBuffer(0);
        var backBufferView = device.createRenderTargetView(backBufferTexture, null);
        deviceContext.omSetRenderTargets(new D3D11.RenderTargetView[] { backBufferView }, null);
        backBufferTexture.release();

        var viewport = D3D11.Viewport.create(0.0f, 0.0f, CLIENT_WIDTH, CLIENT_HEIGHT, -1.0f, 1.0f);
        deviceContext.rsSetViewports(new D3D11.Viewport[] { viewport });

        while (User32.peekEvent()) {
            User32.dispatchEvents();

            deviceContext.clearRenderTargetView(backBufferView, 0.0f, 0.0f, 1.0f, 0.0f);
            swapChain.present(0, 0);
        }

        backBufferView.release();

        device.release();
        deviceContext.release();
        swapChain.release();
    }
}