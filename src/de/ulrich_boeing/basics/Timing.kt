package de.ulrich_boeing.basics

class Timing() {
    private val startTime = System.currentTimeMillis()

    fun get(): Long {
        return System.currentTimeMillis() - startTime
    }

    fun print(process: String = "") {
        val duration = get()
        if (process != "")
            println("Duration of $process: $duration")
        else
            println("Duration: $duration")
    }
}