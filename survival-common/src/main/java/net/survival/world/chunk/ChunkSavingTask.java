package net.survival.world.chunk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import net.survival.concurrent.VoidCoroutineTask;

class ChunkSavingTask implements VoidCoroutineTask
{
    private static final int SERIALIZING = 0;
    private static final int WRITING_TO_CHANNEL = 1;
    private static final int FINISHED = 2;

    private final Chunk chunk;
    private final FileChannel fileChannel;
    private final ByteBuffer serializedChunkData;

    private int state;
    private int counter;

    private ChunkSavingTask(Chunk chunk, FileChannel fileChannel, ByteBuffer serializedChunkData) {
        this.chunk = chunk;
        this.fileChannel = fileChannel;
        this.serializedChunkData = serializedChunkData;
    }

    public static ChunkSavingTask create(Chunk chunk, File outputFile) {
        return moveChunkAndCreate(chunk.makeCopy(), outputFile);
    }

    //
    // WARNING: You may not use the chunk after calling this method.
    //
    public static ChunkSavingTask moveChunkAndCreate(Chunk chunk, File outputFile) {
        try {
            @SuppressWarnings("resource")
            FileChannel fileChannel = new FileOutputStream(outputFile).getChannel();
            ByteBuffer serializedChunkData = ByteBuffer.allocate(Chunk.VOLUME * 2);
            return new ChunkSavingTask(chunk, fileChannel, serializedChunkData);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object pollResult() {
        return state == FINISHED ? FINISHED_TOKEN : null;
    }

    @Override
    public boolean next() {
        try {
            switch (state) {
                case SERIALIZING: {
                    while (counter < Chunk.VOLUME) {
                        int start = counter;
                        int startBlockID = chunk.blockIDs[start];

                        while (counter < Chunk.VOLUME
                                && counter - start < 16
                                && chunk.blockIDs[counter] == startBlockID)
                        {
                            ++counter;
                        }

                        short rleStrip = (short) (startBlockID & 0x0FFF);
                        rleStrip |= (short) ((counter - start) << 12);

                        serializedChunkData.putShort(rleStrip);
                    }

                    /*
                    for (int i = 0; i < Chunk.VOLUME; ++i)
                        serializedChunkData.putShort(chunk.blockIDs[i]);
                    */

                    serializedChunkData.flip();
                    nextState();
                    return false;
                }

                case WRITING_TO_CHANNEL: {
                    if (serializedChunkData.hasRemaining()) {
                        // Writing 65536 bytes every time is slow.
                        fileChannel.write(serializedChunkData);
                    }
                    else {
                        fileChannel.close();
                        nextState();
                    }

                    return false;
                }
            }

            return true;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetCounter() {
        counter = 0;
    }

    private void nextState() {
        ++state;
        resetCounter();
    }
}