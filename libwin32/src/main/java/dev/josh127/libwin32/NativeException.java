package dev.josh127.libwin32;

public abstract class NativeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NativeException() {}

    public NativeException(String message) {
        super(message);
    }

    public NativeException(Throwable cause) {
        super(cause);
    }

    public NativeException(String message, Throwable cause) {
        super(message, cause);
    }
}