package dev.josh127.libwin32.sampleapp;

import java.nio.file.Paths;

import dev.josh127.libwin32.LibWin32Libraries;
import dev.josh127.libwin32.LibWin32Startup;
import dev.josh127.libwin32.LibWin32StartupOptions;
import dev.josh127.libwin32.User32;

public class App {
    public static void main(String[] args) {
        var cwd = System.getProperty("user.dir");
        var dllPath = "../libwin32-native/x64/Debug";
        var dllName = "LIBWIN32NATIVE.dll";
        var fullPath = Paths.get(cwd, dllPath, dllName).toString();
        System.load(fullPath);

        var options = new LibWin32StartupOptions();
        options.includeLibraries(LibWin32Libraries.CORE);
        options.includeLibraries(LibWin32Libraries.USER32);

        LibWin32Startup.init(options);

        var window = User32.createWindow(1280, 720, "LibWin32 Sample Application");

        while (User32.peekEvent()) {
            User32.dispatchEvents();
        }
    }
}