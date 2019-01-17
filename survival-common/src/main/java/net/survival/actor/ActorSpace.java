package net.survival.actor;

import java.util.ArrayList;

public class ActorSpace
{
    private final ArrayList<Actor> actors = new ArrayList<>(1024);

    public Iterable<Actor> getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }
}