package net.survival.world.actor;

public class ActorServiceCollection
{
    private final AlarmService[] alarmServices;
    private final LocomotiveService locomotiveService;

    public ActorServiceCollection(AlarmService[] alarmServices, LocomotiveService locomotiveService) {
        this.alarmServices = alarmServices;
        this.locomotiveService = locomotiveService;
    }

    public AlarmService getAlarmService(int alarmID) {
        return alarmServices[alarmID];
    }

    public LocomotiveService getLocomotiveService() {
        return locomotiveService;
    }
}