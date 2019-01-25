package net.survival.client.ui;

public class UiDom
{
    private final UiBodyElement body;

    public UiDom() {
        body = new UiBodyElement();
    }

    public UiDom(UiBodyElement body) {
        this.body = body;
    }

    public UiBodyElement getBody() {
        return body;
    }
}