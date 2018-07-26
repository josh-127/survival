package net.survival.entity.controller;

import net.survival.entity.Entity;

public interface EntityController
{
    void tick(Entity entity, double elapsedTime);
}