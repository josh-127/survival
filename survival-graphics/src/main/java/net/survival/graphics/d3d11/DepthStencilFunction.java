package net.survival.graphics.d3d11;

/**
 * Represents the function used to operate on stencils.
 */
public class DepthStencilFunction
{
    public final StencilOperation failOperation;
    public final StencilOperation depthFailOperation;
    public final StencilOperation passOperation;
    public final DepthStencilComparison comparison;

    /**
     * Constructs a DepthStencilFunction
     * 
     * @param failOperation      the operation to use when the stencil test fails
     * @param depthFailOperation the operation to use when the depth test fails
     * @param passOperation      the operation to use when the stencil test passes
     * @param comparison         the comparison function used to compare stencil
     *                           values
     */
    public DepthStencilFunction(
            StencilOperation failOperation,
            StencilOperation depthFailOperation,
            StencilOperation passOperation,
            DepthStencilComparison comparison)
    {
        this.failOperation = failOperation;
        this.depthFailOperation = depthFailOperation;
        this.passOperation = passOperation;
        this.comparison = comparison;
    }
}