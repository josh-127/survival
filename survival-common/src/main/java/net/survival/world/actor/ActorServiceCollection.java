package net.survival.world.actor;

public class ActorServiceCollection
{
    private final AlarmService alarmService;
    private final LocomotiveService locomotiveService;

    public ActorServiceCollection(AlarmService alarmService, LocomotiveService locomotiveService) {
        this.alarmService = alarmService;
        this.locomotiveService = locomotiveService;
    }

    public AlarmService getAlarmService() {
        return alarmService;
    }

    public LocomotiveService getLocomotiveService() {
        return locomotiveService;
    }
}