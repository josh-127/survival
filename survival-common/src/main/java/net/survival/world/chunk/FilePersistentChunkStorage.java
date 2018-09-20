package net.survival.world.chunk;

import java.io.File;
import java.nio.file.Paths;

import net.survival.concurrent.CoroutineTask;
import net.survival.concurrent.VoidCoroutineTask;

public class FilePersistentChunkStorage implements PersistentChunkStorage
{
    // TODO: Use less error-prone path structures.

    private final String rootPath;

    public FilePersistentChunkStorage(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public CoroutineTask<Chunk> provideChunkAsync(int cx, int cz) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public VoidCoroutineTask moveAndSaveChunkAsync(int cx, int cz, Chunk chunk) {
        String baseName = String.format("%0X", ChunkPos.hashPos(cx, cz));
        String filePath = Paths.get(rootPath, baseName).toString();

        ChunkSavingTask task = ChunkSavingTask.moveChunkAndCreate(chunk, new File(filePath));
        task.start();

        return task;
    }
}