package net.survival.actor;

import java.util.HashMap;
import java.util.Map;

public class ActorSpace
{
    private final HashMap<Integer, Actor> actors = new HashMap<>();
    private int nextId = 0;

    public Iterable<Map.Entry<Integer, Actor>> iterateActorMap() {
        return actors.entrySet();
    }

    public Iterable<Actor> iterateActors() {
        return actors.values();
    }

    public Actor getActor(int id) {
        return actors.get(id);
    }

    public int addActor(Actor actor) {
        actors.put(nextId, actor);
        return nextId++;
    }
}