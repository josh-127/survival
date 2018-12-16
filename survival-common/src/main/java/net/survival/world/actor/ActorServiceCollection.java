package net.survival.world.actor;

import net.survival.world.World;

public class ActorServiceCollection
{
    private final LocomotiveService locomotiveService;

    public ActorServiceCollection(World world, EventQueue.Producer eventQueue) {
        locomotiveService = new LocomotiveService(world, eventQueue);
    }

    public LocomotiveService getLocomotiveService() {
        return locomotiveService;
    }
}