package net.survival.client.input;

import net.survival.input.Key;

public abstract class KeyboardAdapter
{
    protected void pressKey(Key key) {
        Keyboard.keyState[key.ordinal()] = true;
    }

    protected void releaseKey(Key key) {
        Keyboard.keyState[key.ordinal()] = false;
    }

    public void nextInputFrame() {
        System.arraycopy(Keyboard.keyState, 0, Keyboard.prevKeyState, 0,
                Keyboard.prevKeyState.length);
    }
}