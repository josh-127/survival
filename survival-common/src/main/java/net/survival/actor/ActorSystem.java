package net.survival.actor;

import net.survival.world.World;

public class ActorSystem
{
    public static void update(World world, double elapsedTime) {
        for (Actor actor : world.getActors()) {
            boolean shouldRetry = true;
            while (shouldRetry)
                shouldRetry = update(actor);
        }

        ActorPhysics.update(world, elapsedTime);
        ActorSorter.sortActors(world);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static boolean update(Actor actor) {
        if (actor.currentMessage == null) {
            Object message = actor.loopbackInbox.poll();
            if (message == null)
                message = actor.inbox.poll();

            actor.currentMessage = message;
        }

        if (actor.currentMessage == null)
            return false;

        Object currentMessage = actor.currentMessage;
        MessageHandler handler = actor.messageHandlers.get(currentMessage.getClass());

        if (handler == null) {
            actor.currentMessage = null;
            return true;
        }

        if (!handler.handleMessage(currentMessage))
            actor.currentMessage = null;

        return false;
    }
}