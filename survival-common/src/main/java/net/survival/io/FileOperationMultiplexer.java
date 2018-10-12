package net.survival.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;

import net.survival.concurrent.DeferredResult;
import net.survival.concurrent.Promise;

// "Multiplexer" is not a misnomer. Instead of each call to fileChannel being multiplexed,
// each transaction is multiplexed.
public class FileOperationMultiplexer implements AutoCloseable
{
    private final AsynchronousFileChannel fileChannel;

    // TODO: Consider a priority queue sorted by seek position.
    private final Queue<Transaction> pendingTransactions;
    private Transaction currentTransaction;

    private Future<Integer> bytesReadOrWritten;

    public FileOperationMultiplexer(AsynchronousFileChannel fileChannel) {
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
        if (currentTransaction == null && !pendingTransactions.isEmpty()) {
            currentTransaction = pendingTransactions.remove();
        }

        if (currentTransaction != null) {
            if (currentTransaction.buffer.hasRemaining()) {
                if (bytesReadOrWritten == null || bytesReadOrWritten.isDone()) {
                    if (currentTransaction.type == Transaction.TYPE_READ) {
                        bytesReadOrWritten = fileChannel.read(
                                currentTransaction.buffer, currentTransaction.position);
                    }
                    else if (currentTransaction.type == Transaction.TYPE_WRITE) {
                        bytesReadOrWritten = fileChannel.write(
                                currentTransaction.buffer, currentTransaction.position);
                    }
                }
            }
            else {
                currentTransaction.finish();
                currentTransaction = null;
            }
        }
    }

    public DeferredResult<ByteBuffer> read(long position, int length) {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        Transaction transaction = new Transaction(buffer, position, Transaction.TYPE_READ);
        pendingTransactions.add(transaction);

        return transaction;
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