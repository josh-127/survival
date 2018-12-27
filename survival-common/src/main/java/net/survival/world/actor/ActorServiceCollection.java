package net.survival.world.actor;

public class ActorServiceCollection
{
    private final AlarmService[] alarmServices;
    private final Locomotion.Service locomotiveService;

    public ActorServiceCollection(
            AlarmService[] alarmServices,
            Locomotion.Service locomotiveService)
    {
        this.alarmServices = alarmServices;
        this.locomotiveService = locomotiveService;
    }

    public AlarmService getAlarmService(int alarmID) {
        return alarmServices[alarmID];
    }

    public Locomotion.Service getLocomotiveService() {
        return locomotiveService;
    }
}