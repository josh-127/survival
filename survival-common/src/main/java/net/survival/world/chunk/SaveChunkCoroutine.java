package net.survival.world.chunk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import net.survival.concurrent.VoidCoroutine;

class SaveChunkCoroutine implements VoidCoroutine
{
    private static final int CHUNK_HEADER_SIZE = 1;

    private static final int SERIALIZING = 0;
    private static final int WRITING_TO_CHANNEL = 1;
    private static final int FINISHED = 2;

    private final Chunk chunk;
    private final FileChannel fileChannel;
    private final ByteBuffer serializedChunkData;

    private int state;
    private int counter;

    private SaveChunkCoroutine(Chunk chunk, FileChannel fileChannel, ByteBuffer serializedChunkData) {
        this.chunk = chunk;
        this.fileChannel = fileChannel;
        this.serializedChunkData = serializedChunkData;
    }

    public static SaveChunkCoroutine create(Chunk chunk, File outputFile) {
        return moveChunkAndCreate(chunk.makeCopy(), outputFile);
    }

    //
    // WARNING: You may not use the chunk after calling this method.
    //
    public static SaveChunkCoroutine moveChunkAndCreate(Chunk chunk, File outputFile) {
        try {
            @SuppressWarnings("resource")
            FileChannel fileChannel = new FileOutputStream(outputFile).getChannel();
            ByteBuffer serializedChunkData = ByteBuffer.allocate(Chunk.VOLUME * 2 + CHUNK_HEADER_SIZE);
            return new SaveChunkCoroutine(chunk, fileChannel, serializedChunkData);
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
                    byte flags = (byte) (chunk.isDecorated() ? 1 : 0);
                    serializedChunkData.put(flags);

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
                        rleStrip |= (short) (((counter - start - 1) & 0xF) << 12);

                        serializedChunkData.putShort(rleStrip);
                    }

                    serializedChunkData.flip();
                    nextState();
                    return false;
                }

                case WRITING_TO_CHANNEL: {
                    if (serializedChunkData.hasRemaining()) {
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