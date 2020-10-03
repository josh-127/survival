package net.survival.graphics.opengl;

public class GLException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GLException() {}

    public GLException(String message) {
        super(message);
    }
}