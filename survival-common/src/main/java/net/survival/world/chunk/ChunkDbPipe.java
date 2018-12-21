package net.survival.world.chunk;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChunkDbPipe
{
    private final LinkedBlockingQueue<ChunkRequest> requests = new LinkedBlockingQueue<>();
    private final ConcurrentLinkedQueue<ChunkResponse> responses = new ConcurrentLinkedQueue<>();
    private final ChunkRequestIterator chunkRequestIterator = new ChunkRequestIterator();
    private final ChunkRequestIterable chunkRequestIterable = new ChunkRequestIterable();
    private final ServerSide serverSide = new ServerSide();
    private final ClientSide clientSide = new ClientSide();

    public ServerSide getServerSide() {
        return serverSide;
    }

    public ClientSide getClientSide() {
        return clientSide;
    }

    public class ServerSide
    {
        public Iterable<ChunkRequest> getRequests() {
            return chunkRequestIterable;
        }

        public void respond(ChunkResponse response) {
            responses.add(response);
        }
    }

    private class ChunkRequestIterable implements Iterable<ChunkRequest>
    {
        @Override
        public Iterator<ChunkRequest> iterator() {
            return chunkRequestIterator;
        }
    }

    private class ChunkRequestIterator implements Iterator<ChunkRequest>
    {
        @Override
        public boolean hasNext() {
            return !requests.isEmpty();
        }

        @Override
        public ChunkRequest next() {
            try {
                return requests.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class ClientSide
    {
        public ChunkResponse pollResponse() {
            return responses.poll();
        }

        public void request(ChunkRequest request) {
            requests.add(request);
        }
    }
}