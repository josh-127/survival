package net.survival.ui;

public class UiImageElement extends UiLeafElement
{
    private final String imageUrl;

    public UiImageElement(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public <R> R accept(UiElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(UiElementVoidVisitor visitor) {
        visitor.visit(this);
    }

    public String getImageUrl() {
        return imageUrl;
    }
}