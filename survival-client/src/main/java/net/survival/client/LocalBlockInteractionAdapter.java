package net.survival.client;

import java.util.Queue;

import net.survival.block.BlockSpace;
import net.survival.block.message.BlockMessage;
import net.survival.block.message.BreakBlockMessage;
import net.survival.block.message.PlaceBlockMessage;
import net.survival.blocktype.Block;
import net.survival.interaction.BlockInteractionAdapter;

public class LocalBlockInteractionAdapter implements BlockInteractionAdapter
{
    private final BlockSpace blockSpace;
    private final Queue<BlockMessage> messageQueue;

    public LocalBlockInteractionAdapter(BlockSpace blockSpace, Queue<BlockMessage> messageQueue) {
        this.blockSpace = blockSpace;
        this.messageQueue = messageQueue;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return blockSpace.getBlockState(x, y, z);
    }

    @Override
    public void breakBlock(int x, int y, int z) {
        messageQueue.add(new BreakBlockMessage(x, y, z));
    }

    @Override
    public void placeBlock(int x, int y, int z, int fullID) {
        messageQueue.add(new PlaceBlockMessage(x, y, z, fullID));
    }
}