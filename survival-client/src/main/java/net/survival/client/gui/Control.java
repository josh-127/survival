package net.survival.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.survival.util.Rectangle;

public class Control
{
    private Control parent;
    private ArrayList<Control> children;

    private Rectangle clientRectangle;
    private String text;
    private Font font;
    private TextAlignment textAlignment;

    public Control() {
        children = new ArrayList<>();
        clientRectangle = new Rectangle();
        text = "";
        font = Font.DEFAULT;
        textAlignment = TextAlignment.LEFT;
    }

    public Control getParent() {
        return parent;
    }

    public void setParent(Control parent) {
        this.parent = parent;
    }

    public List<Control> getChildren() {
        return children;
    }

    public Rectangle getClientRectangle() {
        return clientRectangle;
    }

    public void setClientRectangle(Rectangle to) {
        this.clientRectangle = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String to) {
        this.text = to;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font to) {
        font = to;
    }

    public TextAlignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(TextAlignment to) {
        textAlignment = to;
    }
}