package net.survival.graphics

import net.survival.graphics.opengl.GLBlendFactor
import net.survival.graphics.opengl.GLImmediateDrawCall
import net.survival.graphics.opengl.GLMatrixStack
import net.survival.graphics.opengl.GLState
import org.joml.Matrix4f
import java.util.*
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sqrt

private const val MAP_LENGTH_X = 48
private const val MAP_LENGTH_Z = 48
private const val DEFAULT_MAP_SEED = 0L
private const val DEFAULT_SCALE = 48
private const val DEFAULT_DENSITY = 0.5f
private const val DEFAULT_ELEVATION = 383.5f
private const val DEFAULT_SPEED_X = 0.0f
private const val DEFAULT_SPEED_Z = 8.0f
private const val DEFAULT_ALPHA = 0.875f

private const val TOTAL_LENGTH_X = MAP_LENGTH_X * DEFAULT_SCALE
private const val TOTAL_LENGTH_Z = MAP_LENGTH_Z * DEFAULT_SCALE

internal class CloudDisplay {
    private var mapSeed: Long = DEFAULT_MAP_SEED
    private val scale: Int = DEFAULT_SCALE
    private var density: Float = DEFAULT_DENSITY
    var elevation: Float = DEFAULT_ELEVATION
    var speedX: Float = DEFAULT_SPEED_X
    var speedZ: Float = DEFAULT_SPEED_Z
    var alpha: Float = DEFAULT_ALPHA
    private var shouldRebuild = false

    private var vertexPositions: FloatArray? = null
    private var posX = 0f
    private var posZ = 0f

    var seed: Long
        get() = mapSeed
        set(to) {
            mapSeed = to
            shouldRebuild = true
        }

    init {
        rebuildVertexArray()
    }

    fun setDensity(to: Float) {
        density = to
        shouldRebuild = true
    }

    private fun rebuildVertexArray() {
        val random = Random(mapSeed)

        var index = 0
        val positions = FloatArray(scale * scale * 12)

        for (z in 0 until scale) {
            for (x in 0 until scale) {
                if (random.nextDouble() >= density) {
                    continue
                }

                val posX = x * scale
                val posZ = z * scale
                positions[index++] = posX.toFloat()
                positions[index++] = posZ.toFloat()
                positions[index++] = (posX + scale).toFloat()
                positions[index++] = posZ.toFloat()
                positions[index++] = (posX + scale).toFloat()
                positions[index++] = (posZ + scale).toFloat()
                positions[index++] = (posX + scale).toFloat()
                positions[index++] = (posZ + scale).toFloat()
                positions[index++] = posX.toFloat()
                positions[index++] = (posZ + scale).toFloat()
                positions[index++] = posX.toFloat()
                positions[index++] = posZ.toFloat()
            }
        }

        vertexPositions = FloatArray(index)
        System.arraycopy(positions, 0, vertexPositions!!, 0, index)
    }

    fun tick(elapsedTime: Double) {
        posX += speedX * elapsedTime.toFloat()
        posZ += speedZ * elapsedTime.toFloat()

        // NOTE: Clouds will run away if speedX or speedZ is too large.
        if (speedX > 0.0f && posX >= TOTAL_LENGTH_X) {
            posX -= TOTAL_LENGTH_X.toFloat()
        }
        else if (speedX < 0.0f && posX < 0.0f) {
            posX += TOTAL_LENGTH_X.toFloat()
        }

        if (speedZ > 0.0f && posZ >= TOTAL_LENGTH_Z) {
            posZ -= TOTAL_LENGTH_Z.toFloat()
        }
        else if (speedZ < 0.0f && posZ < 0.0f) {
            posZ += TOTAL_LENGTH_Z.toFloat()
        }
    }

    fun display(viewMatrix: Matrix4f, projectionMatrix: Matrix4f, cameraX: Float, cameraZ: Float) {
        if (shouldRebuild) {
            rebuildVertexArray()
            shouldRebuild = false
        }

        var offsetX = (floor(cameraX.toDouble()).toInt() / TOTAL_LENGTH_X).toFloat() * TOTAL_LENGTH_X
        var offsetZ = (floor(cameraZ.toDouble()).toInt() / TOTAL_LENGTH_Z).toFloat() * TOTAL_LENGTH_Z
        offsetX += posX
        offsetZ += posZ

        for (i in -2..0) {
            for (j in -1..0) {
                displayPart(
                    viewMatrix,
                    projectionMatrix,
                    cameraX,
                    cameraZ,
                    offsetX + TOTAL_LENGTH_X * j,
                    offsetZ + TOTAL_LENGTH_Z * i
                )
            }
        }
    }

    private fun displayPart(
        viewMatrix: Matrix4f,
        projectionMatrix: Matrix4f,
        cameraX: Float,
        cameraZ: Float,
        offsetX: Float,
        offsetZ: Float
    ) {
        GLMatrixStack.setProjectionMatrix(projectionMatrix)
        GLMatrixStack.push()
        GLMatrixStack.load(viewMatrix)

        GLState.pushBlendEnabled(true)
        GLState.pushBlendFunction(GLBlendFactor.SRC_ALPHA, GLBlendFactor.ONE_MINUS_SRC_ALPHA)
        GLState.pushCullFaceEnabled(false)

        val drawCall = GLImmediateDrawCall.beginTriangles(null)

        var i = 0
        while (i < vertexPositions!!.size) {
            val vx = vertexPositions!![i]
            val vz = vertexPositions!![i + 1]
            val gx = offsetX + vx
            val gz = offsetZ + vz

            val distX = cameraX - gx
            val distZ = cameraZ - gz
            val dist = sqrt(distX * distX + distZ * distZ.toDouble()).toFloat()
            val alpha = max(0.0f, exp(-dist * 0.002f.toDouble()).toFloat())

            drawCall.color(1.0f, 1.0f, 1.0f, alpha)
            drawCall.vertex(gx, elevation, gz)
            i += 2
        }

        drawCall.end()

        GLState.popCullFaceEnabled()
        GLState.popBlendFunction()
        GLState.popBlendEnabled()

        GLMatrixStack.pop()
    }
}