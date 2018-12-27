package net.survival.world.actor.v0_1_0_snapshot;

import net.survival.util.HitBox;
import net.survival.world.actor.Actor;
import net.survival.world.actor.ActorServiceCollection;
import net.survival.world.actor.ExternalAxisInput;
import net.survival.world.actor.ActorEventQueue;
import net.survival.world.actor.Locomotion;

public class NpcActor extends Actor
{
    private final double initialX;
    private final double initialY;
    private final double initialZ;
    private double yaw;

    private ExternalAxisInput.Component input;
    private Locomotion.Component locomotion;

    public NpcActor(double x, double y, double z) {
        initialX = x;
        initialY = y;
        initialZ = z;
    }

    @Override
    public void setup(ActorServiceCollection services) {
        services.getAlarmService(0).setAlarm(this, 1.0);
        input = services.getExternalAxisInputService().subscribe(this);
        locomotion = services.getLocomotiveService().subscribe(this, initialX, initialY, initialZ, HitBox.NPC);
    }

    @Override
    protected void onAlarm(ActorEventQueue.Producer actorEventQueue, int alarmID) {
        if (alarmID == 0) {
            yaw += 1.0;
        }
    }

    @Override
    public double getX() {
        return locomotion.getX();
    }

    @Override
    public double getY() {
        return locomotion.getY();
    }

    @Override
    public double getZ() {
        return locomotion.getZ();
    }

    @Override
    public double getYaw() {
        return yaw;
    }

    @Override
    public double getMovementDirectionX() {
        return input.getDirectionX();
    }

    @Override
    public double getMovementDirectionZ() {
        return input.getDirectionZ();
    }
}