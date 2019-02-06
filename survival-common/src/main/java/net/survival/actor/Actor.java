package net.survival.actor;

import net.survival.actor.message.ActorMessageVisitor;

public interface Actor extends ActorMessageVisitor
{
    double getX();
    double getY();
    double getZ();
}