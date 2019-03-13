package net.survival.client.graphics2.internal;

/**
 * Represents an element of a vertex.
 */
public class InputLayoutElement
{
    public final int slot;
    public final ShaderPrimitiveType type;
    public final String name;
    public final int index;
    public final int offset;

    /**
     * Constructs an InputLayoutElement.
     * 
     * @param slot   the vertex buffer slot
     * @param type   the type of element
     * @param name   the name of the element
     * @param index  the input of the element
     * @param offset the offset of the element within the vertex structure in bytes
     */
    public InputLayoutElement(
            int slot,
            ShaderPrimitiveType type,
            String name,
            int index,
            int offset)
    {
        this.slot = slot;
        this.type = type;
        this.name = name;
        this.index = index;
        this.offset = offset;
    }
}