package net.survival.world

interface WorldScript {
    fun runCycle(): WorldScript?
}