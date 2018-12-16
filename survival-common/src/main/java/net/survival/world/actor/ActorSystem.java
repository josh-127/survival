package net.survival.world.actor;

import java.util.ArrayList;
import java.util.Iterator;

public class ActorSystem
{
    private final ArrayList<Actor> actors = new ArrayList<>();
    private final EventQueue eventQueue;
    private final ActorServiceCollection services;

    public ActorSystem(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
        this.services = new ActorServiceCollection(eventQueue.getProducer());
    }

    public void add(Actor actor) {
        actors.add(actor);
    }

    public ActorServiceCollection getActorServices() {
        return services;
    }

    public void collect() {
        Iterator<Actor> iterator = actors.iterator();

        while (iterator.hasNext()) {
            Actor actor = iterator.next();

            if (actor.isDead())
                iterator.remove();
        }
    }
}