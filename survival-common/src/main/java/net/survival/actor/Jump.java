package net.survival.actor;

public class Jump {
    private static final double GRAVITY = 32.0;
    
    private Jump() {}
    
    public static void dispatch(Actor actor, double height) {
        actor.setVelY(Math.sqrt(2.0 * GRAVITY * height));
    }
}