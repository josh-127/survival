package net.survival.world.actor;

import java.util.ArrayList;
import java.util.Iterator;

public class ActorSystem
{
    private final ArrayList<Actor> actors = new ArrayList<>();
    private final EventQueue eventQueue;

    public ActorSystem(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void add(Actor actor) {
        actors.add(actor);
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