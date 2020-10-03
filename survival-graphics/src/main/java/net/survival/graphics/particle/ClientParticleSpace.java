package net.survival.graphics.particle;

import java.util.HashMap;
import java.util.Map;

public class ClientParticleSpace {
    private final HashMap<String, ParticleSim> particleDomains = new HashMap<>();

    public void burstParticles(
            double x,
            double y,
            double z,
            double strength,
            int quantity,
            String texture)
    {
        particleDomains.putIfAbsent(texture, new ParticleSim());
        particleDomains.get(texture).burstParticles(x, y, z, strength, quantity);
    }

    public Map<String, ParticleSim> getParticleDomains() {
        return particleDomains;
    }

    public void step(double elapsedTime) {
        for (var particleDomain : particleDomains.values()) {
            particleDomain.step(elapsedTime);
        }
    }
}