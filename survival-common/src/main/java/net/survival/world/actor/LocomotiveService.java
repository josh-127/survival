package net.survival.world.actor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LocomotiveService
{
    public static final double GRAVITY = 3.0;

    private final HashMap<Actor, Data> objects = new HashMap<>();
    private final EventQueue.Producer eventQueue;

    public LocomotiveService(EventQueue.Producer eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void subscribe(Actor actor) {
        objects.put(actor, new Data());
    }

    public void tick(double elapsedTime) {
        collect();

        for (Map.Entry<Actor, Data> entry : objects.entrySet())
            tickSingle(entry.getValue(), elapsedTime);
    }

    private void collect() {
        Iterator<Map.Entry<Actor, Data>> iterator = objects.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Actor, Data> entry = iterator.next();
            Actor actor = entry.getKey();

            if (actor.isDead())
                iterator.remove();
        }
    }

    private void tickSingle(Data data, double elapsedTime) {
    }

    private static class Data
    {
        public double x;
        public double y;
        public double z;
        public double velocityX;
        public double velocityY;
        public double velocityZ;
    }
}