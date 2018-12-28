package net.survival.world.actor;

public class DisplayActorMessage extends Message
{
    public final double x;
    public final double y;
    public final double z;
    public final ActorModel model;

    public DisplayActorMessage(double x, double y, double z, ActorModel model) {
        super(CLIENT_DISPLAY);
        this.x = x;
        this.y = y;
        this.z = z;
        this.model = model;
    }
}