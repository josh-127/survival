package net.survival.world.actor;

import java.util.ArrayList;
import java.util.Iterator;

public class AlarmService
{
    private final ArrayList<Instance> instances = new ArrayList<>();
    private final EventQueue.Producer eventQueue;
    private final int id;

    public AlarmService(EventQueue.Producer eventQueue, int id) {
        this.eventQueue = eventQueue;
        this.id = id;
    }

    public void setAlarm(Actor actor, double duration) {
        instances.add(new Instance(actor, duration));
    }

    public void tick(double elapsedTime) {
        collect();

        for (Instance instance : instances) {
            instance.remainingTime -= elapsedTime;

            if (instance.remainingTime <= 0.0)
                eventQueue.notifyActor(instance.actor, new FinishedToken(id));
        }
    }

    private void collect() {
        Iterator<Instance> iterator = instances.iterator();

        while (iterator.hasNext()) {
            Instance instance = iterator.next();

            if (instance.actor.isDead() || instance.remainingTime <= 0.0)
                iterator.remove();
        }
    }

    private static class Instance
    {
        public final Actor actor;
        public double remainingTime;

        public Instance(Actor actor, double duration) {
            this.actor = actor;
            remainingTime = duration;
        }
    }

    public static class FinishedToken {
        public final int alarmID;

        public FinishedToken(int alarmID) {
            this.alarmID = alarmID;
        }
    }
}