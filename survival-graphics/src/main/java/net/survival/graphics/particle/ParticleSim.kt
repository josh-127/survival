package net.survival.graphics.particle

private const val GRAVITY = -8.0

class ParticleSim {
    val data = ParticleData(32)

    fun burstParticles(x: Double, y: Double, z: Double, strength: Double, quantity: Int) {
        for (i in 0 until quantity) {
            val velocityX = (Math.random() - 0.5) * 2.0 * strength
            val velocityY = (Math.random() - 0.5) * 2.0 * strength
            val velocityZ = (Math.random() - 0.5) * 2.0 * strength
            data.addParticle(x, y, z, velocityX, velocityY, velocityZ)
        }
    }

    fun step(elapsedTime: Double) {
        for (i in 0 until data.maxParticles) data.velocityYs[i] += GRAVITY * elapsedTime
        for (i in 0 until data.maxParticles) data.xs[i] += data.velocityXs[i] * elapsedTime
        for (i in 0 until data.maxParticles) data.ys[i] += data.velocityYs[i] * elapsedTime
        for (i in 0 until data.maxParticles) data.zs[i] += data.velocityZs[i] * elapsedTime
    }
}