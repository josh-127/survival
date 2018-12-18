package net.survival.world.actor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class EventQueue
{
    private final Queue<EventPacket> upcomingEvents = new LinkedList<>();
    private final Producer producer = new Producer();
    private final ConsumerIterator consumerIterator = new ConsumerIterator();
    private final Consumer consumer = new Consumer(consumerIterator);

    public Producer getProducer() {
        return producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public static class EventPacket
    {
        public final Actor target;
        public final Object eventArgs;

        public EventPacket(Actor target, Object eventArgs) {
            this.target = target;
            this.eventArgs = eventArgs;
        }
    }

    public class Producer
    {
        public void notifyActor(Actor actor, Object eventArgs) {
            upcomingEvents.add(new EventPacket(actor, eventArgs));
        }
    }

    public class Consumer implements Iterable<EventPacket>
    {
        private final ConsumerIterator underlyingIterator;

        private Consumer(ConsumerIterator iterator) {
            underlyingIterator = iterator;
        }

        @Override
        public Iterator<EventPacket> iterator() {
            return underlyingIterator;
        }
    }

    private class ConsumerIterator implements Iterator<EventPacket>
    {
        @Override
        public boolean hasNext() {
            return !upcomingEvents.isEmpty();
        }

        @Override
        public EventPacket next() {
            return upcomingEvents.remove();
        }
    }
}