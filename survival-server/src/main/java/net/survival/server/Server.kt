package net.survival.server

class Server private constructor() {
    fun tick(elapsedTime: Double) {}

    companion object {
        private const val TICKS_PER_SECOND = 60.0
        private const val SECONDS_PER_TICK = 1.0 / TICKS_PER_SECOND

        @JvmStatic
        fun main(args: Array<String>) {
            val program = Server()
            val MILLIS_PER_TICK = SECONDS_PER_TICK * 1000.0
            var now = System.currentTimeMillis()
            var prevTime = now
            var unprocessedTicks = 0.0

            while (true) {
                now = System.currentTimeMillis()
                unprocessedTicks += (now - prevTime) / MILLIS_PER_TICK
                prevTime = now

                if (unprocessedTicks >= 1.0) {
                    while (unprocessedTicks >= 1.0) {
                        program.tick(SECONDS_PER_TICK)
                        unprocessedTicks -= 1.0
                    }
                }
            }
        }
    }
}