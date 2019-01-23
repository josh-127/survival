package net.survival.client;

import net.survival.client.input.Keyboard;
import net.survival.input.Key;
import net.survival.interaction.KeyboardInteractionAdapter;

public class LocalKeyboardInteractionAdapter implements KeyboardInteractionAdapter
{
    @Override
    public boolean isKeyDown(Key key) {
        return Keyboard.isKeyDown(key);
    }

    @Override
    public boolean isKeyUp(Key key) {
        return Keyboard.isKeyUp(key);
    }

    @Override
    public boolean isKeyPressed(Key key) {
        return Keyboard.isKeyPressed(key);
    }

    @Override
    public boolean isKeyReleased(Key key) {
        return Keyboard.isKeyReleased(key);
    }
}