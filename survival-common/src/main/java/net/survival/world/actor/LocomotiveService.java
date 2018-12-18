package net.survival.world.actor;

import java.util.Map;

import net.survival.world.World;

public class LocomotiveService extends BasicService<LocomotiveService.Data>
{
    public static final double GRAVITY = 32.0;
    public static final double TERMINAL_VELOCITY = 30.0;

    private final World world;

    public LocomotiveService(World world, EventQueue.Producer eventQueue) {
        super(eventQueue);
        this.world = world;
    }

    @Override
    protected Data createInstance() {
        return new Data();
    }

    public void tick(double elapsedTime) {
        collect();

        for (Map.Entry<Actor, Data> entry : objects.entrySet())
            tickSingle(entry.getValue(), elapsedTime);
    }

    private void tickSingle(Data data, double elapsedTime) {
    }

    protected static class Data
    {
        public double x;
        public double y;
        public double z;
        public double velocityX;
        public double velocityY;
        public double velocityZ;
    }
}