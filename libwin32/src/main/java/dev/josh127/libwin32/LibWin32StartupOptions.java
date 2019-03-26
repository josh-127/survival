package dev.josh127.libwin32;

public class LibWin32StartupOptions {
    private int libraries;

    public LibWin32StartupOptions() {
        libraries = LibWin32Libraries.CORE | LibWin32Libraries.USER32;
    }

    public int getLibraries() {
        return libraries;
    }

    public void setLibraries(int to) {
        libraries = to;
    }

    public void includeLibraries(int libraries) {
        this.libraries |= libraries;
    }
}