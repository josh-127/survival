package net.survival.graphics.d3d11;

/**
 * Contains methods of operation on stencil pixels.
 */
public enum StencilOperation
{
    /**
     * Does not change the stencil pixel.
     */
    KEEP,

    /**
     * Sets the stencil pixel to zero.
     */
    ZERO,

    /**
     * Replaces the stencil pixel with a new value.
     */
    REPLACE,

    /**
     * Inverts the bits of the stencil pixel.
     */
    INVERT,

    /**
     * Increments and wraps the value of the stencil pixel by one.
     */
    INCREMENT_WARP,

    /**
     * Decrements and wraps the value of the stencil pixel by one.
     */
    DECREMENT_WARP,

    /**
     * Increments the value of the stencil pixel by one only if no integer overflow
     * occurs.
     */
    INCREMENT_CLAMP,

    /**
     * Decrements the value of the stencil pixel by one only if no integer overflow
     * occurs.
     */
    DECREMENT_CLAMP
}