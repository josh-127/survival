package net.survival.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.survival.entity.Character;
import net.survival.util.MathEx;
import net.survival.world.chunk.Chunk;

public class CharacterBehavior implements EntityBehavior
{
    @Override
    public void postTick(World world, double elapsedTime) {
        ObjectIterator<Long2ObjectMap.Entry<Chunk>> chunkMapIt = world.getChunkMapFastIterator();

        while (chunkMapIt.hasNext()) {
            Long2ObjectMap.Entry<Chunk> entry = chunkMapIt.next();
            Chunk chunk = entry.getValue();

            for (Character character : chunk.iterateCharacters())
                postTick(world, elapsedTime, character);
        }
    }

    private void postTick(World world, double elapsedTime, Character character) {
        final double FRICTION = 0.5;
        
        double dx = character.getMoveDirectionXControlValue();
        double dz = character.getMoveDirectionZControlValue();
        double magnitude = MathEx.magnitude(dx, dz);

        if (magnitude != 0.0) {
            character.velocityX = character.moveSpeed * (dx / magnitude);
            character.velocityZ = character.moveSpeed * (dz / magnitude);
            character.yaw = MathEx.lerp(character.yaw, Math.atan2(-dz, dx) + Math.PI / 2.0, 0.25);
        }
        else {
            character.velocityX *= FRICTION;
            character.velocityZ *= FRICTION;
        }

        if (character.getJumpControlValue() && character.velocityY == 0.0)
            character.velocityY = character.jumpSpeed;
    }
}