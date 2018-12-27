package net.survival.world.actor;

public abstract class Actor
{
    private boolean dead;

    public abstract void setup(ActorServiceCollection services);

    public void onEventNotification(ActorEventQueue.Producer actorEventQueue, Object eventArgs) {
        if (eventArgs instanceof AlarmService.FinishedToken) {
            AlarmService.FinishedToken finishedToken = (AlarmService.FinishedToken) eventArgs;
            onAlarm(actorEventQueue, finishedToken.alarmID);
        }
    }

    protected void onAlarm(ActorEventQueue.Producer actorEventQueue, int alarmID) {}

    public abstract double getX();

    public abstract double getY();

    public abstract double getZ();

    public double getYaw() {
        return 0.0;
    }

    public double getPitch() {
        return 0.0;
    }

    public double getRoll() {
        return 0.0;
    }

    public double getScaleX() {
        return 1.0;
    }

    public double getScaleY() {
        return 1.0;
    }

    public double getScaleZ() {
        return 1.0;
    }

    public ActorModel getModel() {
        return ActorModel.HUMAN;
    }

    public boolean isDead() {
        return dead;
    }

    protected void killSelf() {
        dead = true;
    }

    public double getMovementDirectionX() {
        return 0.0;
    }

    public double getMovementDirectionZ() {
        return 0.0;
    }
}