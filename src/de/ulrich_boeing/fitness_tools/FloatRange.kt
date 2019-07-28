package de.ulrich_boeing.fitness_tools

import kotlin.random.Random

/**
 * End value can be reached (although not returned by random(), but through mutation)
 */
class FloatRange(val start: Float, val end: Float) {

    /**
     *  Gets a random value between
     *  start (inclusive) and end (exclusive) ✔
     */
    fun random(): Float = expand(Random.nextFloat())

    /**
     * Mutate the value. ✔
     * @param range For range = 1 mutation is 100% or size of range
     *
     */
    fun mutate(value: Float, range: Float): Float {
        val mutation = range * Random.nextFloat() - (range / 2)
        val newValue = normalize(value) + mutation
        return check(expand(newValue))
    }

    /**
     *  The inverse function to normalize ✔
     */
    private fun expand(value: Float): Float = start + value * (end - start)

    /**
     *  Normalizing a value ✔
     */
    private fun normalize(value: Float): Float = (value - start) / (end - start)


    /**
     *  Checks if value is inside range ✔
     */
    fun check(value: Float): Float {
        return when {
            value < start -> start
            value > end -> end
            else -> value
        }
    }
}