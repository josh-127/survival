package net.survival.world.actor;

public class ActorServiceCollection
{
    private final AlarmService[] alarmServices;
    private final ExternalAxisInput.Service externalAxisInput;
    private final Locomotion.Service locomotion;

    public ActorServiceCollection(
            AlarmService[] alarmServices,
            ExternalAxisInput.Service externalAxisInput,
            Locomotion.Service locomotion)
    {
        this.alarmServices = alarmServices;
        this.externalAxisInput = externalAxisInput;
        this.locomotion = locomotion;
    }

    public AlarmService getAlarmService(int alarmID) {
        return alarmServices[alarmID];
    }

    public ExternalAxisInput.Service getExternalAxisInputService() {
        return externalAxisInput;
    }

    public Locomotion.Service getLocomotiveService() {
        return locomotion;
    }
}