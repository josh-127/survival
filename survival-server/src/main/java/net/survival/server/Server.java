package net.survival.server;

public class Server
{
    private static final double TICKS_PER_SECOND = 60.0;
    private static final double SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND;

    private Server() {
    }

    public void tick(double elapsedTime) {
    }

    public static void main(String[] args) {
        Server program = new Server();

        final double MILLIS_PER_TICK = SECONDS_PER_TICK * 1000.0;
        long now = System.currentTimeMillis();
        long prevTime = now;
        double unprocessedTicks = 0.0;

        for (;;) {
            now = System.currentTimeMillis();
            unprocessedTicks += (now - prevTime) / MILLIS_PER_TICK;
            prevTime = now;

            if (unprocessedTicks >= 1.0) {
                while (unprocessedTicks >= 1.0) {
                    program.tick(SECONDS_PER_TICK);
                    unprocessedTicks -= 1.0;
                }
            }
        }
    }
}