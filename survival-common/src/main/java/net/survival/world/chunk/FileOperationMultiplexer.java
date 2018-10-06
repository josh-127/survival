package net.survival.world.chunk;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.Queue;

import net.survival.concurrent.DeferredResult;
import net.survival.concurrent.Promise;

// "Multiplexer" is not a misnomer. Instead of each call to fileChannel being multiplexed,
// each transaction is multiplexed.
class FileOperationMultiplexer implements AutoCloseable
{
    private final FileChannel fileChannel;

    // TODO: Consider a priority queue sorted by seek position.
    private final Queue<Transaction> pendingTransactions;
    private Transaction currentTransaction;

    public FileOperationMultiplexer(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
        pendingTransactions = new LinkedList<>();
    }

    @Override
    public void close() throws RuntimeException {
        while (!canClose())
            update();

        try {
            fileChannel.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canClose() {
        return currentTransaction == null && pendingTransactions.isEmpty();
    }

    public void update() {
        try {
            if (currentTransaction == null && !pendingTransactions.isEmpty()) {
                currentTransaction = pendingTransactions.remove();
                fileChannel.position(currentTransaction.position);
            }
    
            if (currentTransaction != null) {
                if (currentTransaction.buffer.hasRemaining()) {
                    if (currentTransaction.type == Transaction.TYPE_READ)
                        fileChannel.read(currentTransaction.buffer);
                    else if (currentTransaction.type == Transaction.TYPE_WRITE)
                        fileChannel.write(currentTransaction.buffer);
                }
                else {
                    currentTransaction.finish();
                    currentTransaction = null;
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DeferredResult<ByteBuffer> read(long position, int length) {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        return new Transaction(buffer, position, Transaction.TYPE_READ);
    }

    public DeferredResult<ByteBuffer> borrowAndWrite(ByteBuffer buffer, long position) {
        Transaction transaction = new Transaction(buffer, position, Transaction.TYPE_WRITE);
        pendingTransactions.add(transaction);

        return transaction;
    }

    public void write(ByteBuffer buffer, long position) {
        borrowAndWrite(buffer.duplicate(), position);
    }

    private static class Transaction extends Promise<ByteBuffer>
    {
        public static final byte TYPE_READ = 0;
        public static final byte TYPE_WRITE = 1;

        public final ByteBuffer buffer;
        public final long position;
        public final byte type;

        public Transaction(ByteBuffer buffer, long position, byte type) {
            this.buffer = buffer;
            this.position = position;
            this.type = type;
        }

        public void finish() {
            buffer.flip();
            setResult(buffer);
        }
    }
}