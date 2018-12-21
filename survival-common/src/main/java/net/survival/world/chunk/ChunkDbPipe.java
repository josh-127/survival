package net.survival.world.chunk;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChunkDbPipe
{
    private final LinkedBlockingQueue<ChunkRequest> requests = new LinkedBlockingQueue<>();
    private final ConcurrentLinkedQueue<ChunkResponse> responses = new ConcurrentLinkedQueue<>();
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
        public ChunkRequest waitForRequest() {
            try {
                return requests.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void respond(ChunkResponse response) {
            responses.add(response);
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