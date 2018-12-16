package net.survival.world.actor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.survival.world.World;

public class LocomotiveService
{
    public static final double GRAVITY = 32.0;
    public static final double TERMINAL_VELOCITY = 30.0;

    private final HashMap<Actor, Data> objects = new HashMap<>();
    private final World world;
    private final EventQueue.Producer eventQueue;

    public LocomotiveService(World world, EventQueue.Producer eventQueue) {
        this.world = world;
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