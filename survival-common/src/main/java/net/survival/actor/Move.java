package net.survival.actor;

public class Move {
    private Move() {}
    
    public static void dispatch(Actor actor, double dx, double dz) {
        actor.setDirectionX(dx);
        actor.setDirectionZ(dz);
    }
}