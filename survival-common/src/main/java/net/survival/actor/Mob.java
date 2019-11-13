package net.survival.actor;

public class Mob extends Actor {
    private boolean friendly;

    public boolean isFriendly() { return friendly; }
    public void setFriendly(boolean to) { friendly = to; }
}