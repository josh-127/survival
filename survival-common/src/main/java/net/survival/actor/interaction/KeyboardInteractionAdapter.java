package net.survival.actor.interaction;

import net.survival.input.Key;

public interface KeyboardInteractionAdapter
{
    boolean isKeyDown(Key key);
    boolean isKeyUp(Key key);
    boolean isKeyPressed(Key key);
    boolean isKeyReleased(Key key);
}