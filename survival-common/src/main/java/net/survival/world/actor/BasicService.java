package net.survival.world.actor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BasicService<T>
{
    protected final HashMap<Actor, T> objects = new HashMap<>();
    protected final EventQueue.Producer eventQueue;

    public BasicService(EventQueue.Producer eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void subscribe(Actor actor) {
        objects.put(actor, createInstance());
    }

    protected void collect() {
        Iterator<Map.Entry<Actor, T>> iterator = objects.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Actor, T> entry = iterator.next();
            Actor actor = entry.getKey();

            if (actor.isDead())
                iterator.remove();
        }
    }

    protected abstract T createInstance();
}