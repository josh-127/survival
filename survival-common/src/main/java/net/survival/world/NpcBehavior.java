package net.survival.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.entity.Character;
import net.survival.entity.Npc;
import net.survival.entity.NpcMovementStyle;
import net.survival.entity.Player;
import net.survival.world.chunk.Chunk;

public class NpcBehavior implements EntityBehavior
{
    @Override
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

                if (npc.movementStyle == NpcMovementStyle.DEFAULT) {
                    npc.setMoveDirectionControlValues(dx, dz);

                    if (nearestPlayer.y - npc.y >= 1.0)
                        npc.setJumpControlValue();
                }
                else if (npc.movementStyle == NpcMovementStyle.SLIME) {
                    npc.setMoveDirectionControlValues(dx, dz);
                    npc.setJumpControlValue();
                }
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