package net.survival.world.chunk;

import java.io.File;
import java.nio.file.Paths;

import net.survival.concurrent.Coroutine;
import net.survival.concurrent.VoidCoroutine;

public class FilePersistentChunkStorage implements PersistentChunkStorage
{
    // TODO: Use less error-prone path structures.

    private final String rootPath;

    public FilePersistentChunkStorage(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public Coroutine<Chunk> provideChunkAsync(long hashedPos) {
        throw new RuntimeException("Not implemented yet.");
    }

    @Override
    public VoidCoroutine moveAndSaveChunkAsync(long hashedPos, Chunk chunk) {
        String baseName = String.format("%0X", hashedPos);
        String filePath = Paths.get(rootPath, baseName).toString();

        SaveChunkCoroutine coroutine = SaveChunkCoroutine.moveChunkAndCreate(chunk, new File(filePath));
        coroutine.start();

        return coroutine;
    }
}