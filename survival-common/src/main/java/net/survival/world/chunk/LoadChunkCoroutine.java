package net.survival.world.chunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Queue;

import net.survival.concurrent.Coroutine;

class LoadChunkCoroutine implements Coroutine<Chunk>
{
    private static final int MAX_LOAD_RATE = 2;

    private static final int PENDING = 0;
    private static final int OPENING_FILE = 1;
    private static final int READING_FROM_CHANNEL = 2;
    private static final int DESERIALIZING = 3;
    private static final int FINISHED = 4;

    // WARNING: Not thread safe.
    private static final HashSet<LoadChunkCoroutine> loadersInProgress = new HashSet<>();

    private final File inputFile;

    private Chunk chunk;
    private FileChannel fileChannel;
    private ByteBuffer serializedChunkData;
    
    private int state;

    private LoadChunkCoroutine(File inputFile) {
        this.inputFile = inputFile;
    }

    public static LoadChunkCoroutine create(File inputFile) {
        return new LoadChunkCoroutine(inputFile);
    }

    @Override
    public Chunk pollResult() {
        return state == FINISHED ? chunk : null;
    }

    @Override
    public boolean next() {
        try {
            switch (state) {
                case PENDING: {
                    if (loadersInProgress.size() < MAX_LOAD_RATE) {
                        loadersInProgress.add(this);
                        nextState();
                    }

                    return false;
                }

                case OPENING_FILE: {
                    chunk = new Chunk();
                    fileChannel = new FileInputStream(inputFile).getChannel();
                    serializedChunkData = ByteBuffer.allocate((int) inputFile.length());

                    nextState();
                    return false;
                }

                case READING_FROM_CHANNEL: {
                    if (serializedChunkData.hasRemaining()) {
                        fileChannel.read(serializedChunkData);
                    }
                    else {
                        fileChannel.close();
                        serializedChunkData.flip();
                        nextState();
                    }

                    return false;
                }

                case DESERIALIZING: {
                    int index = 0;

                    while (serializedChunkData.hasRemaining()) {
                        short rleStrip = serializedChunkData.getShort();
                        int length = (rleStrip & 0xF000) >>> 12;
                        short blockID = (short) (rleStrip & 0x0FFF);

                        for (int i = 0; i <= length; ++i)
                            chunk.blockIDs[index++] = blockID;
                    }

                    nextState();
                    return false;
                }

                case FINISHED: {
                    loadersInProgress.remove(this);
                    break;
                }
            }

            return true;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void nextState() {
        ++state;
    }
}