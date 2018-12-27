package net.survival.world.actor;

public class ExternalAxisInput
{
    public static class Component
    {
        private double directionX;
        private double directionY;
        private double directionZ;

        public double getDirectionX() {
            return directionX;
        }

        public double getDirectionY() {
            return directionY;
        }

        public double getDirectionZ() {
            return directionZ;
        }
    }

    public static class Service
    {
        private final Component component = new Component();

        public Component subscribe(Actor actor) {
            return component;
        }

        public void setDirection(double x, double y, double z) {
            component.directionX = x;
            component.directionY = y;
            component.directionZ = z;
        }
    }
}