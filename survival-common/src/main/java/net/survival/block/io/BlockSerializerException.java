package net.survival.block.io;

class BlockSerializerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BlockSerializerException() {}

    public BlockSerializerException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {}

    public BlockSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockSerializerException(String message) {
        super(message);
    }

    public BlockSerializerException(Throwable cause) {
        super(cause);
    }
}