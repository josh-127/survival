package net.survival.graphics.particle;

import java.util.HashMap;
import java.util.Map;

public class ClientParticleSpace
{
    private final HashMap<String, ParticleDomain> particleDomains = new HashMap<>();

    public void burstParticles(
            double x,
            double y,
            double z,
            double strength,
            int quantity,
            String texture)
    {
        particleDomains.putIfAbsent(texture, new ParticleDomain());
        particleDomains.get(texture).burstParticles(x, y, z, strength, quantity);
    }

    public Map<String, ParticleDomain> getParticleDomains() {
        return particleDomains;
    }

    public void step(double elapsedTime) {
        for (var particleDomain : particleDomains.values()) {
            particleDomain.step(elapsedTime);
        }
    }
}