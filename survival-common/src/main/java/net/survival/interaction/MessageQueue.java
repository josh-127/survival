package net.survival.interaction;

import java.util.PriorityQueue;

public class MessageQueue
{
    private PriorityQueue<Message> readyMessages = new PriorityQueue<Message>();
    private PriorityQueue<Message> pendingMessages = new PriorityQueue<Message>();
    private int currentPriority;

    public void enqueueMessage(Message message) {
        if (message.getPriority() < currentPriority)
            pendingMessages.add(message);
        else
            readyMessages.add(message);
    }

    public Message dequeueMessage() {
        var message = readyMessages.remove();
        currentPriority = message.getPriority();

        return message;
    }

    public boolean isEmpty() {
        return readyMessages.isEmpty();
    }

    public void nextFrame() {
        readyMessages.addAll(pendingMessages);
        pendingMessages = new PriorityQueue<>();
        currentPriority = 0;
    }
}