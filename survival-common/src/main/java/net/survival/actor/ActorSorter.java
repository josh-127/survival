package net.survival.actor;

import java.util.ArrayList;

import net.survival.world.World;
import net.survival.world.chunk.ChunkPos;

final class ActorSorter
{
    private ActorSorter() {}

    public static void sortActors(World world) {
        ArrayList<Actor> actors = world.getActors();

        for (Actor actor : actors) {
            double x = actor.getX();
            double z = actor.getZ();
            int cx = ChunkPos.toChunkX((int) Math.floor(x));
            int cz = ChunkPos.toChunkZ((int) Math.floor(z));
            long hashedPos = ChunkPos.hashPos(cx, cz);

            actor.containingChunk = hashedPos;
        }

        actors.sort((o1, o2) -> {
            if (o1.containingChunk < o2.containingChunk)
                return -1;
            if (o1.containingChunk > o2.containingChunk)
                return 1;
            return 0;
        });
    }
}