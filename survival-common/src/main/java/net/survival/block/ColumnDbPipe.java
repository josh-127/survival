package net.survival.block;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.survival.block.message.ColumnRequest;
import net.survival.block.message.ColumnResponseMessage;

public class ColumnDbPipe
{
    private final LinkedBlockingQueue<ColumnRequest> requests = new LinkedBlockingQueue<>();
    private final ConcurrentLinkedQueue<ColumnResponseMessage> responses = new ConcurrentLinkedQueue<>();
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

        public void respond(ColumnResponseMessage message) {
            responses.add(message);
        }
    }

    public class ClientSide
    {
        public ColumnResponseMessage pollResponse() {
            return responses.poll();
        }

        public void request(ColumnRequest request) {
            requests.add(request);
        }
    }
}