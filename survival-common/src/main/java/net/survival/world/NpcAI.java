package net.survival.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.entity.Character;
import net.survival.entity.Npc;
import net.survival.entity.Player;
import net.survival.util.MathEx;
import net.survival.world.chunk.Chunk;

public class NpcAI
{
    public void tick(World world, double elapsedTime) {
        ObjectIterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Character character : chunk.iterateCharacters()) {
                if (character instanceof Npc)
                    tick(world, elapsedTime, (Npc) character);
            }
        }
    }

    private void tick(World world, double elapsedTime, Npc npc) {
        // TODO: Optimize search algorithm.

        //
        // TODO: Also need to cache player searching, so if an NPC is already
        // targeting a player, then there's no need to iterate through
        // chunks.
        //
        // HashMap<Npc, Player> targets;
        //

        ObjectIterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();
        Player nearestPlayer = null;
        double nearestPlayerSqrDistance = Double.MAX_VALUE;

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Character character : chunk.iterateCharacters()) {
                if (!(character instanceof Player))
                    continue;

                Player player = (Player) character;

                double sqrDistance = getSqrDistance(npc, player);

                if (sqrDistance < nearestPlayerSqrDistance) {
                    nearestPlayer = player;
                    nearestPlayerSqrDistance = sqrDistance;
                }
            }
        }

        if (nearestPlayer != null) {
            double dx = nearestPlayer.x - npc.x;
            double dy = nearestPlayer.y - npc.y;
            double dz = nearestPlayer.z - npc.z;
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (distance > 1.0 && distance < npc.playerDetectionRadius) {
                dx /= distance;
                dy /= distance;
                dz /= distance;

                npc.velocityX = MathEx.lerp(npc.velocityX, dx * 4.0, 0.25);
                npc.velocityZ = MathEx.lerp(npc.velocityZ, dz * 4.0, 0.25);
                npc.yaw = MathEx.lerp(npc.yaw, Math.atan2(-dz, dx) + Math.PI / 2.0, 0.25);

                if (npc.velocityY == 0.0 && nearestPlayer.y - npc.y >= 1.0) {
                    npc.velocityY = 10.0;
                }
            }
            else {
                npc.velocityX *= 0.5;
                npc.velocityZ *= 0.5;
            }
        }
    }

    private double getSqrDistance(Character c1, Character c2) {
        double dx = c2.x - c1.x;
        double dy = c2.y - c1.y;
        double dz = c2.z - c1.z;

        return dx * dx + dy * dy + dz * dz;
    }
}