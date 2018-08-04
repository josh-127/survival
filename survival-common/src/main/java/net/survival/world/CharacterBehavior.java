package net.survival.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.entity.Character;
import net.survival.entity.Npc;
import net.survival.entity.Player;
import net.survival.util.MathEx;
import net.survival.world.chunk.Chunk;

public class CharacterBehavior
{
    public void tick(World world, double elapsedTime) {
        ObjectIterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Npc npc : chunk.iterateNpcs())
                tick(world, elapsedTime, npc);

            for (Player player : chunk.iteratePlayers())
                tick(world, elapsedTime, player);
        }
    }

    private void tick(World world, double elapsedTime, Character character) {
        final double SPEED = 5.0;
        final double FRICTION = 0.5;
        
        double dx = character.getMoveDirectionXControlValue();
        double dz = character.getMoveDirectionZControlValue();
        double magnitude = MathEx.magnitude(dx, dz);

        if (magnitude != 0.0) {
            character.velocityX = dx / magnitude * SPEED;
            character.velocityZ = dz / magnitude * SPEED;
        }
        else {
            character.velocityX *= FRICTION;
            character.velocityZ *= FRICTION;
        }

        if (character.getJumpControlValue() && character.velocityY == 0.0)
            character.velocityY = 10.0;
    }
}