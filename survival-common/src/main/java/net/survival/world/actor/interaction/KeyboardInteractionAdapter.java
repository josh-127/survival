package net.survival.world.actor.interaction;

import survival.input.Key;

public interface KeyboardInteractionAdapter
{
    boolean isKeyDown(Key key);
    boolean isKeyUp(Key key);
    boolean isKeyPressed(Key key);
    boolean isKeyReleased(Key key);
}