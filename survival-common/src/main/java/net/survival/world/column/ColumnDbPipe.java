package net.survival.world.column;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ColumnDbPipe
{
    private final LinkedBlockingQueue<ColumnRequest> requests = new LinkedBlockingQueue<>();
    private final ConcurrentLinkedQueue<ColumnResponse> responses = new ConcurrentLinkedQueue<>();
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
        public ColumnRequest waitForRequest() {
            try {
                return requests.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void respond(ColumnResponse response) {
            responses.add(response);
        }
    }

    public class ClientSide
    {
        public ColumnResponse pollResponse() {
            return responses.poll();
        }

        public void request(ColumnRequest request) {
            requests.add(request);
        }
    }
}