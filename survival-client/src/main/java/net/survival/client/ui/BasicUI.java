package net.survival.client.ui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import net.survival.client.input.Mouse;

public class BasicUI
{
    private final Queue<ControlDisplayDesc> controlsToDisplay = new LinkedList<>();
    private final Server server = new Server();
    private final Client client = new Client();
    private final ControlIterator controlIterator = new ControlIterator();

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public class Server
    {
        public boolean button(String text, int left, int top, int right, int bottom) {
            boolean hovered =
                    Mouse.getX() >= left &&
                    Mouse.getX() < right &&
                    Mouse.getY() >= top &&
                    Mouse.getY() < bottom;

            boolean held = Mouse.isLmbDown();

            controlsToDisplay.add(ControlDisplayDesc.createButton(hovered, held, left, top, right, bottom, text));
            return hovered && Mouse.isLmbReleased();
        }

        public void label(String text, int left, int top) {
            controlsToDisplay.add(ControlDisplayDesc.createLabel(left, top, text));
        }
    }

    public class Client implements Iterable<ControlDisplayDesc>
    {
        @Override
        public Iterator<ControlDisplayDesc> iterator() {
            return controlIterator;
        }
    }

    private class ControlIterator implements Iterator<ControlDisplayDesc>
    {
        @Override
        public boolean hasNext() {
            return !controlsToDisplay.isEmpty();
        }

        @Override
        public ControlDisplayDesc next() {
            return controlsToDisplay.remove();
        }
    }
}