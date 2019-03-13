package net.survival.client.graphics2.internal;

/**
 * Represents the blend factor in a blend state. Blend factor is used as a
 * coefficient on output values of fragments.
 */
public enum BlendFactor
{
    ZERO,
    ONE,
    SRC_COLOR,
    SRC_ALPHA,
    DST_COLOR,
    DST_ALPHA,
    ONE_MINUS_SRC_COLOR,
    ONE_MINUS_SRC_ALPHA,
    ONE_MINUS_DST_COLOR,
    ONE_MINUS_DST_ALPHA
}