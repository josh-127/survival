package net.survival.world.actor.v0_1_0_snapshot;

import net.survival.world.actor.Actor;
import net.survival.world.actor.ActorServiceCollection;

public class NpcActor extends Actor
{
    private double x;
    private double y;
    private double z;
    private double yaw;

    public NpcActor(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void setup(ActorServiceCollection services) {
        services.getAlarmService(0).setAlarm(this, 1.0);
        services.getAlarmService(1).setAlarm(this, 0.25);
    }

    @Override
    protected void onAlarm(ActorServiceCollection services, int alarmID) {
        if (alarmID == 0) {
            z += 0.25;
            services.getAlarmService(0).setAlarm(this, 1.0);
        }
        else if (alarmID == 1) {
            yaw += 0.25;
            services.getAlarmService(1).setAlarm(this, 0.25);
        }
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }
}