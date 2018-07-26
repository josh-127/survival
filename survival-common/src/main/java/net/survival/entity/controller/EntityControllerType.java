package net.survival.entity.controller;

public enum EntityControllerType
{
    DEFAULT(new DefaultController());

    public final EntityController controller;

    private EntityControllerType(EntityController controller) {
        this.controller = controller;
    }
}