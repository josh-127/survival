package net.survival.world;

public class EntitySystem
{
    private final NpcAI npcAI;

    private final CharacterPhysics characterPhysics;
    private final EntityRelocator entityRelocator;

    public EntitySystem() {
        npcAI = new NpcAI();
        characterPhysics = new CharacterPhysics();
        entityRelocator = new EntityRelocator();
    }

    public void update(World world, double elapsedTime) {
        npcAI.tick(world, elapsedTime);
        characterPhysics.update(world, elapsedTime);
        entityRelocator.relocateEntities(world);
    }
}