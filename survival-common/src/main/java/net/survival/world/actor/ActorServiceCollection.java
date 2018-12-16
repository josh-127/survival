package net.survival.world.actor;

public class ActorServiceCollection
{
    private final LocomotiveService locomotiveService;

    public ActorServiceCollection(EventQueue.Producer eventQueue) {
        locomotiveService = new LocomotiveService(eventQueue);
    }

    public LocomotiveService getLocomotiveService() {
        return locomotiveService;
    }
}