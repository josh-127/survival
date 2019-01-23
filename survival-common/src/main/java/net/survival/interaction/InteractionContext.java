package net.survival.interaction;

import net.survival.blocktype.Block;
import net.survival.input.Key;

public class InteractionContext
{
    private final BlockInteractionAdapter blocks;
    private final KeyboardInteractionAdapter keyboard;
    private final TickInteractionAdapter tick;

    public InteractionContext(
            BlockInteractionAdapter blocks,
            KeyboardInteractionAdapter keyboard,
            TickInteractionAdapter tick)
    {
        this.blocks = blocks;
        this.keyboard = keyboard;
        this.tick = tick;
    }

    public double getElapsedTime() {
        return tick.getElapsedTime();
    }

    public boolean isKeyDown(Key key) {
        return keyboard.isKeyDown(key);
    }

    public boolean isKeyUp(Key key) {
        return keyboard.isKeyUp(key);
    }

    public boolean isKeyPressed(Key key) {
        return keyboard.isKeyPressed(key);
    }

    public boolean isKeyReleased(Key key) {
        return keyboard.isKeyReleased(key);
    }

    public Block getBlock(int x, int y, int z) {
        return blocks.getBlock(x, y, z);
    }
}