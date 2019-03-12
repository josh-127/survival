package dev.josh127.libwin32;

public class HResultException extends NativeException
{
    private static final long serialVersionUID = 1L;

    public HResultException() {}

    public HResultException(String function, int errorCode) {
        super(String.format("%s returned 0x%X.", function, errorCode));
    }
}