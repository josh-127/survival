package net.survival.graphics.particle

import java.util.*

class ClientParticleSpace {
    private val particleSims = HashMap<String, ParticleSim>()

    fun burstParticles(
        x: Double,
        y: Double,
        z: Double,
        strength: Double,
        quantity: Int,
        texture: String
    ) {
        particleSims.putIfAbsent(texture, ParticleSim())
        particleSims[texture]!!.burstParticles(x, y, z, strength, quantity)
    }

    fun getParticleDomains(): Map<String, ParticleSim> {
        return particleSims
    }

    fun step(elapsedTime: Double) {
        for (particleDomain in particleSims.values) {
            particleDomain.step(elapsedTime)
        }
    }
}