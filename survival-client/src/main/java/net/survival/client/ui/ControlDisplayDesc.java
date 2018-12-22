package net.survival.client.ui;

public class ControlDisplayDesc
{
    public static final int TYPE_BUTTON = 1;
    public static final int TYPE_LABEL = 2;

    public final int type;
    public final boolean hovered;
    public final boolean held;
    public final int left;
    public final int top;
    public final int right;
    public final int bottom;
    public final String text;
    public final double fontSize;

    private ControlDisplayDesc(
            int type,
            boolean hovered,
            boolean held,
            int left,
            int top,
            int right,
            int bottom,
            String text,
            double fontSize)
    {
        this.type = type;
        this.hovered = hovered;
        this.held = held;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.text = text;
        this.fontSize = fontSize;
    }

    public static ControlDisplayDesc createButton(
            boolean hovered,
            boolean held,
            int left,
            int top,
            int right,
            int bottom,
            String text,
            double fontSize)
    {
        return new ControlDisplayDesc(TYPE_BUTTON, hovered, held, left, top, right, bottom, text, fontSize);
    }

    public static ControlDisplayDesc createLabel(int left, int top, String text, double fontSize) {
        return new ControlDisplayDesc(TYPE_LABEL, false, false, left, top, 0, 0, text, fontSize);
    }
}