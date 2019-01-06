package net.survival.world.actor.interaction;

import net.survival.block.BlockState;
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

    public BlockState getBlock(int x, int y, int z) {
        return blocks.getBlock(x, y, z);
    }

    public void setBlock(int x, int y, int z, BlockState to) {
    }
}