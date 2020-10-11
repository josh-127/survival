package net.survival.graphics.particle

class ParticleData(maxParticles: Int) {
    val xs: DoubleArray = DoubleArray(maxParticles)
    val ys: DoubleArray = DoubleArray(maxParticles)
    val zs: DoubleArray = DoubleArray(maxParticles)
    val velocityXs: DoubleArray = DoubleArray(maxParticles)
    val velocityYs: DoubleArray = DoubleArray(maxParticles)
    val velocityZs: DoubleArray = DoubleArray(maxParticles)
    val maxParticles: Int = maxParticles
    private var nextIndex = 0

    fun addParticle(x: Double, y: Double, z: Double, velocityX: Double, velocityY: Double, velocityZ: Double) {
        xs[nextIndex] = x
        ys[nextIndex] = y
        zs[nextIndex] = z
        velocityXs[nextIndex] = velocityX
        velocityYs[nextIndex] = velocityY
        velocityZs[nextIndex] = velocityZ
        nextIndex = (nextIndex + 1) % maxParticles
    }
}