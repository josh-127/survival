package net.survival.client.gui;

public enum Font
{
    DEFAULT("../assets/textures/fonts/ascii.png");

    public final String characterSheetPath;

    private Font(String characterSheetPath) {
        this.characterSheetPath = characterSheetPath;
    }
}