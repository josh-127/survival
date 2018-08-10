package net.survival.entity;

public class Npc extends Character
{
    public double playerDetectionRadius;
    public NpcMovementStyle movementStyle;

    public Npc() {
        playerDetectionRadius = 16.0;
        movementStyle = NpcMovementStyle.DEFAULT;
    }
}