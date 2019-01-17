package net.survival.actor;

import net.survival.actor.interaction.InteractionContext;

public interface Actor
{
    void update(InteractionContext ic, Message message);

    double getX();
    double getY();
    double getZ();

    default double getYaw() { return 0.0; }
    default double getPitch() { return 0.0; }
    default double getRoll() { return 0.0; }
    default double getScaleX() { return 1.0; }
    default double getScaleY() { return 1.0; }
    default double getScaleZ() { return 1.0; }
    default ActorModel getModel() { return ActorModel.HUMAN; }
}